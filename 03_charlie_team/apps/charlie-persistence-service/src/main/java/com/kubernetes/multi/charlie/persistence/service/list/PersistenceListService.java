package com.kubernetes.multi.charlie.persistence.service.list;

import com.kubernetes.multi.charlie.persistence.dto.ResponseDto;
import com.kubernetes.multi.charlie.persistence.entities.Value;
import com.kubernetes.multi.charlie.persistence.repositories.ValueRepository;
import com.kubernetes.multi.charlie.persistence.service.list.dto.ListValuesResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersistenceListService {

    private final Logger logger = LoggerFactory.getLogger(PersistenceListService.class);

    @Autowired
    private ValueRepository valueRepository;

    public PersistenceListService() {}

    public ResponseEntity<ResponseDto<ListValuesResponseDto>> run(
            Integer limit
    ) {
        logger.info("Retrieving first " + limit + "values...");

        try {
            var allValues = valueRepository.findAll(PageRequest.of(0, limit))
                    .getContent();

            logger.info("First " + allValues.size() + " values are retrieved successfully.");

            var data = new ListValuesResponseDto();
            data.setValues(allValues);

            var responseDto = new ResponseDto<ListValuesResponseDto>();
            responseDto.setMessage("First " + allValues.size() + " values are retrieved successfully.");
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setData(data);

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        catch (Exception e) {
            logger.error("First " + limit + " values are failed to be retrieved.");

            var responseDto = new ResponseDto<ListValuesResponseDto>();
            responseDto.setMessage("First " + limit + " values are failed to be retrieved.");
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
