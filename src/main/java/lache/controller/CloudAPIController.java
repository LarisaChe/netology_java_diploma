package lache.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lache.model.FileListItem;
import lache.security.AuthenticationRequest;
import lache.security.AuthenticationResponse;
import lache.service.CloudAPIService;
import lache.service.JwtTokenService;
import lache.service.JwtUserDetailsService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

/*@CrossOrigin
(origins = "http://localhost" // {"http://localhost:8080", "http://localhost:8090", "http://192.168.0.142:8080"}
        //,allowCredentials = "true"
        ,allowedHeaders = { "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Origin","Referer", "Accept",
            "Content-Type", "Access-Control-Allow-Methods", "Host","Authorization",  "X-Requested-With", "X-Auth-Token"}
        ,maxAge = 3600) */
@RestController
public class CloudAPIController {

    private final CloudAPIService service;

    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenService jwtTokenService;

    public CloudAPIController(CloudAPIService service, AuthenticationManager authenticationManager,
                              JwtTokenService jwtTokenService, JwtUserDetailsService jwtUserDetailsService) {
        this.service = service;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @PostMapping("/file")  //"/cloud/file")
    @RolesAllowed({"USER"})
    public @ResponseBody String uploadFile(@RequestParam("filename") String filename, @RequestBody MultipartFile file, Authentication authentication) {
        final String login = authentication.getName();
        System.out.println("login: " + login + "filename: " + filename);
        service.uploadFile(file, login);
        return "Success upload " + file.getOriginalFilename();
    }

    @DeleteMapping("/file")
    @RolesAllowed({"USER"})
    public String deleteFile(@RequestParam("fileName") String fileName, Authentication authentication) {
        final String login = authentication.getName();
        System.out.println(login);
        service.deleteFile(fileName, login);
        return "Success deleted " + fileName;
    }

    @GetMapping(value = "/file", produces = "multipart/mixed")
    @RolesAllowed({"USER"})
    public @ResponseBody Resource dowloadFile(@RequestParam("fileName") String fileName, Authentication authentication) throws IOException {
        final String login = authentication.getName();
        System.out.println(login);
        return service.dowloadFile(fileName, login);
    }

    @PutMapping("/file")
    @RolesAllowed({"USER"})
    public String editFile() {
        return "Заглушка эндпоинта /file Put -> \n" + service.editFile();
    }

    @GetMapping(value = "/list")//, produces = "application/json", headers = "Access-Control-Allow-Origin = '*'")
    @RolesAllowed({"USER"})
    //@PreAuthorize("hasAnyRole('WRITE', 'DELETE')")
    public ResponseEntity<List<FileListItem>> getAllFiles(@RequestParam("limit") int limit, Authentication authentication) { //@RequestParam("city") String city) {
        final String login = authentication.getName();
        System.out.println(login);
        System.out.println("limit: " + limit);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");

        //return ResponseEntity.ok(service.getAllFiles(login));
        return new ResponseEntity<>(
                service.getAllFiles(login), headers, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid final AuthenticationRequest authenticationRequest) {
        //System.out.println("ЗАШЛИ!!!!");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getLogin(), authenticationRequest.getPassword()));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getLogin());
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtTokenService.generateToken(userDetails));
        //System.out.println("token: " + authenticationResponse.getAccessToken());
        //return authenticationResponse;
        return ResponseEntity.ok(authenticationResponse);
    }

    //@PostMapping("/logout")
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> logout(Authentication authentication) {
        System.out.println("logout");
        authentication.setAuthenticated(false);
        return ResponseEntity.ok("Success logout");
    }

}
