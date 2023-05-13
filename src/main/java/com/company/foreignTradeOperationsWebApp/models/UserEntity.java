package com.company.foreignTradeOperationsWebApp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "username")},
        schema = "foreign-trade-operations")
@ToString
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column (name = "user_id", nullable = false)
    private Long userId;

    @Basic
    @Column (name = "username", nullable = false, length = 45)
    private String username;
    @Basic
    @Column (name = "password", nullable = false, length = 200)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="role_id")
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="person_id")
    private PersonEntity person;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private transient Set<TradeOperationEntity> operations = new HashSet<>();

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserEntity(String username, String password, RoleEntity role, PersonEntity person) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(role, that.role) && Objects.equals(person, that.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, role, person);
    }


}
