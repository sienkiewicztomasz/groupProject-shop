package pl.sda.shop.cart;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import pl.sda.shop.orders.OrderLine;

import java.util.List;

@Getter
@Setter
public class Cart {

    private List<OrderLine> orderLines = Lists.newArrayList();

    public List<OrderLine> getOrderLines() {
        return null;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}
