package pl.sda.shop.cart;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartService {

    public BigDecimal calculateTotalCartPrice(Cart cart) {
        BigDecimal productsPrice = cart.getOrderLines()
                .stream()
                .map(e -> e.getProductPrice()
                        .multiply(BigDecimal.valueOf(e.getQuantity()))).reduce((a, b) -> a.add(b))
                .orElse(BigDecimal.ZERO);
        return productsPrice.add(calculateDelivery(cart,productsPrice));
    }

    public BigDecimal calculateDelivery(Cart cart, BigDecimal productsPrice) {
        if (cart.getOrderLines().stream().allMatch(ol -> ol.getProduct().getProductType().isElectronic())) {
            return BigDecimal.ZERO;
        }
        if (productsPrice.compareTo(BigDecimal.valueOf(200.0)) > 0) {
            return BigDecimal.ZERO;
        }
        if (productsPrice.compareTo(BigDecimal.valueOf(100.0)) > 0) {
            return BigDecimal.ONE;
        }
        return BigDecimal.TEN;
    }

}
