package com.asd.prirserver.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "post_date")
    private Date postDate;

    @Transient
    private Long authorId;
    @Transient
    private Long chatRoomId;

    public Message() {
    }

    public Message(Long id, ChatRoom chatRoom, String message, User author, Date postDate) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.message = message;
        this.author = author;
        this.postDate = postDate;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + id +
                ", chatRoom=" + chatRoom +
                ", message='" + message + '\'' +
                ", author=" + author +
                ", postDate=" + postDate +
                '}';
    }
}
