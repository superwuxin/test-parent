package org.harmony.test.java.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.harmony.test.java.User;
import org.harmony.test.java.jmx.mxbean.Service;

import com.sun.jdmk.comm.HtmlAdaptorServer;

public class ServiceMXBeanTest {

    public static void main(String[] args) throws Exception {
        //MBeanServer server = MBeanServerFactory.createMBeanServer();
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        Service service = new Service();
        service.setUser(new User(12l, "abc"));
        server.registerMBean(service, new ObjectName("jmx:type=SimpleService"));
        HtmlAdaptorServer adapter = new HtmlAdaptorServer(8080);
        server.registerMBean(adapter, new ObjectName("base_domain:type=HtmlAdapter"));
        adapter.start();
        Thread.sleep(Long.MAX_VALUE);
    }
}
