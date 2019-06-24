package pl.sda.shop.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sda.shop.categories.CategoryRepository;

import java.util.Optional;

@Service
public class ProductToProductDTOBuilder {

    @Autowired
    private AuthorsRepository authorsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository<Product> productRepository;

    public ProductDTO buildDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .authorId(Optional.ofNullable(product.getAuthor()).map(e -> e.getId()).orElse(null))
                .categoryId(Optional.ofNullable(product.getCategory()).map(e -> e.getId()).orElse(null))
                .categoryName(Optional.ofNullable(product.getCategory()).map(e -> e.getName()).orElse(null))
                .description(product.getDescription())
                .pictureURL(product.getPictureURL())
                .price(product.getPrice())
                .productType(product.getProductType())
                .stockAmount(product.getStockAmount())
                .title(product.getTitle())
                .countryOfPublishing(product.getCountryOfPublishing())
                .build();
    }

    public Product buildEntity(ProductDTO dto) {
        Product product;
        if (dto.getId() == null) {
            if (dto.getProductType().isElectronic()) {
                product = new ElectronicProduct();
            } else {
                product = new PhysicalProduct();
            }
        } else {
            product = productRepository.getOne(dto.getId());
        }
        product.setDescription(dto.getDescription());

        product.setAuthor(Optional.ofNullable(dto.getAuthorId()).map(authorsRepository::getOne).orElse(null));
        product.setCategory(Optional.ofNullable(dto.getCategoryId()).map(categoryRepository::getOne).orElse(null));
        product.setDescription(dto.getDescription());
        product.setPictureURL(dto.getPictureURL());
        product.setPrice(dto.getPrice());
        product.setProductType(dto.getProductType());
        product.setStockAmount(dto.getStockAmount());
        product.setTitle(dto.getTitle());
        product.setCountryOfPublishing(dto.getCountryOfPublishing());
        return product;
    }
}
