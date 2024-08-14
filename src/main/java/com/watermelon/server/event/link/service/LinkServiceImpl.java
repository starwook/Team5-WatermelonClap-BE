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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService{

    private final LotteryService lotteryService;
    private final LinkRepository linkRepository;

    @Transactional
    @Override
    public MyLinkDto getShortedLink(String uid) {
        LotteryApplier lotteryApplier =  lotteryService.findLotteryApplierByUid(uid);
        return MyLinkDto.create(
                lotteryApplier
                        .getLink()
                        .getUri()
        );
    }

    @Override
    @Transactional
    public void addLinkViewCount(String uri) {
        Link link = findLink(uri);
        link.addLinkViewCount();
        linkRepository.save(link);
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

    private Link findLink(String uri){
        return linkRepository.findByUri(uri).orElseThrow(LinkNotFoundException::new);
    }

}