package lache.authentication;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthenticationRequest {

    @NotNull
    @Size(max = 32)
    private String login;

    @NotNull
    @Size(max = 255)
    private String password;

    public AuthenticationRequest() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
