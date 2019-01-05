package org.xemi.poc.security.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "membership_audit_trial")
public class MembershipAuditTrial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event", nullable = false)
    private String event;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "operator_id", nullable = false)
    private Long operatorId;

    @Column(name = "created_on", nullable = false)
    private Date createdOn;
}
