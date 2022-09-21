package com.cjrequena.sample.service;

import com.cjrequena.sample.db.entity.AccountEntity;
import com.cjrequena.sample.db.repository.AccountRepository;
import com.cjrequena.sample.dto.AccountDTO;
import com.cjrequena.sample.dto.DepositAccountDTO;
import com.cjrequena.sample.dto.WithdrawAccountDTO;
import com.cjrequena.sample.exception.service.AccountNotFoundServiceException;
import com.cjrequena.sample.exception.service.OptimisticConcurrencyServiceException;
import com.cjrequena.sample.mapper.AccountMapper;
import jakarta.json.JsonMergePatch;
import jakarta.json.JsonPatch;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 */
@Log4j2
@Service
public class AccountService {

  private AccountMapper accountMapper;
  private AccountRepository accountRepository;

  /**
   *
   * @param accountRepository
   */
  @Autowired
  public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
    this.accountRepository = accountRepository;
    this.accountMapper = accountMapper;
  }

  @Transactional
  public AccountDTO create(AccountDTO dto) {
    dto.setId(UUID.randomUUID());
    AccountEntity entity = this.accountMapper.toEntity(dto);
    this.accountRepository.create(entity);
    dto = this.accountMapper.toDTO(entity);
    return dto;
  }

  @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
  public AccountDTO retrieveById(UUID id) throws AccountNotFoundServiceException {
    Optional<AccountEntity> optional = this.accountRepository.findById(id);
    if (!optional.isPresent()) {
      throw new AccountNotFoundServiceException("The account :: " + id + " :: was not Found");
    }
    return accountMapper.toDTO(optional.get());
  }

  @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
  public List<AccountDTO> retrieve() {
    List<AccountEntity> entities = this.accountRepository.findAll();
    List<AccountDTO> dtoList = entities.stream().map(
      (entity) -> this.accountMapper.toDTO(entity)
    ).collect(Collectors.toList());
    return dtoList;
  }

  @Transactional
  public AccountDTO update(AccountDTO dto) throws AccountNotFoundServiceException, OptimisticConcurrencyServiceException {
    Optional<AccountEntity> optional = accountRepository.findById(dto.getId());
    if (optional.isPresent()) {
      AccountEntity entity = optional.get();
      if (entity.getVersion().equals(dto.getVersion())) {
        entity = this.accountMapper.toEntity(dto);
        accountRepository.update(entity);
        log.debug("Updated account with id {}", entity.getId());
        return this.accountMapper.toDTO(entity);
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

  @Transactional
  public AccountDTO patch(UUID id, JsonPatch patchDocument) {
    return null;
  }

  @Transactional
  public AccountDTO patch(UUID id, JsonMergePatch mergePatchDocument) {
    return null;
  }

  @Transactional
  public void delete(UUID id) throws AccountNotFoundServiceException {
    Optional<AccountEntity> optional = accountRepository.findById(id);
    optional.ifPresent(
      entity -> {
        accountRepository.delete(entity);
        log.debug("Deleted User: {}", entity);
      }
    );
    optional.orElseThrow(() -> new AccountNotFoundServiceException("Not Found"));
  }

  @Transactional
  public void deposit(DepositAccountDTO depositAccountDTO) throws AccountNotFoundServiceException, OptimisticConcurrencyServiceException {
    AccountDTO dto = this.retrieveById(depositAccountDTO.getAccountId());
    if(dto.getVersion().equals(depositAccountDTO.getVersion())){
      dto.setBalance(dto.getBalance().add(depositAccountDTO.getAmount()));
      this.update(dto);
    }else{
      log.trace(
        "Optimistic concurrency control error in account {}: actual version doesn't match expected version {}",
        dto.getId(),
        dto.getVersion());
      throw new OptimisticConcurrencyServiceException(
        "Optimistic concurrency control error in account :: " + dto.getId() + " :: actual version doesn't match expected version :: " + dto.getVersion());
    }
  }

  @Transactional
  public void withdraw(WithdrawAccountDTO withdrawAccountDTO) throws AccountNotFoundServiceException, OptimisticConcurrencyServiceException {
    AccountDTO dto = this.retrieveById(withdrawAccountDTO.getAccountId());
    if(dto.getVersion().equals(withdrawAccountDTO.getVersion())){
      dto.setBalance(dto.getBalance().subtract(withdrawAccountDTO.getAmount()));
      this.update(dto);
    }else{
      log.trace(
        "Optimistic concurrency control error in account {}: actual version doesn't match expected version {}",
        dto.getId(),
        dto.getVersion());
      throw new OptimisticConcurrencyServiceException(
        "Optimistic concurrency control error in account :: " + dto.getId() + " :: actual version doesn't match expected version :: " + dto.getVersion());

    }
  }
}