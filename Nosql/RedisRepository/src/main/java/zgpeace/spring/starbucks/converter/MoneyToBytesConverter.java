package zgpeace.spring.starbucks.converter;

import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.nio.charset.StandardCharsets;

@WritingConverter
public class MoneyToBytesConverter implements Converter<Money, byte[]> {
  @Override
  public byte[] convert(Money money) {
    String value = Long.toString(money.getAmountMinorLong());
    return value.getBytes(StandardCharsets.UTF_8);
  }
}
