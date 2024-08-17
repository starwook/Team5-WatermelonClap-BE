package com.watermelon.server.order.service;


import com.watermelon.server.Scheduler;
import com.watermelon.server.order.domain.OrderEvent;
import com.watermelon.server.order.dto.request.RequestOrderEventDto;
import com.watermelon.server.order.dto.request.RequestOrderRewardDto;
import com.watermelon.server.order.dto.request.RequestQuizDto;
import com.watermelon.server.order.repository.OrderEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("test")
@RequiredArgsConstructor
public class OrderEventTestEnvironmentService {
    //private final ExpectationService expectationService;
    //private final ExpectationRepository expectationRepository;
    private final OrderEventRepository orderEventRepository;
    private String testImageSrc ="https://watermelon-s3-bucket.s3.ap-northeast-2.amazonaws.com/07c5e094-fcc302903-7test.jpg";
    private final Scheduler scheduler;
    //private final LotteryApplierRepository lotteryApplierRepository;

//    private void changeExpectationStatus() {
//        List<Expectation> expectations = expectationRepository.findAll();
//        for (Expectation expectation : expectations) {
//            expectation.toggleApproved();
//            expectationRepository.save(expectation);
//        }
//    }

//    @PostConstruct
    @Transactional
    public void setTestEnvironment() {
//        makeOrderEvent();
//        makeExpectations();
//        changeExpectationStatus();
    }

//    private void makeExpectations() {
//        for(int i=0;i<100000;i++){
//            LotteryApplier lotteryApplier = LotteryApplier.createLotteryApplier(String.valueOf(i));
//            lotteryApplierRepository.save(lotteryApplier);
//
//            Expectation expectation = Expectation.makeExpectation(
//                    RequestExpectationDto.makeExpectation("expectation"),
//                    lotteryApplier
//            );
//            lotteryApplier.addNewExpectation(expectation);
//            expectationService.saveExpectation(expectation);
//        }
//    }

    private void makeOrderEvent() {
        if(orderEventRepository.findAll().size()>5) return;
        OrderEvent openedOrderEvent = OrderEvent.makeOrderEventWithImage(
                RequestOrderEventDto.makeForTestOpened(
                        RequestQuizDto.makeForTest(), RequestOrderRewardDto.makeForTest()
                ), testImageSrc,testImageSrc
        );
        orderEventRepository.save(openedOrderEvent);
        for(int i=0;i<4;i++){
            OrderEvent unOpenedOrderEvent = OrderEvent.makeOrderEventWithImage(

                    RequestOrderEventDto.makeWithTime(
                            LocalDateTime.now().plusMonths(1),LocalDateTime.now().plusMonths(1),
                            RequestQuizDto.makeForTest(),RequestOrderRewardDto.makeForTest()
                    ),testImageSrc,testImageSrc
            );
            orderEventRepository.save(unOpenedOrderEvent);
        }
        scheduler.checkOrderEvent();
    }

}
