package pl.sda.shop;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.sda.shop.products.PhysicalProduct;
import pl.sda.shop.products.Product;
import pl.sda.shop.products.ProductRepository;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT,classes = FinalProjectApplication.class)

public class SecurityTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ProductRepository<Product> productRepository;

    private URL base;

    private MockMvc mvc;
    @LocalServerPort
    int port;

    @Before
    public void setup() throws MalformedURLException {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        base = new URL("http://localhost:" + port);

    }

    @Test
    public void notAuthorizedUserShouldNotBeAllowedToGetProductsListWhen() throws Exception {
        ResultActions perform = mvc.perform(get(base.toString()+"/products/list"));
        perform.andExpect(status().is3xxRedirection());
    }

    @WithMockUser(username = "user@user.pl",password = "user12345")
    @Test
    public void mockedUserShouldBeAbleToGetProductsList() throws Exception {
        ResultActions perform = mvc.perform(get(base.toString()+"/products/list"));
        perform.andExpect(status().isOk()).andExpect(view().name("productsList"));
    }

    @Test
    public void apiUserWithInvalidPasswordShouldNotBeAbleToGetProductsList() throws IllegalStateException {
        TestRestTemplate restTemplate = new TestRestTemplate("wrongUserName", "wrongPassword");
        ResponseEntity<String> response = restTemplate.getForEntity(base.toString()+"/api/products", String.class);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response
                .getBody()
                .contains("Unauthorized"));
    }

    @Test
    public void apiUserWithValidPasswordShouldBeAbleToGetProductsList() throws IllegalStateException {
        PhysicalProduct physicalProduct = new PhysicalProduct();
        physicalProduct.setTitle("TEST PRODUCT");
        physicalProduct.setStockAmount(2);
        productRepository.save(physicalProduct);
        TestRestTemplate restTemplate = new TestRestTemplate("apiuser", "apipassword");
        ResponseEntity<String> response = restTemplate.getForEntity(base.toString()+"/api/products", String.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response
                .getBody()
                .contains("TEST PRODUCT"));
        productRepository.delete(physicalProduct);
    }
}
