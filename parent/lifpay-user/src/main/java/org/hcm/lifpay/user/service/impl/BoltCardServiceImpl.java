package org.hcm.lifpay.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.CommonPage;
import org.hcm.lifpay.common.DigitalResultEnum;
import org.hcm.lifpay.user.constant.BoltCarPrivacy;
import org.hcm.lifpay.user.constant.BoltCarStatus;
import org.hcm.lifpay.user.dao.entity.BoltCardDo;
import org.hcm.lifpay.user.dao.repository.BoltCardRepository;
import org.hcm.lifpay.user.dao.repository.LnurlwRequestRepository;
import org.hcm.lifpay.user.dto.UserResultEnum;
import org.hcm.lifpay.user.dto.req.BoltCardsInfoReq;
import org.hcm.lifpay.user.dto.req.CreateBoltCardReq;
import org.hcm.lifpay.user.dto.resp.CreateBoltCardResp;
import org.hcm.lifpay.user.exception.LifpayException;
import org.hcm.lifpay.user.service.BoltCardService;
import org.hcm.lifpay.util.BoltCardKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@Slf4j
public class BoltCardServiceImpl implements BoltCardService {


    @Autowired
    private BoltCardRepository boltCardRepository;

    @Autowired
    private LnurlwRequestRepository lnurlwRequestRepository;



    @Override
    @Transactional(rollbackFor = {Exception.class})
    public BaseResponse<CreateBoltCardResp> createBoltCards(CreateBoltCardReq req) {
        log.info("BoltCardServiceImpl.createBoltCards.req:{}", JSON.toJSONString(req));
        // 1. 基础校验
        if (StringUtils.isBlank(req.getUid())) {
            return BaseResponse.fail(UserResultEnum.BOLT_CARD_UID_IS_NULL_ERROR.getCode(),UserResultEnum.BOLT_CARD_UID_IS_NULL_ERROR.getMsg());
        }

        // 2. UID 唯一性校验（非常重要）
        LambdaQueryWrapper<BoltCardDo> queryWrapper = new LambdaQueryWrapper<BoltCardDo>()
                .eq(BoltCardDo:: getCardUid, req.getUid().toUpperCase())
                .eq(BoltCardDo:: getStatus, BoltCarStatus.NORMAL.getType());

        BoltCardDo exists = boltCardRepository.selectOne(queryWrapper);
        if (exists != null) {
            return BaseResponse.fail(UserResultEnum.BOLT_CARD_ALREADY_BOUND_ERROR.getCode(),UserResultEnum.BOLT_CARD_ALREADY_BOUND_ERROR.getMsg());
        }

        // 3. 生成 BoltCard 密钥（核心）
        String k0 = BoltCardKeyUtil.generateKeyHex();
        String k1 = BoltCardKeyUtil.generateKeyHex();
        String k2 = BoltCardKeyUtil.generateKeyHex();

        BaseResponse<CreateBoltCardResp> response = new BaseResponse<>();

        // 初始化bolt card 信息
        BoltCardDo boltCardDo = new BoltCardDo();
        boltCardDo.setCardUid(req.getUid().toUpperCase());
        boltCardDo.setCardName(req.getName());
        boltCardDo.setUserId(req.getUserId());
        boltCardDo.setK0(k0);
        boltCardDo.setK1(k1);
        boltCardDo.setK2(k2);
        boltCardDo.setMaxAmount(convertStrToBigDecimal(req.getMaxAmount()));
        boltCardDo.setMinAmount(convertStrToBigDecimal(req.getMinAmount()));
        boltCardDo.setPrivacy(BoltCarPrivacy.FORBIDDEN.getType());
        boltCardDo.setStatus(BoltCarStatus.NORMAL.getType());
        boltCardDo.setWalletId(null);
        boltCardDo.setWalletProvider("Breez");
        boltCardDo.setCreateTime(System.currentTimeMillis());
        boltCardDo.setUpdateTime(System.currentTimeMillis());


        int result = boltCardRepository.insert(boltCardDo);
        if (result <= 0 ){
            log.error("bolt card 绑定失败！");
            throw new LifpayException(UserResultEnum.SYSTEM_INTERNAL_ERROR);
        }
        CreateBoltCardResp respData = new CreateBoltCardResp(boltCardDo);
        response.setData(respData);
        return response;
    }


    @Override
    public BaseResponse<CreateBoltCardResp> getBoltCardsInfo(BoltCardsInfoReq req) {
        return null;
    }


    @Override
    public BaseResponse<CreateBoltCardResp> getBoltCardsBeyToken(BoltCardsInfoReq req) {
        return null;
    }


    @Override
    public BaseResponse<CommonPage<CreateBoltCardResp>> queryBoltCards(BaseRequest req) {
        return null;
    }


    @Override
    public BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsTransactionQuery(BaseRequest req) {
        return null;
    }


    @Override
    public BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsPinModify(BaseRequest req) {
        return null;
    }

    @Override
    public BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsTypeModify(BaseRequest req) {
        return null;
    }

    @Override
    public BaseResponse<CommonPage<CreateBoltCardResp>> boltCardsWriteOff(BaseRequest req) {
        return null;
    }


    /**
     * 将String类型金额转换为BigDecimal
     * @param amountStr 字符串金额（如"100.00"、"50"）
     * @return 转换后的BigDecimal，空值/格式错误返回BigDecimal.ZERO（可根据业务调整）
     */
    public static BigDecimal convertStrToBigDecimal(String amountStr) {
        // 1. 处理空值（null/空字符串/全空格）
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return BigDecimal.ZERO; // 业务可调整为null或抛出异常
        }

        try {
            // 2. 核心转换：String -> BigDecimal
            return new BigDecimal(amountStr.trim());
        } catch (NumberFormatException e) {
            // 3. 处理格式错误（如"100a"、"abc"、"100.00.00"）
            log.info("金额格式错误，入参：" + amountStr + "，异常信息：" + e.getMessage());
            return BigDecimal.ZERO; // 或抛出业务异常：throw new BusinessException("金额格式不正确");
        }
    }
}
