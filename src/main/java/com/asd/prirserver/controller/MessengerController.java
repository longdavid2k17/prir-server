package com.asd.prirserver.controller;

import com.asd.prirserver.model.Message;
import com.asd.prirserver.service.MessengerService;
import com.asd.prirserver.utils.ToJsonString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messenger")
@CrossOrigin("*")
public class MessengerController
{
    private final MessengerService messengerService;

    public MessengerController(MessengerService messengerService) {
        this.messengerService = messengerService;
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<?> getMessagesForRoom(@PathVariable Long id){
        if(id==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ToJsonString.toJsonString("Brak parametru ID"));
        return messengerService.findAllForRoom(id);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Message entity){
        return messengerService.send(entity);
    }
}
