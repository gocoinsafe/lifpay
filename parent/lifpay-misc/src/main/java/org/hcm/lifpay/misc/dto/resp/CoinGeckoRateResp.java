package org.hcm.lifpay.misc.dto.resp;


import lombok.Data;

import java.util.Map;

@Data
public class CoinGeckoRateResp {


    // 汇率对象，key是币种缩写（btc/cny/usd），value是RateModel
    private Map<String, RateModel> rates;
}
