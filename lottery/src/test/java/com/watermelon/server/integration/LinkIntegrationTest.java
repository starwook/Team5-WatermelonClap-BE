package com.watermelon.server.integration;

import com.watermelon.server.event.link.service.LinkService;
import com.watermelon.server.event.link.utils.LinkUtils;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.watermelon.server.auth.service.TestTokenVerifier.TEST_UID;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("[통합] 링크 통합 테스트")
public class LinkIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private LinkService linkService;

    @Test
    @DisplayName("뽑기권 지급 - 성공")
    void test2() throws Exception {

        String uri = givenLotteryApplierHasUri();
        LotteryApplier lotteryApplier = linkService.getApplierByLinkKey(uri);
        Assertions.assertThat(lotteryApplier.getRemainChance()).isEqualTo(3);

        whenFirstUserWithLinkIdDrawParts(uri);

        LotteryApplier uidOwner = linkService.getApplierByLinkKey(uri);

        Assertions.assertThat(uidOwner.getRemainChance()).isEqualTo(4);

    }

    @Test
    @DisplayName("단축 링크 조회 - 성공")
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
