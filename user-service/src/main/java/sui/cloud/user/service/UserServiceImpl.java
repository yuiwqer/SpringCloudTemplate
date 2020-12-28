package sui.cloud.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        User u=userRepository.findByUsername(s);
        if(u!=null){
            return u;
        }
        throw new UsernameNotFoundException("用户名错误!");
    }
}
