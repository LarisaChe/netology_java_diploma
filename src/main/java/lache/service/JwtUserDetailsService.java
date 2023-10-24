package lache.service;

import lache.model.User;
import lache.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.time.LocalTime.now;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    /*public static final String USER = "USER";
    public static final String ROLE_USER = "ROLE_" + USER;*/

    private final UserRepository userRepository;
    //private Collection<? extends GrantedAuthority> authorities;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.authorities = Collections.singletonList(new SimpleGrantedAuthority("USER");
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final User user = userRepository.findByLogin(username).orElseThrow(
                () -> new UsernameNotFoundException("User " + username + " not found"));
        //final List<SimpleGrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority(UserRoles.ROLE_USER));
        final List<SimpleGrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
        /*final List<SimpleGrantedAuthority> roles;
        roles = Arrays.asList((SimpleGrantedAuthority) user.getRole());*/
        return  new JwtUserDetails(now().getLong(NANO_OF_SECOND), username, user.getPassword(), roles);  //user.getId(),

    }

    /*public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }*/
}
