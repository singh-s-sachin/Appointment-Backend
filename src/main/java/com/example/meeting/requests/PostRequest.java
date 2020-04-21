package com.example.meeting.requests;

import com.example.meeting.meeting.entities.UserRepository;
import com.example.meeting.meeting.entities.entityUtil.UserTable;
import com.example.meeting.requests.exception.InvalidDataException;


import com.example.meeting.meeting.entities.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;

@Service
public class PostRequest {
    HashMap<String, Object> requestMap = new HashMap<>();
    Object repository;

    public Object invoke(String methodName, HttpServletRequest request, Object repository) throws Exception {
        if ("POST".compareTo(request.getMethod()) != 0) {
            throw new Exception("Not a POST request, try " + request.getMethod().toLowerCase() + "Request.invoke()");
        }
        Method method = PostRequest.class.getMethod(methodName);
        this.requestMap = RequestUtil.generateRequestMap(request.getParameter("JSONString"));
        this.repository = repository;
        return method.invoke(this);
    }

    //Todo: ADD FUNCTIONS FOR NEW FEATURE WITHOUT PARAMETER
    public Object addUser() throws Exception {
        User user = new User();
        UserRepository userRepository = (UserRepository) repository;
        long currentTime = RequestUtil.getTimeInMillisecond();
        if (requestMap.get(UserTable.FIRST_NAME) == null || requestMap.get(UserTable.LAST_NAME) == null || requestMap.get("email") == null || requestMap.get("country") == null || requestMap.get(UserTable.MOBILE) == null) {
            throw new InvalidDataException("One or more mandatory field(s) is/are left null");
        }
        if (RequestUtil.isEmailRegistered(requestMap.get("email").toString(), userRepository)) {
            if(RequestUtil.isDeletedUser(requestMap.get("email").toString(), userRepository))
            {
                user= RequestUtil.reRegisterUser(requestMap.get("email").toString(), userRepository);
                return RequestUtil.generateUserResponseMap(user);
            }
            throw new InvalidDataException("Email already registered");
        }
        user.setFirstName(requestMap.get(UserTable.FIRST_NAME).toString());
        user.setLastName(requestMap.get(UserTable.LAST_NAME).toString());
        if (RequestUtil.isValidMobile(requestMap.get(UserTable.MOBILE).toString())) {
            user.setMobile(requestMap.get(UserTable.MOBILE).toString());
        } else {
            throw new InvalidDataException("Invalid mobile number provided");
        }
        if (RequestUtil.isValidEmail(requestMap.get(UserTable.EMAIL).toString())) {
            user.setEmail(requestMap.get(UserTable.EMAIL).toString());
        } else {
            throw new InvalidDataException("Invalid email provided");
        }
        if (RequestUtil.isCountry(requestMap.get(UserTable.COUNTRY).toString())) {
            user.setCountry(requestMap.get(UserTable.COUNTRY).toString());
            user.setZip(requestMap.get(UserTable.ZIP) != null ? (Integer) requestMap.get(UserTable.ZIP) : 0);
        } else {
            throw new InvalidDataException("Invalid Country/ZIP provided");
        }
        RequestUtil.setLocation(user, requestMap);
        user.setBusy(false);
        user.setActive(true);
        user.setCreatedAt(currentTime);
        user.setUpdatedAt(currentTime);
        userRepository.save(user);
        return RequestUtil.generateUserResponseMap(user);
    }
}
