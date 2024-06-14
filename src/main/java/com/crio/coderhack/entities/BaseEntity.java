package com.crio.coderhack.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document
public abstract class BaseEntity {
    @Id
    private Long id;

    @CreatedDate
    private LocalDate createdDate;
}
