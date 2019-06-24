package pl.sda.shop.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api")
public class ApiProductsController {

    @Autowired
    private ProductService productService;


    @GetMapping(value = "/products")
    @ResponseBody
    public ResponseEntity<List<ProductDTO>> showProducts(@RequestParam(required = false) String query, @RequestParam(required = false) String productType) {
        List<ProductDTO> productsForCustomer = productService.findProductsForCustomer(query, productType);
        return ResponseEntity.ok().body(productsForCustomer);
    }

    @GetMapping(value = "/products/{id}")
    @ResponseBody
    public ResponseEntity<Product> showProducts(@PathVariable Long id) {
        return ResponseEntity.ok().body(productService.findProducts(id).orElse(null));
    }


}
