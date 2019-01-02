package org.xemi.poc.security.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission {

    @Getter
    @Setter
    @Id
    @SequenceGenerator(name = "MYSQL_GENERATOR", sequenceName = "zfc_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MYSQL_GENERATOR")
    private Long permissionId;

    @Getter
    @Setter
    @Column(nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(nullable = false)
    private String notes;
}
