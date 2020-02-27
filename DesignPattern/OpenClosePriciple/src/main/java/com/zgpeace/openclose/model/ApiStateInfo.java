package com.zgpeace.openclose.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiStateInfo {
  private String api;
  private long requestCount;
  private long errorCount;
  private long durationOfSeconds;

}
