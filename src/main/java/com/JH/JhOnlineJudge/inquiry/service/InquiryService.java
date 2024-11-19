package com.JH.JhOnlineJudge.inquiry.service;

import com.JH.JhOnlineJudge.common.Image.InquiryImage.InquiryImage;
import com.JH.JhOnlineJudge.common.Image.InquiryImage.InquiryImageRepository;
import com.JH.JhOnlineJudge.inquiry.domain.Inquiry;
import com.JH.JhOnlineJudge.inquiry.dto.InquirySaveDto;
import com.JH.JhOnlineJudge.inquiry.exception.DuplicateInquiryReplyException;
import com.JH.JhOnlineJudge.inquiry.exception.InquiryPermissionException;
import com.JH.JhOnlineJudge.inquiry.exception.NotFoundInquiryException;
import com.JH.JhOnlineJudge.inquiry.repository.InquiryRepository;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.service.OrderService;
import com.JH.JhOnlineJudge.product.exception.NotFoundProductException;
import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.domain.UserRole;
import com.JH.JhOnlineJudge.user.service.UserService;
import com.JH.JhOnlineJudge.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryImageRepository inquiryImageRepository;
    private final InquiryRepository inquiryRepository;
    private final UserService userService;
    private final OrderService orderService;
    private final S3Uploader s3Uploader;

    private final String DIR_NAME = "Inquiry";


    @Transactional
    public Inquiry findById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(NotFoundInquiryException::new);
    }
    @Transactional
    public Page<Inquiry> getInquiriesPage(int page) {
        Pageable pageable = PageRequest.of(page -1, 15, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Inquiry> inquiries = inquiryRepository.findAll(pageable);
        return inquiries;
    }

    @Transactional
    public Page<Inquiry> getMyInquiriesPage(int page, Long userId) {
        Pageable pageable = PageRequest.of(page -1, 15, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Inquiry> inquiries = inquiryRepository.findAllByUserId(pageable,userId);
        return inquiries;
    }

    @Transactional
    public Inquiry getInquiry(Long userId, Long id) {
        Inquiry inquiry =  inquiryRepository.findById(id)
                      .orElseThrow(NotFoundProductException::new);
        if (inquiry.getUser().getId() != userId && userService.findUserById(userId).getRole() != UserRole.관리자) {
            throw new InquiryPermissionException();
        }

          return inquiry;
    }

    @Transactional
    public List<InquiryImage> getInquiryImages(Long inquiryId) {
        return inquiryImageRepository.findAllByInquiryId(inquiryId);
    }

    @Transactional
    public Inquiry saveInquiry(Long userId, InquirySaveDto request, MultipartFile[] files) {

        User user = userService.findUserById(userId);

        Order order = request.getOrderId().isEmpty() ? null : orderService.findByExternalId(request.getOrderId());

        Inquiry inquiry = Inquiry.of(user, order, request.getTitle(), request.getContent());
        inquiryRepository.save(inquiry);

        List<InquiryImage> imageList = new ArrayList<>();

         if (files != null && files.length > 0){
             for (MultipartFile file : files) {
                   String uploadUrl = s3Uploader.upload(file, DIR_NAME);

                 InquiryImage inquiryImage = InquiryImage.builder()
                           .url(uploadUrl)
                           .inquiry(inquiry)
                           .build();

                   imageList.add(inquiryImage);
             }
             inquiryImageRepository.saveAll(imageList);
         }
        return inquiry;
    }

    @Transactional
    public void saveReply(Long inquiryId, String reply) {
          Inquiry inquiry = findById(inquiryId);
          if(inquiry.checkReplyState()){throw new DuplicateInquiryReplyException();}
          inquiry.updateReplyState(reply);
      }
}
