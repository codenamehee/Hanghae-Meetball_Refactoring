package com.finalproject.backend.baseballmate.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "room_id")
    private Long id;

    @Column
    private String roomUuid;

    @Column
    private String roomName;

    @Column
    private Long ownUserId; //방장 인덱스

    @Column
    private int userCount; //채팅방 인원수

    @Column
    private boolean chatValid; // 채팅방 이용가능 여부

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="GroupInx")
    private Group group;

    public ChatRoom(String uuid, Group group, User user) {
        this.roomUuid = uuid;
        this.roomName = group.getTitle();
        this.ownUserId = user.getId();
        this.group = group;
        this.chatValid = true;
    }

    public void updatechatValid(boolean chatValid) {
        this.chatValid = chatValid;
    }
}
