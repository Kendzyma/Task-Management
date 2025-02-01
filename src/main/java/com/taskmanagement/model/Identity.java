package com.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.taskmanagement.enums.IdentityStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "identity")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Identity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private String pin;
    private String password;
    @Enumerated(EnumType.STRING)
    private IdentityStatus pinStatus;
    @Enumerated(EnumType.STRING)
    private IdentityStatus passwordStatus;
    private int pinFailureCount;
    private int passwordFailureCount;
}
