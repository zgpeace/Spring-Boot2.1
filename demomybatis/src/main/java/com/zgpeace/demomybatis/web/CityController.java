package com.zgpeace.demomybatis.web;

import com.zgpeace.demomybatis.bean.City;
import com.zgpeace.demomybatis.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/list")
    public List<City> getCitys() {
        return cityService.findCityList();
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable("id") int id) {
        return cityService.findById(id);
    }

    @PutMapping("/{id}")
    public String updateCity(@PathVariable("id") int id,
                             @RequestParam(value = "name", required = true) String name,
                             @RequestParam(value = "state", required = true) String state,
                             @RequestParam(value = "country", required = true) String country) {
        int count = cityService.update(name, state, country, id);
        if (count == 1) {
            return "update success";
        } else  {
            return "update fail";
        }
    }

    @PostMapping("")
    public String addCity(@RequestParam(value = "name", required = true) String name,
                          @RequestParam(value = "state", required = true) String state,
                          @RequestParam(value = "country", required = true) String country) {
        int count = cityService.add(name, state, country);
        if (count == 1) {
            return "add success";
        } else {
            return "add fail";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(value = "id") int id) {
        int count = cityService.delete(id);
        if (count == 1) {
            return "delete success";
        } else {
            return "delete fail";
        }
    }

    @Transactional
    @GetMapping("/addTwoCity")
    public String addTwoCity() throws RuntimeException {
        cityService.add("HangZhou", "ZheJiang", "China");
        int temp = 1/0;
        cityService.add("WenZhou", "ZheJiang", "China");

        return "both two success";
    }


}
