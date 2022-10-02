package com.guigu.ggkt.activity.service.impl;

import com.atguigu.ggkt.model.activity.CouponInfo;
import com.atguigu.ggkt.model.activity.CouponUse;
import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.vo.activity.CouponUseQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guigu.ggkt.activity.mapper.CouponInfoMapper;
import com.guigu.ggkt.activity.service.CouponInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guigu.ggkt.activity.service.CouponUseService;
import com.guigu.ggkt.client.user.UserInfoFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author guigu
 * @since 2022-09-09
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {

    @Autowired
    private CouponUseService couponUseService;

    @Autowired
    private UserInfoFeignClient userInfoFeignClient;


    //获取已经使用优惠劵列表（条件查询分页）
    @Override
    public IPage<CouponUse> selectCouponUsePage(Page<CouponUse> pageParam,
                                                CouponUseQueryVo couponUseQueryVo) {

        //获取条件
        Long couponId = couponUseQueryVo.getCouponId();
        String couponStatus = couponUseQueryVo.getCouponStatus();
        String getTimeBegin = couponUseQueryVo.getGetTimeBegin();
        String getTimeEnd = couponUseQueryVo.getGetTimeEnd();
        //封装条件
        QueryWrapper<CouponUse> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(couponId)) {
            wrapper.eq("coupon_id",couponId);
        }
        if(!StringUtils.isEmpty(couponStatus)) {
            wrapper.eq("coupon_status",couponStatus);
        }
        if(!StringUtils.isEmpty(getTimeBegin)) {
            wrapper.ge("get_time",getTimeBegin);//ge：大于
        }
        if(!StringUtils.isEmpty(getTimeEnd)) {
            wrapper.le("get_time",getTimeEnd);//le:小于
        }

        //调用方法查询
        IPage<CouponUse> page = couponUseService.page(pageParam, wrapper);
        //封装用户昵称和手机号
        List<CouponUse> couponUseList = page.getRecords();
        couponUseList.stream().forEach(item->{
            this.getUserInfoBycouponUse(item);
        });
        return page;
    }

    //封装用户昵称和手机号
    private CouponUse getUserInfoBycouponUse(CouponUse couponUse) {
        Long userId = couponUse.getUserId();
        if(!StringUtils.isEmpty(userId)) {
            UserInfo userInfo = userInfoFeignClient.getById(userId);
            if(userInfo != null) {
                couponUse.getParam().put("nickName", userInfo.getNickName());
                couponUse.getParam().put("phone", userInfo.getPhone());
            }
        }
        return couponUse;
    }
}
