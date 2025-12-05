package org.hcm.lifpay.user.dao.repository;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hcm.lifpay.user.dao.entity.UserInfoDo;

@Mapper
public interface UserInfoRepository extends BaseMapper<UserInfoDo> {



}
