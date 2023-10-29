package lache.repository;

import lache.exception.ErrorDeleteFile;
import lache.exception.ErrorInputData;
import lache.exception.ErrorUploadFile;
import lache.model.FileListItem;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class CloudAPIRepositoryTest {

    private Environment env = new MockEnvironment();
    private static final String login = "test@test.ru";
    private static final String dir = "cloud";
    //private String dir;

    private final CloudAPIRepository cloudAPIRepository = new CloudAPIRepository(env);

    private static final MultipartFile multipartFile  = new MockMultipartFile("MultipartFile.txt", "MultipartFile.txt", "byte",
                                                            new String("It's a Multipart File  for test.").getBytes());

    private static byte[] b;
    private static final MultipartFile multipartFileEmpty  = new MockMultipartFile("EmptyMultipartFile.txt", b);

    /*public static void createFile(String fn, String str, boolean append) throws IOException {
        try (FileWriter writer = new FileWriter(fn, StandardCharsets.UTF_8, append)) {
            writer.write(str);
            writer.append('\n');
            writer.flush();
        }
    }
    @BeforeAll
    public static void testR00_createFileForTest() {
        Assertions.assertDoesNotThrow(() -> createFile("testFile.txt", "Hello, world! It's a file for test.", false));
        //multipartFile = new MockMultipartFile()
    }*/
    public static void deleteDirectory(File directory) {
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File file : contents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }

    @BeforeAll
    public static void testR00_clearDir() {
        File testDir = new File(Path.of(dir)
                .resolve(Paths.get(login))
                .normalize().toAbsolutePath().toString());
        deleteDirectory(testDir);
    }

    @Test
    public void testR01_getAllFiles_emptyList() {
        List<FileListItem> expectedResult = new ArrayList<>();
        List<FileListItem> actualResult = cloudAPIRepository.getAllFiles(login);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testR02_uploadFile() {
        Assertions.assertDoesNotThrow(() -> cloudAPIRepository.uploadFile(multipartFile, login));
    }

    @Test
    public void testR03_uploadFile_fileEmpty_throwsException() {
        Assertions.assertThrows(ErrorUploadFile.class, () -> {
            cloudAPIRepository.uploadFile(multipartFileEmpty, login);
        });
    }

    @Test
    public void testR04_getAllFiles() {
        String expectedResult = "[File{name='MultipartFile.txt', size=32}]";
        List<FileListItem> actualResult = cloudAPIRepository.getAllFiles(login);
        Assertions.assertEquals(expectedResult, actualResult.toString());
    }

    @Test
    public void testR05_editFile() {
        String expectedResult = "Success edit file name";
        String actualResult = cloudAPIRepository.editFile("MultipartFile.txt", "renamedFile.txt", login);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testR06_editFile_throwsException() {
        Assertions.assertThrows(ErrorInputData.class, () -> {
            cloudAPIRepository.editFile("MultipartFile.txt", "renamedFile.txt", login); // файла с таким именем уже нет, поэтому ошибка
        });
    }

    @Test
    public void testR07_editFile_nullOldFile_throwsException() {
        Assertions.assertThrows(ErrorInputData.class, () -> {
            cloudAPIRepository.editFile("", "renamedFile.txt", login);
        });
    }

    @Test
    public void testR08_editFile_nullNewFile_throwsException() {
        Assertions.assertThrows(ErrorInputData.class, () -> {
            cloudAPIRepository.editFile("renamedFile.txt", "", login);
        });
    }

    @Test
    public void testR09_downloadFile() throws IOException {
        byte[] expectedResult = new String("It's a Multipart File  for test.").getBytes();
        Resource actualResult = cloudAPIRepository.downloadFile("renamedFile.txt", login);
        Assertions.assertArrayEquals(expectedResult, actualResult.getContentAsByteArray());
    }

    @Test
    public void testR10_downloadFile_nullFileName_throwsException() {
        Assertions.assertThrows(ErrorUploadFile.class, () -> {
            cloudAPIRepository.downloadFile(null,  login);
        });
    }

    @Test
    public void testR11_downloadFile_emptyFileName_throwsException() {
        Assertions.assertThrows(ErrorUploadFile.class, () -> {
            cloudAPIRepository.downloadFile("",  login);
        });
    }

    @Test
    public void testR12_deleteFile_nullFileName_throwsException() {
        Assertions.assertThrows(ErrorDeleteFile.class, () -> {
            cloudAPIRepository.deleteFile(null,  login);
        });
    }

    @Test
    public void testR13_deleteFile_emptyFileName_throwsException() {
        Assertions.assertThrows(ErrorDeleteFile.class, () -> {
            cloudAPIRepository.deleteFile("",  login);
        });
    }

    @Test
    public void testR14_deleteFile()  {
        Assertions.assertDoesNotThrow(() -> cloudAPIRepository.deleteFile("renamedFile.txt", login));
    }

    @Test
    public void testR15_deleteFile_fileNoExist_throwsException() {
        Assertions.assertThrows(ErrorDeleteFile.class, () -> {
            cloudAPIRepository.deleteFile("renamedFile.txt",  login);
        });
    }
}
