package org.hcm.lifpay.misc.dto.resp;


import lombok.Data;

/**
 * 汇率返回主模型（封装btc/cny/usd）
 */
@Data
public class ExchangeRateModel {

    private RateModel btc;
    private RateModel cny;
    private RateModel usd;
}
