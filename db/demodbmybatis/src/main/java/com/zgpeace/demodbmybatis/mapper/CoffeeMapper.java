package com.zgpeace.demodbmybatis.mapper;

import com.zgpeace.demodbmybatis.model.Coffee;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CoffeeMapper {
  @Insert("insert into t_coffee (name, price, create_time, update_time)"
      + "values (#{name}, #{price}, now(), now())")
  @Options(useGeneratedKeys = true)
  int save(Coffee coffee);

  @Select("select * from t_coffee where id = #{id}")
  @Results({
      @Result(id = true, column = "id", property = "id"),
      @Result(column = "create_time", property = "createTime"),
      // map-underscore-to-camel-case = true 可以实现一样的效果
      // @Result(column = "update_time", property = "updateTime"),
  })
  Coffee findById(@Param("id") Long id);
}
