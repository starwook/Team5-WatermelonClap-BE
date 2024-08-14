package com.watermelon.server.integration;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.watermelon.server.event.link.utils.LinkUtils;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("[통합] 링크 통합 테스트")
public class LinkIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("[예정] 공유된 링크를 통해 이벤트 페이지로 이동해 가입한 사람이 있다면 링크의 유저에게 뽑기권을 1개 지급한다.")
    void test2() {

    }

    @Test
    @DisplayName("[예정] 공유한 링크를 통해 컬렉션 페이지에 방문한 사람에게는 장착된 파츠만 전달한다.")
    void test3() {

    }

    @Test
    @DisplayName("유저의 링크를 조회하면 단축된 링크를 반환한다.")
    void test4() throws Exception {

        LotteryApplier lotteryApplier = LotteryApplier.createLotteryApplier(TEST_UID);
        lotteryApplierRepository.save(lotteryApplier);

        String originUrl = lotteryApplier.getLink().getUri();

        String shortedUrl = LinkUtils.toBase62(originUrl);

        whenLinkIsRetrieved();

        resultActions.andExpect(
                jsonPath("link").value("http://43.202.54.29:8080/link/"+shortedUrl)
        );

    }

    @Test
    @DisplayName("Uri 포함 주소 리디렉션 - 성공")
    void redirect() throws Exception {

        givenOriginUri();

        whenRedirect();

        thenRedirect();

    }

}
