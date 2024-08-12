package com.watermelon.server.lottery.link.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.ControllerTest;
import com.watermelon.server.DocumentConstants;
import com.watermelon.server.event.lottery.link.controller.LinkController;
import com.watermelon.server.event.lottery.link.dto.MyLinkDto;
import com.watermelon.server.event.lottery.link.service.LinkService;
import com.watermelon.server.event.lottery.link.utils.LinkUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.watermelon.server.Constants.*;
import static com.watermelon.server.common.constants.PathConstants.MY_LINK;
import static com.watermelon.server.common.constants.PathConstants.SHORTED_LINK;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LinkController.class)
@DisplayName("[단위] 링크 컨트롤러")
class LinkControllerTest extends ControllerTest {

    @Test
    @DisplayName("자신의 링크 반환 - 성공")
    void getMyLink() throws Exception {

        givenLink();

        whenLinkIsRetrieved();

        thenLinkIsRetrieved();

        resultActions
                .andDo(document(DocumentConstants.MY_LINK,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LINK)
                                        .description("로그인한 유저의 링크를 조회")
                                        .build()
                        )));
    }

    @Test
    @DisplayName("Uri 포함 주소 리디렉션 - 성공")
    void redirect() throws Exception {

        givenOriginUri();

        whenRedirect();

        thenRedirect();

        resultActions
                .andDo(document(DocumentConstants.SHORTED_LINK,
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG_LINK)
                                        .description("단축된 URL 에 대한 공유 페이지로 리디렉션")
                                        .build()
                        )));
    }

}