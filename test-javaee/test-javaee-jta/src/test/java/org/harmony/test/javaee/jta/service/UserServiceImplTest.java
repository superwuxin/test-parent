package org.harmony.test.javaee.jta.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;

import org.harmony.test.javaee.jta.persistence.User;
import org.harmony.test.javaee.jta.repository.UserRepository;
import org.harmony.test.javaee.jta.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class UserServiceImplTest {

    private EJBContainer container;
    @EJB(beanName = "UserServiceImpl")
    private UserService service;
    @EJB
    private UserRepository userRepository;
    List<User> users = new ArrayList<User>();

    @Before
    public void setUp() throws Exception {
        Properties props = new Properties();
        props.put("openejb.conf.file", "src/main/resources/openejb.xml");
        container = EJBContainer.createEJBContainer(props);
        container.getContext().bind("inject", this);
        for (int i = 0; i < 10; i++) {
            users.add(new User("user_" + i, i, "F", "10024125125125" + i, Calendar.getInstance()));
        }
    }

    @After
    public void tearDown() throws Exception {
        container.close();
    }

    @Test(expected = RuntimeException.class)
    public void testThrowRuntimeExceptionAfterBatchSave() {
        try {
            service.throwRuntimeExceptionAfterBatchSave(users);
        } finally {
            assertNotEquals(users.size(), userRepository.count());
            assertEquals(4, userRepository.count());
        }
    }
}