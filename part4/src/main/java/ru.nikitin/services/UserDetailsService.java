package ru.nikitin.services;


import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.nikitin.entity.User;
import ru.nikitin.repository.UserRepository;


public interface UserDetailsService {


   UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
//   @Bean
//   public UserDetailsService userDetailsService(UserRepository userRepo) {
//      return username -> {
//         User user = userRepo.findByUsername(username);
//         if (user != null) return user;
//         throw new UsernameNotFoundException("User ‘" + username + "’ not found");
//      };
//   }

}
