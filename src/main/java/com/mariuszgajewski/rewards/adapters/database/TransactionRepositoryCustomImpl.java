package com.mariuszgajewski.rewards.adapters.database;

import com.mariuszgajewski.rewards.domain.model.Transaction;
import com.mariuszgajewski.rewards.domain.model.TransactionAmount;
import com.mariuszgajewski.rewards.domain.ports.TransactionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class TransactionRepositoryCustomImpl implements TransactionRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Transaction> getCustomerTransactionFromDate(String customerId, ZonedDateTime fromDate) {
        TypedQuery<TransactionModel> query = entityManager.createQuery("SELECT t FROM  TransactionModel t where t.customerId = :customerId and t.transactionDate > :fromDate", TransactionModel.class);
        query.setParameter("customerId", customerId);
        query.setParameter("fromDate", fromDate);
        List<TransactionModel> transactionModels = query.getResultList();
        return transactionModels.stream()
                .map(t -> new Transaction(
                        t.getId(), t.getCustomerId(), new TransactionAmount(t.getTransactionValue(), t.getCurrency()), t.getTransactionDate())
                ).toList();
    }
}
