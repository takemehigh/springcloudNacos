package wg.consumer.test.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import wg.consumer.test.entity.Course;
import wg.consumer.test.mapper.CourseMapper;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wg
 * @since 2021-10-06
 */
@RestController
@RequestMapping("/test/course")
public class CourseController {

    @Autowired
    CourseMapper courseMapper;


    @PostMapping
    public List<Course> queryCourse(){

        QueryWrapper queryWrapper = new QueryWrapper<Course>();
        //List resultList = courseMapper.selectList();
        return null;
    }
}
