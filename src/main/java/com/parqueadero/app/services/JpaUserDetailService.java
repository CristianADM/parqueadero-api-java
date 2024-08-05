package com.parqueadero.app.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parqueadero.app.models.UserEntity;
import com.parqueadero.app.repositories.UserRepository;

@Service
public class JpaUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    public JpaUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = this.userRepository.findByEmail(username);

        if(userOptional.isEmpty()) {
            throw new UsernameNotFoundException(String.format("The email:  %s does not exist", username));
        }

        UserEntity userEntity = userOptional.get();

        List<GrantedAuthority> authorities = userEntity.getRoles()
            .stream().map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getAudit().isActive(),
                true,
                true,
                true,
                authorities);
    }
}