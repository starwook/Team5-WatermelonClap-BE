package com.watermelon.server.event.parts.controller;

import com.watermelon.server.common.exception.ErrorResponse;
import com.watermelon.server.auth.annotations.Uid;
import com.watermelon.server.event.parts.dto.response.ResponseMyPartsListDto;
import com.watermelon.server.event.parts.dto.response.ResponsePartsDrawDto;
import com.watermelon.server.event.parts.dto.response.ResponseRemainChanceDto;
import com.watermelon.server.event.parts.exception.PartsDrawLimitExceededException;
import com.watermelon.server.event.parts.service.PartsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;  

import java.util.List;

import static com.watermelon.server.common.constants.HttpConstants.HEADER_LINK_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event/parts")
public class PartsController {

    private final PartsService partsService;

    @PostMapping
    public ResponseEntity<ResponsePartsDrawDto> drawParts(
            @Uid String uid
    ) throws PartsDrawLimitExceededException {
        return new ResponseEntity<>(partsService.drawParts(uid), HttpStatus.OK);
    }

    @PatchMapping("/{partsId}")
    public ResponseEntity<Void> equipParts(
            @PathVariable Long partsId,
            @Uid String uid
    ){
        partsService.toggleParts(uid, partsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/remain")
    public ResponseEntity<ResponseRemainChanceDto> getRemainChance(
            @Uid String uid
    ) {
        return new ResponseEntity<>(partsService.getRemainChance(uid), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<ResponseMyPartsListDto>> getMyPartsList(
            @Uid String uid
    ){
        return new ResponseEntity<>(partsService.getMyParts(uid), HttpStatus.OK);

    }

    @GetMapping("/link/{link_key}")
    public ResponseEntity<List<ResponseMyPartsListDto>> getLinkPartsList(
            @PathVariable String link_key,
            HttpServletResponse response
    ){
        makeLinkCookie(response, link_key);
        return new ResponseEntity<>(partsService.getPartsList(link_key), HttpStatus.OK);
    }
    @ExceptionHandler(PartsDrawLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handlePartsDrawLimitExceedException(PartsDrawLimitExceededException partsDrawLimitExceededException){
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ErrorResponse.of(partsDrawLimitExceededException.getMessage()));
    }

    private void makeLinkCookie(HttpServletResponse response, String link_key){
        // 쿠키 생성
        Cookie cookie = new Cookie(HEADER_LINK_ID, link_key);

        // 쿠키 설정
        cookie.setHttpOnly(true); // 클라이언트 측 JavaScript에서 접근 불가
        //cookie.setSecure(true);   // HTTPS에서만 전송
        cookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키의 유효 기간 (7일)
        cookie.setPath("/");      // 모든 경로에서 유효
        //cookie.setDomain("example.com"); // 쿠키를 설정할 도메인

        response.addCookie(cookie);
    }

}
