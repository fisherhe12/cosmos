package org.cosmos.admin.test;

import org.cosmos.admin.BootApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * SpringBoot环境的测试基类
 *
 * @author fisher
 * @date 2018-01-19
 */
@SpringBootTest(classes = BootApplication.class)
@RunWith(SpringRunner.class)
@Profile(value = "dev")
public class BaseSpringBootTest {
}
