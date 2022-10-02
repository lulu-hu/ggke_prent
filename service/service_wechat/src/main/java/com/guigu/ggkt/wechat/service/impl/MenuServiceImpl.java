package com.guigu.ggkt.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.model.wechat.Menu;
import com.atguigu.ggkt.vo.wechat.MenuVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guigu.ggkt.exctption.GgktException;
import com.guigu.ggkt.wechat.mapper.MenuMapper;
import com.guigu.ggkt.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author guigu
 * @since 2022-09-10
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {


    @Autowired
    private WxMpService wxMpService;


    //获取所有菜单，按照一级和二级菜单封装
    @Override
    public List<MenuVo> findMenuInfo() {
        //1 创建list集合，用户最终数据封装
        List<MenuVo> finalMenuList = new ArrayList<>();
        //2 查询所有数据（包含一级和二级）
        List<Menu> menuList = baseMapper.selectList(null);
        //3 从所有菜单数据获取所有一级菜单数据（parent_id=0）
        List<Menu> oneMenuList = menuList.stream()
                .filter(menu -> menu.getParentId().longValue()== 0)
                .collect(Collectors.toList());
        //封装一级菜单数据，封装到最终数据list集合
        for(Menu oneMenu : oneMenuList) {
            MenuVo oneMenuVo = new MenuVo();
            BeanUtils.copyProperties(oneMenu, oneMenuVo);

            //5 封装二级菜单数据（判断一级菜单id和二级菜单parent_id是否相同）
            // 如果相同，把二级菜单数据放到一级菜单里面
            List<Menu> twoMenuList = menuList.stream()
                    .filter(menu -> menu.getParentId().longValue() == oneMenu.getId())
                    .sorted(Comparator.comparing(Menu::getSort))
                    .collect(Collectors.toList());
            List<MenuVo> children = new ArrayList<>();
            for(Menu twoMenu : twoMenuList) {
                MenuVo twoMenuVo = new MenuVo();
                BeanUtils.copyProperties(twoMenu, twoMenuVo);
                children.add(twoMenuVo);
            }
            //把二级菜单数据放到一级菜单里面
            oneMenuVo.setChildren(children);
            //把oneMenuVo放到最终list集合
            finalMenuList.add(oneMenuVo);
        }
        //最终数据返回
        return finalMenuList;
    }

    //获取所有一级菜单
    @Override
    public List<Menu> findMenuOneInfo() {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",0);
        List<Menu> menus = baseMapper.selectList(wrapper);
        return menus;
    }

    //同步菜单
    @SneakyThrows
    @Override
    public void syncMenu() {
        List<MenuVo> menuVoList = this.findMenuInfo();
        //封装button里面结构，数组格式
        JSONArray buttonList = new JSONArray();
        for(MenuVo oneMenuVo : menuVoList) {
            //json数组 一级菜单
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            //json数组 二级菜单
            JSONArray subButton = new JSONArray();
            for(MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                JSONObject view = new JSONObject();
                view.put("type", twoMenuVo.getType());
                if(twoMenuVo.getType().equals("view")) {
                    view.put("name", twoMenuVo.getName());
                    view.put("url", "http://ggkt2.vipgz1.91tunnel.com/#"
                            +twoMenuVo.getUrl());
                } else {
                    view.put("name", twoMenuVo.getName());
                    view.put("key", twoMenuVo.getMeunKey());
                }
                subButton.add(view);
            }
            one.put("sub_button", subButton);
            buttonList.add(one);
        }
        //菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);
        this.wxMpService.getMenuService().menuCreate(button.toJSONString());
    }

    //删除公众号菜单
    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new GgktException(20001,"公众号菜单删除失败");
        }
    }
}
