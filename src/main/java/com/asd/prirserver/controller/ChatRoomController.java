package com.asd.prirserver.controller;


import com.asd.prirserver.model.ChatRoom;
import com.asd.prirserver.model.payloads.requests.JoinRoomRequest;
import com.asd.prirserver.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat-room")
@CrossOrigin("*")
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

    @PostMapping("/create-room")
    public ResponseEntity<?> createRoom(@RequestBody ChatRoom chatRoom)
    {
        return chatRoomService.createRoom(chatRoom);
    }

    @GetMapping("/join-room")
    public ResponseEntity<?> joinRoom(@RequestBody JoinRoomRequest joinRoomRequest)
    {
        return chatRoomService.joinRoom(joinRoomRequest);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUserByUserName(@RequestParam("name")String name)
    {

        return chatRoomService.searchByName(name);
    }

}
