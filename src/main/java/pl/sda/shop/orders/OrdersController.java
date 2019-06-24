package pl.sda.shop.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @RequestMapping(value = "/placeOrder")
    public String makeAnOrder(Model model) {
        model.addAttribute("order", ordersService.placeOrder());
        return "orderFinished";
    }


}
