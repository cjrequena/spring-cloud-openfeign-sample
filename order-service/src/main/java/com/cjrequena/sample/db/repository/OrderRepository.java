package com.cjrequena.sample.db.repository;

import com.cjrequena.sample.db.entity.OrderEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
@Repository
@Transactional(readOnly = true)
public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {

  @Override
  @Transactional(readOnly = true)
  Optional<OrderEntity> findById(Integer id);

  @Override
  @Transactional(readOnly = true)
  List<OrderEntity> findAll();

  @Modifying
  @Transactional
  @Query(value = "INSERT INTO T_ORDER "
    + " (ACCOUNT_ID, STATUS, TOTAL, VERSION) "
    + " VALUES (:#{#entity.accountId}, :#{#entity.status},:#{#entity.total}, 1)"
    , nativeQuery = true)
  void create(@Param("entity") OrderEntity entity);

  @Modifying
  @Transactional
  @Query(value = "UPDATE T_ORDER "
    + " SET ACCOUNT_ID = :#{#entity.accountId}, "
    + " STATUS = :#{#entity.status}, "
    + " TOTAL = :#{#entity.total}, "
    + " VERSION= VERSION + 1"
    + " WHERE ID = :#{#entity.id} AND VERSION = :#{#entity.version}",
    nativeQuery = true)
  void update(@Param("entity") OrderEntity entity);
}