package pl.sda.shop.products;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.shop.categories.CategoriesService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping(value = "/admin")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoriesService categoriesService;


    @PostMapping(value = "/product/add")
    public String addProduct(@RequestParam String productName,
                             @RequestParam Integer stockAmount,
                             @RequestParam BigDecimal price,
                             @RequestParam ProductType productType,
                             @RequestParam Long categoryId,
                             @RequestParam String pictureURL,
                             @RequestParam String description) {
        productService.createNewProduct(productName, stockAmount, price, productType, categoryId, pictureURL, description);
        return "redirect:/admin/products"; // tworzy nowy request na url /products
    }

    @GetMapping(value = "/product")
    public String addProduct(Model model) {
        model.addAttribute("productTypes", ProductType.values());
        model.addAttribute("categories", categoriesService.filterCategories(null));
        return "addProduct";
    }

    @GetMapping(value = "/product/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Optional<ProductDTO> productToEdit = productService.findProductById(id);
        if (productToEdit.isPresent()) {
            model.addAttribute("productToEdit", productToEdit.get());
            model.addAttribute("productTypes", ProductType.values());
            model.addAttribute("categories", categoriesService.filterCategories(null));
            return "editProduct";
        }
        return "redirect:/admin/product";
    }

    @PostMapping(value = "/product")
    public String editProduct(@ModelAttribute ProductDTO product) {
        productService.updateProduct(product);
        return "redirect:/admin/products";
    }


    @GetMapping(value = "/products")
    public String showProducts(@RequestParam(required = false) String query, @RequestParam(required = false) String productType, Model model) {
        model.addAttribute("productsList", productService.findProductsToEdit(query, productType));
        model.addAttribute("productTypes", ProductType.values());
        model.addAttribute("query", StringUtils.defaultIfBlank(query, ""));
        model.addAttribute("productType", Arrays.stream(ProductType.values()).filter(e -> e.name().equals(productType)).findFirst().orElse(null));
        return "adminProductsList";
    }
}
