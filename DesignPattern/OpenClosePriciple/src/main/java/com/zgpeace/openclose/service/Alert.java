package com.zgpeace.openclose.service;

import com.zgpeace.openclose.model.ApiStateInfo;

import java.util.ArrayList;
import java.util.List;

public class Alert {
  private List<AlertHandler> alertHandlers = new ArrayList<>();

  public void addAlertHandler(AlertHandler alertHandler) {
    this.alertHandlers.add(alertHandler);
  }

  public void check(ApiStateInfo apiStateInfo) {
    for (AlertHandler handler: alertHandlers) {
      handler.check(apiStateInfo);
    }
  }
}
