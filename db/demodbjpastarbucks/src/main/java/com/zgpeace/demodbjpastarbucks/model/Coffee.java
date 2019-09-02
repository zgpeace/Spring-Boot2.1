package com.zgpeace.demodbjpastarbucks.model;

import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "T_MENU")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Coffee extends BaseEntity implements Serializable {
  private String name;
  @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount"
      , parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
  private Money price;
}
