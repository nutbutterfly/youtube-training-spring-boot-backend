package com.iamnbty.training.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "m_address")
public class Address extends BaseEntity implements Serializable {

    @Column(length = 120)
    private String line1;

    @Column(length = 120)
    private String line2;

    @Column(length = 120)
    private String zipcode;

    @ManyToOne
    @JoinColumn(name = "m_user_id", nullable = false)
    private User user;

}
