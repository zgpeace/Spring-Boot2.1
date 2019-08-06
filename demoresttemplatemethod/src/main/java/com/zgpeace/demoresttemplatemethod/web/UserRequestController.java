package com.zgpeace.demoresttemplatemethod.web;

import com.zgpeace.demoresttemplatemethod.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class UserRequestController {

  @Autowired
  private RestTemplate restTemplate;

  private static String PROTOCOL = "http";
  private static String HOST = "localhost";
  private static String PORT = "8080";
  private static String PRE_URL = PROTOCOL + "://" + HOST + ":" + PORT + "/";

  private static String GET_URL = PRE_URL + "testGet";
  private static String POST_URL = PRE_URL + "testPost";
  private static String POST_PARAM_URL = PRE_URL + "testPostParam";
  private static String PUT_URL = PRE_URL + "testPut";
  private static String DEL_URL = PRE_URL + "testDel";

  @GetMapping("/requestTestGet")
  public String requestTestGet() throws URISyntaxException {
    // 1. getForObject()
    User user1 = restTemplate.getForObject(GET_URL, User.class);
    System.out.println("get user1: " + user1);

    // 2. getForEntity()
    ResponseEntity<User> responseEntity1 = restTemplate.getForEntity(GET_URL, User.class);
    HttpStatus statusCode = responseEntity1.getStatusCode();
    HttpHeaders header = responseEntity1.getHeaders();
    User user2 = responseEntity1.getBody();
    System.out.println("get user2: " + user2);
    System.out.println("get statusCode: " + statusCode);
    System.out.println("get header: " + header);

    // 3. exchange()
    RequestEntity requestEntity = RequestEntity.get(new URI(GET_URL)).build();
    ResponseEntity<User> responseEntity2 = restTemplate.exchange(requestEntity, User.class);
    User user3 = responseEntity2.getBody();
    System.out.println("get user3: " + user3);

    return "requestTestGet";
  }

  @GetMapping("/requestTestPost")
  public String requestTestPost() throws URISyntaxException {
    HttpHeaders headers = new HttpHeaders();
    String data = new String();
    HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);

    // 1. postForObject()
    User user1 = restTemplate.postForObject(POST_URL, formEntity, User.class);
    System.out.println("post user1: " + user1);

    // 2. postForEntity()
    ResponseEntity<User> responseEntity1 = restTemplate.postForEntity(POST_URL, formEntity, User.class);
    HttpStatus statusCode = responseEntity1.getStatusCode();
    HttpHeaders header = responseEntity1.getHeaders();
    User user2 = responseEntity1.getBody();
    System.out.println("post user2: " + user2);
    System.out.println("post statusCode: " + statusCode);
    System.out.println("post header: " + header);

    // 3. exchange()
    RequestEntity requestEntity = RequestEntity.post(new URI(POST_URL)).body(formEntity);
    ResponseEntity<User> responseEntity2 = restTemplate.exchange(requestEntity, User.class);
    User user3 = responseEntity2.getBody();
    System.out.println("post user3: " + user3);

    return "requestTestPost";
  }

  @GetMapping("/requestTestPostParam")
  public String requestTestPostParam() {
    HttpHeaders headers = new HttpHeaders();
    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.add("id", "100");
    map.add("methodName", "requestTestPostParam");
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

    String data = restTemplate.postForObject(POST_PARAM_URL, request, String.class);
    System.out.println("requestTestPostParam data: " + data);
    System.out.println("requestTestPostParam success");

    return "requestTestPostParam";
  }

  @GetMapping("/requestTestPut")
  public String requestTestPut() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.add("id", "101");
    map.add("methodName", "requestTestPut");
    restTemplate.put(PUT_URL, map);
    System.out.println("requestTestPut success");

    return "requestTestPut";
  }

  @GetMapping("/requestTestDel")
  public String requestTestDel() throws URISyntaxException {
    HttpHeaders headers = new HttpHeaders();
    //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.add("id", "101");
    map.add("methodName", "requestTestDel");
    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
    // 方法一
    ResponseEntity<String> resp = restTemplate.exchange(DEL_URL, HttpMethod.DELETE, requestEntity, String.class, 227);
    System.out.println("requestTestDel response: " + resp.getBody());

    // 方法二
//        restTemplate.delete(DEL_URL + "?id={id}", 102);
    System.out.println("requestTestDel success");

    return "requestTestDel";
  }

}
