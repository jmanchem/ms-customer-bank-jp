package com.bootcamp.mscustomerj.repositories;

import com.bootcamp.mscustomerj.models.entities.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ICustomerRepository extends IRepository<Customer, String> {
    //Mono<Customer> findByName(String name);
    Mono<Customer> findByCustomerIdentityNumber(String customerIdentityName);
}
