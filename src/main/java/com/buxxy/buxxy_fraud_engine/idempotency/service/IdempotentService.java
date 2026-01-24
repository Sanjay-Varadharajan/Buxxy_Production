    package com.buxxy.buxxy_fraud_engine.idempotency.service;

    import com.buxxy.buxxy_fraud_engine.idempotency.enums.IdempotencyStatus;
    import com.buxxy.buxxy_fraud_engine.idempotency.model.IdempotencyRecord;
    import com.buxxy.buxxy_fraud_engine.idempotency.repository.IdempotencyRepository;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import lombok.AllArgsConstructor;
    import org.apache.commons.codec.digest.DigestUtils;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.Optional;
    import java.util.function.Supplier;


    @Service
    @AllArgsConstructor
    public class IdempotentService {

        private final IdempotencyRepository idempotencyRepository;
        private final ObjectMapper objectMapper;

        private static final int WAIT_INTERVAL_MS=100;
        private static final int MAX_WAIT_MS=5000;


        @Transactional
        public <T> T executeIdempotent(
                String key,
                Object request,
                Class<T> responseType,
                Supplier<T> action
        ){
            String requestJson=serialize(request);
            String hash= DigestUtils.sha256Hex(requestJson);

            Optional<IdempotencyRecord> exists=idempotencyRepository.findByIdempotencyKey(key);
            IdempotencyRecord record = exists.orElse(new IdempotencyRecord());

            if(!exists.isPresent()) {
                record.setIdempotencyKey(key);
                record.setRequestHash(hash);
                record.setStatus(IdempotencyStatus.IN_PROGRESS);
                idempotencyRepository.save(record);
            }
                else{
                if (!record.getRequestHash().equals(hash)) {
                    throw new IllegalStateException(
                            "Request content does not match previous request for key: " + key
                    );
                }

                if (record.getStatus()==IdempotencyStatus.IN_PROGRESS){
                    int waited=0;
                    while (record.getStatus()==IdempotencyStatus.IN_PROGRESS) {
                        try {
                            Thread.sleep(WAIT_INTERVAL_MS);
                            waited = waited + WAIT_INTERVAL_MS;

                            if (waited > MAX_WAIT_MS) {
                                throw new IllegalStateException(
                                        "Request with key " + key + " is still processing after max wait"
                                );
                            }
                            record = idempotencyRepository.findByIdempotencyKey(key)
                                    .orElseThrow(() -> new IllegalStateException("Record disappeared unexpectedly"));
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new RuntimeException(e);
                        }
                    }
                    if (record.getStatus() == IdempotencyStatus.SUCCESS) {
                        return deserialize(record.getResponse(), responseType);
                    }

                    if (record.getStatus() == IdempotencyStatus.FAILED) {
                        record.setStatus(IdempotencyStatus.IN_PROGRESS);
                        idempotencyRepository.save(record);
                    }
                }
            }

            try {
                T response = action.get();
                record.setResponse(serialize(response));
                record.setStatus(IdempotencyStatus.SUCCESS);
                idempotencyRepository.save(record);
                return response;
            } catch (Exception e) {
                record.setStatus(IdempotencyStatus.FAILED);
                idempotencyRepository.save(record);
                throw new RuntimeException("Failed to execute idempotent action", e);
            }
        }
        private String serialize(Object obj) {
            try {
                return objectMapper.writeValueAsString(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        private <T> T deserialize(String json, Class<T> type) {
            try {
                return objectMapper.readValue(json, type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
