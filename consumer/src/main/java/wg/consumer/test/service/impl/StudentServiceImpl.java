package wg.consumer.test.service.impl;

import wg.consumer.test.entity.Student;
import wg.consumer.test.mapper.StudentMapper;
import wg.consumer.test.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wg
 * @since 2021-10-14
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

}
