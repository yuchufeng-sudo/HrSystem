package com.ys.sync.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Frank
 */
@Data
public class ExternalId {
    private String id;
    private String type;
    @JsonProperty("organization_id")
    private String organizationId;
}
