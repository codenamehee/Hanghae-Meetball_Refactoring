package com.finalproject.backend.baseballmate.groupChat;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.Screen;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {
    private final String title;
    private final Long ownUserId;
    private final Long roomId;
    private final Long groupId;
    private final Long headCountChat;
    private final boolean chatValid;
    private final boolean newMessage;
<<<<<<< HEAD
    private final String chatRoomIamge;
    private final String chatRoomtype;
=======
    private final String chatRoomImage;
>>>>>>> e20c445424f14094f817aa03878b9af877ce0f29

    public ChatRoomResponseDto(ChatRoom chatRoom, Group group, Long headCountChat, boolean newMessage) {
        this.title = group.getTitle();
        this.ownUserId = chatRoom.getOwnUserId();
        this.roomId = chatRoom.getId();
        this.groupId = group.getGroupId();
        this.headCountChat = headCountChat;
        this.chatValid = chatRoom.isChatValid();
        this.newMessage = newMessage;
<<<<<<< HEAD
        this.chatRoomIamge = chatRoom.getChatRoomImage();
        this.chatRoomtype = "group";
    }

    public ChatRoomResponseDto(ChatRoom chatRoom, Screen group, Long headCountChat, boolean newMessage) {
        this.title = group.getTitle();
        this.ownUserId = chatRoom.getOwnUserId();
        this.roomId = chatRoom.getId();
        this.groupId = group.getScreenId();
        this.headCountChat = headCountChat;
        this.chatValid = chatRoom.isChatValid();
        this.newMessage = newMessage;
        this.chatRoomIamge = chatRoom.getChatRoomImage();
        this.chatRoomtype = "screen";
=======
        this.chatRoomImage = chatRoom.getChatRoomImage();
>>>>>>> e20c445424f14094f817aa03878b9af877ce0f29
    }
}