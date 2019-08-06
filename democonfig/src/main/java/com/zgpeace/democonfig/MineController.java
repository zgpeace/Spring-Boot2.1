package com.zgpeace.democonfig;

import com.zgpeace.democonfig.bean.ConfigBean;
import com.zgpeace.democonfig.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableConfigurationProperties({ConfigBean.class, User.class})
public class MineController {

  @Autowired
  ConfigBean configBean;
  @Autowired
  User user;

  @Value("${account.name}")
  private String name;
  @Value("${account.age}")
  private int age;

  @RequestMapping(value = "/author")
  public String mine() {
    return name + ": " + age;
  }

  @RequestMapping(value = "/lucy")
  public String person() {
    return configBean.getGreeting() + " >>>" + configBean.getName() + " >>>" + configBean.getUuid() + " >>>" + configBean.getMax();
  }

  @RequestMapping(value = "/user")
  public String user() {
    return "hobby: " + user.getHobby() + " ; fruit: " + user.getFruit();
  }
}































