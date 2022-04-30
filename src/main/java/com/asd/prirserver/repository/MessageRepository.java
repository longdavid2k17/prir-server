package com.asd.prirserver.repository;

import com.asd.prirserver.model.ChatRoom;
import com.asd.prirserver.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findAllByChatRoom(ChatRoom chatRoom);
}
