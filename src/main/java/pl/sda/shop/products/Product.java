package pl.sda.shop.products;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sda.shop.BaseEntity;
import pl.sda.shop.categories.Category;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="system_type",
        discriminatorType = DiscriminatorType.STRING)
public class Product extends BaseEntity {
    private Integer stockAmount;
    @ManyToOne
    private Author author;
    private String countryOfPublishing;
    private String title;
    private String description;
    private String pictureURL;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    @ManyToOne
    private Category category;





}
