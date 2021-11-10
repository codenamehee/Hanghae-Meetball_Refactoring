package com.finalproject.backend.baseballmate.chat;

import com.finalproject.backend.baseballmate.model.Timestamped;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.service.UserService;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends Timestamped {

    public enum MessageType {
        ENTER, TALK, QUIT, BAN, BREAK
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(value = EnumType.STRING)
    private MessageType type;

    @Column
    private String roomId;

    @Column
    private String message;

    @ManyToOne
    private User sender;

    @Builder
    public ChatMessage(MessageType type, String roomId, String message, User sender) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }

    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto, UserService userService) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.sender =  userService.getUser(chatMessageRequestDto.getSenderId());
        this.message = chatMessageRequestDto.getMessage();
    }
