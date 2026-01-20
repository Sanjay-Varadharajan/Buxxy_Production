package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.ipService;

import com.buxxy.buxxy_fraud_engine.buxxyengine.engine.device.service.DeviceFingerPrintService;
import com.buxxy.buxxy_fraud_engine.model.DeviceIpHistory;
import com.buxxy.buxxy_fraud_engine.repositories.DeviceIPHistoryRepository;
import com.buxxy.buxxy_fraud_engine.repositories.TransactionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IpServiceForAnomaly {

    private final DeviceFingerPrintService deviceFingerPrintService;

    private final TransactionRepository transactionRepository;

    private final DeviceIPHistoryRepository deviceIPHistoryRepository;

    public DeviceIpHistory checkIpAnomaly(long userId, Integer deviceId, HttpServletRequest httpServletRequest){
        String deviceIp=httpServletRequest.getRemoteAddr();
        return null;
    }


}
