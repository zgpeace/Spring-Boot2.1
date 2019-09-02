package com.zgpeace.demoredis.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@RestController
public class SessionController {

  @RequestMapping(value = "/uid", method = RequestMethod.GET)
  public String uid(HttpSession session) {
    UUID uid = (UUID) session.getAttribute("uid");
    if (uid == null) {
      uid = UUID.randomUUID();
    }
    session.setAttribute("uid", uid);
    return session.getId();
  }
}
