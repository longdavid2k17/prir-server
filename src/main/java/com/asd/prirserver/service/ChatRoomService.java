package com.asd.prirserver.service;


import com.asd.prirserver.model.ChatRoom;
import com.asd.prirserver.model.User;
import com.asd.prirserver.repository.ChatRoomRepository;
import com.asd.prirserver.utils.ToJsonString;

import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService {

    private final Logger LOGGER = LoggerFactory.getLogger(ChatRoomService.class);
    private final ChatRoomRepository chatRoomRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;

    }

    public ResponseEntity<?> getAll()
    {
        try{

            return ResponseEntity.ok().body(chatRoomRepository.findAll());

        }catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("Wystapił bład podczas pobierania listy pokoi czatowych");
            return ResponseEntity.badRequest().body(ToJsonString.toJsonString("Nie udało się pobrać listy pokoi czatowych"));
        }
    }

    public ResponseEntity<?> findOne (Long id)
    {
        try{

            return ResponseEntity.ok().body(chatRoomRepository.findById(id));

        }catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("Wystapił błąd podczas pobierania pokoju czatoweego o id:"+id);
            return ResponseEntity.badRequest().body(ToJsonString.toJsonString("Nie udało się pobrać pokoju o ID: "+id));
        }
    }

    public ResponseEntity<?> getByName(String name)
    {
        try{
            Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByNameEquals(name);
            if(optionalChatRoom.isPresent())
            return ResponseEntity.ok().body(optionalChatRoom.get());
            else
             return ResponseEntity.ok().body(ToJsonString.toJsonString("Nie odnaleziono pokoju o nazwie : "+name));
        }catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("Wystapił błąd podczas pobierania pokoju o nazwie :"+name);
            return ResponseEntity.badRequest().body(ToJsonString.toJsonString("Wystąpił błąd podczas wyszukiwania pokoju o nazwie : "+name));
        }
    }

    public ResponseEntity<?> searchByName(String name)
    {
        try
        {
            SearchSession searchSession = Search.session( entityManager );
            MassIndexer indexer = searchSession.massIndexer( ChatRoom.class )
                    .threadsToLoadObjects( 3 );
            indexer.startAndWait();

            SearchResult<ChatRoom> result =Search.session(entityManager).search(
                    ChatRoom.class).where(f->f.wildcard().fields("name").matching(
                    name+"*"
            )).fetchAll();

            List<ChatRoom> hits = result.hits();

            return ResponseEntity.ok().body(hits);

        }catch (Exception e)
        {

            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(ToJsonString.toJsonString("Wystąpił błąd podczas wyszukiwania pokoju czatowego"));

        }
    }

    public ResponseEntity<?> createRoom(ChatRoom chatRoom) {
        try{

            if(chatRoom!=null)
            {
                ChatRoom saved =chatRoomRepository.save(chatRoom);
                return ResponseEntity.ok().body(saved);
            }
            else {
                return ResponseEntity.badRequest().body(ToJsonString.toJsonString("Przesłany obiekt jest pusty"));
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ToJsonString.toJsonString("Nie udało się stworzyc pokoju czatowego"));
        }
    }
}
