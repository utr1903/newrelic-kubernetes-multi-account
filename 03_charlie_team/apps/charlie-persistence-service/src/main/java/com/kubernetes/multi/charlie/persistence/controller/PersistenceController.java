package com.kubernetes.multi.charlie.persistence.controller;

import com.kubernetes.multi.charlie.persistence.dto.ResponseDto;
import com.kubernetes.multi.charlie.persistence.service.delete.PersistenceDeleteService;
import com.kubernetes.multi.charlie.persistence.service.list.PersistenceListService;
import com.kubernetes.multi.charlie.persistence.service.list.dto.ListValuesResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("persistence")
public class PersistenceController {

    @Autowired
    private PersistenceListService listService;

    @Autowired
    private PersistenceDeleteService deleteService;

    @GetMapping("health")
    public ResponseEntity<ResponseDto<String>> checkHealth() {

        var responseDto = new ResponseDto<String>();
        responseDto.setMessage("OK");
        responseDto.setStatusCode(HttpStatus.OK.value());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("list")
    public ResponseEntity<ResponseDto<ListValuesResponseDto>> list(
            @RequestParam(
                    name = "limit",
                    defaultValue = "5",
                    required = false
            ) Integer limit
    ) {
        return listService.run(limit);
    }

    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> delete(
            @RequestParam(
                    name = "valueId",
                    required = true
            ) String valueId
    ) {
        return deleteService.run(valueId);
    }
}
