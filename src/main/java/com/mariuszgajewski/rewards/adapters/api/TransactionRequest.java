package com.mariuszgajewski.rewards.adapters.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionRequest {
    @JsonProperty("customer")
    @Schema(description = "Unique customer id", example = "User1005")
    private String userId;
}
