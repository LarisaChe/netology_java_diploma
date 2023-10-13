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

    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUser(username);
        if( user == null )
            throw new UsernameNotFoundException(username);

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUser())
                .password(user.getPassword())
                .roles("USER")
                .build();
    } */

    public void uploadFile(MultipartFile file, String login) { //String fileName,
        repository.uploadFile(file, login);
        //return "Заглушка service uploadFile -> \n" + repository.uploadFile() ;
    }


    public void deleteFile(String fileName, String login) {
        repository.deleteFile(fileName, login);
        //return "Заглушка service deleteFile -> \n" + repository.deleteFile();
    }


    public Resource dowloadFile(String fileName, String login) throws IOException {
        return repository.dowloadFile(fileName, login);
        //return "Заглушка service dowloadFile -> \n" + repository.dowloadFile();
    }


    public String editFile() {
        return "Заглушка service editFile -> \n" + repository.editFile();
    }


    public List<FileListItem> getAllFiles(String login) {
         return repository.getAllFiles(login);
    }

}
