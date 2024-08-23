package com.watermelon.server.event.link.service;

import com.watermelon.server.event.lottery.domain.Link;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.link.exception.LinkNotFoundException;
import com.watermelon.server.event.link.repository.LinkRepository;
import com.watermelon.server.event.link.dto.MyLinkDto;
import com.watermelon.server.event.link.utils.LinkUtils;
import com.watermelon.server.event.lottery.service.LotteryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LotteryService lotteryService;
    private final LinkRepository linkRepository;

    @Value("${server.parts-share-url}")
    private String partsShareUrl;

    @Value("${server.base-url}")
    private String baseUrl;

    @Transactional
    @Override
    public MyLinkDto getShortedLink(String uid) {
        LotteryApplier lotteryApplier = lotteryService.findLotteryApplierByUid(uid);
        String shortedLink = baseUrl+"/link/"+LinkUtils
                .toBase62(lotteryApplier
                        .getLink()
                        .getUri());
        return MyLinkDto.create(
            shortedLink
        );
    }

    @Override
    @Transactional
    public void addLinkViewCount(String uri) {
        log.info("Add link view count: {}", uri);
        lotteryService.addRemainChance(getApplierByLinkKey(uri).getUid());
        linkRepository.incrementViewCount(uri);
    }

    @Override
    public LotteryApplier getApplierByLinkKey(String uri) {
        Link link = findLink(uri);
        return link.getLotteryApplier();
    }

    @Override
    public String getUrl(String shortedUri) {
        return LinkUtils.fromBase62(shortedUri);
    }

    @Override
    public String getRedirectUrl(String shortedUri) {
        return partsShareUrl+"/"+getUrl(shortedUri);
    }

    private Link findLink(String uri) {
        return linkRepository.findByUri(uri).orElseThrow(LinkNotFoundException::new);
    }

}