package com.kubernetes.multi.charlie.proxy.controller;

import com.kubernetes.multi.charlie.proxy.dto.ResponseDto;
import com.kubernetes.multi.charlie.proxy.service.persistence.create.dto.CreateValueRequestDto;
import com.kubernetes.multi.charlie.proxy.service.persistence.create.CreateValueService;
import com.kubernetes.multi.charlie.proxy.service.persistence.delete.DeleteValueService;
import com.kubernetes.multi.charlie.proxy.service.persistence.list.ListValuesService;
import com.kubernetes.multi.charlie.proxy.service.persistence.list.dto.ListValuesResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("charlie/proxy")
public class PersistenceController {

    private final Logger logger = LoggerFactory.getLogger(PersistenceController.class);

    @Autowired
    private CreateValueService createService;

    @Autowired
    private ListValuesService listService;

    @Autowired
    private DeleteValueService deleteService;

    @GetMapping("health")
    public ResponseEntity<ResponseDto<String>> checkHealth() {

        var responseDto = new ResponseDto<String>();
        responseDto.setMessage("OK");
        responseDto.setStatusCode(HttpStatus.OK.value());

        return new ResponseEntity<>(
                responseDto,
                HttpStatus.OK
        );
    }

    @PostMapping("persistence/create")
    public ResponseEntity<ResponseDto<CreateValueRequestDto>> create(
        @RequestBody CreateValueRequestDto requestDto
    ) {
        logger.info("Create method is triggered...");

        var responseDto = createService.run(requestDto);

        logger.info("Create method is executed.");

        return responseDto;
    }

    @GetMapping("persistence/list")
    public ResponseEntity<ResponseDto<ListValuesResponseDto>> list(
            @RequestParam(
                    name = "limit",
                    defaultValue = "5",
                    required = false
            ) Integer limit
    ) {
        logger.info("List method is triggered...");

        var responseDto = listService.run(limit);

        logger.info("List method is executed.");

        return responseDto;
    }
    @DeleteMapping("persistence/delete")
    public ResponseEntity<ResponseDto<String>> delete(
            @RequestParam String id
    ) {
        logger.info("Delete method is triggered...");

        var responseDto = deleteService.run(id);

        logger.info("Delete method is executed.");

        return responseDto;
    }
}
