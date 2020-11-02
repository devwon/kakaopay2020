package com.kakaopay.rainMoney.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class RoomUserId implements Serializable
{
    //serialVerionUID 상수로 고정하여 역직렬화문제 방지
    private static final long serialVersionUID = 1L;

    @Column(updatable = false, length = 40, nullable = false)
    private String roomId;

    @Column(updatable = false, nullable = false)
    private Long userId;

    public String getRoomId() {
        return roomId;
    }

    public Long getUserId() {
        return userId;
    }

    // 영속성 context가 식별자 비교시 사용하기 때문에 Override 추가
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        RoomUserId roomUserId = (RoomUserId) o;
        return userId.equals(roomUserId.userId) && roomId.equals(roomUserId.roomId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(userId, roomId);
    }
}
