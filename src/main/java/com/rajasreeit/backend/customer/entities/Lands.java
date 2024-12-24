package com.rajasreeit.backend.customer.entities;


import com.rajasreeit.backend.customer.enums.LandsAvailble;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lands {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "land_seq")
    @SequenceGenerator(
            name = "land_seq",
            sequenceName = "land_availability_sequence",  // Sequence name for DB (no new table)
            allocationSize = 1,   // Increment by 1
            initialValue = 1001   // Start with 1001
    )
    private int id;

    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    private Properties properties;

    @Column(name = "land_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LandsAvailble landsAvailble;

    @Column(name = "minimum_amount")
    private double minAmount;

    @Column(name = "thirty_percent")
    private double thirtyPercentAmout;

    @Column(name = "full_amount")
    private double fullAmount;

    @Column(name = "size", nullable = false)
    private double size;

    @Column(name = "available", nullable = false)
    private boolean isAvailable;

}
