package pl.sda.shop.orders;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sda.shop.BaseEntity;
import pl.sda.shop.products.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Setter
@Getter
@Entity
@ToString
@Table(name = "order_lines")
public class OrderLine extends BaseEntity implements Serializable {

    @OneToOne
    private Product product;

    private Integer quantity;

    @Column(name = "product_price")
    private BigDecimal productPrice;
}
