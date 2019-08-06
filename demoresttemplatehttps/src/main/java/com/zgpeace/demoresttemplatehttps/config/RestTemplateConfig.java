package com.zgpeace.demoresttemplatehttps.config;

//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.ssl.SSLContextBuilder;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    // unable to find valid certification path to requested target
        /*
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
        */

    // 401 Unauthorized
        /*
        String host = "localhttps";
        int port = 23333;
        String user = "john";
        String password = "123456";
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(host, port),
                new UsernamePasswordCredentials(user, password));
        */


    CloseableHttpClient httpClient
        = HttpClients.custom()
        .setSSLHostnameVerifier(new NoopHostnameVerifier())
//                .setSSLSocketFactory(sslConnectionSocketFactory)
//                .setDefaultCredentialsProvider(credsProvider)
        .build();
    HttpComponentsClientHttpRequestFactory requestFactory
        = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(httpClient);
    RestTemplate restTemplate = new RestTemplate(requestFactory);

    return restTemplate;
  }
}