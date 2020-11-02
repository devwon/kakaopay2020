package com.kakaopay.rainMoney.repository;

import com.kakaopay.rainMoney.entity.RainMoney;
import com.kakaopay.rainMoney.entity.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RainMoneyRepository extends JpaRepository<RainMoney, String> {
    /**
     *  채팅방 ID, 토큰으로 뿌리기 정보를 조회
     *  @param roomUserId 채팅방 ID
     *  @param token 뿌리기 토큰(ID)
     */
    RainMoney findByRoomUserIdAndToken(RoomUser roomUserId, String token);
    /**
     *  채팅방 ID로 뿌리기 정보를 조회
     *  @param roomUserId 채팅방 ID
     */
    List<RainMoney> findByRoomUserId(RoomUser roomUserId);
}