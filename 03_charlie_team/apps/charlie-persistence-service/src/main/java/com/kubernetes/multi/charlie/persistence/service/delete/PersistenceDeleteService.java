package com.kubernetes.multi.charlie.persistence.service.delete;

import com.kubernetes.multi.charlie.persistence.dto.ResponseDto;
import com.kubernetes.multi.charlie.persistence.repositories.ValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PersistenceDeleteService {
    private final Logger logger = LoggerFactory.getLogger(PersistenceDeleteService.class);

    @Autowired
    private ValueRepository valueRepository;

    public PersistenceDeleteService() {}

    public ResponseEntity<ResponseDto<String>> run(
            String valueId
    ) {
        try {
            logger.info("Checking if given ID " + valueId + "is valid...");
            UUID.fromString(valueId);
            logger.info("Given ID " + valueId + " is valid...");
        }
        catch (Exception e) {
            logger.error("Given ID " + valueId + " is not valid...");

            var responseDto = new ResponseDto<String>();
            responseDto.setMessage("Given ID " + valueId + " is not valid...");
            responseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }

        logger.info("Checking if value with ID " + valueId + "exists...");

        var value = valueRepository.findById(valueId);
        if (value.isEmpty()) {
            logger.error("The value with ID " + valueId + " does not exist.");

            var responseDto = new ResponseDto<String>();
            responseDto.setMessage("The value with ID " + valueId + " does not exist.");
            responseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }
        else
            logger.info("The value with ID " + valueId + " exists.");

        try {
            logger.info("Deleting value with ID " + valueId + "...");

            valueRepository.delete(value.get());

            var responseDto = new ResponseDto<String>();
            responseDto.setMessage("The value with ID " + valueId + " deleted successfully.");
            responseDto.setStatusCode(HttpStatus.ACCEPTED.value());

            return new ResponseEntity<>(responseDto, HttpStatus.ACCEPTED);
        }
        catch (Exception e) {
            logger.error("The value with ID " + valueId + " is failed to be deleted.");

            var responseDto = new ResponseDto<String>();
            responseDto.setMessage("The value with ID " + valueId + " is failed to be deleted.");
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
