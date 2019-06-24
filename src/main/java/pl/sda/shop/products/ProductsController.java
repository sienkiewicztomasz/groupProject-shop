package pl.sda.shop.products;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sda.shop.products.datatables.DataTablesResponse;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Controller
@RequestMapping(value = "/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public String productsList(@RequestParam(required = false) String text, Model model) {
        model.addAttribute("products", productService.findProductsForCustomer(text, null));
        model.addAttribute("query", StringUtils.defaultIfBlank(text, ""));
        model.addAttribute("cartActive", true);
        return "productsList";
    }

    @GetMapping("/table")
    public String productsTable(Model model) {
        model.addAttribute("firstTime", true);
        model.addAttribute("cartActive", true);
        return "productsTable";
    }


    @RequestMapping(value = "/tableData", method = RequestMethod.GET)
    @ResponseBody
    public DataTablesResponse<ProductDTO> find(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer length,@RequestParam(required = false) String sortColumn,@RequestParam(required = false) String sortOrder,@RequestParam(required = false) String searchText) {
        return productService.getProductDataTable(defaultIfNull(start, 0), defaultIfNull(length, 5), StringUtils.defaultIfBlank(sortColumn, "id"), StringUtils.defaultIfBlank(sortOrder, "asc"), StringUtils.defaultIfBlank(searchText, ""));

    }
}
