package com.asd.prirserver.service;


import com.asd.prirserver.model.User;
import com.asd.prirserver.repository.RoleRepository;
import com.asd.prirserver.repository.UserRepository;
import com.asd.prirserver.utils.ToJsonString;

import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserDetailsService.class);
    private final EntityManager entityManager;

    public UserDetailsService(UserRepository userRepository, RoleRepository roleRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.entityManager = entityManager;
    }


    public ResponseEntity<?> getListAvatars()
    {
        try{

            List<BufferedImage> avatars= new ArrayList<>();
             final File dir = new File("avatars");
            if (dir.isDirectory()) {
                for (final File f : Objects.requireNonNull(dir.listFiles())) {
                    BufferedImage img = null;
                    img = ImageIO.read(f);
                    if(img!=null)
                    avatars.add(img);
                }
            }

            return ResponseEntity.ok().body(avatars);

        }catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("WEystąpił błąd podczas pobierania listy avatarów");
            return ResponseEntity.badRequest().body(ToJsonString.toJsonString("Nie udało się pobrać listy avatarów"));

        }
    }

    public ResponseEntity<?> searchByUserName(String userName)
    {

        try
        {

            SearchSession searchSession = Search.session( entityManager );
            SearchResult<User> result =searchSession.search(User.class).where(f->f.match().field("username").matching(
                    userName)
            ).fetch(20);

            long totalHitCount = result.total().hitCount();
            List<User> hits = result.hits();


            return ResponseEntity.ok().body(hits);

        }catch (Exception e)
        {

            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(ToJsonString.toJsonString("Wystąpił błąd podczas wyszukiwania użytkownika"));

        }

    }

    public ResponseEntity<?> getAll() {

        try{

            return ResponseEntity.ok().body(userRepository.findAll());

        }catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("Wystapił bład podczas pobierania listy użytkowników");
            return ResponseEntity.badRequest().body(ToJsonString.toJsonString("Nie udało się pobrać listy użytkowników"));
        }
    }
}
