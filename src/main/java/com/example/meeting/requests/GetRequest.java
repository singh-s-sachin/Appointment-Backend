package com.example.meeting.requests;

import com.example.meeting.meeting.entities.User;
import com.example.meeting.meeting.entities.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;

public class GetRequest {
    HashMap<String, Object> requestMap = new HashMap<>();
    Object repository;

    public Object invoke(String methodName, HttpServletRequest request, Object repository) throws Exception {
        if ("GET".compareTo(request.getMethod()) != 0) {
            throw new Exception("Not a GET request, try " + request.getMethod().toLowerCase() + "Request.invoke()");
        }
        Method method = GetRequest.class.getMethod(methodName);
        this.requestMap = RequestUtil.generateRequestMap(request.getParameter("JSONString"));
        this.repository = repository;
        return method.invoke(this);
    }

    //Todo: ADD FUNCTIONS FOR NEW FEATURE WITHOUT PARAMETER
    public Object getUser() throws Exception {
        UserRepository userRepository = (UserRepository) repository;
        User user=RequestUtil.getUserData(requestMap,userRepository);
        return RequestUtil.generateUserResponseMap(user);
    }
}
