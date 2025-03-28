package com.JH.JhOnlineJudge.domain.inquiry.entity;

import com.JH.JhOnlineJudge.domain.Image.InquiryImage.InquiryImage;
import com.JH.JhOnlineJudge.domain.order.entity.Order;
import com.JH.JhOnlineJudge.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Inquiry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String reply;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryState state;

    @OneToMany(mappedBy = "inquiry" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InquiryImage> images = new ArrayList<>();

    @Builder
    private Inquiry(User user, Order order, String title, String content) {
        this.user = user;
        this.order = order;
        this.title = title;
        this.content = content;
        this.state = InquiryState.질문중;
        this.createdAt = LocalDateTime.now();
    }

    public static Inquiry of(User user, Order order, String title, String content) {
        return Inquiry.builder()
                .title(title)
                .content(content)
                .user(user)
                .order(order)
                .build();
    }

    public boolean checkReplyState(){return state.equals("답변완료");}

    public void updateReplyState(String reply) {
        this.reply = reply;
        this.state = InquiryState.답변완료;
    }
}
