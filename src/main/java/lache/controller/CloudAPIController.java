package lache.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lache.model.FileListItem;
import lache.authentication.AuthenticationRequest;
import lache.authentication.AuthenticationResponse;
import lache.service.CloudAPIService;
import lache.authentication.JwtTokenService;
import lache.authentication.JwtUserDetailsService;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public @ResponseBody String uploadFile(@RequestParam("filename") String filename,
                                           @RequestBody MultipartFile file, Authentication authentication) {
        final String login = authentication.getName();
        service.uploadFile(file, login);
        return "Success upload " + file.getOriginalFilename();
    }

    @DeleteMapping("/file")
    @RolesAllowed({"USER"})
    public String deleteFile(@RequestParam("filename") String fileName, Authentication authentication) {
        final String login = authentication.getName();
        service.deleteFile(fileName, login);
        return "Success deleted " + fileName;
    }

    @GetMapping(value = "/file", produces = "multipart/mixed")
    @RolesAllowed({"USER"})
    public @ResponseBody Resource downloadFile(@RequestParam("filename") String fileName, Authentication authentication) throws IOException {
        final String login = authentication.getName();
        return service.downloadFile(fileName, login);
    }

    @PutMapping("/file")
    @RolesAllowed({"USER"})
    public String editFile(@RequestParam("filename") String filename,
                           @RequestBody Map<String, String> newFileName, Authentication authentication) {
        final String login = authentication.getName();
        return service.editFile(filename, newFileName.get("filename"), login);
    }

    @GetMapping(value = "/list")
    @RolesAllowed({"USER"})
    public ResponseEntity<List<FileListItem>> getAllFiles(@RequestParam("limit") int limit, Authentication authentication) {
        final String login = authentication.getName();
        return ResponseEntity.ok(service.getAllFiles(login));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid final AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getLogin(), authenticationRequest.getPassword()));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getLogin());
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtTokenService.generateToken(userDetails));
        return ResponseEntity.ok(authenticationResponse);
    }

    @GetMapping("/login")
    public ResponseEntity<String> logout(@RequestParam String logout, HttpServletRequest request) {
        final String header = request.getHeader("auth-token");
        if (header.startsWith("Bearer ")) {
            jwtTokenService.addBlackTokens(header.substring(7));
        } else {
            jwtTokenService.addBlackTokens(header);
        }
        return ResponseEntity.ok("Success logout");
    }

}
