package com.asd.prirserver.config;

import com.asd.prirserver.model.User;
import com.asd.prirserver.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Klasa serwisu implementująca UserDetailsService</p>
 * @author Dawid Kańtoch
 * @version 1.0
 * @since 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    /**
     * Metoda zwracająca użytkownika z bazy
     * @param username parametr przekazywany do repozytorium do pobrania danego usera
     * @return instancję UserDetailsImpl
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }
}
