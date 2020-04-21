package com.example.meeting.requests;

import com.example.meeting.meeting.entities.User;
import com.example.meeting.meeting.entities.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;

public class DeleteRequest {
    HashMap<String, Object> requestMap = new HashMap<>();
    Object repository;

    public Object invoke(String methodName, HttpServletRequest request, Object repository) throws Exception {
        if ("DELETE".compareTo(request.getMethod()) != 0) {
            throw new Exception("Not a DELETE request, try " + request.getMethod().toLowerCase() + "Request.invoke()");
        }
        Method method = DeleteRequest.class.getMethod(methodName);
        this.requestMap = RequestUtil.generateRequestMap(request.getParameter("JSONString"));
        this.repository = repository;
        return method.invoke(this);
    }

    //Todo: ADD FUNCTIONS FOR NEW FEATURE WITHOUT PARAMETER
    public Object deleteUser() throws Exception {
        UserRepository userRepository = (UserRepository) repository;
        long currentTime = RequestUtil.getTimeInMillisecond();
        User user = RequestUtil.getUserData(requestMap, userRepository);
        user.setActive(false);
        user.setBusy(false);
        user.setDeleted(true);
        user.setUpdatedAt(currentTime);
        userRepository.save(user);
        return RequestUtil.generateUserResponseMap(user);
    }
}
