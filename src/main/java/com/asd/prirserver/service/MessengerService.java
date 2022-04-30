package com.asd.prirserver.service;

import com.asd.prirserver.model.ChatRoom;
import com.asd.prirserver.model.Message;
import com.asd.prirserver.repository.ChatRoomRepository;
import com.asd.prirserver.repository.MessageRepository;
import com.asd.prirserver.utils.ToJsonString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public MessengerService(MessageRepository messageRepository,
                            ChatRoomRepository chatRoomRepository,
                            PasswordEncoder passwordEncoder) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> findAllForRoom(Long id){
        try {
            Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(id);
            if(chatRoomOptional.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ToJsonString.toJsonString("Nie znaleziono pokoju o ID="+id));
            List<Message> messageList = messageRepository.findAllByChatRoom(chatRoomOptional.get());
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
            entity.setPostDate(new Date());
            entity.setMessage(passwordEncoder.encode(entity.getMessage()));
            Message saved = messageRepository.save(entity);
            return ResponseEntity.ok().body(saved);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ToJsonString.toJsonString("Wystąpił błąd podczas wysyłania wiadomości. Komunikat: "+e.getMessage()));
        }
    }
}
