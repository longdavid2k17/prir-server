package com.asd.prirserver.controller;

import com.asd.prirserver.config.JWT.JWTUtil;
import com.asd.prirserver.config.UserDetailsImpl;
import com.asd.prirserver.model.ERole;
import com.asd.prirserver.model.Role;
import com.asd.prirserver.model.User;
import com.asd.prirserver.model.payloads.requests.LoginRequest;
import com.asd.prirserver.model.payloads.requests.SignUpRequest;
import com.asd.prirserver.model.payloads.response.JwtResponse;
import com.asd.prirserver.repository.RoleRepository;
import com.asd.prirserver.repository.UserRepository;
import com.asd.prirserver.utils.ToJsonString;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/** Klasa kontrolera dla REST dla procesów autoryzacyjnych
 * @author Dawid Kańtoch
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController
{
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil)
    {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Metoda obsługująca logowanie użytkownika;
     * @param loginRequest instancja klasy LoginRequest która posiada dwa pola: login i hasło.
     * Po sprawdzeniu poprawności przekazanych danych funkcja zwraca ResponseEntity
     * @return ResponseEntity wraz z danymi typu JwtResponse: token, id użytkownika, login użytkownika, adres email użytkownika oraz uprawnienia
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(),userDetails.getUsername(),userDetails.getEmail(),
                roles));
    }

    /**
     * Metoda typu ResponseEntity obsługująca proces rejestracji użytkowników.
     * @param signUpRequest instancja klasy SignUpRequest, posiadająca pola username,password,email, oraz kolekcja roli
     * Po sprawdzeniu poprawności danych oraz tego czy konto z takimi atrybutami już istnieje, funkcja wywołuje zapis użytkownika do bazy danych
     * @return ResponseEntity z informacją że użytkownik został poprawnie utworzony
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest)
    {
        if (userRepository.existsByUsername(signUpRequest.getUsername()))
        {
            return ResponseEntity
                    .badRequest()
                    .body((ToJsonString.toJsonString("Error: Username is already taken!")));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail()))
        {
            return ResponseEntity
                    .badRequest()
                    .body((ToJsonString.toJsonString("Error: Email is already in use!")));
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        if(signUpRequest.getAvatar()!=null)
        user.setAvatar(signUpRequest.getAvatar());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null)
        {
            Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
        else
        {
            strRoles.forEach(role ->
            {
                switch (role)
                {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(ToJsonString.toJsonString("User registered successfully!"));
    }
}
