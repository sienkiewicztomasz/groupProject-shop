package pl.sda.shop.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.sda.shop.UserContextService;
import pl.sda.shop.products.Product;
import pl.sda.shop.products.ProductRepository;

@Controller
public class CartController {

    @Autowired
    private ProductRepository<Product> productRepository;

    @Autowired
    private UserContextService userContextService;

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestParam(required = false) String prodId) {
        productRepository.findProductById(Long.valueOf(prodId)).ifPresent(userContextService::addProductToCart);
        return ResponseEntity.ok().body(userContextService.getCartAsJson());
    }

    @GetMapping(value = "/cartElements")
    public ResponseEntity<String> cartElements() {
        String cartAsJson = userContextService.getCartAsJson();
        if (cartAsJson == null) {
            return ResponseEntity.badRequest().body("Brak produktów");
        }
        return ResponseEntity.ok().body(cartAsJson);
    }
}
