package org.dddml.ffvtraceability.restful.test;

import org.dddml.ffvtraceability.restful.config.TestSecurityConfig;
import org.dddml.ffvtraceability.specialization.hibernate.TableIdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestSecurityConfig.class})
public class TableIdGeneratorTest {

    // @Autowired
//    private TableIdGenerator tableIdGenerator;
//
//    @BeforeEach
//    void setUp() {
//        tableIdGenerator = new TableIdGenerator();
//        assertNotNull(tableIdGenerator, "TableIdGenerator should be autowired");
//    }
//
//    @Test
//    void testGenerateId() {
//        // 测试生成ID
//        Long id1 = tableIdGenerator.getNextId();
//        System.out.println("TableIdGeneratorTest.testGenerateId(), id1: " + id1);
//        assertNotNull(id1, "Generated ID should not be null");
//
//        // 测试生成的ID是否唯一
//        Long id2 = tableIdGenerator.getNextId();
//        System.out.println("TableIdGeneratorTest.testGenerateId(), id2: " + id2);
//        assertNotNull(id2, "Generated ID should not be null");
//        assertNotEquals(id1, id2, "Generated IDs should be unique");
//    }

}
