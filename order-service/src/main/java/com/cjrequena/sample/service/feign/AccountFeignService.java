//package com.cjrequena.sample.service.feign;
//
//import com.cjrequena.sample.fooclientservice.dto.FooDTOV1;
//import com.cjrequena.sample.fooclientservice.exception.service.FeignBadRequestServiceException;
//import com.cjrequena.sample.fooclientservice.exception.service.FeignConflictServiceException;
//import com.cjrequena.sample.fooclientservice.exception.service.FeignNotFoundServiceException;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.json.JsonMergePatch;
//import javax.json.JsonPatch;
//import java.util.List;
//
//import static com.cjrequena.sample.fooclientservice.common.Constant.VND_FOO_SERVICE_V1;
//
///**
// * <p>
// * <p>
// * <p>
// * <p>
// *
// * @author cjrequena
// * @version 1.0
// */
//@FeignClient(name = "account-service", url = "${account-service.url}", contextId = "account-service")
//@RequestMapping(value = "/foo-server-service", headers = {"Accept-Version=" + VND_FOO_SERVICE_V1})
//public interface AccountFeignService {
//
//  @PostMapping(value = "/fooes",
//    produces = {
//      MediaType.APPLICATION_JSON_VALUE
//    }
//  )
//  ResponseEntity<Void> create(@RequestBody FooDTOV1 dto) throws FeignConflictServiceException, FeignBadRequestServiceException;
//
//
//}
