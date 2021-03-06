package com.finalproject.backend.baseballmate.responseDto;

import com.finalproject.backend.baseballmate.model.GoodsLikes;
import com.finalproject.backend.baseballmate.model.TimeLineLikes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginCheckResponseDto {
    private Long useridx;
    private String userid;
    private String username;
    private String myteam;
    private String picture;
    private String address;
    private String usertype;
    private String selfIntroduce;

    private List<Long> myTimeLineLikesList;
    private List<Long> myGoodsLikesList;
    private List<Long> myGroupLikesList;
    private List<Long> myGroupCommentLikesList;
    private List<Long> myScreenLikesList;
    private List<Long> myScreenCommentLikesList;
}
