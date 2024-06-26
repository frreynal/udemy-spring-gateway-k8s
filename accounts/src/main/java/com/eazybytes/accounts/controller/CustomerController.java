package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.dto.CustomerDetailsDto;
import com.eazybytes.accounts.dto.ErrorResponseDto;
import com.eazybytes.accounts.service.ICustomersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
		name = "REST API for Customers in EazyBank",
		description = "REST APIs in EazyBank to FETCH customer details"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@Slf4j
public class CustomerController {
	
	private final ICustomersService iCustomersService;
	
	
	public CustomerController(ICustomersService iCustomersService) {
		this.iCustomersService = iCustomersService;
	}
	
	@Operation(
			summary = "Fetch Customer Details REST API",
			description = "REST API to fetch Customer details based on a mobile number"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDto.class)
					)
			)
	}
	)
	@GetMapping("/fetchCustomerDetails")
	public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(@RequestHeader("eazybank-correlation-id")
	                                                               String correlationId,
	                                                               @RequestParam @Pattern(regexp = "(^$|[0-9]{10})",
			                                                               message = "Mobile number must be 10 digits")
	                                                               String mobileNumber) {
		log.debug("eazyBank-correlation-id found: {} ", correlationId);
		log.info("fetchCustomerDetails() start");
		CustomerDetailsDto customerDetailsDto = iCustomersService.fetchCustomerDetails(mobileNumber, correlationId);
		log.info("fetchCustomerDetails() stop");
		
		return ResponseEntity.status(HttpStatus.SC_OK).body(customerDetailsDto);
		
	}
	
	
}
