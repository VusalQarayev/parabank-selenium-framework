package com.parabank.models;

import net.datafaker.Faker;
import java.util.UUID;

public class PayeeModel {
    private final String name;
    private final String street;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String phone;
    private final String accountNumber;

    public PayeeModel(String name, String street, String city, String state,
                      String zipCode, String phone, String accountNumber) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.phone = phone;
        this.accountNumber = accountNumber;
    }

    public static PayeeModel generateRandomPayee() {
        Faker faker = new Faker();
        String randomDigits = UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 5);
        return new PayeeModel(
                faker.company().name().replaceAll("[^a-zA-Z0-9 ]", "") + " Utilities",
                faker.address().streetAddress(),
                faker.address().city(),
                faker.address().stateAbbr(),
                faker.address().zipCode(),
                faker.phoneNumber().subscriberNumber(10),
                "98" + randomDigits
        );
    }

    public String getName() { return name; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
    public String getPhone() { return phone; }
    public String getAccountNumber() { return accountNumber; }
}