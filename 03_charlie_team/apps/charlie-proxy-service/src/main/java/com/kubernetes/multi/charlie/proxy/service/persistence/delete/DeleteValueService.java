package com.kubernetes.multi.charlie.proxy.service.persistence.delete;

import com.kubernetes.multi.charlie.proxy.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class DeleteValueService {
    private final Logger logger = LoggerFactory.getLogger(DeleteValueService.class);

    @Autowired
    private RestTemplate restTemplate;

    public DeleteValueService() {}

    public ResponseEntity<ResponseDto<String>> run(
            String valueId
    ) {
        logger.info("Making request to persistence service...");

        var response = makeRequestToPersistenceService(valueId);

        logger.info("Request to persistence service is made.");
        return response;
    }

    private ResponseEntity<ResponseDto<String>> makeRequestToPersistenceService(
            String valueId
    ) {
        var url = "http://persistence.charlie.svc.cluster.local:8080/persistence/delete?valueId=" + valueId;

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        var entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, HttpMethod.DELETE, entity,
                new ParameterizedTypeReference<ResponseDto<String>>() {});
    }
}


