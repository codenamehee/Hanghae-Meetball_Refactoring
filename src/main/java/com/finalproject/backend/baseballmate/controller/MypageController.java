package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MypageController {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupService groupService;
    // 마이페이지에서 유저 프로필 사진 등록
//    @PostMapping("")


    //내가 작성한 그룹 조회
    @GetMapping("/my/groups/write")
    public List<AllGroupResponseDto>  MyGroupsWrite(@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails==null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }
        return groupService.getMywriteAllGroups(userDetails.getUser());
    }

    //내가 참여한 그룹 조회
    @GetMapping("/my/groups/applications")
    public List<AllGroupResponseDto> MyGroupsApplications(@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails==null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }
        return groupService.getMyapplicationAllGroups(userDetails.getUser());
    }

    //내가 좋아요한 그룹 조회
    @GetMapping("/my/groups/like")
    public List<AllGroupResponseDto> MyGroupsLike(@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails==null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 마이페이지 기능을 이용할 수 있습니다.");
        }

        return groupService.getMylikeAllGroups(userDetails.getUser());
    }
}