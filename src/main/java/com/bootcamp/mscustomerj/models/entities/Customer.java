package com.bootcamp.mscustomerj.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Document(collection = "customers")
@Getter
@Setter
@AllArgsConstructor
public class Customer {
    @Id
    private String customerId;
    @NotBlank
    @Field(name = "customerIdentityType")//Tipo de documento
    private String customerIdentityType;
    @Field(name = "customerIdentityNumber")//Numero de documento
    private String customerIdentityNumber;
    @Field(name = "customerTypeEP")
    private String customerTypeEP;
    @Field(name = "customerTypeTF")
    private String customerTypeTF;
    @Size(max = 40)
    @Field(name = "name")
    private String name;
    @Size(max = 40)
    @Field(name = "lastname")
    private String lastname;
    @Size(max = 75)
    @Email
    @Field(name = "email")
    private String email;
    @Size(max = 9)
    @Field(name = "phone")
    private String phone;
    @Field(name = "address")
    private String address;
    @Field(name = "timestamp")
    private Date timestamp;
}