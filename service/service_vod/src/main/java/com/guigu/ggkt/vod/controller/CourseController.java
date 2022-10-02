package com.guigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guigu.ggkt.result.Result;
import com.guigu.ggkt.vod.service.CourseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author guigu
 * @since 2022-09-01
 */
@RestController
@RequestMapping(value="/admin/vod/course")
//@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "courseVo", value = "查询对象", required = false)
                    CourseQueryVo courseQueryVo) {
        Page<Course> pageParam = new Page<>(page, limit);
        Map<String,Object> map = courseService.findPage(pageParam, courseQueryVo);
        return Result.ok(map);
    }


    //添加课程基本信息
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody CourseFormVo courseFormVo) {
        Long courseId = courseService.saveCourseInfo(courseFormVo);
        return Result.ok(courseId);
    }

    //根据id获取课程信息
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        CourseFormVo courseFormVo = courseService.getCourseInfoById(id);
        return Result.ok(courseFormVo);
    }

    //修改课程信息
    @PostMapping("update")
    public Result update(@RequestBody CourseFormVo courseFormVo){
        courseService.updateCourseId(courseFormVo);
        //课程id
        return Result.ok(courseFormVo.getId());
    }

    //根据课程id查询发布课程信息
    @ApiOperation("根据id获取课程发布信息")
    @GetMapping("getCoursePublishVo/{id}")
    public Result getCoursePublishVo(@PathVariable Long id){
       CoursePublishVo coursePublishVo = courseService.getCoursePublishVo(id);
       return Result.ok(coursePublishVo);
    }

    //课程最终发布
    @ApiOperation("根据id发布课程")
    @PutMapping("publishCourseById/{id}")
    public Result publishCourseById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable Long id) {
        courseService.publishCourseById(id);
        return Result.ok(null);
    }

    //删除课程
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        courseService.removeCourseId(id);
        return Result.ok(null);
    }
}

