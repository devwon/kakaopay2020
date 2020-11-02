package com.kakaopay.rainMoney.entity;

import lombok.*;
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
@Table(name = User.TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "KP_TB_USER";

    @Id
    private Long id;

    @Column(length = 40, unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdTime;
}