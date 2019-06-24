package pl.sda.shop.products;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@AllArgsConstructor
@DiscriminatorValue(value = "E")
public class ElectronicProduct extends Product {

}
