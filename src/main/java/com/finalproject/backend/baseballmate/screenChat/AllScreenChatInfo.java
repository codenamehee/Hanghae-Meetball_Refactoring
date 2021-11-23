package com.finalproject.backend.baseballmate.screenChat;

import com.finalproject.backend.baseballmate.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class AllScreenChatInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //    @JsonBackReference -> jsonignore 사용하려면 이거 없어야 조회가 됨
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User enteredUser;

    //    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ScreenChatRoom screenChatRoom;

    @Column(nullable = false)
    private Long lastMessageId;

    public AllScreenChatInfo(User user, ScreenChatRoom screenChatRoom) {
        this.enteredUser = user;
        this.screenChatRoom = screenChatRoom;
        this.lastMessageId = 0L;
    }

    public void updateLastMessageId(Long lastMessageId){
        this.lastMessageId = lastMessageId;
    }
}
