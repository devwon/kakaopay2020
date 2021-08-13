package com.kakaopay.rainMoney.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = Room.TABLE_NAME)
public class Room {
    public static final String TABLE_NAME = "KP_TB_ROOM";

    @Id
    @Column(length = 40, columnDefinition="아이디")
    private String id;

    @Column(length = 100, nullable = false, columnDefinition="이름")
    private String name;

    @Column(nullable = false, columnDefinition="생성일자")
    @CreationTimestamp
    private LocalDateTime createdTime;
}

