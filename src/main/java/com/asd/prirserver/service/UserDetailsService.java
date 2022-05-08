package com.asd.prirserver.service;


import com.asd.prirserver.model.User;
import com.asd.prirserver.repository.RoleRepository;
import com.asd.prirserver.repository.UserRepository;
import com.asd.prirserver.utils.ToJsonString;

import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    @PersistenceContext
    private EntityManager entityManager;

    public UserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

    }


    public ResponseEntity<?> getListAvatars()
    {
        try{

            List<String> avatars= new ArrayList<>();
             final File dir = new File("avatars");
            if (dir.isDirectory()) {
                for (final File f : Objects.requireNonNull(dir.listFiles())) {
                    BufferedImage img = null;
                    img = ImageIO.read(f);
                    if(img!=null)
                    avatars.add(f.getAbsolutePath());
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
            MassIndexer indexer = searchSession.massIndexer( User.class )
                    .threadsToLoadObjects( 3 );
            indexer.startAndWait();

            SearchResult<User> result =Search.session(entityManager).search(
                    User.class).where(f->f.wildcard().fields("username").matching(
                    userName+"*"
            )).fetchAll();

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
