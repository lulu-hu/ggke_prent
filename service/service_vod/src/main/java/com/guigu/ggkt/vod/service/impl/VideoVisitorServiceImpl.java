package com.guigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.VideoVisitor;
import com.atguigu.ggkt.vo.vod.VideoVisitorCountVo;
import com.guigu.ggkt.vod.mapper.VideoVisitorMapper;
import com.guigu.ggkt.vod.service.VideoVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频来访者记录表 服务实现类
 * </p>
 *
 * @author guigu
 * @since 2022-09-05
 */
@Service
public class VideoVisitorServiceImpl extends ServiceImpl<VideoVisitorMapper, VideoVisitor> implements VideoVisitorService {

    //课程统计接口
    @Override
    public Map<String, Object> findCount(Long courseId, String startDate, String endDate) {
        //调用Mapper的方法
        List<VideoVisitorCountVo> videoVisitorCountVoList =
                baseMapper.findCount(courseId,startDate,endDate);
        //创建map集合
        Map<String,Object> map = new HashMap<>();
        //创建两个list集合，一个代表所有日期，一个代表日期对应的数量
        //封装数据 所有日期
        List<String> dateList = videoVisitorCountVoList.stream().
                map(VideoVisitorCountVo::getJoinTime).collect(Collectors.toList());

        //代表日期和数量
        List<Integer> countList = videoVisitorCountVoList.stream()
                .map(VideoVisitorCountVo::getUserCount).collect(Collectors.toList());
        //放到map集合
        map.put("xData", dateList);
        map.put("yData", countList);
        return map;
    }
}
