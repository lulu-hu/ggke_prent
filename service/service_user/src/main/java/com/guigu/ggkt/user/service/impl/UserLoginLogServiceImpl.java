package com.guigu.ggkt.user.service.impl;

import com.atguigu.ggkt.model.user.UserLoginLog;
import com.guigu.ggkt.user.mapper.UserLoginLogMapper;
import com.guigu.ggkt.user.service.UserLoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登陆记录表 服务实现类
 * </p>
 *
 * @author guigu
 * @since 2022-09-09
 */
@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService {

}
