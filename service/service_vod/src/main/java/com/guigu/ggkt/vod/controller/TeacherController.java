package com.guigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.vo.vod.TeacherQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guigu.ggkt.result.Result;
import com.guigu.ggkt.vod.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author guigu
 * @since 2022-08-05
 */
@Api(tags = "讲师管理接口")
@RestController
@RequestMapping("/admin/vod/teacher")
//@CrossOrigin
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("查询所有讲师")
    @GetMapping("findAll")
    public Result findAllTeachter(){
        //调用Service方法
        List<Teacher> list = teacherService.list();
        return Result.ok(list).message("查询数据成功");
    }

    //删除讲师
    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("remove/{id}")
    public Result removeTeacher(@ApiParam(name = "id", value = "ID", required = true)
                                @PathVariable Long id) {
        boolean isSuccess = teacherService.removeById(id);
        if(isSuccess) {
            return Result.ok(null);
        } else {
            return Result.fail(null);
        }
    }


    //3.分页查询
    @ApiOperation("条件查询分页")
    @PostMapping("findQueryPage/{current}/{limit}")
    public Result findPage(@PathVariable long current,
                           @PathVariable long limit,
                           TeacherQueryVo teacherQueryVo){
        //创建page对象，传递当前页和每页记录数
        Page<Teacher> pageParam = new Page<>(current, limit);
        //获取条件值
        String name = teacherQueryVo.getName();//讲师名称
        Integer level = teacherQueryVo.getLevel();//讲师级别
        String joinDateBegin = teacherQueryVo.getJoinDateBegin();//开始时间
        String joinDateEnd = teacherQueryVo.getJoinDateEnd();//结束时间
        //封装条件
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)) {
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)) {
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(joinDateBegin)) {
            wrapper.ge("join_date",joinDateBegin);
        }
        if(!StringUtils.isEmpty(joinDateEnd)) {
            wrapper.le("join_date",joinDateEnd);
        }
        //调用方法得到分页查询结果
        IPage<Teacher> pageModel = teacherService.page(pageParam, wrapper);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody Teacher teacher) {
        teacherService.save(teacher);
        return Result.ok(null);
    }

    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Teacher teacher = teacherService.getById(id);
        return Result.ok(teacher);
    }

    @ApiOperation(value = "修改")
    @PostMapping("update")
    public Result updateById(@RequestBody Teacher teacher) {
        teacherService.updateById(teacher);
        return Result.ok(teacher);
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        teacherService.removeByIds(idList);
        return Result.ok(null);
    }

}

