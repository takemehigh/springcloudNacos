package wg.consumer.test.service.impl;

import wg.consumer.test.entity.Course;
import wg.consumer.test.mapper.CourseMapper;
import wg.consumer.test.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wg
 * @since 2021-10-06
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

}
