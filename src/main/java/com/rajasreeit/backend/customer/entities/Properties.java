package com.rajasreeit.backend.customer.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Properties {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    private String propertyName;

    private String location;

    private String isAvailable;

}
