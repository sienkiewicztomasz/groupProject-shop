package pl.sda.shop;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sda.shop.categories.CategoryRepository;
import pl.sda.shop.users.*;

import javax.annotation.PostConstruct;

@Service
public class MockData {

    @Autowired
    private UsersRepository<Customer> usersRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void mockData() {
        if (categoryRepository.checkSize() == 0) {
            categoryRepository.initializeCategories();
        }
        if (!usersRepository.existsByUsername("user@user.pl")) {

            Role role = roleRepository.findRoleByRoleName(RoleTypeEnum.USER.getRoleName());
            if (role == null) {
                role = roleRepository.save(new Role(RoleTypeEnum.USER.getRoleName()));
            }
            Customer customer = new Customer();
            usersRepository.save(customer);
            customer.setUsername("user@user.pl");
            customer.setPasswordHash(passwordEncoder.encode("user12345"));
            customer.setRoles(Sets.newHashSet(role));
            customer.setFirstName("Imię");
            customer.setSurName("Nazwisko");
            customer.setUserAddress(UserAddress.builder().zipCode("01-000").street("Kaczyńskiego").city("Łódź").country("PL").build());
            usersRepository.save(customer);
        }

    }

}
