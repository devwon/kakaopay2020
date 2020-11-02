package com.kakaopay.rainMoney.repository;

import com.kakaopay.rainMoney.entity.User;

interface RainMoneyDetailCustomRepository {
    /**
     * 미할당 뿌리기 분배건을 특정 사용자에게 할당한다.
     *
     * @param id     업데이트할 엔티티 ID
     * @param userId 할당할 사용자 ID
     * @return 할당 성공시 true. 이미 다른 사용자에게 할당된 경우 false.
     */
    Boolean updateTargetUserId(Long id, User userId);
}
