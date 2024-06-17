package com.dayun.springmall.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/notice")
    String noticeList(Model model) {
        model.addAttribute("notices", noticeService.noticeList());
        return "noticeList";
    }
}
