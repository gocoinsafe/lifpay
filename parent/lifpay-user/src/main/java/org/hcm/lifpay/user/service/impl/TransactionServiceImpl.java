package org.hcm.lifpay.user.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.common.CommonPage;
import org.hcm.lifpay.user.dao.entity.UserContactDo;
import org.hcm.lifpay.user.dao.entity.UserTransactionDo;
import org.hcm.lifpay.user.dao.repository.UserTransactionRepository;
import org.hcm.lifpay.user.dto.req.ContactListReq;
import org.hcm.lifpay.user.dto.req.TransactionSubmitReq;
import org.hcm.lifpay.user.dto.resp.ContactListResp;
import org.hcm.lifpay.user.dto.resp.TransactionListResp;
import org.hcm.lifpay.user.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RefreshScope
@Service
public class TransactionServiceImpl implements TransactionService {


    private final static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private UserTransactionRepository userTransactionRepository;








    @Override
    public BaseResponse<?> transactionSubmit(TransactionSubmitReq request) {
        logger.info("transactionSubmit.req:{}", JSON.toJSONString(request));
        BaseResponse response = new BaseResponse<>();

        UserTransactionDo transactionDo = new UserTransactionDo();
        transactionDo.setUserId(request.getUserId());
        transactionDo.setTransactionHash(request.getTransactionHash());
        transactionDo.setFromAddress(request.getFromAddress());
        transactionDo.setToAddress(request.getToAddress());
        transactionDo.setAmount(request.getAmount());
        transactionDo.setFee(request.getFee());
        transactionDo.setCreateTime(System.currentTimeMillis());
        transactionDo.setUpdateTime(System.currentTimeMillis());
        int result = userTransactionRepository.insert(transactionDo);
        if (result <= 0){
            logger.info("save error!");
        }

        return response;
    }


    @Override
    public BaseResponse<CommonPage<TransactionListResp>> transactionList(ContactListReq request) {
        logger.info("transactionList.req:{}",JSON.toJSONString(request));

        BaseResponse<CommonPage<TransactionListResp>> response = new BaseResponse<>();

        Page<UserTransactionDo>  queryPage = new Page<>(request.getPageNo(),request.getPageSize());

        LambdaQueryWrapper<UserTransactionDo> queryWrapper = new LambdaQueryWrapper<UserTransactionDo>()
                .eq(UserTransactionDo:: getUserId, request.getUserId())
                .orderByDesc(UserTransactionDo:: getCreateTime);
        Page<UserTransactionDo> page = userTransactionRepository.selectPage(queryPage,queryWrapper);

        List<TransactionListResp> transactionRespList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(page.getRecords())){
            // 抛出对外返回值内容
            for (UserTransactionDo  data :page.getRecords()){
                TransactionListResp transactionInfo = new TransactionListResp();
                transactionInfo.setId(data.getId());
                transactionInfo.setUserId(data.getUserId());
                transactionInfo.setFromAddress(data.getFromAddress());
                transactionInfo.setToAddress(data.getToAddress());
                transactionInfo.setAmount(data.getAmount());
                transactionInfo.setFee(data.getFee());
                transactionInfo.setStatus(data.getStatus());
                transactionInfo.setCreateTime(data.getCreateTime());
                transactionInfo.setUpdateTime(data.getUpdateTime());

                transactionRespList.add(transactionInfo);
            }
        }

        response.setData(CommonPage.restPage(page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), transactionRespList));

        return response;
    }
}
