package com.example.meeting.requests;

import com.example.meeting.meeting.entities.User;
import com.example.meeting.meeting.entities.UserRepository;
import com.example.meeting.meeting.entities.entityUtil.UserTable;
import com.example.meeting.requests.exception.InvalidDataException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtil {
    public static List<String> COUNTRY_LIST = Arrays.asList(RequestConstants.COUNTRY);

    public static boolean isCountry(String countryName) {
        return COUNTRY_LIST.contains(countryName);
    }

    public static HashMap<String, Object> generateRequestMap(String JSONString) throws JSONException {
        HashMap<String, Object> response = new HashMap();
        JSONObject jObject = new JSONObject(JSONString);
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object value = jObject.get(key);
            response.put(key, value);
        }
        return response;
    }

    public static HashMap<String, Object> generateErrorResponse(Exception e) {
        HashMap<String, Object> responseMap = new HashMap();
        responseMap.put("code", 0);
        responseMap.put("message", "An error occured");
        switch (e.getClass().getName()) {
            case "org.springframework.dao.DataIntegrityViolationException": {
                responseMap.put("description", "Incorrect data provided");
                break;
            }
            case "java.lang.NullPointerException": {
                responseMap.put("description", "Null pointer exception occured.");
                break;
            }
            default:
                responseMap.put("description", e.getCause().getMessage() == null ? "Internal error" : e.getCause().getMessage());
        }

        return responseMap;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {

        }
        return pat.matcher(email).matches();
    }

    public static boolean isValidMobile(String mobile) {
        Pattern p = Pattern.compile("(0/91)?[6-9][0-9]{9}");
        Matcher m = p.matcher(mobile);
        return (m.find() && m.group().equals(mobile));
    }

    public static long getTimeInMillisecond() {
        Date date = new Date();
        return date.getTime();
    }

    public static void setLocation(User user, HashMap<String, Object> requestMap) {
        if (requestMap.get(UserTable.LATITUDE) == null || requestMap.get(UserTable.LONGITUDE) == null) {
            user.setLatitude(0.000);
            user.setLongitude(0.000);
        } else {
            user.setLatitude((Double) requestMap.get(UserTable.LATITUDE));
            user.setLongitude((Double) requestMap.get(UserTable.LONGITUDE));
        }
    }

    public static boolean isEmailRegistered(String email, UserRepository userRepository) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        return true;
    }

    public static HashMap<String, Object> generateUserResponseMap(User user) {
        HashMap<String, Object> response = new HashMap<>();
        response.put(UserTable.USERID, user.getId());
        response.put(UserTable.FIRST_NAME, user.getFirstName());
        response.put(UserTable.LAST_NAME, user.getLastName());
        response.put(UserTable.EMAIL, user.getEmail());
        response.put(UserTable.MOBILE, user.getMobile());
        response.put(UserTable.ZIP, user.getZip());
        response.put(UserTable.COUNTRY, user.getCountry());
        response.put(UserTable.LATITUDE, user.getLatitude());
        response.put(UserTable.LONGITUDE, user.getLongitude());
        response.put(UserTable.ACTIVE, user.isActive());
        response.put(UserTable.DELETED, user.isDeleted());
        response.put(UserTable.BUSY, user.isBusy());
        return response;
    }

    public static User getUserData(HashMap<String, Object> requestMap, UserRepository userRepository) throws InvalidDataException {
        User user = null;
        Optional<User> userOptional = null;
        if (requestMap.containsKey(UserTable.EMAIL)) {
            user = userRepository.findByEmail(requestMap.get(UserTable.EMAIL).toString());
            if (user != null && !user.isDeleted()) {
                return user;
            }
        } else if (requestMap.containsKey(UserTable.USERID)) {
            userOptional = userRepository.findById(Long.parseLong(requestMap.get(UserTable.USERID).toString()));
            if (userOptional.isPresent() && !userOptional.get().isDeleted()) {
                return userOptional.get();
            }
        } else {
            throw new InvalidDataException("User email or user_id required to fetch user data");
        }
        throw new InvalidDataException("User not found");
    }


    public static boolean isDeletedUser(String email, UserRepository userRepository) throws InvalidDataException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.isDeleted();
        }
        throw new InvalidDataException("User not found");
    }

    public static User reRegisterUser(String email, UserRepository userRepository) throws InvalidDataException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setDeleted(false);
            user.setActive(true);
            user.setUpdatedAt(RequestUtil.getTimeInMillisecond());
            userRepository.save(user);
            return user;
        }
        throw new InvalidDataException("User not found");
    }
}
