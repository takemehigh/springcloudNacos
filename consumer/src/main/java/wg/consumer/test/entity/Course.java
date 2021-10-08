package wg.consumer.test.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wg
 * @since 2021-10-06
 */
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Course{" +
            "name=" + name +
        "}";
    }
}
