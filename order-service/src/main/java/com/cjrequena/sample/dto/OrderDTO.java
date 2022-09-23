package com.cjrequena.sample.dto;

import com.cjrequena.sample.common.EStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder(value = {
  "id",
  "account_id",
  "status",
  "total",
  "creation_date",
  "version"
})
@Schema
public class OrderDTO {

  @JsonProperty(value = "id")
  @Schema(accessMode = READ_ONLY)
  private Integer id;

  @NotNull(message = "account_id is a required field")
  @JsonProperty(value = "account_id", required = true)
  private UUID accountId;

  @JsonProperty(value = "status")
  @Schema(accessMode = READ_ONLY)
  private EStatus status = EStatus.PENDING; // Default value

  @NotNull(message = "total is a required field")
  @JsonProperty(value = "total", required = true)
  private BigDecimal total;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty(value = "creation_date")
  @Schema(accessMode = READ_ONLY)
  private LocalDate creationDate;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty(value = "version")
  @Schema(accessMode = READ_ONLY)
  private Long version;
}
