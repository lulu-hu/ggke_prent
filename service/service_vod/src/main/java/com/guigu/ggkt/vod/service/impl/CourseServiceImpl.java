package com.guigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.Course;;
import com.atguigu.ggkt.model.vod.CourseDescription;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guigu.ggkt.vod.mapper.CourseMapper;
import com.guigu.ggkt.vod.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author guigu
 * @since 2022-09-01
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {


    @Resource
    private TeacherService teacherService;

    @Resource
    private SubjectService subjectService;

    @Resource
    private CourseDescriptionService courseDescriptionService;

    @Resource
    private VideoService videoService;

    @Resource
    private ChapterService chapterService;



    //点播课程列表
    @Override
    public Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {

       //获取条件值
        String title = courseQueryVo.getTitle();
        Long subjectId = courseQueryVo.getSubjectId();//二层分类
        Long subjectParentId = courseQueryVo.getSubjectParentId();//一层分类
        Long teacherId = courseQueryVo.getTeacherId();

        //判断条件为空，封装条件
        QueryWrapper<Course> wrapper = new QueryWrapper<>();

        if(!StringUtils.isEmpty(title)) {
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id",teacherId);
        }

        //调用方法实现条件分页查询
        Page<Course> pages = baseMapper.selectPage(pageParam, wrapper);
        long totalCount = pages.getTotal();//总记录数
        long totalPage = pages.getPages();//总页数
        long currentPage = pages.getCurrent();//当前页
        long size = pages.getSize();//每页记录数

        List<Course> records = pages.getRecords();

        //遍历封装讲师和分类名称
        records.stream().forEach(item -> {
            this.getTeacherOrSubjectName(item);
        });

        //封装返回数据
        Map<String,Object> map = new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("records",records);

        return map;
    }

    //实现方法
    //添加课程基本信息
    @Override
    public Long saveCourseInfo(CourseFormVo courseFormVo) {
        //保存课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo, course);
        baseMapper.insert(course);

        //保存课程详情信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseFormVo.getDescription());
        courseDescription.setCourseId(course.getId());
        courseDescriptionService.save(courseDescription);

        //返回课程id
        return course.getId();
    }

    //根据id获取课程信息
    @Override
    public CourseFormVo getCourseInfoById(Long id) {
       //课程基本信息
        Course course = baseMapper.selectById(id);
        if(course == null){
            return null;
        }

        //课程描述信息
        CourseDescription courseDescription = courseDescriptionService.getById(id);
        //封装
        CourseFormVo courseFormVo = new CourseFormVo();
        BeanUtils.copyProperties(course,courseFormVo);

        //封装描述
        if(courseDescription != null){
            courseFormVo.setDescription(courseDescription.getDescription());
        }
        return courseFormVo;
    }

    //修改课程信息
    @Override
    public void updateCourseId(CourseFormVo courseFormVo) {
        //修改课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo, course);
        baseMapper.updateById(course);

        //修改课程描述信息
        CourseDescription description = new CourseDescription();
        description.setDescription(courseFormVo.getDescription());
        description.setId(course.getId());
        courseDescriptionService.updateById(description);


    }

    //根据课程id查询发布课程信息
    @Override
    public CoursePublishVo getCoursePublishVo(Long id) {

        return baseMapper.selectCoursePublishVoById(id);
    }

    //课程最终发布
    @Override
    public void publishCourseById(Long id) {
        Course course = baseMapper.selectById(id);
        course.setStatus(1);
        course.setPublishTime(new Date());
        baseMapper.updateById(course);
    }

    //删除课程
    @Override
    public void removeCourseId(Long id) {
        //根据课程id删除小节
        videoService.removVideoByCourseId(id);

        //根据课程id删除小节
        chapterService.removeChapterByCourseId(id);

        //根据课程id删除课程描述
        courseDescriptionService.removeById(id);

        //根据课程id删除课程
        baseMapper.deleteById(id);

    }

    //获取讲师和分类名称
    private Course getTeacherOrSubjectName(Course course) {
        //查询讲师名称
        Teacher teacher = teacherService.getById(course.getTeacherId());
        if(teacher != null) {
            course.getParam().put("teacherName",teacher.getName());
        }
        //查询分类名称
        Subject subjectOne = subjectService.getById(course.getSubjectParentId());
        if(subjectOne != null) {
            course.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }
        Subject subjectTwo = subjectService.getById(course.getSubjectId());
        if(subjectTwo != null) {
            course.getParam().put("subjectTitle",subjectTwo.getTitle());
        }
        return course;
    }
}
