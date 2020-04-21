package com.example.meeting.meeting;

import java.util.HashMap;

import com.example.meeting.meeting.entities.UserRepository;
import com.example.meeting.requests.*;
import com.example.meeting.requests.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/api/v1")
public class UserRequests {

    @Autowired
    public
    UserRepository userRepository;

    @RequestMapping(path = "/user")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    HashMap<String, Object> registerUser(HttpServletRequest request) {
        HashMap<String, Object> responseMap = new HashMap<>();
        HashMap<String, Object> response = new HashMap<>();
        PostRequest postRequest = new PostRequest();
        GetRequest getRequest = new GetRequest();
        PutRequest putRequest = new PutRequest();
        DeleteRequest deleteRequest = new DeleteRequest();
        try {
            switch (request.getMethod()) {
                case "POST": {
                    response = (HashMap<String, Object>) postRequest.invoke("addUser", request, userRepository);
                    break;
                }
                case "GET": {
                    response = (HashMap<String, Object>) getRequest.invoke("getUser", request, userRepository);
                    break;
                }
                case "PUT": {
                    response = (HashMap<String, Object>) putRequest.invoke("updateUser", request, userRepository);
                    break;
                }
                case "DELETE": {
                    response = (HashMap<String, Object>) deleteRequest.invoke("deleteUser", request, userRepository);
                    break;
                }
                default:
                    throw new InvalidDataException("Invalid method requested");
            }
        } catch (Exception e) {
            responseMap = RequestUtil.generateErrorResponse(e);
            return responseMap;
        }
        responseMap.put("code", 1000);
        responseMap.put("data", response);
        return responseMap;
    }
}

