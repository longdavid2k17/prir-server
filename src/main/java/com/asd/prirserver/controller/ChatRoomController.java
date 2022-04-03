package com.asd.prirserver.controller;


import com.asd.prirserver.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAll()
    {
        return chatRoomService.getAll();
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(@RequestParam("id")Long id)
    {
        return chatRoomService.findOne(id);
    }
    @GetMapping("/get-by-name")
    public ResponseEntity<?> getById(@RequestParam("name")String name)
    {
        return chatRoomService.getByName(name);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUserByUserName(@RequestParam("name")String name)
    {

        return chatRoomService.searchByName(name);
    }

}
