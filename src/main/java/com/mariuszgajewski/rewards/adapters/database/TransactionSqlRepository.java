package com.mariuszgajewski.rewards.adapters.database;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionSqlRepository extends CrudRepository<TransactionModel, Long>, TransactionRepositoryCustom {
}
