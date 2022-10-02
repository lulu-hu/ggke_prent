package com.guigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Subject;
import com.guigu.ggkt.result.Result;
import com.guigu.ggkt.vod.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author guigu
 * @since 2022-08-31
 */
@Api(tags = "课程分类管理")
@RestController
@RequestMapping(value = "/admin/vod/subject")
//@CrossOrigin
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation("查询下一层的课程分类")
    @GetMapping("getChildSubject/{id}")
    public Result getChildSubject(@PathVariable Long id) {
        List<Subject> list = subjectService.selectSubjectList(id);
        return Result.ok(list);
    }


    //课程分类导出
    @ApiOperation("课程分类导出")
    @GetMapping("exportData")
    public void exportData(HttpServletResponse response){
        subjectService.exportData(response);
    }

    @ApiOperation("课程分类导入")
    @PostMapping("importData")
    public Result exportData(MultipartFile file){
        subjectService.importData(file);
        return Result.ok(null);
    }



}

