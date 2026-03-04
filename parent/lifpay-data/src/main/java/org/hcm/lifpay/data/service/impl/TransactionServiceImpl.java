package org.hcm.lifpay.data.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.CommonPage;
import org.hcm.lifpay.data.common.DataResultEnum;
import org.hcm.lifpay.data.dao.entity.UserTransactionDo;
import org.hcm.lifpay.data.dao.repository.UserTransactionRepository;
import org.hcm.lifpay.data.dto.req.TransactionListReq;
import org.hcm.lifpay.data.dto.req.TransactionSubmitReq;
import org.hcm.lifpay.data.dto.resp.TransactionListResp;
import org.hcm.lifpay.data.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RefreshScope
@Service
public class TransactionServiceImpl implements TransactionService {


    private final static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private UserTransactionRepository userTransactionRepository;




    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<?> transactionSubmit(TransactionSubmitReq request) {
        logger.info("transactionSubmit.req:{}", JSON.toJSONString(request));
        BaseResponse response = new BaseResponse<>();
        try {

            // 1. 构建数据库实体
            UserTransactionDo transactionDo = new UserTransactionDo();
            // 拷贝同名字段（简化赋值）
            BeanUtils.copyProperties(request, transactionDo);

            // 2. 补充公共字段（如果BaseEntity的自动填充未生效，手动设置）
            long currentTime = System.currentTimeMillis();
            transactionDo.setCreateTime(currentTime);
            transactionDo.setUpdateTime(currentTime);


            // 3. 插入数据库
            int insertResult = userTransactionRepository.insert(transactionDo);
            if (insertResult <= 0) {
                logger.error("交易记录插入失败，request：{}", JSON.toJSONString(request));
                response.setCode(DataResultEnum.TRANSACTION_INSERTION_FAILED_ERROR.getCode());
                response.setMessage(DataResultEnum.TRANSACTION_INSERTION_FAILED_ERROR.getDesc());
                return response;
            }

            logger.info("交易记录插入成功，交易ID：{}，tradeHash：{}", transactionDo.getId(), request.getTradeHash());
            return response;
        } catch (Exception e) {
            logger.error("提交交易记录异常，request：{}", JSON.toJSONString(request), e);
            throw new RuntimeException("交易记录提交失败：" + e.getMessage());
        }

    }


    @Override
    public BaseResponse<CommonPage<TransactionListResp>> transactionList(TransactionListReq request) {
        logger.info("transactionList.req:{}",JSON.toJSONString(request));
        BaseResponse<CommonPage<TransactionListResp>> response = new BaseResponse<>();
        if (null == request.getUserId() && StringUtils.isEmpty(request.getUserPrimaryKey())){
            response.setCode(DataResultEnum.PARAM_ERROR.getCode());
            response.setMessage(DataResultEnum.PARAM_ERROR.getDesc());
            return response;
        }


        Page<UserTransactionDo>  queryPage = new Page<>(request.getPageNo(),request.getPageSize());
        Page<UserTransactionDo> page = null;
        // 在获取交易记录时 需要考虑 用户没有登录的情况，如果没有登录就不会有userId
        if (StringUtils.isNotEmpty(request.getUserPrimaryKey())){

            LambdaQueryWrapper<UserTransactionDo> queryWrapper = new LambdaQueryWrapper<UserTransactionDo>()
                    .eq(UserTransactionDo:: getUserPrimaryKey, request.getUserPrimaryKey())
                    .orderByDesc(UserTransactionDo:: getTradeTime);
            page = userTransactionRepository.selectPage(queryPage,queryWrapper);
        }else {

            LambdaQueryWrapper<UserTransactionDo> queryWrapper = new LambdaQueryWrapper<UserTransactionDo>()
                    .eq(UserTransactionDo:: getUserId, request.getUserId())
                    .orderByDesc(UserTransactionDo:: getTradeTime);
            page = userTransactionRepository.selectPage(queryPage,queryWrapper);
        }


        List<TransactionListResp> transactionRespList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(page.getRecords())){
            // 抛出对外返回值内容
            for (UserTransactionDo data :page.getRecords()){
                TransactionListResp transactionInfo = new TransactionListResp(data);

                transactionRespList.add(transactionInfo);
            }
        }

        response.setData(CommonPage.restPage(page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), transactionRespList));

        return response;
    }
}
