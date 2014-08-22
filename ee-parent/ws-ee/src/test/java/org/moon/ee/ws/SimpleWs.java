package org.moon.ee.ws;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@Stateless
@WebService(serviceName = "Simple", targetNamespace = "http://www.moon.com/simple")
public class SimpleWs implements Simple {

	@Override
	@WebMethod
	public String sayHi(@WebParam(name = "name") String name) {
		return "Hi " + name;
	}

}