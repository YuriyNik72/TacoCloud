package ru.nikitin.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.nikitin.entity.User;
import org.springframework.data.repository.CrudRepository;
import ru.nikitin.services.UserDetailsService;

public interface UserRepository extends CrudRepository<User, Long>{
    User findByUsername(String username);

    @Bean
    default UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            User user = userRepo.findByUsername(username);
            if (user != null) return user;
            throw new UsernameNotFoundException("User ‘" + username + "’ not found");
        };
    }
}
