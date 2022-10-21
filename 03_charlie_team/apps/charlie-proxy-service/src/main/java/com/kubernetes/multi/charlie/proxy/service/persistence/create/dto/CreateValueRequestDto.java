package com.kubernetes.multi.charlie.proxy.service.persistence.create.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateValueRequestDto {

    private String value;
    private String tag;
}
