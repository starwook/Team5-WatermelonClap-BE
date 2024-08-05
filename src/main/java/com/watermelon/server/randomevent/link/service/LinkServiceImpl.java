package com.watermelon.server.randomevent.link.service;

import com.watermelon.server.randomevent.domain.Link;
import com.watermelon.server.randomevent.domain.LotteryApplier;
import com.watermelon.server.randomevent.link.dto.MyLinkDto;
import com.watermelon.server.randomevent.link.exception.LinkNotFoundException;
import com.watermelon.server.randomevent.link.repository.LinkRepository;
import com.watermelon.server.randomevent.service.LotteryService;
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
    public MyLinkDto getMyLink(String uid) {
        LotteryApplier lotteryApplier =  lotteryService.findLotteryApplierByUid(uid);
        return MyLinkDto.create(
                lotteryApplier
                        .getLink()
                        .getLinkKey()
        );
    }

    @Override
    @Transactional
    public void addLinkViewCount(String linkKey) {
        Link link = findLink(linkKey);
        link.addLinkViewCount();
        linkRepository.save(link);
    }

    @Override
    public LotteryApplier getApplierByLinkKey(String linkKey) {
        Link link = findLink(linkKey);
        return link.getLotteryApplier();
    }

    private Link findLink(String linkKey){
        return linkRepository.findByLinkKey(linkKey).orElseThrow(LinkNotFoundException::new);
    }

}