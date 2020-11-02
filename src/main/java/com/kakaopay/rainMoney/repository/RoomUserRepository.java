package com.kakaopay.rainMoney.repository;

import com.kakaopay.rainMoney.entity.RoomUser;
import com.kakaopay.rainMoney.entity.RoomUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomUserRepository extends JpaRepository<RoomUser, RoomUserId> {
    /**
     * 방 ID, 사용자 ID로 채팅방 정보를 조회
     * @param roomId 방 ID
     * @param userId 사용자 ID
     */
    RoomUser findByIdRoomIdAndIdUserId(String roomId, Long userId);
    /**
     * 방 ID로 채팅방 정보를 조회
     * @param roomId 방 ID
     */
    List<RoomUser> findByIdRoomId(String roomId);
    /**
     * 사용자 ID로 채팅방 정보를 조회
     * @param userId 사용자 ID
     */
    List<RoomUser> findByIdUserId(Long userId);
}