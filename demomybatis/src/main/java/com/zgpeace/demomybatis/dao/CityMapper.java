package com.zgpeace.demomybatis.dao;

import com.zgpeace.demomybatis.bean.City;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CityMapper {

    @Select("SELECT id, name, state, country FROM city WHERE state = #{state}")
    City findByState(String state);

    @Select("SELECT id, name, state, country FROM city WHERE id = #{id}")
    City findById(@Param("id") int id);

    @Select("SELECT id, name, state, country FROM city")
    List<City> findCityList();

    @Insert("insert into city(name, state, country) values(#{name}, #{state}, #{country})")
    int add(@Param("name") String name, @Param("state") String state, @Param("country") String country);

    @Update("update city set name = #{name}, state = #{state}, country = #{country} where id = #{id}")
    int update(@Param("name") String name, @Param("state") String state, @Param("country") String country, @Param("id") int id);

    @Delete("delete from city where id = #{id}")
    int delete(int id);

}
