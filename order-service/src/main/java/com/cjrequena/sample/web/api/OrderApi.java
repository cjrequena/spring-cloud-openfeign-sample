package com.cjrequena.sample.web.api;

import com.cjrequena.sample.dto.OrderDTO;
import com.cjrequena.sample.exception.api.BadRequestApiException;
import com.cjrequena.sample.exception.api.NotFoundApiException;
import com.cjrequena.sample.exception.service.AccountNotFoundServiceException;
import com.cjrequena.sample.exception.service.OptimisticConcurrencyServiceException;
import com.cjrequena.sample.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/order-api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderApi {

  private final OrderService orderService;

  @PostMapping(
    path = "/orders",
    produces = {APPLICATION_JSON_VALUE}
  )
  public Mono<ResponseEntity<Void>> create(@Valid @RequestBody OrderDTO dto, ServerHttpRequest request, UriComponentsBuilder ucBuilder) {
    dto = orderService.create(dto);
    URI resourcePath = ucBuilder.path(new StringBuilder().append(request.getPath()).append("/{id}").toString()).buildAndExpand(dto.getId()).toUri();
    HttpHeaders headers = new HttpHeaders();
    headers.set(CACHE_CONTROL, "no store, private, max-age=0");
    headers.setLocation(resourcePath);
    return Mono.just(ResponseEntity.created(resourcePath).headers(headers).build());
  }

  @GetMapping(
    path = "/orders/{id}",
    produces = {APPLICATION_JSON_VALUE}
  )
  public Mono<ResponseEntity<OrderDTO>> retrieveById(@PathVariable(value = "id") Integer id) throws NotFoundApiException {
    try {
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
      OrderDTO dto = this.orderService.retrieveById(id);
      return Mono.just(new ResponseEntity<>(dto, responseHeaders, HttpStatus.OK));
    } catch (AccountNotFoundServiceException ex) {
      throw new NotFoundApiException(ex.getMessage());
    }
  }

  @GetMapping(
    path = "/orders",
    produces = {APPLICATION_JSON_VALUE}
  )
  public Mono<ResponseEntity<List<OrderDTO>>> retrieve() {

    List<OrderDTO> dtoList = this.orderService.retrieve();
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
    return Mono.just(new ResponseEntity<>(dtoList, responseHeaders, HttpStatus.OK));
  }

  @PutMapping(
    path = "/orders/{id}",
    produces = {APPLICATION_JSON_VALUE}
  )
  public Mono<ResponseEntity<Void>> update(@PathVariable(value = "id") Integer id, @Valid @RequestBody OrderDTO dto) throws NotFoundApiException, BadRequestApiException {
    try {
      dto.setId(id);
      this.orderService.update(dto);
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
    path = "/orders/{id}",
    produces = {APPLICATION_JSON_VALUE}
  )
  public Mono<ResponseEntity<Void>> delete(@PathVariable(value = "id") Integer id) throws NotFoundApiException {
    try {
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
      this.orderService.delete(id);
      return Mono.just(new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT));
    } catch (AccountNotFoundServiceException ex) {
      throw new NotFoundApiException(ex.getMessage());
    }
  }

}
