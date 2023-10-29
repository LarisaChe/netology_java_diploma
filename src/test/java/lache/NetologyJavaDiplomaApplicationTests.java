package lache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lache.authentication.AuthenticationRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class NetologyJavaDiplomaApplicationTests {


    @Autowired
    private TestRestTemplate testRestTemplate;
    @Container
    private static final GenericContainer<?> tmApp = new GenericContainer<>("cloud:latest")
            .withExposedPorts(8090);

    private final HttpHeaders headers = new HttpHeaders();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String clodUrl = "http://localhost:8090";

    private static AuthenticationRequest user = new AuthenticationRequest();

    private static String ttt;

    private static JsonNode ddd;

    @BeforeAll
    public static void beforeTest() {
        user.setLogin("test@test.ru");
        user.setPassword("123");
    }

    @Test
    public void test01() throws JsonProcessingException {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(user), headers);

        ResponseEntity<String> responseEntityStr = testRestTemplate.
                postForEntity(clodUrl+"/login", request, String.class);
        JsonNode root = objectMapper.readTree(responseEntityStr.getBody());

        String token = root.get("auth-token").toString();

        Assertions.assertTrue(token!=null && !token.isEmpty());
    }

}
