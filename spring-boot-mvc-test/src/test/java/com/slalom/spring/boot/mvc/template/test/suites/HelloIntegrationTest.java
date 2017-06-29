package com.slalom.spring.boot.mvc.template.test.suites;

import com.slalom.spring.boot.mvc.template.test.categories.Integration;
import com.slalom.spring.boot.mvc.template.test.config.Config;
import com.slalom.spring.boot.mvc.template.test.util.HeadersFactory;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class, loader = AnnotationConfigContextLoader.class)
public class HelloIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HeadersFactory headersFactory;

    @Value("${host}")
    private String host;

    @Test
    @Category(Integration.class)
    public void helloReturnsProperGreeting() {
        //arrange
        String name = "You";

        //act
        Map<String, Object> actual = restTemplate.exchange(
                this.host + "/hello/{1}",
                HttpMethod.GET, new HttpEntity<>(null, headersFactory.getHeaders()),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }, name).getBody();

        //assert
        assertEquals("Hello You!", actual.get("greeting"));
    }

    @Test
    @Category(Integration.class)
    public void helloReturnsNotFound() {
        //act
        try {
            restTemplate.exchange(
                    this.host + "/hello",
                    HttpMethod.GET,
                    new HttpEntity<>(null, headersFactory.getHeaders()),
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });
        } catch (HttpStatusCodeException hsce) {
            assertEquals(HttpStatus.NOT_FOUND, hsce.getStatusCode());
        }
    }
}

