package com.zgpeace.demomybatis.service;

import com.zgpeace.demomybatis.bean.City;
import com.zgpeace.demomybatis.dao.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityMapper cityMapper;

    public City findById(int id) {
        return cityMapper.findById(id);
    }

    public List<City> findCityList() {
        return cityMapper.findCityList();
    }

    public int add(String name, String state, String country) {
        return cityMapper.add(name, state, country);
    }

    public int update(String name, String state, String country, int id) {
        return cityMapper.update(name, state, country, id);
    }

    public int delete(int id) {
        return cityMapper.delete(id);
    }
}
