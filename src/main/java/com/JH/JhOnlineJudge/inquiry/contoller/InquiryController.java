package com.JH.JhOnlineJudge.inquiry.contoller;

import com.JH.JhOnlineJudge.Image.InquiryImage.InquiryImage;
import com.JH.JhOnlineJudge.inquiry.domain.Inquiry;
import com.JH.JhOnlineJudge.inquiry.dto.InquiryReplyRequest;
import com.JH.JhOnlineJudge.inquiry.dto.InquirySaveRequest;
import com.JH.JhOnlineJudge.inquiry.service.InquiryService;
import com.JH.JhOnlineJudge.user.admin.domain.Admin;
import com.JH.JhOnlineJudge.user.domain.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping("/list")
    public String getInquiriesPage(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 Model model) {
        Page<Inquiry> inquiries = inquiryService.getInquiriesPage(page);
        model.addAttribute("inquiries", inquiries);

        int totalPages = inquiries.getTotalPages() == 0 ? 1 : inquiries.getTotalPages();
        int currentPage = inquiries.getNumber() + 1;
        int pageGroup = (currentPage - 1) / 5;
        int startPage = pageGroup * 5 + 1;
        int endPage = Math.min(startPage + 4, totalPages);

       model.addAttribute("currentPage", currentPage);
       model.addAttribute("startPage", startPage);
       model.addAttribute("endPage", endPage);
       model.addAttribute("totalPages", totalPages);

        return "admin/inquiry";
    }

    @GetMapping("/my-list")
    public String getMyInquiriesPage(@AuthUser Long userId,
                                    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                    Model model) {
       Page<Inquiry> inquiries = inquiryService.getMyInquiriesPage(page,userId);
       model.addAttribute("inquiries", inquiries);

       int totalPages = inquiries.getTotalPages() == 0 ? 1 : inquiries.getTotalPages();
       int currentPage = inquiries.getNumber() + 1;
       int pageGroup = (currentPage - 1) / 5;
       int startPage = pageGroup * 5 + 1;
       int endPage = Math.min(startPage + 4, totalPages);

      model.addAttribute("currentPage", currentPage);
      model.addAttribute("startPage", startPage);
      model.addAttribute("endPage", endPage);
      model.addAttribute("totalPages", totalPages);

       return "inquiry/list";
    }

    @GetMapping("/{inquiryId}")
    public String getInquiryPage(@AuthUser Long userId,
                                 @PathVariable Long inquiryId,
                                 Model model) {
        Inquiry inquiry = inquiryService.getInquiryByIdAndUserId(userId, inquiryId);
        List<InquiryImage> images = inquiryService.getInquiryImages(inquiryId);
        model.addAttribute("inquiry",inquiry);
        model.addAttribute("images",images);
        return "inquiry/detail";
    }

    @GetMapping("/write")
    public String getInquiryWritePage(@AuthUser Long userId,
                                      Model model) {
        model.addAttribute("userId",userId);
        return "inquiry/write";
    }

    @PostMapping
    public String createInquiry(@AuthUser Long userId,
                                @ModelAttribute InquirySaveRequest request,
                                @RequestParam(value = "files", required = false) MultipartFile[] files) {

        if (files != null && files.length > 5) {
             throw new IllegalArgumentException("이미지는 최대 5개까지 첨부할 수 있습니다.");
         }

        Inquiry inquiry = inquiryService.createInquiry(userId, request, files);

        return "redirect:/inquiry/" + inquiry.getId();
    }

    @PostMapping("/reply")
    public String addInquiryReply(@Admin Long admin,
                                  @ModelAttribute InquiryReplyRequest request) {
        inquiryService.addReplyToInquiry(request.getInquiryId(), request.getReply());
        return "redirect:/inquiry/" + request.getInquiryId();
    }
}
