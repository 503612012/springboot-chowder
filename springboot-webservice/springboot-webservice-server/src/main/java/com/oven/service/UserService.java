package com.oven.service;

import com.oven.vo.User;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = "wsdl.oven.com", name = "userPortType")
public interface UserService {

    @WebMethod(operationName = "getUserByName")
    User getUserByName(@WebParam(name = "name") String name);

    @WebMethod
    List<User> getList();

    @WebMethod
    String getString(@WebParam(name = "msg") String msg);

}
