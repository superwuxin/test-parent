package org.harmony.test.cxf;


public class SimpleWs implements Simple {

    @Override
    public String sayHi(String name) {
        return "Hi " + name;
    }

}
