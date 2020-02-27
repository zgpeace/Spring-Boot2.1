package com.zgpeace.openclose.service;

import com.zgpeace.openclose.model.ApiStateInfo;

public class ErrorAlertHandler extends AlertHandler {
  public ErrorAlertHandler(AlertRule rule, Notification notification) {
    super(rule, notification);
  }

  @Override
  public void check(ApiStateInfo apiStateInfo) {
    if (apiStateInfo.getErrorCount() > rule.getMatchedRule(apiStateInfo.getApi()).getMaxErrorCount()) {
      notification.notify(NotificationEmergencyLevel.SERVER, "...");
    }
  }
}
