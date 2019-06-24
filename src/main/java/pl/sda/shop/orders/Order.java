package pl.sda.shop.orders;

import lombok.*;
import pl.sda.shop.BaseEntity;
import pl.sda.shop.LocalDateTimeConverter;
import pl.sda.shop.users.User;
import pl.sda.shop.users.UserAddress;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "Orders")
@ToString(exclude = "customer")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity implements Serializable {

    private String customerName;

    private BigDecimal totalCost;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "customer_street")),
            @AttributeOverride(name = "city", column = @Column(name = "customer_city")),
            @AttributeOverride(name = "country", column = @Column(name = "customer_country")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "customer_postal_code"))})
    private UserAddress customerAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "delivery_street")),
            @AttributeOverride(name = "city", column = @Column(name = "delivery_city")),
            @AttributeOverride(name = "country", column = @Column(name = "delivery_country")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "delivery_postal_code"))})
    private UserAddress deliveryAddress;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime creationDate;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLine> orderLines;

    @ManyToOne
    private User customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

}
