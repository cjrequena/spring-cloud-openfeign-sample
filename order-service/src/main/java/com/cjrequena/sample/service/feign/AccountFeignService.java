package com.cjrequena.sample.service.feign;

import com.cjrequena.sample.common.Constants;
import com.cjrequena.sample.dto.DepositAccountDTO;
import com.cjrequena.sample.dto.WithdrawAccountDTO;
import com.cjrequena.sample.exception.service.FeignServiceException;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author cjrequena
 * @version 1.0
 */
@FeignClient(name = "account-service", url = "${account-service.url}", contextId = "account-service", path = "/account-service/api")
@Headers("Accept-Version: " + Constants.VND_SAMPLE_SERVICE_V1)
public interface AccountFeignService {

  @PostMapping(
    value = "/accounts/deposit",
    consumes = {MediaType.APPLICATION_JSON_VALUE},
    headers = {"Accept-Version=" + Constants.VND_SAMPLE_SERVICE_V1}
  )
  ResponseEntity<Void> deposit(@RequestBody DepositAccountDTO dto) throws FeignServiceException;

  @PostMapping(
    value = "/accounts/withdraw",
    consumes = {MediaType.APPLICATION_JSON_VALUE},
    headers = {"Accept-Version=" + Constants.VND_SAMPLE_SERVICE_V1}
  )
  Mono<ResponseEntity<Void>> withdraw(WithdrawAccountDTO dto) throws FeignServiceException;

}
