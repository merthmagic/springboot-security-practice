package org.xemi.poc.security.events;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditTrailEvent  {

    private Long operatorId;

    private String event;

    private String eventType;

    private String content;

    public AuditTrailEvent(Long operatorId, String event, String eventType, String content) {
        this.operatorId = operatorId;
        this.event = event;
        this.eventType = eventType;
        this.content = content;
    }
}
