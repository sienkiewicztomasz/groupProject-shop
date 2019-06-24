package pl.sda.shop.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;


public interface ProductRepository<T extends Product> extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {

    Optional<T> findProductById(Long id);

    List<T> findProductsByProductType(ProductType productType);

    @Query("select p from Product p where upper(p.title) like concat('%',upper(?1),'%')")
    List<T> findProductsByTittleLike(String searchText);

    @Query("select p from Product p where upper(p.title) like concat('%',upper(?1),'%') and p.productType = ?2")
    List<T> findByTitleAndProductType(String searchText, ProductType productType);
}
