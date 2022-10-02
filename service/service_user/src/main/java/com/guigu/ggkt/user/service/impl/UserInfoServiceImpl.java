package com.guigu.ggkt.user.service.impl;

import com.atguigu.ggkt.model.user.UserInfo;
import com.guigu.ggkt.user.mapper.UserInfoMapper;
import com.guigu.ggkt.user.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author guigu
 * @since 2022-09-09
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
