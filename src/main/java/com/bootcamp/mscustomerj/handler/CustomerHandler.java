package com.bootcamp.mscustomerj.handler;

import com.bootcamp.mscustomerj.models.entities.Customer;
import com.bootcamp.mscustomerj.services.ICustomerService;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
@Slf4j
@Component
public class CustomerHandler {
    @Autowired
    private ICustomerService icustomerService;

    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(icustomerService.findAll(), Customer.class);
    }
    public Mono<ServerResponse> create(ServerRequest request){
        Mono<Customer> Mcustomer=request.bodyToMono(Customer.class);//Le indicamos al request el formato que debe tener
        return Mcustomer.flatMap(icustomerService::create)
                .flatMap(customer-> ServerResponse.created(URI.create("/customer/".concat(customer.getCustomerId())))
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(customer)
                .onErrorResume(error -> {
                    WebClientResponseException errorResponse = (WebClientResponseException) error;
                    if(errorResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return ServerResponse.badRequest()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(errorResponse.getResponseBodyAsString());
                    }
                    return Mono.error(errorResponse);
                }));
    }
    public Mono<ServerResponse> findById(ServerRequest request){
        return errorHandler(
                icustomerService.findById(request.pathVariable("idCustomer"))
                        .flatMap(customer -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(customer)
                        .switchIfEmpty(ServerResponse.notFound().build())
                    ));

    }
    public Mono<ServerResponse> findByCustomerIdentityNumber(ServerRequest request){
        String customerIdentityNumber = request.pathVariable("customerIdentityNumber");
        return errorHandler(
                icustomerService.findByCustomerIdentityNumber(customerIdentityNumber)
                        .flatMap(customer -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(customer))
                        .switchIfEmpty(ServerResponse.notFound().build())
                );
    }
    public Mono<ServerResponse> update(ServerRequest request){
        //log.info("ENTRO");
        Mono<Customer> Customer = request.bodyToMono(Customer.class);
        return Customer.flatMap(customerEdit ->
                icustomerService.findByCustomerIdentityNumber(customerEdit.getCustomerIdentityNumber())
                            .flatMap(currentCustomer -> {
                                currentCustomer.setCustomerIdentityType(customerEdit.getCustomerIdentityType());
                                currentCustomer.setCustomerIdentityNumber(customerEdit.getCustomerIdentityNumber());
                                currentCustomer.setName(customerEdit.getName());
                                currentCustomer.setAddress(customerEdit.getAddress());
                                currentCustomer.setPhone(customerEdit.getPhone());
                                currentCustomer.setEmail(customerEdit.getEmail());
                                return icustomerService.update(currentCustomer);}))
                        .flatMap(CustomerUpdate ->
                        ServerResponse.created(URI.create("/customer-j/".concat(CustomerUpdate.getCustomerId())))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(CustomerUpdate))
                        .onErrorResume(e -> Mono.error(new RuntimeException("Error update customer")));
    }
    private Mono<ServerResponse> errorHandler(Mono<ServerResponse> response){
        return response.onErrorResume(error -> {
            WebClientResponseException errorResponse = (WebClientResponseException) error;
            if(errorResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                Map<String, Object> body = new HashMap<>();
                body.put("error", "Don't exist customer: ".concat(errorResponse.getMessage()));
                body.put("timestamp", new Date());
                body.put("status", errorResponse.getStatusCode().value());
                return ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(body);
            }
            return Mono.error(errorResponse);
        });
    }
}
