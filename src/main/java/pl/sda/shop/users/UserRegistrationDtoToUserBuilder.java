package pl.sda.shop.users;


import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRegistrationDtoToUserBuilder {

    public static Customer rewriteToCustomer(CustomerRegistrationDto dto, PasswordEncoder passwordEncoder) {
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName().trim());
        customer.setSurName(dto.getLastName().trim());
        customer.setUsername(dto.getEmail().trim());
        customer.setPasswordHash(passwordEncoder.encode(dto.getPassword().trim()));
        customer.setUserAddress(UserAddress.builder()
                .city(dto.getCity().trim())
                .country(dto.getCountry().trim())
                .street(dto.getStreet().trim())
                .zipCode(dto.getZipCode())
                .build()
        );

        return customer;
    }
}
