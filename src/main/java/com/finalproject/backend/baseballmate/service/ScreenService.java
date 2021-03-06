package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.ChatRoom;
import com.finalproject.backend.baseballmate.repository.ChatRoomRepository;
import com.finalproject.backend.baseballmate.repository.JoinRequestQueryRepository;
import com.finalproject.backend.baseballmate.model.JoinRequests;
import com.finalproject.backend.baseballmate.model.*;
import com.finalproject.backend.baseballmate.repository.*;
import com.finalproject.backend.baseballmate.requestDto.ScreenRequestDto;
import com.finalproject.backend.baseballmate.responseDto.*;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.utils.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final ScreenApplicationRepository screenApplicationRepository;
    private final ScreenCommentRepository screenCommentRepository;
    private final ScreenLikesRepository screenLikesRepository;
    private String commonPath = "/images";
    private final UserRepository userRepository;
    private final CanceledScreenListRepository canceledScreenListRepository;
    private final ChatRoomService chatRoomService;
    String[] picturelist = {"screen0","screen1","screen2","screen3","screen4","screen5","screen6","screen7","screen8","screen9"};
//    String[] picturelist = {"screen0.jpg","screen1.jpg","screen2.jpg","screen3.jpg","screen4.jpg","screen5.jpg","screen6.jpg","screen7.jpg","screen8.jpg","screen9.jpg"};
    Random random = new Random();
    private final ChatRoomRepository chatRoomRepository;
    private final JoinRequestQueryRepository joinRequestQueryRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
    public ScreenResponseDto createScreenlegacy(ScreenRequestDto requestDto, UserDetailsImpl userDetails,MultipartFile files) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("????????? ???????????? ????????? ?????? ??????????????? ???????????????");
        }
        try {
            String filename = picturelist[random.nextInt(10) + 1];
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

            User loginedUser = userDetails.getUser();
            String loginedUsername = userDetails.getUser().getUsername();
            Screen screen = new Screen(requestDto, loginedUser);
            screenRepository.save(screen);
            chatRoomService.createScreenChatRoom(screen.getScreenId(), userDetails);
            ScreenResponseDto screenResponseDto = new ScreenResponseDto("success", "?????? ??????");
            return screenResponseDto;
        }
        catch (Exception e)
        {
            ScreenResponseDto screenResponseDto = new ScreenResponseDto("failed", "?????? ?????? ??????");
            return screenResponseDto;
        }
    }

    @Transactional
    public ScreenResponseDto createScreen(ScreenRequestDto requestDto, UserDetailsImpl userDetails) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("????????? ???????????? ????????? ?????? ??????????????? ???????????????");
        }
        if (requestDto.getFilePath() == "") {
            requestDto.setFilePath(picturelist[random.nextInt(10) + 1]);
        }
            User loginedUser = userDetails.getUser();
            String loginedUsername = userDetails.getUser().getUsername();
            Screen screen = new Screen(requestDto, loginedUser);
            screenRepository.save(screen);
            chatRoomService.createScreenChatRoom(screen.getScreenId(), userDetails);
            ScreenResponseDto screenResponseDto = new ScreenResponseDto("success", "?????? ??????");
            return screenResponseDto;
        }


    public List<AllScreenResponseDto> getAllScreens() {
        List<Screen> screenList = screenRepository.findAllByOrderByCreatedAtDesc();
        List<AllScreenResponseDto> allScreenResponseDtos = new ArrayList<>();
        for (int i = 0; i < screenList.size(); i++) {
            Screen screen = screenList.get(i);

            Long screenId = screen.getScreenId();
            String title = screen.getTitle();
            String createdUsername = screen.getCreatedUsername();
            int peopleLimit = screen.getPeopleLimit();
            int canApplyNum = screen.getCanApplyNum();
            double hotPercent = screen.getHotPercent();
            String groupDate = screen.getGroupDate();
            String filePath = screen.getFilePath();
            String selectPlace = screen.getSelectPlace();
            String placeInfomation = screen.getPlaceInfomation();
            int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(), month, day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(), target);
            String dday = countingday.toString();
            boolean allowtype = screen.isAllowtype();
            AllScreenResponseDto allScreenResponseDto =
                    new AllScreenResponseDto(screenId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday,allowtype);
            allScreenResponseDtos.add(allScreenResponseDto);
        }
        return allScreenResponseDtos;
    }

    public List<AllScreenResponseDto> showScreenByregion(String location, Pageable pageable) {
//        PageRequest
        Page<Screen> grouppage = screenRepository.findByPlaceInfomation(location, pageable);
        List<AllScreenResponseDto> allScreenResponseDtos = new ArrayList<>();
        List<Screen> groupList = grouppage.getContent();
        for (int i = 0; i < groupList.size(); i++) {
            Screen group = groupList.get(i);

            Long groupId = group.getScreenId();
            String title = group.getTitle();
            String createdUsername = group.getCreatedUsername();
            int peopleLimit = group.getPeopleLimit();
            int canApplyNum = group.getCanApplyNum();
            double hotPercent = group.getHotPercent();
            String groupDate = group.getGroupDate();
            String filePath = group.getFilePath();
            String selectPlace = group.getSelectPlace();
            String placeInfomation = group.getPlaceInfomation();

            int month = Integer.parseInt(group.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(group.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(), month, day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(), target);
            String dday = countingday.toString();
            boolean allowtype = group.isAllowtype();
            AllScreenResponseDto allGroupResponseDto =
                    new AllScreenResponseDto(groupId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday,allowtype);

            allScreenResponseDtos.add(allGroupResponseDto);
        }

        return allScreenResponseDtos;
    }


    // ????????? ?????? ????????????
    public ScreenDetailResponseDto getScreenDetails(Long id) {
        Screen screen = screenRepository.findByScreenId(id);
        List<Map<String, String>> appliedUsers = new ArrayList<>();

        if (screen.getScreenApplications().size() != 0) {
            // ????????? ?????? ????????? ?????????
            for (int i = 0; i < screen.getScreenApplications().size(); i++) {
                ScreenApplication screenApplication = screen.getScreenApplications().get(i);
                String appliedUserInx = screenApplication.getAppliedUser().getId().toString();
                String appliedUserId = screenApplication.getAppliedUser().getUserid();
                String appliedUsername = screenApplication.getAppliedUser().getUsername();
                String appliedUserImage = screenApplication.getAppliedUser().getPicture();

                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("UserImage", appliedUserImage);
                userInfo.put("Username", appliedUsername);
                userInfo.put("UserId", appliedUserId);
                userInfo.put("UserInx", appliedUserInx);

                appliedUsers.add(i, userInfo);
            }
        }
        Long screenId = screen.getScreenId();
        String createdUserId = screen.getScreenCreatedUser().getUserid();
        String createdUserProfileImg = screen.getScreenCreatedUser().getPicture();
        String title = screen.getTitle();
        String createdUsername = screen.getCreatedUsername();
        String content = screen.getContent();
        int peopleLimit = screen.getPeopleLimit();
        int nowAppliedNum = screen.getNowAppliedNum();
        int canApplyNum = screen.getCanApplyNum();
        double hotPercent = screen.getHotPercent();
        String groupDate = screen.getGroupDate();
        String filePath = screen.getFilePath();
        String placeInfomation = screen.getPlaceInfomation();
        List<ScreenComment> screenCommentList = screenCommentRepository.findAllByScreenScreenIdOrderByCreatedAtAsc(id);
        List<Map<String, String>> appliedUserInfo = appliedUsers;

        // D - day ??????
        int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
        int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
        LocalDate target = LocalDate.of(LocalDate.now().getYear(), month, day);
        Long countingday = ChronoUnit.DAYS.between(LocalDate.now(), target);
        String dday = countingday.toString();
        boolean allowtype = screen.isAllowtype();
        ScreenDetailResponseDto screenDetailResponseDto =
                new ScreenDetailResponseDto(screenId, createdUsername, createdUserId, createdUserProfileImg, title,content, peopleLimit, nowAppliedNum, canApplyNum, hotPercent, groupDate, filePath,dday,placeInfomation,allowtype,appliedUserInfo,screenCommentList);

        return screenDetailResponseDto;
    }

    // ????????? ?????? ?????? ??????
    @Transactional
    public MsgResponseDto deleteScreen(Long screenId, UserDetailsImpl userDetails) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("???????????? ????????????");
        }

        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Screen screen = screenRepository.findByScreenId(screenId);
        ChatRoom chatRoom = chatRoomRepository.findByScreen(screen);
        if (screen != null) {
            createdUserId = screen.getScreenCreatedUser().getUserid();

            if (!loginedUserId.equals(createdUserId)) {
                throw new IllegalArgumentException("?????? ????????? ????????????");
            }
            chatRoomRepository.delete(chatRoom);
            screenRepository.deleteById(screenId);
            joinRequestQueryRepository.findBydeletescreen(screenId);

            Alarm targetAlarm = alarmRepository.findByAlarmTypeAndPostId("Screen",screenId);
            if(targetAlarm!=null){
                alarmRepository.deleteById(targetAlarm.getId());
                MsgResponseDto msgResponseDto = new MsgResponseDto("success", "?????? ??????");
                return msgResponseDto;
            }
        } else {
            throw new IllegalArgumentException("?????? ???????????? ???????????? ????????????");
        }
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "?????? ??????");
        return msgResponseDto;
    }

    // ????????? ?????? ?????? ????????????
    @Transactional
    public MsgResponseDto applyScreen(Long screenId, UserDetailsImpl userDetails) {
//        List<User> cancleUserList = appliedScreen.getCanceledUser();
        Screen appliedScreen = screenRepository.findByScreenId(screenId);

        if(!appliedScreen.isAllowtype())
        {
            throw new IllegalArgumentException("????????? ????????? ?????????????????????.");
        }

        if (userDetails == null) {
            throw new IllegalArgumentException("????????? ??? ???????????? ????????? ??? ????????????.");
        } else {
            User loginedUser = userDetails.getUser();

        ScreenApplication screenApplication = screenApplicationRepository.findByAppliedScreenAndAndAppliedUser(appliedScreen, loginedUser);
        // ????????? ?????? ?????? ???????????? ?????? ????????? ??????, ????????? ?????? ????????? ?????? ????????? ?????? ???????????? ?????? -> ??? ????????? ?????? ?????? ??????
        if((screenApplication == null) && (!Objects.equals(loginedUser.getUserid(), appliedScreen.getScreenCreatedUser().getUserid()))){

            // ????????? ???????????? ?????? ??????????????? ?????? ????????? ???????????? ?????? ?????? ???????????? ????????????
            List<CanceledScreenList> canceledScreenLists = canceledScreenListRepository.findAllByCancledScreen_ScreenId(screenId);
            if(canceledScreenLists.size() != 0) {
                for(int i=0; i<canceledScreenLists.size(); i++){
                    CanceledScreenList canceledScreenList = canceledScreenLists.get(i);
                    if(canceledScreenList.getCanceledUser().getId().equals(loginedUser.getId())){
                        throw new IllegalArgumentException("?????? ????????? ???????????? ???????????????");
                        }
                    else {
                        ScreenApplication application = new ScreenApplication(loginedUser,appliedScreen);
                        screenApplicationRepository.save(application);

                        int nowAppliedNum = application.getAppliedScreen().getNowAppliedNum();
                        int updateAppliedNum = nowAppliedNum + 1;
                        application.getAppliedScreen().setNowAppliedNum(updateAppliedNum);

                        int nowCanApplyNum = application.getAppliedScreen().getCanApplyNum();
                        int updatedCanApplyNum = nowCanApplyNum - 1;
                        application.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                        // ????????? ??? ??????
                        int peopleLimit = application.getAppliedScreen().getPeopleLimit();
                        double updatedHotPercent = ((double) updateAppliedNum / (double) peopleLimit * 100.0);
                        application.getAppliedScreen().setHotPercent(updatedHotPercent);

                        }
                    }
                } else {
                    // ?????? ???????????? ?????? ?????? ?????? ?????? ?????? ??????
                    ScreenApplication application1 = new ScreenApplication(loginedUser, appliedScreen);
                    screenApplicationRepository.save(application1);

                    int nowAppliedNum = application1.getAppliedScreen().getNowAppliedNum();
                    int updateAppliedNum = nowAppliedNum + 1;
                    application1.getAppliedScreen().setNowAppliedNum(updateAppliedNum);

                    int nowCanApplyNum = application1.getAppliedScreen().getCanApplyNum();
                    int updatedCanApplyNum = nowCanApplyNum - 1;
                    application1.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                    // ????????? ??? ??????
                    int peopleLimit = application1.getAppliedScreen().getPeopleLimit();
                    double updatedHotPercent = ((double) updateAppliedNum / (double) peopleLimit * 100.0);
                    application1.getAppliedScreen().setHotPercent(updatedHotPercent);
                }
            } else {
                throw new IllegalArgumentException("????????? ??????????????? ??????????????? ????????????."); // ????????? ?????? ????????? ???????????? ?????? or ?????? ????????? ?????? ??????
            }
        }
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "?????? ?????? ??????");
        return msgResponseDto;
    }


    @Transactional
    public MsgResponseDto cancleApplication(Long screenId, UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("????????? ??? ???????????? ????????? ??? ????????????.");
        }
        Screen screen = screenRepository.findByScreenId(screenId);
        List<ScreenApplication> screenApplicationList = screenApplicationRepository.findAllByAppliedScreen(screen);
        User loginedUser = userDetails.getUser();
        Long loginedUserIndex = userDetails.getUser().getId();

        List<Long> testlist = new ArrayList<>();

        for(int j=0; j<screenApplicationList.size();j++)
        {
            testlist.add(screenApplicationList.get(j).getAppliedUser().getId());
        }

        if(!testlist.contains(loginedUserIndex))
        {
            throw new IllegalArgumentException("???????????? ????????? ????????????.");
        }

        for (int i = 0; i < screenApplicationList.size(); i++) {
            // ?????? ?????? ????????? ????????? screenId??? ?????? groupapplication????????? ?????????
            ScreenApplication screenApplication = screenApplicationList.get(i);
            // ?????? ?????? ????????? ???????????? ????????? ?????? ?????? ???????????? ??????
            if (screenApplication != null && screenApplication.getAppliedUser().getId().equals(loginedUserIndex)) {

                // ????????? ??? ????????? ?????? ????????? ?????? ????????? ?????????
                    // ?????? ?????? ?????? ?????? 1 ??????
                    int nowAppliedNum = screenApplication.getAppliedScreen().getNowAppliedNum();
                    int updatedAppliedNum = nowAppliedNum - 1;
                    screenApplication.getAppliedScreen().setNowAppliedNum(updatedAppliedNum);

                    // ?????? ?????? ?????? ????????? ?????? 1 ??????
                    int nowCanApplyNum = screenApplication.getAppliedScreen().getCanApplyNum();
                    int updatedCanApplyNum = nowCanApplyNum + 1;
                    screenApplication.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                    // ????????? ??? ??????
                    int peopleLimit = screenApplication.getAppliedScreen().getPeopleLimit();
                    double updatedHotPercent = ((double) updatedAppliedNum / (double) peopleLimit * 100.0);
                    screenApplication.getAppliedScreen().setHotPercent(updatedHotPercent);

                    // ?????? ?????? ?????? ????????????
                    screenApplicationRepository.delete(screenApplication);

                    // ?????? ???????????? ????????????
                    CanceledScreenList canceledScreenList = new CanceledScreenList(loginedUser, screen);
                    canceledScreenListRepository.save(canceledScreenList);

                }
        }
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "?????? ?????? ?????? ??????");
        return msgResponseDto;
    }


    public MsgResponseDto updateScreenlegacy(Long screenId, MultipartFile file, ScreenRequestDto requestDto, UserDetailsImpl userDetails) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // ?????? ????????? ??????
        if (userDetails == null) {
            throw new IllegalArgumentException("????????? ?????? ??? ??????????????????.");
        }

        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Screen screen = screenRepository.findByScreenId(screenId);
        if (screen != null) {
            createdUserId = screen.getScreenCreatedUser().getUserid();

            if (!loginedUserId.equals(createdUserId)) {
                throw new IllegalArgumentException("?????? ????????? ????????????.");
            }
            if (file != null) {
                String origFilename = file.getOriginalFilename();
                String filename = new MD5Generator(origFilename).toString() + "jpg";

                String savePath = System.getProperty("user.dir") + commonPath;

                // ????????? ???????????? ????????? ?????? ?????? ?????? ??????
                if (!new java.io.File(savePath).exists()) {
                    try {
                        new java.io.File(savePath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }

                // ????????? ?????? ??????
                String filePath = savePath + "/" + filename;
                try {
                    file.transferTo(new File(filePath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                screen.setFilePath(filename);

            }
            if(file == null)
            {
                String filename = picturelist[random.nextInt(10)+1];
                screen.setFilePath(filename);
            }
            screen.updateScreen(requestDto);
            screenRepository.save(screen);
            MsgResponseDto msgResponseDto = new MsgResponseDto("success", "?????? ??????");
            return msgResponseDto;
        } else {
            throw new NullPointerException("?????? ???????????? ???????????? ????????????.");
        }
    }

    public MsgResponseDto updateScreen(Long screenId, ScreenRequestDto requestDto, UserDetailsImpl userDetails) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // ?????? ????????? ??????
        if (userDetails == null) {
            throw new IllegalArgumentException("????????? ?????? ??? ??????????????????.");
        }

        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Screen screen = screenRepository.findByScreenId(screenId);
        if (screen != null) {
            createdUserId = screen.getScreenCreatedUser().getUserid();

            if (!loginedUserId.equals(createdUserId)) {
                throw new IllegalArgumentException("?????? ????????? ????????????.");
            }
            screen.updateScreen(requestDto);
            screenRepository.save(screen);
            MsgResponseDto msgResponseDto = new MsgResponseDto("success", "?????? ??????");
            return msgResponseDto;
        } else {
            throw new NullPointerException("?????? ???????????? ???????????? ????????????.");
        }
    }
    public List<AllScreenResponseDto> getMywriteAllScreens(User userdetail) {
        List<Screen> screenList = screenRepository.findAllByScreenCreatedUser(userdetail);
        List<AllScreenResponseDto> allScreenResponseDtoList = new ArrayList<>();
        for (int i = 0; i < screenList.size(); i++) {
            Screen screen = screenList.get(i);

            Long screenId = screen.getScreenId();
            String title = screen.getTitle();
            String createdUsername = screen.getCreatedUsername();
            int peopleLimit = screen.getPeopleLimit();
            int canApplyNum = screen.getCanApplyNum();
            double hotPercent = screen.getHotPercent();
            String groupDate = screen.getGroupDate();
            String filePath = screen.getFilePath();
            String selectPlace = screen.getSelectPlace();
            String placeInfomation = screen.getPlaceInfomation();
            int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(), month, day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(), target);
            String dday = countingday.toString();
            boolean allowtype = screen.isAllowtype();
            AllScreenResponseDto allScreenResponseDto =
                    new AllScreenResponseDto(screenId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday,allowtype);
            allScreenResponseDtoList.add(allScreenResponseDto);
        }
        return allScreenResponseDtoList;
    }

    // ?????? ????????? ?????? ??????
    public List<AllScreenResponseDto> getMyapplicationAllScreens(User userdetail) {
        List<Screen> screenList = new ArrayList<>();
        List<ScreenApplication> myscreenApplicationList = screenApplicationRepository.findAllByAppliedUser(userdetail);
        for (int i = 0; i < myscreenApplicationList.size(); i++) {
            screenList.add(myscreenApplicationList.get(i).getAppliedScreen());
        }
        List<AllScreenResponseDto> allScreenResponseDtoList = new ArrayList<>();
        for (int i = 0; i < screenList.size(); i++) {
            Screen screen = screenList.get(i);

            Long screenId = screen.getScreenId();
            String title = screen.getTitle();
            String createdUsername = screen.getCreatedUsername();
            int peopleLimit = screen.getPeopleLimit();
            int canApplyNum = screen.getCanApplyNum();
            double hotPercent = screen.getHotPercent();
            String groupDate = screen.getGroupDate();
            String filePath = screen.getFilePath();
            String selectPlace = screen.getSelectPlace();
            String placeInfomation = screen.getPlaceInfomation();
            int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(), month, day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(), target);
            String dday = countingday.toString();
            boolean allowtype = screen.isAllowtype();
            AllScreenResponseDto allScreenResponseDto =
                    new AllScreenResponseDto(screenId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday,allowtype);
            allScreenResponseDtoList.add(allScreenResponseDto);
        }
        return allScreenResponseDtoList;
    }

    public List<AllScreenResponseDto> getMylikeAllScreens(User userdetail) {
        List<Screen> screenList = new ArrayList<>();
        List<ScreenLikes> myscreenlikeslist = screenLikesRepository.findAllByUserId(userdetail.getId());
        for(int i = 0; i<myscreenlikeslist.size(); i++)
        {
            screenList.add(myscreenlikeslist.get(i).getScreenlikes());
        }
        List<AllScreenResponseDto> allScreenResponseDtoList = new ArrayList<>();
        for (int i = 0; i < screenList.size(); i++) {
            Screen screen = screenList.get(i);

            Long screenId = screen.getScreenId();
            String title = screen.getTitle();
            String createdUsername = screen.getCreatedUsername();
            int peopleLimit = screen.getPeopleLimit();
            int canApplyNum = screen.getCanApplyNum();
            double hotPercent = screen.getHotPercent();
            String groupDate = screen.getGroupDate();
            String filePath = screen.getFilePath();
            String selectPlace = screen.getSelectPlace();
            String placeInfomation = screen.getPlaceInfomation();
            int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(), month, day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(), target);
            String dday = countingday.toString();
            boolean allowtype = screen.isAllowtype();
            AllScreenResponseDto allScreenResponseDto =
                    new AllScreenResponseDto(screenId, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday,allowtype);
            allScreenResponseDtoList.add(allScreenResponseDto);
        }
        return allScreenResponseDtoList;
    }


    @Transactional
    public MsgResponseDto denyScreen(Long screenId, UserDetailsImpl userDetails) {
        Screen screen = screenRepository.findByScreenId(screenId);
        if (screen.getScreenCreatedUser().getUserid().equals(userDetails.getUser().getUserid())) {
            if (screen.isAllowtype()) {
                screen.setAllowtype(false);
                String msg =  "?????? ?????? ??????. ???????????? ????????? ?????? ????????????.";
                MsgResponseDto msgResponseDto = new MsgResponseDto("success", msg);
                return msgResponseDto;
            } else {
                screen.setAllowtype(true);
                String msg = "?????? ???????????? ??????. ???????????? ????????? ?????? ??? ??? ????????????.";
                MsgResponseDto msgResponseDto = new MsgResponseDto("success", msg);
                return msgResponseDto;
            }
        } else {
            throw new IllegalArgumentException("???????????? ????????? ???????????????");
        }
    }
    // ?????? ????????? ??????
    @Transactional
    public List<AllScreenResponseDto> getnowScreen(int number) throws ParseException
    {
        List<Screen> screenList = screenRepository.findAllByOrderByCreatedAtDesc();
        List<AllScreenResponseDto> data = new ArrayList<>();

        if(screenList.size()<=number){
            number = screenList.size();
        }
        for(int i = 0; i<screenList.size(); i++){
            Screen screen = screenList.get(i);

            Long id = screen.getScreenId();
            String title = screen.getTitle();
            String createdUsername = screen.getCreatedUsername();
            int peopleLimit = screen.getPeopleLimit();
            int canApplyNum = screen.getCanApplyNum();
            double hotPercent = screen.getHotPercent();
            String groupDate = screen.getGroupDate();
            String filePath = screen.getFilePath();
            String selectPlace = screen.getSelectPlace();
            String placeInfomation = screen.getPlaceInfomation();
            int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
            String dday = countingday.toString();
            boolean allowtype = screen.isAllowtype();
            AllScreenResponseDto allScreenResponseDto =
                    new AllScreenResponseDto(id, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday, allowtype);
            data.add(allScreenResponseDto);
        }
        return data;
    }
    // ?????? ????????? ??????
    public List<HotScreenResponseDto> getHotScreen() {
        List<Screen> hotScreenList = screenRepository.findAllByOrderByHotPercentDesc();
        List<HotScreenResponseDto> hotScreenResponseDtoList = new ArrayList<>();

        for(int i = 0; i<hotScreenList.size(); i++){
            Screen screen = hotScreenList.get(i);

            Long id = screen.getScreenId();
            String title = screen.getTitle();
            String createdUsername = screen.getCreatedUsername();
            int peopleLimit = screen.getPeopleLimit();
            int canApplyNum = screen.getCanApplyNum();
            double hotPercent = screen.getHotPercent();
            String groupDate = screen.getGroupDate();
            String filePath = screen.getFilePath();
            String selectPlace = screen.getSelectPlace();
            String placeInfomation = screen.getPlaceInfomation();

            int month = Integer.parseInt(screen.getGroupDate().split("[.]")[0]);
            int day = Integer.parseInt(screen.getGroupDate().split("[.]")[1].split(" ")[0]);
            LocalDate target = LocalDate.of(LocalDate.now().getYear(),month,day);
            Long countingday = ChronoUnit.DAYS.between(LocalDate.now(),target);
            String dday = countingday.toString();
            boolean allowtype = screen.isAllowtype();

            HotScreenResponseDto hotScreenResponseDto =
                    new HotScreenResponseDto(id, title, createdUsername, peopleLimit, canApplyNum, hotPercent, groupDate, filePath, selectPlace, placeInfomation, dday, allowtype);

            hotScreenResponseDtoList.add(hotScreenResponseDto);
        }
        return hotScreenResponseDtoList;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ????????? ?????? ?????? ????????????
    @Transactional
    public void applyScreen2(Long screenId, JoinRequests joinRequests) {
//        List<User> cancleUserList = appliedScreen.getCanceledUser();
        Screen appliedScreen = screenRepository.findByScreenId(screenId);

        if(!appliedScreen.isAllowtype())
        {
            throw new IllegalArgumentException("????????? ????????? ?????????????????????.");
        }
        User loginedUser = userRepository.findById(joinRequests.getUserId()).orElseThrow(
                ()-> new IllegalArgumentException("???????????? ?????????????????? ?????? ??? ????????????.")
        );

            ScreenApplication screenApplication = screenApplicationRepository.findByAppliedScreenAndAndAppliedUser(appliedScreen, loginedUser);
            // ????????? ?????? ?????? ???????????? ?????? ????????? ??????, ????????? ?????? ????????? ?????? ????????? ?????? ???????????? ?????? -> ??? ????????? ?????? ?????? ??????
            if((screenApplication == null) && (!Objects.equals(loginedUser.getUserid(), appliedScreen.getScreenCreatedUser().getUserid()))){

                // ????????? ???????????? ?????? ??????????????? ?????? ????????? ???????????? ?????? ?????? ???????????? ????????????
                List<CanceledScreenList> canceledScreenLists = canceledScreenListRepository.findAllByCancledScreen_ScreenId(screenId);
                if(canceledScreenLists.size() != 0) {
                    for(int i=0; i<canceledScreenLists.size(); i++){
                        CanceledScreenList canceledScreenList = canceledScreenLists.get(i);
                        if(canceledScreenList.getCanceledUser().getId().equals(loginedUser.getId())){
                            throw new IllegalArgumentException("?????? ????????? ???????????? ???????????????");
                        }
                        else {
                            ScreenApplication application = new ScreenApplication(loginedUser,appliedScreen);
                            screenApplicationRepository.save(application);

                            int nowAppliedNum = application.getAppliedScreen().getNowAppliedNum();
                            int updateAppliedNum = nowAppliedNum + 1;
                            application.getAppliedScreen().setNowAppliedNum(updateAppliedNum);

                            int nowCanApplyNum = application.getAppliedScreen().getCanApplyNum();
                            int updatedCanApplyNum = nowCanApplyNum - 1;
                            application.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                            // ????????? ??? ??????
                            int peopleLimit = application.getAppliedScreen().getPeopleLimit();
                            double updatedHotPercent = ((double) updateAppliedNum / (double) peopleLimit * 100.0);
                            application.getAppliedScreen().setHotPercent(updatedHotPercent);

                        }
                    }
                } else {
                    // ?????? ???????????? ?????? ?????? ?????? ?????? ?????? ??????
                    ScreenApplication application1 = new ScreenApplication(loginedUser, appliedScreen);
                    screenApplicationRepository.save(application1);

                    int nowAppliedNum = application1.getAppliedScreen().getNowAppliedNum();
                    int updateAppliedNum = nowAppliedNum + 1;
                    application1.getAppliedScreen().setNowAppliedNum(updateAppliedNum);

                    int nowCanApplyNum = application1.getAppliedScreen().getCanApplyNum();
                    int updatedCanApplyNum = nowCanApplyNum - 1;
                    application1.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                    // ????????? ??? ??????
                    int peopleLimit = application1.getAppliedScreen().getPeopleLimit();
                    double updatedHotPercent = ((double) updateAppliedNum / (double) peopleLimit * 100.0);
                    application1.getAppliedScreen().setHotPercent(updatedHotPercent);
                }
            } else {
                throw new IllegalArgumentException("????????? ??????????????? ??????????????? ????????????."); // ????????? ?????? ????????? ???????????? ?????? or ?????? ????????? ?????? ??????
            }
        }



    @Transactional
    public void cancleApplication2(Long screenId, JoinRequests joinRequests) {
        Screen screen = screenRepository.findByScreenId(screenId);
        List<ScreenApplication> screenApplicationList = screenApplicationRepository.findAllByAppliedScreen(screen);
        User loginedUser = userRepository.findById(joinRequests.getUserId()).orElseThrow(
                ()-> new IllegalArgumentException("???????????? ?????????????????? ?????? ??? ????????????.")
        );

        Long loginedUserIndex = loginedUser.getId();

        List<Long> testlist = new ArrayList<>();

        for(int j=0; j<screenApplicationList.size();j++)
        {
            testlist.add(screenApplicationList.get(j).getAppliedUser().getId());
        }

        if(!testlist.contains(loginedUserIndex))
        {
            throw new IllegalArgumentException("???????????? ????????? ????????????.");
        }

        for (int i = 0; i < screenApplicationList.size(); i++) {
            // ?????? ?????? ????????? ????????? screenId??? ?????? groupapplication????????? ?????????
            ScreenApplication screenApplication = screenApplicationList.get(i);
            // ?????? ?????? ????????? ???????????? ????????? ?????? ?????? ???????????? ??????
            if (screenApplication != null && screenApplication.getAppliedUser().getId().equals(loginedUserIndex)) {

                // ????????? ??? ????????? ?????? ????????? ?????? ????????? ?????????
                // ?????? ?????? ?????? ?????? 1 ??????
                int nowAppliedNum = screenApplication.getAppliedScreen().getNowAppliedNum();
                int updatedAppliedNum = nowAppliedNum - 1;
                screenApplication.getAppliedScreen().setNowAppliedNum(updatedAppliedNum);

                // ?????? ?????? ?????? ????????? ?????? 1 ??????
                int nowCanApplyNum = screenApplication.getAppliedScreen().getCanApplyNum();
                int updatedCanApplyNum = nowCanApplyNum + 1;
                screenApplication.getAppliedScreen().setCanApplyNum(updatedCanApplyNum);

                // ????????? ??? ??????
                int peopleLimit = screenApplication.getAppliedScreen().getPeopleLimit();
                double updatedHotPercent = ((double) updatedAppliedNum / (double) peopleLimit * 100.0);
                screenApplication.getAppliedScreen().setHotPercent(updatedHotPercent);

                // ?????? ?????? ?????? ????????????
                screenApplicationRepository.delete(screenApplication);

                // ?????? ???????????? ????????????
                CanceledScreenList canceledScreenList = new CanceledScreenList(loginedUser, screen);
                canceledScreenListRepository.save(canceledScreenList);

            }
        }
    }
}