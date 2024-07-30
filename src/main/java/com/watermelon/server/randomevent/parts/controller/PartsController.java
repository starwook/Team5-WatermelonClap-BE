package com.watermelon.server.randomevent.parts.controller;

import com.watermelon.server.randomevent.auth.annotations.Uid;
import com.watermelon.server.randomevent.parts.dto.response.ResponsePartsDrawDto;
import com.watermelon.server.randomevent.parts.exception.PartsDrawLimitExceededException;
import com.watermelon.server.randomevent.parts.service.PartsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event/parts")
public class PartsController {

    private final PartsService partsService;

    @PostMapping
    public ResponseEntity<ResponsePartsDrawDto> drawParts(
            @Uid String uid
    ){
        try {
            return new ResponseEntity<>(partsService.drawParts(uid), HttpStatus.OK);
        }catch (PartsDrawLimitExceededException e){
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    @PatchMapping("/{category}/{partsId}")
    public ResponseEntity<Void> equipParts(
            @PathVariable String category,
            @PathVariable Long partsId,
            @Uid String uid
    ){
        partsService.toggleParts(uid, partsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
