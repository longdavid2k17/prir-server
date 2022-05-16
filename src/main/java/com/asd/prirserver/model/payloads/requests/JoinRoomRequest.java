package com.asd.prirserver.model.payloads.requests;

import javax.validation.constraints.NotBlank;

public class JoinRoomRequest {

    private String roomName;
    private String password;

    public JoinRoomRequest() {
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
