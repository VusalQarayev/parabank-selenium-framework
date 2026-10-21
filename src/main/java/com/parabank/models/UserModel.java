package com.parabank.models;

import net.datafaker.Faker;
import java.util.UUID;

public class UserModel {
    private final String firstName;
    private final String lastName;
    private final String street;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String phone;
    private final String ssn;
    private final String username;
    private final String password;

    public UserModel(String firstName, String lastName, String street, String city,
                     String state, String zipCode, String phone, String ssn,
                     String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.phone = phone;
        this.ssn = ssn;
        this.username = username;
        this.password = password;
    }

    public static UserModel generateRandomUser() {
        Faker faker = new Faker();
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 5);
        return new UserModel(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().streetAddress(),
                faker.address().city(),
                faker.address().stateAbbr(),
                faker.address().zipCode(),
                faker.phoneNumber().subscriberNumber(10),
                faker.idNumber().ssnValid(),
                "usr_" + uniqueSuffix,
                "Pass@" + uniqueSuffix
        );
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
    public String getPhone() { return phone; }
    public String getSsn() { return ssn; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}