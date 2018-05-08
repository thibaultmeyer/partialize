import com.fasterxml.jackson.databind.node.ContainerNode;
import com.zero_x_baadf00d.partialize.annotation.Partialize;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.Optional;

/**
 * BasicTest.
 *
 * @author Thibault Meyer
 * @version 18.05.08
 * @since 18.05.08
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OptionalTest {

    private PojoWithOptional pojoWithOptional;

    /**
     * Initialize data.
     *
     * @since 18.05.08
     */
    @Before
    public void initializePojo() {
        if (this.pojoWithOptional == null) {
            this.pojoWithOptional = new PojoWithOptional();
            this.pojoWithOptional.setFirstName("John");
            this.pojoWithOptional.setAge(18);
        }
    }

    /**
     * @since 18.05.08
     */
    @Test
    public void optionalTest001() {
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final ContainerNode result = partialize.buildPartialObject(
            "firstName,age,tags",
            OptionalTest.PojoWithOptional.class,
            this.pojoWithOptional
        );

        Assert.assertNotNull(result);
        Assert.assertEquals("John", result.get("firstName").asText());
        Assert.assertEquals(18, result.get("age").asInt());
        Assert.assertTrue(result.get("tags").isNull());
    }

    /**
     * PojoWithOptional.
     *
     * @author Thibault Meyer
     * @version 18.05.08
     * @since 18.05.08
     */
    @Partialize(allowedFields = {"firstName", "age", "tags"})
    public static class PojoWithOptional {

        private String firstName;
        private Integer age;
        private List<String> tags;

        public String getFirstName() {
            return this.firstName;
        }

        public void setFirstName(final String firstName) {
            this.firstName = firstName;
        }

        public Optional<Integer> getAge() {
            return Optional.ofNullable(this.age);
        }

        public void setAge(final Integer age) {
            this.age = age;
        }

        public Optional<List<String>> getTags() {
            return Optional.ofNullable(this.tags);
        }

        public void setTags(final List<String> tags) {
            this.tags = tags;
        }
    }
}
