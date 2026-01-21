package com.buxxy.buxxy_fraud_engine.repositories;


import com.buxxy.buxxy_fraud_engine.model.DeviceIpHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceIPHistoryRepository extends JpaRepository<DeviceIpHistory,Integer> {

    Optional<DeviceIpHistory> findTopByUserIdOrderBySeenAtDesc(long userId);
}
