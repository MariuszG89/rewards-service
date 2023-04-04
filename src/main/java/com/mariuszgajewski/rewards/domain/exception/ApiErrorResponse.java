package com.mariuszgajewski.rewards.domain.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorResponse {

    @JsonProperty(value = "apiErrorMessage", required = true)
    @Schema(description = "Unique API error message")
    private String apiErrorMessage;

    @JsonProperty(value = "httpStatusCode", required = true)
    @Schema(description = "Http status code")
    private int httpStatusCode;

    @JsonProperty(value = "requestPath", required = true)
    @Schema(description = "Requested path")
    private String requestPath;

    @JsonProperty("resourceInformation")
    @Schema(description = "The map of additional information of the resource")
    private Map<String, String> resourceInformation;

    @JsonProperty(value = "errorMessage", required = true)
    @Schema(description = "Short error message ")
    private String errorMessage;
}
