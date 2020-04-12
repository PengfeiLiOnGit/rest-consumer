package com.jony.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rest")
public class RestConsumeController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Traverson traverson;

    @GetMapping("/traverson/add")
    /**
     * traverson 与 restTemplate 结合提交数据形式
     */
    public Map addByRestTemplateAndTraverson(){
        String addUrl = traverson.follow("country").asLink().getHref();
        Map<String,Object> data = new HashMap<>();
        data.put("","");
        Map result = restTemplate.postForObject(addUrl,data,Map.class);
        return result;
    }

    @GetMapping("/traversion")
    @ResponseBody
    public List getTraverson(){
        // 声明参数类型
        ParameterizedTypeReference<CollectionModel<Map>> reference = new ParameterizedTypeReference<CollectionModel<Map>>() {};
        // 查询traverson 数据
        CollectionModel<Map> model = traverson.follow("countries")
                .toObject(reference);
        // 获取数据
        List list = new ArrayList(model.getContent());
        return list;
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> getEntity() {
        Map<String, Object> resut = restTemplate.getForObject("http://localhost:9090/api/countries/2", Map.class);
        return resut;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> getEntityById(@PathVariable String id) {
        // 使用map参数查询
//        Map<String,String> params = new HashMap<>();
//        params.put("id",id);
//        Map<String, Object> resut = restTemplate.getForObject("http://localhost:9090/api/countries/{id}", Map.class,params);

        // 使用 可变参数配置url {id} 占位符
        Map<String, Object> resut = restTemplate.getForObject("http://localhost:9090/api/countries/{id}", Map.class, id);

        return resut;
    }

    @GetMapping("/entity/{id}")
    public ResponseEntity<Map> getEntity(@PathVariable String id){
        ResponseEntity entity = restTemplate.getForEntity("http://localhost:9090/api/countries/{id}",Map.class,id);
        return entity;
    }

    @PutMapping
    public void putEntity(@RequestBody Map<String,Object> map){
        restTemplate.put("http://localhost:9090/api/countries",map);
    }

}
