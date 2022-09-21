package com.ssafy.api.controller;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.response.UserRes;
import com.ssafy.api.service.UserService;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.db.entity.User;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signin")
    @ApiOperation(value = "회원 가입", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입 한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends BaseResponseBody> register(
            @RequestBody @ApiParam(value = "회원가입 정보", required = true) UserRegisterPostReq registerInfo) {
        System.out.println("sign in controller");
        //임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.
        User user = userService.createUser(registerInfo);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
    }

    @GetMapping("/id-info")
    @ApiOperation(value = "아이디 중복 검사", notes = "<strong>아이디가 DB에 있는 지 확인한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<UserRes> idOverlapCheck(
            @ApiParam(value = "아이디 정보", required = true) @RequestParam("user-id") String userId) {
        System.out.println("중복확인 시작");
        User user = userService.getUserByUserId(userId);
        if (user == null) {
            System.out.println("중복이 없습니다. 성공");
            return ResponseEntity.ok(UserRes.of(200, "Success", null));
        } else {
            System.out.println("중복 아이디가 있습니다. 실패");
            return ResponseEntity.status(401).body(UserRes.of(409, "중복된 아이디가 있습니다.", user));
        }
    }

    @GetMapping("/nickname-info")
    @ApiOperation(value = "닉네임 중복 검사", notes = "<strong>닉네임이 DB에 있는 지 확인한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<UserRes> nicknameOverlapCheck(
            @ApiParam(value = "닉네임 정보", required = true) @RequestParam("nickname") String nickname) {
        System.out.println("중복확인 시작");
        User user = userService.getUserByNickname(nickname);
        if (user == null) {
            System.out.println("중복이 없습니다. 성공");
            return ResponseEntity.ok(UserRes.of(200, "Success", null));
        } else {
            System.out.println("중복 닉네임이 있습니다. 실패");
            return ResponseEntity.status(409).body(UserRes.of(401, "중복된 아이디가 있습니다.", user));
        }
    }

    @GetMapping("/me")
    @ApiOperation(value = "회원 본인 정보 조회", notes = "로그인한 회원 본인의 정보를 응답한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<UserRes> getUserInfo(@ApiIgnore Authentication authentication) {
        /**
         * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
         * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
         */
        SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
        String userId = userDetails.getUsername();
        User user = userService.getUserByUserId(userId);

        return ResponseEntity.status(200).body(UserRes.of(200, "user_info 호출", user));
    }
}