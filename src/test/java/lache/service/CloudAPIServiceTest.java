package lache.service;

import lache.repository.CloudAPIRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class CloudAPIServiceTest {

    private Environment env = new MockEnvironment();
    private static final String login = "test@test.ru";
    private final CloudAPIService cloudAPIService = new CloudAPIService(new CloudAPIRepository(env));

    private static final MultipartFile multipartFile  = new MockMultipartFile("MultipartFileS.txt", "MultipartFileS.txt", "byte",
            new String("It's a Multipart File  for test cloudAPIService.").getBytes());

    @Test
    public void testS01_uploadFile() {
        Assertions.assertDoesNotThrow(() -> cloudAPIService.uploadFile(multipartFile, login));
    }

    @Test
    public void testS02_getAllFiles() {
        Assertions.assertDoesNotThrow(() -> cloudAPIService.getAllFiles(login));
    }

    @Test
    public void testS03_editFile() {
        Assertions.assertDoesNotThrow(() -> cloudAPIService.editFile("MultipartFileS.txt", "renamedFileS.txt", login));
    }

    @Test
    public void testS04_downloadFile() {
        Assertions.assertDoesNotThrow(() -> cloudAPIService.downloadFile("renamedFileS.txt", login));
    }

    @Test
    public void testS05_deleteFile()  {
        Assertions.assertDoesNotThrow(() -> cloudAPIService.deleteFile("renamedFileS.txt", login));
    }

}
