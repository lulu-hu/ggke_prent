package com.guigu.ggkt.vod.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.vo.vod.SubjectEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guigu.ggkt.exctption.GgktException;
import com.guigu.ggkt.vod.listener.SubjectListener;
import com.guigu.ggkt.vod.mapper.SubjectMapper;
import com.guigu.ggkt.vod.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author guigu
 * @since 2022-08-31
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {


    @Autowired
   private SubjectListener subjectListener;

    //课程分类列表
    @Override
    public List<Subject> selectSubjectList(Long id) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        //查询课程分类表所有数据
        List<Subject> subjectList = baseMapper.selectList(queryWrapper);

        //向list集合每个Subject对象中设置hasChildren
        for (Subject subject:subjectList) {
            Long subjectId = subject.getId();
            boolean isChild = this.isChildren(subjectId);
            subject.setHasChildren(isChild);
        }
        return subjectList;
    }

    //课程分类导出
    @Override
    public void exportData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("课程分类", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            //查询课程分类表所有数据
            List<Subject> subjectList = baseMapper.selectList(null);

            List<SubjectEeVo> subjectEeVoList = new ArrayList<>(subjectList.size());

            for (Subject subject: subjectList) {
                SubjectEeVo subjectEeVo = new SubjectEeVo();
                BeanUtils.copyProperties(subject,subjectEeVo);
                subjectEeVoList.add(subjectEeVo);
            }

            //EasyExcel写操作
            EasyExcel.write(response.getOutputStream(), SubjectEeVo.class)
                    .sheet("课程分类")
                    .doWrite(subjectEeVoList);
        }catch (Exception e){
            throw new GgktException(20001,"导出失败");
        }
    }

    //课程分类导入
    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),SubjectEeVo.class,subjectListener
                    ).sheet().doRead();
        } catch (IOException e) {
            throw new GgktException(20001,"导入失败");
        }
    }

    //判断id下面是否有子节点
    private boolean isChildren(Long id) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count>0;
    }
}
