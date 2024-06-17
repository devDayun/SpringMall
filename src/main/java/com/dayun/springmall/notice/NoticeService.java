package com.dayun.springmall.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> noticeList() {
       return noticeRepository.findAll();
    }

}
