package com.kakaopay.rainMoney.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "createdTime")
@Setter
@Getter
@Entity
@Table(name = RoomUser.TABLE_NAME)
public class RoomUser {
    public static final String TABLE_NAME = "KP_TB_ROOM_USER";

    @EmbeddedId
    private RoomUserId id;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdTime;
}

