package com.company.foreignTradeOperationsWebApp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "person", schema = "foreign-trade-operations")
public class PersonEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "person_id", nullable = false)
    private Long personId;

    @Basic
    @Column( nullable = false, length = 45)
    private String surname;

    @Basic
    @Column(nullable = false, length = 45)
    private String name;

    @Basic
    @Column(nullable = true, length = 45)
    private String patronymic;

    @Basic
    @Column( nullable = true, length = 45)
    private String workEmail;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private transient List<UserEntity> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEntity that = (PersonEntity) o;
        return Objects.equals(personId, that.personId) && Objects.equals(surname, that.surname) && Objects.equals(name, that.name) && Objects.equals(patronymic, that.patronymic) && Objects.equals(workEmail, that.workEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, surname, name, patronymic, workEmail);
    }
}
