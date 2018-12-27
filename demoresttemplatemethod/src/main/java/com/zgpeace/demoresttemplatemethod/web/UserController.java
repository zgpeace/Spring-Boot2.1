package com.zgpeace.demoresttemplatemethod.web;

import com.zgpeace.demoresttemplatemethod.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping(value = "/testGet", method = RequestMethod.GET)
    public User testGet() {
        User user = new User();
        user.setId(1);
        user.setMethodName("get");
        return user;
    }

    @RequestMapping(value = "/testPost", method = RequestMethod.POST)
    public User testPost() {
        User user = new User();
        user.setId(1);
        user.setMethodName("post");
        return user;
    }

    @RequestMapping(value = "/testPostParam", method = RequestMethod.POST)
    public String testPostParam(@RequestParam("id") String id, @RequestParam("methodName") String methodName) {
        System.out.println("PostParam id: " + id);
        System.out.println("PostParam methodName: " + methodName);
        return "PostParam id{" + id + "} success";
    }

    @RequestMapping(value = "/testPut", method = RequestMethod.PUT)
    public String testPut(@RequestParam("id") String id, @RequestParam("methodName") String methodName) {
        System.out.println("put id: " + id);
        System.out.println("put methodName: " + methodName);
        return "put id{" + id + "} success";
    }

    @RequestMapping(value = "/testDel", method = RequestMethod.DELETE)
    public String testDel(@RequestParam("id") String id) {
        System.out.println("del id: " + id);
        return "del id{" + id + "} success";
    }

}
