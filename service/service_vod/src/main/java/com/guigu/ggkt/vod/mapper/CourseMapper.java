package com.guigu.ggkt.vod.mapper;


import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author guigu
 * @since 2022-09-01
 */
public interface CourseMapper extends BaseMapper<Course> {

    //根据课程id查询发布课程信息
    CoursePublishVo selectCoursePublishVoById(Long id);
}
