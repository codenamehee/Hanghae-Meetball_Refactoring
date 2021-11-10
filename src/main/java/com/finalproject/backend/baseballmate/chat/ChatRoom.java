package com.finalproject.backend.baseballmate.chat;

import com.finalproject.backend.baseballmate.model.Timestamped;
import com.finalproject.backend.baseballmate.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends Timestamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "room_id")
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private Long ownUserId;

    @Column(nullable = false)
    private boolean chatValid;

    @OneToOne(mappedBy = "chatRoom")
    private Post post;

    public ChatRoom(String uuid, User user) {
        this.uuid = uuid;
        this.ownUserId = user.getId();
        this.chatValid = true;
    }

    public void updatechatValid(boolean chatValid) {
        this.chatValid = chatValid;
    }