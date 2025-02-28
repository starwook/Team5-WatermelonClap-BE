package com.watermelon.server.event.link.controller;

import com.watermelon.server.event.link.service.LinkService;
import com.watermelon.server.auth.annotations.Uid;
import com.watermelon.server.event.link.dto.MyLinkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @GetMapping("/link")
    public ResponseEntity<MyLinkDto> getMyLink(
            @Uid String uid
    ){
        return new ResponseEntity<>(linkService.getShortedLink(uid), HttpStatus.OK);
    }

    @GetMapping("/link/{shortedUri}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortedUri
    ){
        HttpHeaders headers = new HttpHeaders();

        // 현재 서버의 URL을 기반으로 새로운 경로를 생성
        headers.add(HttpHeaders.LOCATION, linkService.getRedirectUrl(shortedUri));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}