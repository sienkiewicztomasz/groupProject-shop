package pl.sda.shop.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sda.shop.UserContextService;
import pl.sda.shop.cart.Cart;
import pl.sda.shop.cart.CartService;
import pl.sda.shop.products.ProductRepository;
import pl.sda.shop.users.Customer;
import pl.sda.shop.users.UsersRepository;

import java.time.LocalDateTime;

@Service
public class OrdersService {

    @Autowired
    private UserContextService userContextService;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UsersRepository<Customer> usersRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    public Order placeOrder() {
        Cart cart = userContextService.getCart();
        String loggedUserEmail = userContextService.getLoggedUserEmail();
        Customer customer = usersRepository.findByUsername(loggedUserEmail).get();

        cart.getOrderLines()
                .stream()
                .peek(p -> p.getProduct().setStockAmount(p.getProduct().getStockAmount() - p.getQuantity()))
                .map(e->e.getProduct()).forEach(productRepository::save);

        Order order = ordersRepository.save(new Order(customer.getUsername(), cartService.calculateTotalCartPrice(cart), customer.getUserAddress(), customer.getUserAddress(), LocalDateTime.now(), cart.getOrderLines(), customer, OrderStatus.NEW));
        userContextService.clearCart();
        return order;
    }
}
