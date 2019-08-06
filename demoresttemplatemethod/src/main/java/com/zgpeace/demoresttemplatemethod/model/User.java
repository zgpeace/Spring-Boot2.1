package com.zgpeace.demoresttemplatemethod.model;

public class User {

  private Integer id;
  private String methodName;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", methodName='" + methodName + '\'' +
        '}';
  }
}

