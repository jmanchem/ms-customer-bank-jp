package com.bootcamp.mscustomerj.services;

import com.bootcamp.mscustomerj.models.entities.Customer;
import com.bootcamp.mscustomerj.repositories.ICustomerRepository;
import com.bootcamp.mscustomerj.repositories.IRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j(topic = "CUSTOMER_SERVICE")
public class CustomerService extends BaseService<Customer, String> implements ICustomerService{
    @Autowired
    private ICustomerRepository customerRepository;
    @Override
    protected IRepository<Customer, String> getRepository() {
        return customerRepository;
    }
    @Override
    public Mono<Customer> findByCustomerIdentityNumber(String customerIdentityName) {
        return customerRepository.findByCustomerIdentityNumber(customerIdentityName);
    }
}
