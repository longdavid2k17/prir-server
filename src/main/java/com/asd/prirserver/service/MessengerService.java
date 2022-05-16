package com.asd.prirserver.service;

import com.asd.prirserver.model.ChatRoom;
import com.asd.prirserver.model.Message;
import com.asd.prirserver.model.User;
import com.asd.prirserver.repository.ChatRoomRepository;
import com.asd.prirserver.repository.MessageRepository;
import com.asd.prirserver.repository.UserRepository;
import com.asd.prirserver.utils.ToJsonString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessengerService
{
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final CipherService cipherService;
    private final UserRepository userRepository;

    public MessengerService(MessageRepository messageRepository,
                            ChatRoomRepository chatRoomRepository,
                            CipherService cipherService,
                            UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.cipherService = cipherService;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> findAllForRoom(Long id){
        try {
            Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(id);
            if(chatRoomOptional.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ToJsonString.toJsonString("Nie znaleziono pokoju o ID="+id));
            List<Message> messageList = messageRepository.findAllByChatRoom(chatRoomOptional.get());
            messageList.forEach(e->{
                String tmpMessage;
                if(e.getMessage()!=null) {
                    tmpMessage = cipherService.decrypt(e.getMessage());
                    e.setMessage(tmpMessage);
                }
            });
            messageList.sort(Comparator.comparing(Message::getPostDate,Comparator.nullsLast(Comparator.reverseOrder())));
            return ResponseEntity.ok().body(messageList);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ToJsonString.toJsonString("Wystąpił błąd podczas pobierania wiadomości. Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> send(Message entity) {
        try {
            if(entity.getMessage()==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ToJsonString.toJsonString("Brak treści wiadomości!"));
            if(entity.getAuthorId()==null && entity.getAuthor()==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ToJsonString.toJsonString("Brak parametru autora!"));
            entity.setPostDate(new Date());
            if(entity.getChatRoomId()!=null){
                Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(entity.getChatRoomId());
                chatRoomOptional.ifPresent(entity::setChatRoom);
            }
            if(entity.getAuthorId()!=null){
                Optional<User> userOptional = userRepository.findById(entity.getAuthorId());
                userOptional.ifPresent(entity::setAuthor);
            }
            if(entity.getAuthor()==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ToJsonString.toJsonString("Brak nadawcy!"));
            entity.setMessage(cipherService.encrypt(entity.getMessage()));
            Message saved = messageRepository.save(entity);
            return ResponseEntity.ok().body(saved);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ToJsonString.toJsonString("Wystąpił błąd podczas wysyłania wiadomości. Komunikat: "+e.getMessage()));
        }
    }
}
