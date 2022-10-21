package com.kubernetes.multi.charlie.persistence.service.create;

import com.kubernetes.multi.charlie.persistence.common.NewRelicTracer;
import com.kubernetes.multi.charlie.persistence.config.Constants;
import com.kubernetes.multi.charlie.persistence.entities.Value;
import com.kubernetes.multi.charlie.persistence.repositories.ValueRepository;
import com.newrelic.api.agent.Trace;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PersistenceCreateService {

    private final Logger logger = LoggerFactory.getLogger(PersistenceCreateService.class);

    @Autowired
    private NewRelicTracer newRelicTracer;

    @Autowired
    private ValueRepository valueRepository;

    @Trace(dispatcher = true)
    @KafkaListener(topics = Constants.TOPIC, groupId = Constants.GROUP_ID)
    public void listen(
        ConsumerRecord<String, String> record
    )
    {
        newRelicTracer.track(record);

        logger.info("Value: " + record.value());
        logger.info("Tag: " + record.key());

        var value = new Value();
        value.setId(UUID.randomUUID().toString());
        value.setValue(record.value());
        value.setTag(record.key());

        valueRepository.save(value);

        logger.info("Value is stored successfully.");
    }
}
