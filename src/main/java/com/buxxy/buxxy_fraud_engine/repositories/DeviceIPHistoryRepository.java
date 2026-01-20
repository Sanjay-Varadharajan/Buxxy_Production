package com.buxxy.buxxy_fraud_engine.repositories;


import com.buxxy.buxxy_fraud_engine.model.DeviceIPHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceIPHistoryRepository extends JpaRepository<DeviceIPHistory,Integer> {

    DeviceIPHistory findTopByUserIdOrderBySeenAtDesc(long userId);
}
