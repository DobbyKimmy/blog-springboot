package com.github.springbootdemo.demo.integration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.github.springbootdemo.demo.Application;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
// 因为我们的集成测试需要启动应用，这也是集成测试非常昂贵的原因
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// 集成测试我们使用的是宿主机的3307端口，需要重新的配置文件
@TestPropertySource(locations = "classpath:test.properties")
public class MyIntegrationTest {
    // 集成测试
    // 需要拿到端口
    @Inject
    Environment environment;

    @Test
    public void notLoggedInByDefault() throws IOException, InterruptedException {
        String port = environment.getProperty("local.server.port");
        System.out.println(port);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/auth"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertTrue(response.body().contains("用户没有登录"));
        Assertions.assertEquals(200,response.statusCode());
    }

    @Test
    public void canRegisterNewUser() throws IOException, InterruptedException {
        String port = environment.getProperty("local.server.port");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 注册
            HttpPost httpPost = new HttpPost("http://localhost:" + port + "/auth/register");
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("username", "zhangsan");
            requestMap.put("password", "zhangsan");
            String entity = JSON.toJSONString(requestMap);
            httpPost.setEntity(new StringEntity(entity,ContentType.APPLICATION_JSON));
            httpclient.execute(httpPost, (ResponseHandler<String>) httpResponse -> {
                Assertions.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
                String response = EntityUtils.toString(httpResponse.getEntity());
                Assertions.assertTrue(response.contains("success"));
                return null;
            });

            // 登录
            httpPost = new HttpPost("http://localhost:" + port + "/auth/login");
            Map<String, String> requestMap2 = new HashMap<>();
            requestMap.put("username", "zhangsan");
            requestMap.put("password", "zhangsan");
            String entity2 = JSON.toJSONString(requestMap);
            httpPost.setEntity(new StringEntity(entity,ContentType.APPLICATION_JSON));
            httpclient.execute(httpPost, (ResponseHandler<String>) httpResponse -> {
                Assertions.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
                String response = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(response);
                Assertions.assertTrue(response.contains("登录成功"));
                return null;
            });
        }finally {
            httpclient.close();
        }
    }
}
