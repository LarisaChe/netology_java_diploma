package lache.controller;

import lache.model.FileListItem;
import lache.service.CloudAPIService;
import org.springframework.core.io.Resource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin (origins = "http://localhost:8080" // {, "http://localhost:8090", "http://192.168.0.142:8080"}
        ,allowedHeaders = {"Access-Control-Allow-Credentials", "Access-Control-Allow-Origin", "Origin","Referer", "Accept", "Content-Type",
        "Access-Control-Allow-Methods", "Access-Control-Allow-Headers", "Host","Authorization",  "X-Requested-With"} //"*"// {"Authorization", "Origin"}
        ,allowCredentials = "true"
        ,maxAge = 3600
)
@RestController
public class CloudAPIController {

    private final static String login = "testLaChe"; // TODO: подставлять логин пользователя
    private final CloudAPIService service;

    public CloudAPIController(CloudAPIService service) {
        this.service = service;
    }

    @PostMapping("/cloud/file")
    public @ResponseBody String uploadFile(@RequestParam("file") MultipartFile file) {
        service.uploadFile(file, login);
        return "Success upload " + file.getOriginalFilename();
    }

    @DeleteMapping("/cloud/file")
    public String deleteFile(@RequestParam("fileName") String fileName) {
        service.deleteFile(fileName, login);
        return "Success deleted " + fileName;
    }

    @GetMapping(value ="/cloud/file", produces = "multipart/mixed")
    public @ResponseBody Resource dowloadFile(@RequestParam("fileName") String fileName) throws IOException {
        return service.dowloadFile(fileName, login);
        //return "Заглушка эндпоинта /file Get -> \n" + service.dowloadFile();
    }

    @PutMapping("/cloud/file")
    public String editFile() {
        return "Заглушка эндпоинта /file Put -> \n" + service.editFile();
    }

    @GetMapping (value = "/cloud/list", produces = "application/json")
    //@PreAuthorize("hasAnyRole('WRITE', 'DELETE')")
    public List<FileListItem> getAllFiles() { //@RequestParam("city") String city) {
        return service.getAllFiles(login);
        //return "Заглушка эндпоинта /list get list -> \n" + service.getAllFiles();
    }

    /*@PostMapping("/login")
    public Model login1(Model model, String error, String logout) {  //
        System.out.println("ЗАШЛИ1111!!!!");
        System.out.println(model.toString());
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return model;
    }

    @GetMapping("/login")
    public String login2(Model model) {
        System.out.println("ЗАШЛИ2222!!!!");
        System.out.println(model.toString());
        /*if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login2";
    }*/
}
