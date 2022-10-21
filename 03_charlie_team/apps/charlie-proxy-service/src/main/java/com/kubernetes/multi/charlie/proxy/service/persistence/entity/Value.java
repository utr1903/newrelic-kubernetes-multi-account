package com.kubernetes.multi.charlie.proxy.service.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Value {

    private String id;
    private String value;
    private String tag;
}
