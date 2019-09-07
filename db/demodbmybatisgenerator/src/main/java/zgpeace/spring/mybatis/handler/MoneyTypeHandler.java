package zgpeace.spring.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * exchange data type between Money and BigInt for 'CNY'
 */
public class MoneyTypeHandler extends BaseTypeHandler<Money> {
  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Money parameter, JdbcType jdbcType) throws SQLException {
    ps.setLong(i, parameter.getAmountMinorLong());
  }

  @Override
  public Money getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return parseMoney(rs.getLong(columnName));
  }

  @Override
  public Money getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return parseMoney(rs.getLong(columnIndex));
  }

  @Override
  public Money getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return parseMoney(cs.getLong(columnIndex));
  }

  private Money parseMoney(Long value) {
    return Money.of(CurrencyUnit.of("CNY"), value / 100.0);
  }
}