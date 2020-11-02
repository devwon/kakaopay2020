package com.kakaopay.rainMoney.repository;

import com.kakaopay.rainMoney.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository
@Transactional
public class RainMoneyDetailCustomRepositoryImpl implements RainMoneyDetailCustomRepository{
    @Autowired
    EntityManager em;

    @Override
    public Boolean updateTargetUserId(Long id , User userId){
        // 받은 고객 정보 업데이트 쿼리문
        String jpql = "UPDATE RainMoneyDetail rmd set rmd.targetUserId = :userId where rmd.id = :id and rmd.targetUserId is null";
        Query query = em.createQuery(jpql);
        query.setParameter("id", id);
        query.setParameter("userId", userId);
        // 업데이트 실행
        int updateCount = query.executeUpdate();
        return updateCount > 0;
    }
}