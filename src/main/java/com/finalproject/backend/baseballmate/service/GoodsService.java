package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Goods;
import com.finalproject.backend.baseballmate.model.GoodsComment;
import com.finalproject.backend.baseballmate.model.GoodsLikes;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GoodsCommentRepository;
import com.finalproject.backend.baseballmate.repository.GoodsLikesRepository;
import com.finalproject.backend.baseballmate.repository.GoodsRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.GoodsDetailRequestDto;
import com.finalproject.backend.baseballmate.requestDto.GoodsRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGoodsResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GoodsDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GoodsResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.utils.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final GoodsCommentRepository goodsCommentRepository;
    private final GoodsLikesRepository goodsLikesRepository;
    private final UserRepository userRepository;
    private String commonPath = "/images";
    String[] picturelist = {"basic0","basic1","basic2","basic3","basic4","basic5","basic6","basic7","basic8","basic9"};
//    String[] picturelist = {"basic0.jpg","basic1.jpg","basic2.jpg","basic3.jpg","basic4.jpg","basic5.jpg","basic6.jpg","basic7.jpg","basic8.jpg","basic9.jpg"};
    Random random = new Random();

    @Transactional
    public GoodsResponseDto createGoods(UserDetailsImpl userDetails, GoodsRequestDto requestDto) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("????????? ??? ???????????? ?????? ???????????????");
        }
            User loginUser = userDetails.getUser();
            String goodsUserPicture = loginUser.getPicture();
            String myTeam = loginUser.getMyselectTeam();
            String userAddress = loginUser.getAddress();

            Long userId = loginUser.getId();
            String usertype = "";
            if (loginUser.getKakaoId() == null) {
                usertype = "normal";
            } else {
                usertype = "kakao";
            }

            if(requestDto.getFilePath()=="")
            {
                requestDto.setFilePath(picturelist[random.nextInt(10) + 1]);
            }
            Goods goods = new Goods(loginUser, requestDto, goodsUserPicture, myTeam, userAddress, userId, usertype);
            goodsRepository.save(goods);
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("success", "????????????");
            return goodsResponseDto;
        }


    @Transactional
    public GoodsResponseDto createGoodsLegacy(UserDetailsImpl userDetails, GoodsRequestDto requestDto, MultipartFile files) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("????????? ??? ???????????? ?????? ???????????????");
        }
        try {
            String filename = picturelist[random.nextInt(10) + 1];
//            String filename = "basic.jpg";
            if (files != null) {
                String origFilename = files.getOriginalFilename();
                filename = new MD5Generator(origFilename).toString() + ".jpg";
                /* ???????????? ????????? 'files' ????????? ????????? ???????????????. */

                String savePath = System.getProperty("user.dir") + commonPath;
                /* ????????? ???????????? ????????? ????????? ????????? ???????????????. */
                //files.part.getcontententtype() ?????? ???????????? ????????? false???????????????.
                if (!new File(savePath).exists()) {
                    try {
                        new File(savePath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                String filePath = savePath + "/" + filename;// ???????????? ???????????? ???????????? ???????????? ??????????????? ????????? : / ????????? \\ ????????????.
                files.transferTo(new File(filePath));
            }
            requestDto.setFilePath(filename);
            User loginUser = userDetails.getUser();
            String goodsUserPicture = loginUser.getPicture();
            String myTeam = loginUser.getMyselectTeam();
            String userAddress = loginUser.getAddress();

            Long userId = loginUser.getId();
            String usertype = "";
            if (loginUser.getKakaoId() == null) {
                usertype = "normal";
            } else {
                usertype = "kakao";
            }
            Goods goods = new Goods(loginUser, requestDto, goodsUserPicture, myTeam, userAddress, userId, usertype);
            goodsRepository.save(goods);
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("success", "????????????");
            return goodsResponseDto;
        }
        catch (Exception e)
        {
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("","????????????");
            return goodsResponseDto;
        }
    }

    @Transactional
    public List<AllGoodsResponseDto> getGoods() throws ParseException {

        List<Goods> goodsList = goodsRepository.findAllByOrderByCreatedAtDesc();

        List<AllGoodsResponseDto> data = new ArrayList<>();
        for(int i=0; i<goodsList.size(); i++) {
            Goods goods = goodsList.get(i);

            Long id = goods.getId();
            String userName = goods.getUserName();
            String goodsName = goods.getGoodsName();
            String goodsContent = goods.getGoodsContent();
            String filePath = goods.getFilePath();
            String dayBefore = getDayBefore(goods);
            String goodsUserPicture = goods.getGoodsUserPicture();
//            int likeCount = goods.getLikeCount();
            List<GoodsComment> goodsCommentList = goodsCommentRepository.findAllByGoods_IdOrderByCreatedAtAsc(id);
            List<GoodsLikes> goodsLikesList = goodsLikesRepository.findAllByGoods_Id(goods.getId());
            String myTeam = goods.getMyTeam();
            String userAddress = goods.getUserAddress();

            Long userId = goods.getUserId();
            String usertype = goods.getUsertype();

            AllGoodsResponseDto responseDto =
                    new AllGoodsResponseDto(id,userName ,goodsName,goodsContent,filePath,dayBefore, goodsUserPicture, goodsCommentList,goodsLikesList, myTeam, userAddress, userId,usertype);
            data.add(responseDto);
        }
        return data;
    }

    @Transactional
    public List<AllGoodsResponseDto> getnowGoods(int number, UserDetailsImpl userDetails) throws ParseException {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("????????? ????????? ?????? ??? ????????????")
        );
        List<Goods> goodsList = goodsRepository.findAllByOrderByCreatedAtDesc();
        List<AllGoodsResponseDto> data = new ArrayList<>();
        if(goodsList.size()<=number) {
            number = goodsList.size();
        }

        for(int i=0; i<number; i++) {
            Goods goods = goodsList.get(i);

            Long id = goods.getId();
            String userName = goods.getUserName();
            String goodsName = goods.getGoodsName();
            String goodsContent = goods.getGoodsContent();
            String filePath = goods.getFilePath();
            String dayBefore = getDayBefore(goods);
            String goodsUserPicture = goods.getGoodsUserPicture();
//            int likeCount = goods.getLikeCount();
            List<GoodsComment> goodsCommentList = goodsCommentRepository.findAllByGoods_IdOrderByCreatedAtAsc(id);
            List<GoodsLikes> goodsLikesList = goodsLikesRepository.findAllByGoods_IdAndUserId(goods.getId(), user.getId());
            String myTeam = goods.getMyTeam();
            String userAddress = goods.getUserAddress();
            Long userId = goods.getUserId();
            String usertype = goods.getUsertype();

            AllGoodsResponseDto responseDto =
                    new AllGoodsResponseDto(id, userName, goodsName,goodsContent,filePath,dayBefore, goodsUserPicture, goodsCommentList,goodsLikesList, myTeam, userAddress,userId,usertype);
            data.add(responseDto);
        }
        return data;
    }

    public GoodsDetailResponseDto getGoodsDetail(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(
                () -> new NullPointerException("???????????? ?????? ??????????????????.")
        );
        List<GoodsDetailRequestDto> goodsCommentList = new ArrayList<>();
        GoodsDetailRequestDto goodsDetailRequestDto = new GoodsDetailRequestDto();
        String userName = goods.getUserName();
        String goodsName = goods.getGoodsName();
        String goodsContent = goods.getGoodsContent();
        String filePath = goods.getFilePath();
        List<GoodsComment> goodsComments = goodsCommentRepository.findAllByGoodsId(goodsId);
        for (int i = 0; i < goodsComments.size(); i++ ) {
            goodsDetailRequestDto.setId(goodsComments.get(i).getId());
            goodsDetailRequestDto.setUserName(goodsComments.get(i).getUserName());
            goodsDetailRequestDto.setComment(goodsComments.get(i).getComment());
            goodsCommentList.add(goodsDetailRequestDto);
        }
        GoodsDetailResponseDto goodsDetailResponseDto =
                new GoodsDetailResponseDto(userName, goodsName, goodsContent, filePath,goodsCommentList);
        return goodsDetailResponseDto;
    }

    @Transactional
    public GoodsResponseDto updateGoods(Long id, GoodsRequestDto requestDto,UserDetailsImpl userDetails) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("????????? ??????????????? ????????? ??? ????????????");
        }

        Goods goods = goodsRepository.findById(id).orElseThrow(
                () -> new NullPointerException("???????????? ?????? ??????????????????.")
        );
        String loginUser = userDetails.getUser().getUserid();
        String writer = "";

        if(goods != null){
            writer = goods.getCreatedUser().getUserid();
            if(!loginUser.equals(writer)){
                throw new IllegalArgumentException("?????? ????????? ????????????");
            }
            goods.update(requestDto);
            goodsRepository.save(goods);
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("success","????????????");
            return goodsResponseDto;
        }else{
            throw new NullPointerException("?????? ????????? ???????????? ????????????");
        }

    }

    public GoodsResponseDto deleteGoods(Long id, UserDetailsImpl userDetails) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("????????? ??????????????? ????????? ??? ????????????");
        }

        String loginUser = userDetails.getUser().getUserid();
        String writer = "";

        Goods goods = goodsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("????????? ?????? ??? ????????????")
        );
        if(goods != null){
            writer = goods.getCreatedUser().getUserid();

            if(!loginUser.equals(writer)){
                throw new IllegalArgumentException("??????????????? ????????????");
            }
            goodsRepository.deleteById(id);
            GoodsResponseDto goodsResponseDto = new GoodsResponseDto("success","????????????");
            return goodsResponseDto;
        }
        else{
            throw new NullPointerException("?????? ????????? ???????????? ????????????");
        }
    }

    public String getDayBefore(Goods goods) throws ParseException {

        //LocalDateTime -> Date?????? ??????
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = goods.getCreatedAt();

        Date format1 = java.sql.Timestamp.valueOf(now);
        Date format2 = java.sql.Timestamp.valueOf(createdAt);


        // date.getTime() : Date??? ?????????????????? ??????. 1??? = 1000?????????
        Long diffSec = (format1.getTime() - format2.getTime()) / 1000; // ??? ??????
        Long diffMin = (format1.getTime() - format2.getTime()) / 60000; // ??? ??????
        Long diffHour = (format1.getTime() - format2.getTime()) / 3600000; // ??? ??????, 24????????? ??????
        Long diffDays = diffSec / (24 * 60 * 60); // ????????? ?????? ???:7???, 6???

        //DayBefore ??????
        // ??? ????????? 60??? ????????? ??? -> return ??? ??????
        // ??? ????????? 60??? ??????, ??? ????????? 60??? ????????? ??? -> return ??? ??????
        // ??? ????????? 60??? ??????, ??? ????????? 24 ????????? ??? -> return ??? ??????
        // ??? ????????? 24 ??????, ??? ????????? 7??? ????????? ??? -> return ????????? ??????
        // ??? ????????? 7??? ????????? ??? -> return createdAt??? ???, ???, ?????????

        String dayBefore = "";

        if(diffSec < 60) {
            String secstr = diffSec.toString();
            dayBefore = secstr + "??? ???";
        } else if(diffSec >= 60 && diffMin < 60) {
            String minstr = diffMin.toString();
            dayBefore = minstr + "??? ???";
        } else if(diffMin >= 60 && diffHour < 24) {
            String hourstr = diffHour.toString();
            dayBefore = hourstr + "?????? ???";
        } else if(diffHour >= 24 && diffDays < 7) {
            String daystr = diffDays.toString();
            dayBefore = daystr + "??? ???";
        } else if (diffDays > 7) {
            dayBefore = format2.toString();
        }
        return dayBefore;
    }




}
