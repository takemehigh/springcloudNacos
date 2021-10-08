package wg.consumer.test.mapper;

import org.apache.ibatis.annotations.Mapper;
import wg.consumer.test.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wg
 * @since 2021-10-06
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

}
