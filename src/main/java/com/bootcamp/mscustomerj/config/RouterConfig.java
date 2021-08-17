package com.bootcamp.mscustomerj.config;

import com.bootcamp.mscustomerj.handler.CustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> rute(CustomerHandler handler){
        return route(GET("/customer-bank-jp"), handler::findAll)
            .andRoute(POST("/customer-bank-jp"),handler::create)
            .andRoute(GET("/customer-bank-jp/{idCustomer}"),handler::findById)
            .andRoute(GET("/customer-bank-jp/identityNumber/{customerIdentityNumber}"), handler::findByCustomerIdentityNumber)
            .andRoute(POST("/customer-bank-jp/update"),handler::update);
    }
}
