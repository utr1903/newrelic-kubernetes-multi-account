package com.kubernetes.multi.charlie.persistence.service.list.dto;

import com.kubernetes.multi.charlie.persistence.entities.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListValuesResponseDto {
    private List<Value> values;
}
