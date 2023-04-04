package com.mariuszgajewski.rewards.adapters.api;

import com.mariuszgajewski.rewards.domain.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rewards-service")
@RequiredArgsConstructor
@Tag(name = "Rewards service", description = "Resp API for rewards service for logged-in user")
public class RewardsController {

    private final RewardFacade rewardFacade;

    @Operation(
            summary = "Create and persist new transaction for client.")
    @ApiResponse(responseCode = "200",
            description = "Transaction was created successfully - created transaction is in the response body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class)))
    @ApiResponse(responseCode = "400",
            description = "Missing or bad request parameter - error infos are in the response body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500",
            description = "An internal error occurred - no response body",
            content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping("/transaction")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TransactionResponse createTransactions(@RequestBody CreateTransactionsRequest request) {
        return rewardFacade.createTransaction(request);
    }

    @Operation(
            summary = "Update transaction.")
    @Parameter(in = ParameterIn.PATH, name = "transactionId", required = true, example = "12004",
            description = "This is the unique identifier of the transaction")
    @ApiResponse(responseCode = "200",
            description = "Transaction was updated successfully  - updated transaction is in the response body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class)))
    @ApiResponse(responseCode = "404",
            description = "Transaction not exists - error infos are in the response body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500",
            description = "An internal error occurred - no response body",
            content = @Content(schema = @Schema(implementation = Void.class)))
    @PatchMapping("/transaction/{transactionId}")
    @ResponseStatus(value = HttpStatus.OK)
    public TransactionResponse updateTransaction(@RequestBody UpdateTransactionRequest request, @PathVariable final long transactionId) {
        return rewardFacade.updateTransaction(request, transactionId);
    }

    @Operation(
            summary = "Remove transaction.")
    @Parameter(in = ParameterIn.PATH, name = "transactionId", required = true, example = "12004",
            description = "This is the unique identifier of the transaction")
    @ApiResponse(responseCode = "200",
            description = "Transaction was removed successfully - removed transaction is in the response body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class)))
    @ApiResponse(responseCode = "404",
            description = "Transaction not exists - error infos are in the response body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500",
            description = "An internal error occurred - no response body",
            content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping("/transaction/{transactionId}")
    @ResponseStatus(value = HttpStatus.OK)
    public TransactionResponse removeTransaction(@PathVariable final long transactionId) {
        return rewardFacade.removeTransaction(transactionId);
    }

    @Operation(
            summary = "Get transaction for given transaction ID")
    @Parameter(in = ParameterIn.PATH, name = "transactionId", required = true, example = "12004",
            description = "This is the unique identifier of the transaction")
    @ApiResponse(responseCode = "200",
            description = "Transaction was fetched successfully - fetched transaction is in the response body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class)))
    @ApiResponse(responseCode = "404",
            description = "Transaction not exists - error infos are in the response body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500",
            description = "An internal error occurred - no response body",
            content = @Content(schema = @Schema(implementation = Void.class)))
    @GetMapping("/transaction/{transactionId}")
    @ResponseStatus(value = HttpStatus.OK)
    public TransactionResponse getTransaction(@PathVariable final long transactionId) {
        return rewardFacade.getTransaction(transactionId);
    }

    @Operation(
            summary = "Calculate reward points for given customer")
    @Parameter(in = ParameterIn.PATH, name = "customerId", required = true, example = "Cus123",
            description = "This is the unique identifier of the customer")
    @ApiResponse(responseCode = "200",
            description = "Calculated reward points - calculation result is in the response body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class)))
    @ApiResponse(responseCode = "500",
            description = "An internal error occurred - no response body",
            content = @Content(schema = @Schema(implementation = Void.class)))
    @GetMapping("/calculate/{customerId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CalculationResponse calculateCustomerRewards(@PathVariable final String customerId) {
        return rewardFacade.calculateUserReward(customerId);
    }
}
