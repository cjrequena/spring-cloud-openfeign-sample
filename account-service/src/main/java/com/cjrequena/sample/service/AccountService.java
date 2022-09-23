package com.cjrequena.sample.service;

import com.cjrequena.sample.db.entity.AccountEntity;
import com.cjrequena.sample.db.repository.AccountRepository;
import com.cjrequena.sample.dto.AccountDTO;
import com.cjrequena.sample.dto.DepositAccountDTO;
import com.cjrequena.sample.dto.WithdrawAccountDTO;
import com.cjrequena.sample.exception.service.AccountNotFoundServiceException;
import com.cjrequena.sample.exception.service.OptimisticConcurrencyServiceException;
import com.cjrequena.sample.exception.service.ServiceException;
import com.cjrequena.sample.mapper.AccountMapper;
import jakarta.json.JsonMergePatch;
import jakarta.json.JsonPatch;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountService {

  private final AccountMapper accountMapper;
  private final AccountRepository accountRepository;

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
  public AccountDTO create(AccountDTO dto) {
    dto.setId(UUID.randomUUID());
    AccountEntity entity = this.accountMapper.toEntity(dto);
    this.accountRepository.create(entity);
    dto = this.accountMapper.toDTO(entity);
    return dto;
  }

  @Transactional(readOnly = true)
  public AccountDTO retrieveById(UUID id) throws AccountNotFoundServiceException {
    Optional<AccountEntity> optional = this.accountRepository.findById(id);
    if (!optional.isPresent()) {
      throw new AccountNotFoundServiceException("The account :: " + id + " :: was not Found");
    }
    return accountMapper.toDTO(optional.get());
  }

  @Transactional(readOnly = true)
  public List<AccountDTO> retrieve() {
    List<AccountEntity> entities = this.accountRepository.findAll();
    List<AccountDTO> dtoList = entities.stream().map(
      (entity) -> this.accountMapper.toDTO(entity)
    ).collect(Collectors.toList());
    return dtoList;
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
  public AccountDTO update(AccountDTO dto) throws AccountNotFoundServiceException, OptimisticConcurrencyServiceException {
    AccountDTO currentStateAccountDTO = this.retrieveById(dto.getId());
    if (currentStateAccountDTO.getVersion().equals(dto.getVersion())) {
      AccountEntity entity = this.accountMapper.toEntity(dto);
      this.accountRepository.update(entity);
      log.debug("Updated account with id {}", entity.getId());
      return this.accountMapper.toDTO(entity);
    } else {
      log.trace(
        "Optimistic concurrency control error in account :: {} :: actual version doesn't match expected version {}",
        currentStateAccountDTO.getId(),
        currentStateAccountDTO.getVersion());
      throw new OptimisticConcurrencyServiceException(
        "Optimistic concurrency control error in account :: " + currentStateAccountDTO.getId() + " :: actual version doesn't match expected version "
          + currentStateAccountDTO.getVersion());
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
  public AccountDTO patch(UUID id, JsonPatch patchDocument) {
    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
  public AccountDTO patch(UUID id, JsonMergePatch mergePatchDocument) {
    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
  public void delete(UUID id) throws AccountNotFoundServiceException {
    AccountDTO dto = this.retrieveById(id);
    this.accountRepository.delete(accountMapper.toEntity(dto));
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
  public void deposit(DepositAccountDTO depositAccountDTO) throws AccountNotFoundServiceException, OptimisticConcurrencyServiceException {
    AccountDTO dto = this.retrieveById(depositAccountDTO.getAccountId());
    dto.setBalance(dto.getBalance().add(depositAccountDTO.getAmount()));
    this.update(dto);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
  public void withdraw(WithdrawAccountDTO withdrawAccountDTO) throws AccountNotFoundServiceException, OptimisticConcurrencyServiceException {
    AccountDTO dto = this.retrieveById(withdrawAccountDTO.getAccountId());
    dto.setBalance(dto.getBalance().subtract(withdrawAccountDTO.getAmount()));
    this.update(dto);
  }
}
