package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.requestDto.GroupRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GroupDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class GroupController {

    private final GroupService groupService;
    private final GroupRepository groupRepository;

    // 모임페이지 전체 조회 :
    @GetMapping("/page/group")
    public AllGroupResponseDto getAllGroups() {
        AllGroupResponseDto GroupList = groupService.getAllGroups();
        return GroupList;
    }

    // 모임 생성
    @PostMapping("/page/group")
    public MsgResponseDto createGroup(@RequestBody GroupRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인한 유저의 유저네임 가져오기
        String loginedUsername = userDetails.getUser().getUsername();
        if (loginedUsername == null) {
            throw new IllegalArgumentException("로그인 한 이용자만 모임을 생성할 수 있습니다.");
        }
        groupService.createGroup(requestDto, loginedUsername);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "모임 등록 성공");

        return msgResponseDto;
    }

    // 모임페이지 상세 조회
    @GetMapping("/page/group/detail/{groupId}")
    public GroupDetailResponseDto getGroupDetails(@PathVariable("groupId") Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String loginedUserid = userDetails.getUser().getUserid();
        if (loginedUserid == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 모임을 조회할 수 있습니다.");
        }
        GroupDetailResponseDto detailResponseDto = groupService.getGroupDetail(groupId);
        return detailResponseDto;
    }

    // 모임 참여신청하기
    @PostMapping("/page/group/detail/apply/{groupId}")
    public MsgResponseDto applyGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User appliedUser = userDetails.getUser();
        Group appliedGroup = groupRepository.findByGroupId(groupId);
        groupService.applyGroup(appliedUser, appliedGroup);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "모임 신청 완료");
        return msgResponseDto;
    }
}
