package org.xemi.poc.security.domain;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"login_name"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_name", nullable = false,columnDefinition = "VARCHAR(32)")
    private String loginName;

    @Column(nullable = false)
    private String password;
}
