package lache.repository;

import lache.exception.ErrorDeleteFile;
import lache.exception.ErrorUploadFile;
import lache.model.FileListItem;
import lache.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Repository
public class CloudAPIRepository {

    @Autowired
    private Environment env;
    private String dir;

    public CloudAPIRepository(Environment env) {
        this.env = env;
        this.dir = env.getProperty("cloud-storage-directory");
        if (dir == null || dir.isEmpty()) {
            dir = "cloud";
        }
    }

    public void uploadFile(MultipartFile file, String login) {
        if (!file.isEmpty()) {
            Path destinationFile = Path.of(dir)
                    .resolve(Paths.get(login))
                    .resolve(Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            System.out.println(file.getOriginalFilename() + " " + String.valueOf(file.getSize()));
            //System.out.println(destinationFile.toString());
            try (InputStream inputStream = file.getInputStream()) {
                destinationFile.toFile().mkdirs();
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new ErrorUploadFile("Failed to store file.", 0); //TODO: возможно 0 заменить на значение
            }
        }
    }

    public void deleteFile(String fileName, String login) {
        if (fileName != null && !fileName.isEmpty()) {
            Path sourceFile = Path.of(dir)
                    .resolve(Paths.get(login))
                    .resolve(Paths.get(fileName))
                    .normalize().toAbsolutePath();
            Path targetFile = Path.of(dir)
                    .resolve(Paths.get(login))
                    .resolve(Paths.get("deleted"))
                    .resolve(Paths.get(fileName))
                    .normalize().toAbsolutePath();
            System.out.println("sourceFile " + sourceFile.toString());
            System.out.println("targetFile " + targetFile.toString());
            try {
                targetFile.toFile().mkdirs();
                Files.move(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new ErrorDeleteFile("Error delete file", 0); //TODO: возможно 0 заменить на значение
            }
        }
    }


    public Resource dowloadFile(String fileName, String login) throws IOException {
        if (fileName != null && !fileName.isEmpty()) {
            Path destinationFile = Path.of(dir)
                    .resolve(Paths.get(login))
                    .resolve(Paths.get(fileName))
                    .normalize().toAbsolutePath();

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(destinationFile));
            return resource;
        } else {
            throw new ErrorUploadFile("Error download file", 0); //TODO: возможно 0 заменить на значение ?
        }
    }

    public String editFile() {
        return "Заглушка repository editFile";
    }


    public List<FileListItem> getAllFiles(String login) {
        Path destinationFolder = Path.of(dir)
                .resolve(Paths.get(login))
                .normalize().toAbsolutePath();
        System.out.println(destinationFolder.toString());
        File folder = new File(destinationFolder.toString());
        File[] files = folder.listFiles();
        List<FileListItem> filesList = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                System.out.println(file.getName() + " " + file.length());
                filesList.add(new FileListItem(file.getName(), file.length()));
            }
        }
        System.out.println(filesList.toString());
        return filesList;

    }
}
