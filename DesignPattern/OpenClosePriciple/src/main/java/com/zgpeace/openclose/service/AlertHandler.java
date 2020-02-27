package com.zgpeace.openclose.service;

import com.zgpeace.openclose.model.ApiStateInfo;

public abstract class AlertHandler {
  protected AlertRule rule;
  protected Notification notification;
  public AlertHandler(AlertRule rule, Notification notification) {
    this.rule = rule;
    this.notification = notification;
  }

  public abstract void check(ApiStateInfo apiStateInfo);
}
