package com.watermelon.server.event.link.utils;

import com.watermelon.server.event.link.utils.LinkUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[단위] 링크 유틸 테스트")
class LinkUtilsTest {

    @Test
    @DisplayName("인코딩 & 디코딩 - 성공")
    void test(){

        //given
        String uuid = UUID.randomUUID().toString();

        //when
        String encoded = LinkUtils.toBase62(uuid);
        String decoded = LinkUtils.fromBase62(encoded);

        //then
        assertEquals(uuid, decoded);

    }

}