package com.cjrequena.sample.service;

import com.cjrequena.sample.db.entity.OrderEntity;
import com.cjrequena.sample.db.repository.OrderRepository;
import com.cjrequena.sample.dto.OrderDTO;
import com.cjrequena.sample.dto.WithdrawAccountDTO;
import com.cjrequena.sample.exception.service.AccountNotFoundServiceException;
import com.cjrequena.sample.exception.service.FeignServiceException;
import com.cjrequena.sample.exception.service.OptimisticConcurrencyServiceException;
import com.cjrequena.sample.exception.service.ServiceException;
import com.cjrequena.sample.mapper.OrderMapper;
import com.cjrequena.sample.service.feign.AccountFeignService;
import jakarta.json.JsonMergePatch;
import jakarta.json.JsonPatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {

  private final OrderMapper orderMapper;
  private final OrderRepository orderRepository;
  private final AccountFeignService accountFeignService;

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
  public OrderDTO create(OrderDTO dto) throws FeignServiceException {
    OrderEntity entity = this.orderMapper.toEntity(dto);
    this.orderRepository.create(entity);
    dto = this.orderMapper.toDTO(entity);
    WithdrawAccountDTO withdrawAccountDTO = new WithdrawAccountDTO();
    withdrawAccountDTO.setAccountId(dto.getAccountId());
    withdrawAccountDTO.setAmount(dto.getTotal());
    Mono<ResponseEntity<Void>> response = this.accountFeignService.withdraw(withdrawAccountDTO);
    return dto;
  }

  @Transactional(readOnly = true)
  public OrderDTO retrieveById(Integer id) throws AccountNotFoundServiceException {
    Optional<OrderEntity> optional = this.orderRepository.findById(id);
    if (!optional.isPresent()) {
      throw new AccountNotFoundServiceException("The account :: " + id + " :: was not Found");
    }
    return orderMapper.toDTO(optional.get());
  }

  @Transactional(readOnly = true)
  public List<OrderDTO> retrieve() {
    List<OrderEntity> entities = this.orderRepository.findAll();
    List<OrderDTO> dtoList = entities.stream().map(
      (entity) -> this.orderMapper.toDTO(entity)
    ).collect(Collectors.toList());
    return dtoList;
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
  public OrderDTO update(OrderDTO dto) throws AccountNotFoundServiceException, OptimisticConcurrencyServiceException {
    Optional<OrderEntity> optional = orderRepository.findById(dto.getId());
    if (optional.isPresent()) {
      OrderEntity entity = optional.get();
      if (entity.getVersion().equals(dto.getVersion())) {
        entity = this.orderMapper.toEntity(dto);
        orderRepository.update(entity);
        log.debug("Updated account with id {}", entity.getId());
        return this.orderMapper.toDTO(entity);
      } else {
        log.trace(
          "Optimistic concurrency control error in account {}: actual version doesn't match expected version {}",
          entity.getId(),
          entity.getVersion());
        throw new OptimisticConcurrencyServiceException(
          "Optimistic concurrency control error in account :: " + entity.getId() + " :: actual version doesn't match expected version :: " + entity.getVersion());
      }
    } else {
      throw new AccountNotFoundServiceException("The account " + dto.getId() + " was not Found");
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
  public OrderDTO patch(Integer id, JsonPatch patchDocument) {
    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
  public OrderDTO patch(Integer id, JsonMergePatch mergePatchDocument) {
    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
  public void delete(Integer id) throws AccountNotFoundServiceException {
    Optional<OrderEntity> optional = orderRepository.findById(id);
    optional.ifPresent(
      entity -> {
        orderRepository.delete(entity);
        log.debug("Deleted User: {}", entity);
      }
    );
    optional.orElseThrow(() -> new AccountNotFoundServiceException("Not Found"));
  }

}
