package com.kubernetes.multi.charlie.proxy.service.persistence.create;

import com.kubernetes.multi.charlie.proxy.common.NewRelicTracer;
import com.kubernetes.multi.charlie.proxy.dto.ResponseDto;
import com.kubernetes.multi.charlie.proxy.service.persistence.create.dto.CreateValueRequestDto;
import com.newrelic.api.agent.Trace;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateValueService {

    private final String TOPIC = "charlie";
    private final Logger logger = LoggerFactory.getLogger(CreateValueService.class);

    @Autowired
    private KafkaProducer<String, String> producer;

    @Autowired
    private NewRelicTracer newRelicTracer;

    public CreateValueService() {}

    @Trace(dispatcher = true)
    public ResponseEntity<ResponseDto<CreateValueRequestDto>> run(
        CreateValueRequestDto requestDto
    )
    {
        // Create Kafka producer record
        var record = createProducerRecord(requestDto);

        // Send record to topic
        producer.send(record, (recordMetadata, e) -> {
            if (e == null)
                logger.info("Record is successfully sent to topic.");
            else
                logger.error("Record is failed to be sent to topic: "
                    + e.getMessage());
        });

        producer.flush();

        return createResponseDto(requestDto);
    }

    private ProducerRecord<String, String> createProducerRecord(
        CreateValueRequestDto requestDto
    ) {
        var record = new ProducerRecord<>(
            TOPIC,
            requestDto.getTag(),
            requestDto.getValue()
        );

        newRelicTracer.track(record);

        return record;
    }

    private ResponseEntity<ResponseDto<CreateValueRequestDto>> createResponseDto(
        CreateValueRequestDto requestDto
    ) {
        logger.info("Request is successfully processed.");

        var responseDto = new ResponseDto<CreateValueRequestDto>();

        responseDto.setMessage("Value is created successfully.");
        responseDto.setStatusCode(HttpStatus.CREATED.value());
        responseDto.setData(requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
