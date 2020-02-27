package com.zgpeace.openclose.controller;

import com.zgpeace.openclose.model.ApiStateInfo;
import com.zgpeace.openclose.service.Alert;
import com.zgpeace.openclose.service.AlertHandler;
import com.zgpeace.openclose.service.ErrorAlertHandler;
import com.zgpeace.openclose.service.TpsAlertHandler;

public class ApplicationContext {
  private AlertRule alertRule;
  private Notification notification;
  private Alert alert;

  public void initializeBeans() {
    alertRule = new AlertRule();
    notification = new Notification();
    alert = new Alert();
    alert.addAlertHandler(new TpsAlertHandler(alertRule, notification));
    alert.addAlertHandler(new ErrorAlertHandler(alertRule, notification));

  }

  public Alert getAlert() {
    return alert;
  }

  private static final ApplicationContext instance = new ApplicationContext();
  private ApplicationContext() {
    instance.initializeBeans();
  }

  public static ApplicationContext getInstance() {
    return instance;
  }


}

public class Demo {
  public static void main(String[] args) {
    ApiStateInfo apiStateInfo = new ApiStateInfo();
    ApplicationContext.getInstance().getAlert().check(apiStateInfo);
  }
}








































