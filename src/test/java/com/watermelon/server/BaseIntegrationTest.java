package com.watermelon.server;


import com.epages.restdocs.apispec.ResourceSnippet;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermelon.server.event.lottery.parts.repository.LotteryApplierPartsRepository;
import com.watermelon.server.event.lottery.parts.repository.PartsRepository;
import com.watermelon.server.event.lottery.repository.LotteryApplierRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;

@SpringBootTest
@Disabled
@Transactional
@Import(PartsRegistrationConfig.class)
public class BaseIntegrationTest extends APITest{

    @Autowired
    protected LotteryApplierRepository lotteryApplierRepository;

    @Autowired
    protected PartsRepository partsRepository;

    @Autowired
    protected LotteryApplierPartsRepository lotteryApplierPartsRepository;

    protected ResourceSnippet resourceSnippet(String description) {
        return resource(
                ResourceSnippetParameters.builder()
                        .description(description)
                        .build()
        );
    }

    protected ResourceSnippet resourceSnippetAuthed(String description){

        return resource(
                ResourceSnippetParameters.builder()
                        .description(description)
                        .requestHeaders(
                                headerWithName("Authorization").description("Bearer token for authentication"))
                        .build()
        );

    }


    @Override
    protected void givenLotteryRewardInfo() {

    }

    @Override
    protected void givenLotteryWinners() {

    }

    @Override
    protected void givenLotteryWinnersForAdmin() {

    }

    @Override
    protected void givenLotteryWinnerInfo() {

    }

    @Override
    protected void givenLotteryApplierNotExist() {

    }

    @Override
    protected void givenLotteryWinner() {

    }

    @Override
    protected void givenPartsListForUri() {

    }

    @Override
    protected void givenLotteryApplierWhoHasNoRemainChance() {

    }

    @Override
    protected void givenLotteryApplierWhoDrawsPartsFirst() {

    }

    @Override
    protected void givenLotteryApplierWhoHasRemainChance() {

    }

    @Override
    protected void givenMyPartsList() {

    }

    @Override
    protected void givenLink() {

    }

    @Override
    protected void givenOriginUri() {

    }
}
