package org.hcm.lifpay.misc.job;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections4.CollectionUtils;
import org.hcm.lifpay.misc.dao.entity.StoreFormDo;
import org.hcm.lifpay.misc.dao.mapper.StoreFormMapper;
import org.hcm.lifpay.misc.service.MailService;
import org.hcm.lifpay.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 原生Java定时任务（每隔30分钟执行一次）
 */

@Component // 必须加这个注解，让Spring扫描到
public class MiscFormTimer {


    // 注入依赖（确保已通过@Autowired或构造器注入）
    private final StoreFormMapper storeFormMapper;
    private final MailService mailService;
    private final Logger log = LoggerFactory.getLogger(MiscFormTimer.class);

    // 构造器注入（推荐，比@Autowired更优雅）
    public MiscFormTimer(StoreFormMapper storeFormMapper, MailService mailService) {
        this.storeFormMapper = storeFormMapper;
        this.mailService = mailService;
    }


    // 每隔30分钟执行一次（cron表达式：分 时 日 月 周 年，?表示忽略周/日冲突）
    @Scheduled(cron = "0 0/30 * * * ?")
    public void statisticsFormExecuteTask() {
        try {
            // 1. 计算时间区间（毫秒级精度）
            Long currentTime = System.currentTimeMillis(); // 任务执行当前时间（结束时间）
            // 取整到30分钟的倍数（消除毫秒差异）
            long alignedCurrentTime = currentTime - (currentTime % (30 * 60 * 1000L));
            long thirtyMinutesMillis = 30 * 60 * 1000L;
            long startTime = alignedCurrentTime - thirtyMinutesMillis;


             log.info("Spring定时任务执行：查询[{}]至[{}]区间内的新增表单",
                    startTime, currentTime);

            // 2. 构造查询条件：createTime在[startTime, currentTime)之间（左闭右开，避免重复查询）
            LambdaQueryWrapper<StoreFormDo> queryWrapper = Wrappers.<StoreFormDo>lambdaQuery()
                    // 时间区间查询（适配毫秒级字段，数据库需是datetime(3)或timestamp(3)）
                    .ge(StoreFormDo::getCreateTime, startTime) // 大于等于开始时间
                    .lt(StoreFormDo::getCreateTime, currentTime); // 小于结束时间（避免和下一次任务重复）

            // 3. 查询数据
            List<StoreFormDo> formList = storeFormMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(formList)) {
                log.info("当前区间无新增表单，任务结束");
                return;
            }
            // 优化核心：用 StringBuilder 替代字符串拼接（循环中 += 效率极低）、修复覆盖问题、非空防护、字段后加空格
            StringBuilder formInfo = new StringBuilder(); // 替换 String 为 StringBuilder，提升循环拼接效率
            for (StoreFormDo formData : formList) {
                // 1. 非空防护：避免字段为 null 时拼接出 "null" 字符串（用 Objects.toString 兜底空字符串）
                Long id = formData.getId();
                String name = Objects.toString(formData.getName(), "");
                String email = Objects.toString(formData.getEmail(), "");
                String company = Objects.toString(formData.getCompany(), "");
                String contactInfo = Objects.toString(formData.getContact(), "");
                // isCustom 补充非空判断（若字段是 Integer 类型，防止 NPE）
                String isCustom = (formData.getIsCustom() != null && formData.getIsCustom() == 1) ? "是" : "否";
                String content = Objects.toString(formData.getMessage(), "");
                String country = Objects.toString(formData.getCountry(), "");
                String area = Objects.toString(formData.getPostCode(), "");
                String remark = Objects.toString(formData.getRemark(), "");

                // 2. 每个字段（键值对）后面添加空格，格式统一整洁（最后一个字段也加空格，保持一致性）
                formInfo.append("id: ").append(id).append(" ")
                        .append("姓名: ").append(name).append(" ")
                        .append("邮件: ").append(email).append(" ")
                        .append("公司: ").append(company).append(" ")
                        .append("联系方式: ").append(contactInfo).append(" ")
                        .append("是否定制: ").append(isCustom).append(" ")
                        .append("内容: ").append(content).append(" ")
                        .append("国家: ").append(country).append(" ")
                        .append("邮政编码: ").append(area).append(" ")
                        .append("备注: ").append(remark).append(" ")
                        .append("\n"); // 每条数据结束后换行，区分不同表单
            }

            // 最终转为字符串使用（若无数据，formInfo 为空字符串，避免 null）
            String finalFormInfo = formInfo.toString();

            // 4. 发送邮件（正文补充表单数量，方便接收人了解情况）
            int formCount = formList.size();
            mailService.sendSimpleMail(
                    "an@hcm.capital,sunny@hcm.capital,jerry@lifpay.me,aubrey@bittheory.us", // 收件人
                    "[表单通知] 新增" + formCount + "条商城表单", // 主题（含数量）
                    String.format("商城有新的form表单进来了，请及时处理！\n" +
                                    "查询时间：%s 至 %s\n 新增表单数量：%d条 \n" +
                                    "信息: %s" ,
                            DateTimeUtil.timestampToStr(startTime), DateTimeUtil.timestampToStr(currentTime), formCount, finalFormInfo) // 正文（补充关键信息）
            );

            log.info("定时任务执行成功：查询到{}条新增表单，邮件已发送", formCount);

        } catch (Exception e) {

            log.info("Spring定时任务执行失败：" + e.getMessage());
            e.printStackTrace();
        }
    }


}
