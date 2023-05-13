package com.emsi.entities;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Emission implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String titre;
    private Date dateEmission;
    private Integer dureeEmission;
    private String genre;
    private Producer producer;


}
