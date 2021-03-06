package com.finalproject.backend.baseballmate.service;


import com.finalproject.backend.baseballmate.model.TimeLine;
import com.finalproject.backend.baseballmate.model.TimeLineLikes;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.TimeLineLikesRepository;
import com.finalproject.backend.baseballmate.repository.TimeLineRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.LikesRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class TimeLineLikesService {

    private final TimeLineLikesRepository timeLineLikesRepository;
    private final UserRepository userRepository;
    private final TimeLineRepository timeLineRepository;



    @Transactional
    public String liked(Long timeLineId, LikesRequestDto requestDto, UserDetailsImpl userDetails) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 사용 가능합니다");
        }


        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다.")
        );
        TimeLine timeLine = timeLineRepository.findById(timeLineId).orElseThrow(
                () -> new IllegalArgumentException("타임라인이 존재하지 않습니다.")
        );

        if (requestDto.getIsLiked().equals("true")) {
            TimeLineLikes likes = timeLineLikesRepository.findByTimeLineIdAndUserId(timeLine.getId(), user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물의 좋아요 이력이 없습니다.")
            );

            user.deleteLikes(likes);
            timeLine.deleteLikes(likes);
            timeLineLikesRepository.delete(likes);
            return "false";
        } else {
            if(timeLineLikesRepository.findByTimeLineIdAndUserId(timeLine.getId(), user.getId()).isPresent())
            {
                return "true";
            }
            TimeLineLikes likes = timeLineLikesRepository.save(new TimeLineLikes(timeLine, user));
            user.addLikes(likes);
            timeLine.addLikes(likes);
            return "true";
        }

    }
}
