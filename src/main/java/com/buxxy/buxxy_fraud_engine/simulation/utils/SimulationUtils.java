package com.buxxy.buxxy_fraud_engine.simulation.utils;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;


@Component
public class SimulationUtils {

    private static final Random random = new Random();

    public static <T> T pickRandomFrom(List<T> list) {
        if (list == null || list.isEmpty()) return null;
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    public static String generateDeviceId() {
        return "DEV-" + random.nextInt(999999);
    }

    public static String generateChannel() {
        return pickRandomFrom(List.of("UPI", "Wallet", "NetBanking", "Card"));
    }
}
