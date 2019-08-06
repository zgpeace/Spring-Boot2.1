package com.zgpeace.demomybatis.bean;

import java.io.Serializable;

public class City implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String name;
  private String state;
  private String country;

  @Override
  public String toString() {
    return String.format(
        "City[id=%d, name='%s', state='%s', country='%s']",
        id, name, state, country
    );
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
