package com.cjrequena.sample.web.api;

import com.cjrequena.sample.dto.AccountDTO;
import com.cjrequena.sample.exception.api.BadRequestApiException;
import com.cjrequena.sample.exception.api.NotFoundApiException;
import com.cjrequena.sample.exception.service.AccountNotFoundServiceException;
import com.cjrequena.sample.exception.service.OptimisticConcurrencyServiceException;
import com.cjrequena.sample.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/account-api")
public class AccountApi {

  private AccountService accountService;

  @PostMapping(
    path = "/accounts",
    produces = {APPLICATION_JSON_VALUE}
  )
  public Mono<ResponseEntity<Void>> create(@Valid @RequestBody AccountDTO dto, ServerHttpRequest request, UriComponentsBuilder ucBuilder) {
    dto = accountService.create(dto);
    URI resourcePath = ucBuilder.path(new StringBuilder().append(request.getPath()).append("/{id}").toString()).buildAndExpand(dto.getId()).toUri();
    HttpHeaders headers = new HttpHeaders();
    headers.set(CACHE_CONTROL, "no store, private, max-age=0");
    headers.setLocation(resourcePath);
    return Mono.just(ResponseEntity.created(resourcePath).headers(headers).build());
  }

  @GetMapping(
    path = "/accounts/{id}",
    produces = {APPLICATION_JSON_VALUE}
  )
  public Mono<ResponseEntity<AccountDTO>> retrieveById(@PathVariable(value = "id") UUID id) throws NotFoundApiException {
    try {
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
      AccountDTO dto = this.accountService.retrieveById(id);
      return Mono.just(new ResponseEntity<>(dto, responseHeaders, HttpStatus.OK));
    } catch (AccountNotFoundServiceException ex) {
      throw new NotFoundApiException(ex.getMessage());
    }
  }

  @GetMapping(
    path = "/accounts",
    produces = {APPLICATION_JSON_VALUE}
  )
  public Mono<ResponseEntity<List<AccountDTO>>> retrieve() {

    List<AccountDTO> dtoList = this.accountService.retrieve();
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
    return Mono.just(new ResponseEntity<>(dtoList, responseHeaders, HttpStatus.OK));
  }

  @PutMapping(
    path = "/accounts/{id}",
    produces = {APPLICATION_JSON_VALUE}
  )
  public Mono<ResponseEntity<Void>> update(@PathVariable(value = "id") UUID id, @Valid @RequestBody AccountDTO dto) throws NotFoundApiException, BadRequestApiException {
    try {
      dto.setId(id);
      this.accountService.update(dto);
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
      return Mono.just(new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT));
    } catch (AccountNotFoundServiceException ex) {
      throw new NotFoundApiException(ex.getMessage());
    } catch (OptimisticConcurrencyServiceException ex){
      throw new BadRequestApiException(ex.getMessage());
    }
  }

  @DeleteMapping(
    path = "/accounts/{id}",
    produces = {APPLICATION_JSON_VALUE}
  )
  public Mono<ResponseEntity<Void>> delete(@PathVariable(value = "id") UUID id) throws NotFoundApiException {
    try {
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
      this.accountService.delete(id);
      return Mono.just(new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT));
    } catch (AccountNotFoundServiceException ex) {
      throw new NotFoundApiException(ex.getMessage());
    }
  }

}