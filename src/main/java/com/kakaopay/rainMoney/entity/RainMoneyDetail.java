package com.kakaopay.rainMoney.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@DynamicUpdate // 변경한 필드만 대응
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = RainMoneyDetail.TABLE_NAME,
        uniqueConstraints = {@UniqueConstraint(
                name="UK_token_seq",columnNames = {"token", "seq"})})
public class RainMoneyDetail {
    public static final String TABLE_NAME = "KP_TB_RAIN_MONEY_DETAIL";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private int seq;

    @NonNull
    @Column(nullable = false)
    private int amount;

    @NonNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TOKEN", nullable = false)
    private RainMoney token;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TARGET_USER_ID")
    private User targetUserId;

    @NonNull
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdTime;
}