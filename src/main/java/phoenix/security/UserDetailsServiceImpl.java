package phoenix.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import phoenix.entities.User;
import phoenix.services.UserService;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Set<GrantedAuthority> roles = new HashSet<>();
        User user = userService.getByLogin(login);
        if (user!=null) {
            roles.add(new SimpleGrantedAuthority("ROLE_".concat(user.getRole())));
            if(!user.getActive())
                return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                        true, true, true, false, roles);

            userService.updateLastActivity(user);
        }
            return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), roles);
    }
}
