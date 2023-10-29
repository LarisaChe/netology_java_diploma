package lache.controller;

import jakarta.servlet.http.HttpServletRequest;
import lache.authentication.AuthenticationRequest;
import lache.authentication.JwtTokenService;
import lache.authentication.JwtUserDetailsService;
import lache.repository.CloudAPIRepository;
import lache.service.CloudAPIService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class CloudAPIControllerTest {
    private static final String login = "test@test.ru";

    private Environment env = new MockEnvironment();

    private final CloudAPIController cloudAPIController = new CloudAPIController(new CloudAPIService(new CloudAPIRepository(env))
                    ,Mockito.mock(AuthenticationManager.class), Mockito.mock(JwtTokenService.class), Mockito.mock(JwtUserDetailsService.class));

    private static final MultipartFile multipartFile  = new MockMultipartFile("MultipartFileC.txt", "MultipartFileC.txt", "byte",
            new String("It's a Multipart File  for test CloudAPIController.").getBytes());

    @Test
    public void testC01_login() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setLogin("adm@mail.ru");
        authenticationRequest.setPassword("321");

        Assertions.assertDoesNotThrow(() -> cloudAPIController.authenticate(authenticationRequest));
    }

    @Test
    public void testC02_logout() {
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(requestMock.getHeader("auth-token")).thenReturn("Bearer test token");
        cloudAPIController.logout("logout", requestMock);
        //Assertions.assertDoesNotThrow(() -> cloudAPIController.logout("logout", requestMock));
        Assertions.assertTrue(JwtTokenService.getBlackTokens().contains("test token"));
    }


    @Test
    public void testC03_uploadFile() {
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        Mockito.when(authenticationMock.getName()).thenReturn(login);

        Assertions.assertDoesNotThrow(() -> cloudAPIController.uploadFile("MultipartFileC.txt", multipartFile, authenticationMock));
    }

    @Test
    public void testC04_getAllFiles() {
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        Mockito.when(authenticationMock.getName()).thenReturn(login);

        Assertions.assertDoesNotThrow(() -> cloudAPIController.getAllFiles(3, authenticationMock));
    }

    @Test
    public void testС05_editFile() {
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        Mockito.when(authenticationMock.getName()).thenReturn(login);

        Map<String, String> newFileName =new HashMap<>();
        newFileName.put("filename", "renamedFileC.txt");

        Assertions.assertDoesNotThrow(() -> cloudAPIController.editFile("MultipartFileC.txt", newFileName, authenticationMock));
    }

    @Test
    public void testС06_downloadFile() {
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        Mockito.when(authenticationMock.getName()).thenReturn(login);

        Assertions.assertDoesNotThrow(() -> cloudAPIController.downloadFile("renamedFileC.txt", authenticationMock));
    }

    @Test
    public void testС07_deleteFile()  {
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        Mockito.when(authenticationMock.getName()).thenReturn(login);

        Assertions.assertDoesNotThrow(() -> cloudAPIController.deleteFile("renamedFileC.txt", authenticationMock));
    }
}
