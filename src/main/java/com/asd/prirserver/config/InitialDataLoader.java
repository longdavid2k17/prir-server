package com.asd.prirserver.config;

import com.asd.prirserver.model.ERole;
import com.asd.prirserver.model.Role;
import com.asd.prirserver.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class InitialDataLoader implements ApplicationRunner {

    private final RoleRepository roleRepository;

    public InitialDataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Role> roleList = roleRepository.findAll();
        if(roleList.size() == 0)
            roleRepository.saveAll(Arrays.asList(new Role(ERole.ROLE_USER),new Role(ERole.ROLE_ADMIN),new Role(ERole.ROLE_MODERATOR)));
    }
}
