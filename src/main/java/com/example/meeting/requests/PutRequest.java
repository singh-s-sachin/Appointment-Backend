package com.example.meeting.requests;

import com.example.meeting.meeting.entities.User;
import com.example.meeting.meeting.entities.UserRepository;
import com.example.meeting.meeting.entities.entityUtil.UserTable;
import com.example.meeting.requests.exception.InvalidDataException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;

public class PutRequest {
    HashMap<String, Object> requestMap = new HashMap<>();
    Object repository;

    public Object invoke(String methodName, HttpServletRequest request, Object repository) throws Exception {
        if ("PUT".compareTo(request.getMethod()) != 0) {
            throw new Exception("Not a PUT request, try " + request.getMethod().toLowerCase() + "Request.invoke()");
        }
        Method method = PutRequest.class.getMethod(methodName);
        this.requestMap = RequestUtil.generateRequestMap(request.getParameter("JSONString"));
        this.repository = repository;
        return method.invoke(this);
    }

    //Todo: ADD FUNCTIONS FOR NEW FEATURE WITHOUT PARAMETER
    public Object updateUser() throws Exception {
        UserRepository userRepository = (UserRepository) repository;
        boolean isUpdated=false;
        long currentTime = RequestUtil.getTimeInMillisecond();
        User user = RequestUtil.getUserData(requestMap,userRepository);
        if(requestMap.containsKey(UserTable.FIRST_NAME))
        {
            user.setFirstName(requestMap.get(UserTable.FIRST_NAME).toString());
            isUpdated = true;
        }
        if(requestMap.containsKey(UserTable.LAST_NAME))
        {
            user.setLastName(requestMap.get(UserTable.LAST_NAME).toString());
            isUpdated = true;
        }
        if(requestMap.containsKey(UserTable.MOBILE))
        {
            user.setMobile(requestMap.get(UserTable.MOBILE).toString());
            isUpdated = true;
        }
        if(requestMap.containsKey(UserTable.COUNTRY))
        {
            user.setCountry(requestMap.get(UserTable.COUNTRY).toString());
            isUpdated = true;
        }
        if(requestMap.containsKey(UserTable.ZIP))
        {
            user.setZip((Integer) requestMap.get(UserTable.ZIP));
            isUpdated = true;
        }
        if(requestMap.containsKey(UserTable.ACTIVE))
        {
            user.setActive((boolean) requestMap.get(UserTable.ACTIVE));
            isUpdated = true;
        }
        if(requestMap.containsKey(UserTable.BUSY))
        {
            user.setActive((boolean) requestMap.get(UserTable.BUSY));
            isUpdated = true;
        }
        if(requestMap.containsKey(UserTable.LATITUDE)&&requestMap.containsKey(UserTable.LONGITUDE))
        {
            RequestUtil.setLocation(user,requestMap);
            isUpdated = true;
        }
        if (isUpdated)
        {
            user.setUpdatedAt(currentTime);
            userRepository.save(user);
            return RequestUtil.generateUserResponseMap(user);
        }
        else
        {
            throw new InvalidDataException("Fields requested dose'nt exists or can't be updated.");
        }
    }
}
