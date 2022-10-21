package com.kubernetes.multi.charlie.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "value")
public class Value {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "tag", nullable = false)
    private String tag;

    public String toString() {
        return "[" +
                "id:" + id +
                ",name:" + value +
                "]";
    }
}
