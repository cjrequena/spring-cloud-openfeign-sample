package com.cjrequena.sample.web.controller;

import com.cjrequena.sample.dto.DepositAccountDTO;
import com.cjrequena.sample.dto.WithdrawAccountDTO;
import com.cjrequena.sample.exception.api.BadRequestApiException;
import com.cjrequena.sample.exception.api.NotFoundApiException;
import com.cjrequena.sample.exception.controller.BadRequestControllerException;
import com.cjrequena.sample.exception.controller.ConflictControllerException;
import com.cjrequena.sample.exception.controller.NotFoundControllerException;
import com.cjrequena.sample.exception.service.AccountNotFoundServiceException;
import com.cjrequena.sample.exception.service.OptimisticConcurrencyServiceException;
import com.cjrequena.sample.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author cjrequena
 */
@Slf4j
@RestController
@RequestMapping(value = "/account-api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountController {

  private final AccountService accountService;

  @PostMapping(path = "/accounts/deposit", produces = {APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<Void>> deposit(@RequestBody DepositAccountDTO dto, @RequestHeader("version") Long version, ServerHttpRequest request)
    throws NotFoundControllerException, BadRequestControllerException, ConflictControllerException, NotFoundApiException, BadRequestApiException {
    try {
      dto.setVersion(version);
      this.accountService.deposit(dto);
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
      return Mono.just(new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT));
    } catch (AccountNotFoundServiceException ex) {
      throw new NotFoundApiException(ex.getMessage());
    } catch (OptimisticConcurrencyServiceException ex) {
      throw new BadRequestApiException(ex.getMessage());
    }
  }

  @PostMapping(path = "/accounts/withdraw", produces = {APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<Void>> withdraw(@RequestBody WithdrawAccountDTO dto, @RequestHeader("version") Long version, ServerHttpRequest request)
    throws NotFoundControllerException, BadRequestControllerException, ConflictControllerException, NotFoundApiException, BadRequestApiException {
    try {
      dto.setVersion(version);
      this.accountService.withdraw(dto);
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
      return Mono.just(new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT));
    } catch (AccountNotFoundServiceException ex) {
      throw new NotFoundApiException(ex.getMessage());
    } catch (OptimisticConcurrencyServiceException ex) {
      throw new BadRequestApiException(ex.getMessage());
    }
  }
}