package com.buxxy.buxxy_fraud_engine.engine.handlingdecision.decisions;

public enum Decision {
    ALLOW, STEP_UP, BLOCK;

    public boolean isBlocked() {
        return this == BLOCK;
    }

    public boolean requiresStepUp() {
        return this == STEP_UP;
    }
    public boolean allowed(){
        return this == ALLOW;
    }
}
