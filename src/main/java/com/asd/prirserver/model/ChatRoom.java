package com.asd.prirserver.model;


import javax.persistence.*;

@Entity
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_name")
    private String name;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "room_password")
    private String roomPassword;

    public ChatRoom() {
    }

    public ChatRoom(Long id, String name, Integer capacity, String roomPassword) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.roomPassword = roomPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", roomPassword='" + roomPassword + '\'' +
                '}';
    }
}
