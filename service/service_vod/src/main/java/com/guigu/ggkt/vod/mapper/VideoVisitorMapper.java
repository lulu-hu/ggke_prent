package com.guigu.ggkt.vod.mapper;

import com.atguigu.ggkt.model.vod.VideoVisitor;
import com.atguigu.ggkt.vo.vod.VideoVisitorCountVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 视频来访者记录表 Mapper 接口
 * </p>
 *
 * @author guigu
 * @since 2022-09-05
 */
public interface VideoVisitorMapper extends BaseMapper<VideoVisitor> {

    //课程统计接口
    List<VideoVisitorCountVo> findCount(@Param("courseId") Long courseId,
                                        @Param("startDate")String startDate,
                                        @Param("endDate")String endDate);
}
