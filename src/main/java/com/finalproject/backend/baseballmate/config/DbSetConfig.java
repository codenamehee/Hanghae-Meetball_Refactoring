//package com.finalproject.backend.baseballmate.config;
//
//import com.finalproject.backend.baseballmate.model.Goods;
//import com.finalproject.backend.baseballmate.model.Group;
//import com.finalproject.backend.baseballmate.model.User;
//import com.finalproject.backend.baseballmate.repository.GoodsRepository;
//import com.finalproject.backend.baseballmate.repository.GroupRepository;
//import com.finalproject.backend.baseballmate.repository.UserRepository;
//import com.finalproject.backend.baseballmate.requestDto.*;
//import com.finalproject.backend.baseballmate.service.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import javax.transaction.Transactional;
//import java.io.IOException;
//
//@RequiredArgsConstructor
//@Component
//public class DbSetConfig {
//    private final UserService userService;
//    private final GroupService groupService;
//    private final TimeLineService timeLineService;
//    private final GoodsService goodsService;
//    private final MatchDataService matchDataService;
//    private final GroupCommentService groupCommentService;
//    private final GoodsCommentService goodsCommentService;
//    private final UserRepository userRepository;
//    private final GroupRepository groupRepository;
//    private final GoodsRepository goodsRepository;
//    public void dbset1() throws IOException {
//        kboSet();
//        userSet();
//    }
//
//    public void dbset2(){
//        timelineSet();
//        groupSet();
//        goodsSet();
//    }
//
//    public void dbset3(){
//        commentSet();
//    }
//
//    @Transactional
//    public void kboSet() throws IOException {
//        matchDataService.createKBODatas();
//    }
//
//    @Transactional
//    public void userSet()
//    {
//        UserRequestDto user = new UserRequestDto();
//        user.setUserid("bbb@bbb.com");
//        user.setUsername("bbb");
//        user.setPassword("a123123!");
//        user.setPhonenumber("01071583399");
////        user.setProfileImage("/Profileimages/basic_profile");
//        userService.registerUser(user);
//
//        UserRequestDto user2 = new UserRequestDto();
//        user2.setUserid("aaa@aaa.com");
//        user2.setUsername("aaa");
//        user2.setPassword("a123123!");
//        user2.setPhonenumber("01071583399");
////        user2.setProfileImage("/Profileimages/basic_profile");
//        userService.registerUser(user2);
//
//        UserRequestDto user3 = new UserRequestDto();
//        user3.setUserid("ccc@ccc.com");
//        user3.setUsername("ccc");
//        user3.setPassword("a123123!");
//        user3.setPhonenumber("01071583399");
////        user2.setProfileImage("/Profileimages/basic_profile");
//        userService.registerUser(user3);
//    }
//
//    @Transactional
//    public void timelineSet()
//    {
//        TimeLineRequestDto reqdto = new TimeLineRequestDto();
//        reqdto.setContent("aaa????????????????????????1");
//        User loginuser=userRepository.findByUsername("aaa").orElseThrow(
//                ()->new IllegalArgumentException("???????????????")
//        );
//        timeLineService.createTimeLine(loginuser,reqdto);
//
//        reqdto.setContent("bbb????????????????????????2");
//        loginuser = userRepository.findByUsername("bbb").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        timeLineService.createTimeLine(loginuser,reqdto);
//
//        reqdto.setContent("ccc????????????????????????3");
//        loginuser = userRepository.findByUsername("ccc").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        timeLineService.createTimeLine(loginuser,reqdto);
//
//        reqdto.setContent("aaa??????,???????????????");
//        loginuser = userRepository.findByUsername("aaa").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        timeLineService.createTimeLine(loginuser,reqdto);
//
//        reqdto.setContent("bbb??????,???????????????");
//        loginuser = userRepository.findByUsername("bbb").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        timeLineService.createTimeLine(loginuser,reqdto);
//
//        reqdto.setContent("ccc??????,???????????????");
//        loginuser = userRepository.findByUsername("ccc").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        timeLineService.createTimeLine(loginuser,reqdto);
//
//        loginuser = userRepository.findByUsername("aaa").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        timeLineService.createTimeLine(loginuser,reqdto);
//
//        reqdto.setContent("bbb??????,??????,?????? ????????????");
//        loginuser = userRepository.findByUsername("bbb").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        timeLineService.createTimeLine(loginuser,reqdto);
//    }
//
//    @Transactional
//    public void groupSet()
//    {
//        GroupRequestDto requestDto = new GroupRequestDto();
//        requestDto.setTitle("aaa??????,????????????1");
//        requestDto.setContent("???????????????????????????");
//        requestDto.setGroupDate("11???7???");
//        requestDto.setPeopleLimit(3);
//        User loginuser=userRepository.findByUsername("aaa").orElseThrow(
//                ()->new IllegalArgumentException("???????????????")
//        );
//        groupService.createGroup(requestDto,loginuser);
//
//        requestDto.setTitle("bbb??????,????????????1");
//        requestDto.setContent("?????????????????????");
//        requestDto.setGroupDate("11???25???");
//        requestDto.setPeopleLimit(5);
//        loginuser = userRepository.findByUsername("bbb").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        groupService.createGroup(requestDto,loginuser);
//
//        requestDto.setTitle("ccc??????,????????????1");
//        requestDto.setContent("ccccc11???25??? ????????? ??????");
//        requestDto.setGroupDate("11???25???");
//        requestDto.setPeopleLimit(9);
//        loginuser = userRepository.findByUsername("ccc").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        groupService.createGroup(requestDto,loginuser);
//
//        requestDto.setTitle("bbb??????,???????????????");
//        requestDto.setContent("????????? 1???");
//        requestDto.setGroupDate("12???25???");
//        requestDto.setPeopleLimit(2);
//        loginuser = userRepository.findByUsername("bbb").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        groupService.createGroup(requestDto,loginuser);
//    }
//    @Transactional
//    public void goodsSet(){
//        GoodsRequestDto requestDto = new GoodsRequestDto();
//        requestDto.setGoodsName("???????????????");
//        requestDto.setGoodsContent("???");
//        requestDto.setGoodsImg("sample.png");
//        requestDto.setGoodsPrice(30000);
//        User loginuser=userRepository.findByUsername("aaa").orElseThrow(
//                ()->new IllegalArgumentException("???????????????")
//        );
//        goodsService.createGoods(loginuser, requestDto);
//        requestDto.setGoodsName("????????? ?????????");
//        requestDto.setGoodsContent("???");
//        requestDto.setGoodsImg("sample.png");
//        requestDto.setGoodsPrice(100000);
//        loginuser = userRepository.findByUsername("bbb").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        goodsService.createGoods(loginuser, requestDto);
//
//        requestDto.setGoodsName("???????????????????????????");
//        requestDto.setGoodsContent("??????");
//        requestDto.setGoodsImg("sample.png");
//        requestDto.setGoodsPrice(2300);
//        loginuser = userRepository.findByUsername("ccc").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        goodsService.createGoods(loginuser, requestDto);
//
//        requestDto.setGoodsName("???????????????");
//        requestDto.setGoodsContent("?????? ?????? ???");
//        requestDto.setGoodsImg("sample.png");
//        requestDto.setGoodsPrice(57000);
//        loginuser = userRepository.findByUsername("aaa").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        goodsService.createGoods(loginuser, requestDto);
//
//        requestDto.setGoodsName("11???25??? ????????? ????????????");
//        requestDto.setGoodsContent("s??? ??? 2???");
//        requestDto.setGoodsImg("sample.png");
//        requestDto.setGoodsPrice(90000);
//        loginuser = userRepository.findByUsername("bbb").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        goodsService.createGoods(loginuser, requestDto);
//    }
//
//    @Transactional
//    public void commentSet()
//    {
//        GroupCommentRequestDto com1 = new GroupCommentRequestDto();
//        com1.setComment("aaa??? ????????? ???????????? ??????");
//        Group groud = groupRepository.findById(17L).orElseThrow(
//                ()->new IllegalArgumentException("?????????????????????")
//        );
//        User user = userRepository.findByUsername("aaa").orElseThrow(
//                ()->new IllegalArgumentException("????????????????????????")
//        );
//        groupCommentService.createComment(com1,groud.getGroupId(),user);
//
//        com1.setComment("bbb??? ????????? ??????????????????");
//        groud = groupRepository.findById(17L).orElseThrow(
//                ()->new IllegalArgumentException("?????????????????????")
//        );
//        user = userRepository.findByUsername("bbb").orElseThrow(
//                () -> new IllegalArgumentException("????????????????????????")
//        );
//        groupCommentService.createComment(com1,groud.getGroupId(),user);
//
//        com1.setComment("ccc??? ????????? ??????????????????");
//        groud = groupRepository.findById(17L).orElseThrow(
//                ()->new IllegalArgumentException("?????????????????????")
//        );
//        user = userRepository.findByUsername("ccc").orElseThrow(
//                () -> new IllegalArgumentException("????????????????????????")
//        );
//        groupCommentService.createComment(com1,groud.getGroupId(),user);
//
//        GoodsCommentRequestDto com2 = new GoodsCommentRequestDto();
//        com2.setComment("aaa??? ????????? ??????????????????");
//        Goods goods = goodsRepository.findById(21L).orElseThrow(
//                ()->new IllegalArgumentException("?????????????????????")
//        );
//        user = userRepository.findByUsername("ccc").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        goodsCommentService.createComment(user,com2,goods.getId());
//
//
//        com2.setComment("bbb??? ????????? ??????????????????");
//        goods = goodsRepository.findById(21L).orElseThrow(
//                () -> new IllegalArgumentException("?????????????????????")
//        );
//        user = userRepository.findByUsername("bbb").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        goodsCommentService.createComment(user,com2,goods.getId());
//
//        com2.setComment("ccc??? ????????? ??????????????????");
//        goods = goodsRepository.findById(21L).orElseThrow(
//                () -> new IllegalArgumentException("?????????????????????")
//        );
//        user = userRepository.findByUsername("bbb").orElseThrow(
//                () -> new IllegalArgumentException("???????????????")
//        );
//        goodsCommentService.createComment(user,com2,goods.getId());
//
//    }
//}