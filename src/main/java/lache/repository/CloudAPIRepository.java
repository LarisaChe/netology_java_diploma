package lache.repository;

import lache.exception.ErrorDeleteFile;
import lache.exception.ErrorInputData;
import lache.exception.ErrorUploadFile;
import lache.model.FileListItem;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

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

            try (InputStream inputStream = file.getInputStream()) {
                destinationFile.toFile().mkdirs();
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new ErrorUploadFile("Failed to store file.", 0); 
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

            try {
                targetFile.toFile().mkdirs();
                Files.move(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new ErrorDeleteFile("Error delete file", 0);
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
            throw new ErrorUploadFile("Error download file", 0);
        }
    }

    public String editFile(String oldFilename, String newFilename, String login) {

        File oldFile = new File(Path.of(dir)
                .resolve(Paths.get(login))
                .resolve(Paths.get(oldFilename))
                .normalize().toAbsolutePath().toString());
        File newFile = new File(Path.of(dir)
                .resolve(Paths.get(login))
                .resolve(Paths.get(newFilename))
                .normalize().toAbsolutePath().toString());
        if (!oldFile.renameTo(newFile)) {
            throw new ErrorInputData("Error edit file name", 0);
        }
        return "Success edit file name";
    }


    public List<FileListItem> getAllFiles(String login) {
        Path destinationFolder = Path.of(dir)
                .resolve(Paths.get(login))
                .normalize().toAbsolutePath();
        destinationFolder.toFile().mkdirs();

        File folder = new File(destinationFolder.toString());

        File[] files = folder.listFiles();
        List<FileListItem> filesList = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                filesList.add(new FileListItem(file.getName(), file.length()));
            }
        }
        return filesList;
    }
}
