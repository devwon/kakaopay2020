package com.kakaopay.rainMoney.repository;

import com.kakaopay.rainMoney.entity.RainMoney;
import com.kakaopay.rainMoney.entity.RainMoneyDetail;
import com.kakaopay.rainMoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RainMoneyDetailRepository extends JpaRepository<RainMoneyDetail, Long>, RainMoneyDetailCustomRepository {

    /**
     * 뿌리기 분배건 조회
     * @param token 뿌리기 토큰
     */
    List<RainMoneyDetail> findByToken(String token);

    /**
     * 받기 완료된 뿌리기 분배건 조회
     * @param token 뿌리기 토큰
     */
    @Query("SELECT rmd FROM RainMoneyDetail rmd where rmd.token = :token and rmd.targetUserId is not null")
    List<RainMoneyDetail> findRainMoneyDetail(@Param("token") RainMoney token);

    /**
     * 미할당 뿌리기 분배건을 조회, 이미 할당받은 경우도 조회
     * @param token 뿌리기 토큰
     * @param userId 사용자 ID
     */
    @Query("SELECT rmd FROM RainMoneyDetail rmd WHERE rmd.token=:token AND (rmd.targetUserId is null OR rmd.targetUserId=:targetUserId)")
    List<RainMoneyDetail> findAvailableDetailsWithTokenAndTargetUser(@Param("token") RainMoney token, @Param("targetUserId") User userId);

    /**
     * 특정 사용자가 이미 할당받은 적이 있는지 조회
     * @param token 뿌리기 토큰
     * @param userId 사용자 ID
     */
    @Query("SELECT rmd FROM RainMoneyDetail rmd WHERE rmd.token=:token AND rmd.targetUserId=:targetUserId")
    List<RainMoneyDetail> findHistoryWithTokenAndTargetUser(@Param("token") RainMoney token, @Param("targetUserId") User userId);
}