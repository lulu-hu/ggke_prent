package com.guigu.ggkt.vod.controller;


import com.guigu.ggkt.result.Result;
import com.guigu.ggkt.vod.service.VideoVisitorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 前端控制器
 * </p>
 *
 * @author guigu
 * @since 2022-09-05
 */
@RestController
@RequestMapping(value="/admin/vod/videoVisitor")
//@CrossOrigin
public class VideoVisitorController {

    @Resource
    private VideoVisitorService videoVisitorService;

    //课程统计接口
    @GetMapping("findCount/{courseId}/{startDate}/{endDate}")
    public Result findCount(@PathVariable Long courseId,
                            @PathVariable String startDate,
                            @PathVariable String endDate){
       Map<String,Object> map = videoVisitorService.findCount(courseId,startDate,endDate);
        return Result.ok(map);
    }
}

