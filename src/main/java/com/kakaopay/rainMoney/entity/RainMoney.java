package com.kakaopay.rainMoney.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@DynamicUpdate // 변경한 필드만 대응
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = RainMoney.TABLE_NAME)
public class RainMoney {
    public static final String TABLE_NAME = "KP_TB_RAIN_MONEY";

    @Id
    @Column(columnDefinition = "CHAR(3)")
    private String token;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name="ROOM_ID", nullable = false),
            @JoinColumn(name="USER_ID", nullable = false)
    })
    private RoomUser roomUserId;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdTime;
}
