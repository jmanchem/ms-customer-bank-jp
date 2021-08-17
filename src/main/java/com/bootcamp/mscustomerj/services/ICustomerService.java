package com.bootcamp.mscustomerj.services;

import com.bootcamp.mscustomerj.models.entities.Customer;
import reactor.core.publisher.Mono;

public interface ICustomerService extends IBaseService<Customer, String>{
    //Mono<Customer> findByName(String name);
    Mono<Customer> findByCustomerIdentityNumber(String customerIdentityName);
}
