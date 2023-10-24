package lache.service;

import lache.model.FileListItem;
import lache.repository.CloudAPIRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CloudAPIService {

    private final CloudAPIRepository repository;

    public CloudAPIService(CloudAPIRepository repository) {
        this.repository = repository;
    }

    public void uploadFile(MultipartFile file, String login) {
        repository.uploadFile(file, login);
    }


    public void deleteFile(String fileName, String login) {
        repository.deleteFile(fileName, login);
    }


    public Resource dowloadFile(String fileName, String login) throws IOException {
        return repository.dowloadFile(fileName, login);
    }


    public String editFile() {
        return "Заглушка service editFile -> \n" + repository.editFile();
    }


    public List<FileListItem> getAllFiles(String login) {
         return repository.getAllFiles(login);
    }

}
