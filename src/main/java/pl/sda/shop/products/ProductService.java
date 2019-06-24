package pl.sda.shop.products;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.sda.shop.categories.CategoriesService;
import pl.sda.shop.categories.CategoryDTO;
import pl.sda.shop.products.datatables.DataTablesOrder;
import pl.sda.shop.products.datatables.DataTablesResponse;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository<Product> productRepository;

    @Autowired
    private ProductToProductDTOBuilder productToProductDTOBuilder;

    @Autowired
    private CategoriesService categoriesService;


    public void createNewProduct(String productName, Integer stockAmount, BigDecimal price, ProductType productType, Long categoryId, String pictureURL, String description) {
        Product product = productType == ProductType.EBOOK ? new ElectronicProduct() : new PhysicalProduct();
        product.setTitle(productName);
        product.setStockAmount(stockAmount);
        product.setPrice(price);
        product.setPictureURL(pictureURL);
        product.setDescription(description);
        if (categoryId != null) {
            product.setCategory(categoriesService.getCategoryById(categoryId).orElse(null));
        }
        product.setProductType(productType);
        productRepository.save(product);

    }

    public void updateProduct(ProductDTO productDTO) {
        Product s = productToProductDTOBuilder.buildEntity(productDTO);
        productRepository.save(s);
    }

    public Optional<Product> findProducts(Long id) {
        return productRepository.findProductById(id);
    }

    public List<Product> findProductsToEdit(String query, String productType) {
        return findProducts(query, productType);
    }

    public List<ProductDTO> findProductsForCustomer(String query, String productType) {
        return findProducts(query, productType)
                .stream()
                .filter(e -> ObjectUtils.defaultIfNull(e.getStockAmount(), 0) > 0)
                .map(productToProductDTOBuilder::buildDto)
                .collect(Collectors.toList());
    }

    private List<Product> findProducts(String query, String productType) {
        if (StringUtils.isBlank(query) && StringUtils.isBlank(productType)) {
            return productRepository.findAll();
        }
        if (StringUtils.isBlank(query)) {
            return productRepository.findProductsByProductType(ProductType.valueOf(productType));
        }
        if (StringUtils.isBlank(productType)) {
            return productRepository.findProductsByTittleLike(query);
        }
        return productRepository.findByTitleAndProductType(query, ProductType.valueOf(productType));
    }

    public Optional<ProductDTO> findProductById(Long id) {
        return productRepository.findById(id).map(productToProductDTOBuilder::buildDto);
    }

    public DataTablesResponse<ProductDTO> getProductDataTable(Integer start, Integer length, String sortColumn, String sortOrder, String searchText) {
        DataTablesResponse<ProductDTO> dtResponse = new DataTablesResponse<>();
        Page<Product> booksByName = findProductsByName(searchText, start == 0 ? 0 : (start / length), length, getSort(sortColumn, sortOrder));
        dtResponse.setData(booksByName.getContent()
                .stream()
                .map(productToProductDTOBuilder::buildDto)
                .collect(Collectors.toList()));
        dtResponse.setRecordsTotal((int) booksByName.getTotalElements());
        dtResponse.setRecordsFiltered((int) booksByName.getTotalElements());
        return dtResponse;
    }

    private Sort getSort(String name, String direction) {
        return direction.equalsIgnoreCase(DataTablesOrder.Direction.asc.name()) ? Sort.by(name).ascending() : Sort.by(name).descending();
    }

    private Page<Product> findProductsByName(String query, int page, int size, Sort sort) {
        Function<String, Page<Product>> supplierForNotBlankQuery = (q) -> productRepository.findAll(QProduct.product.title.likeIgnoreCase("%" + q + "%").and(QProduct.product.stockAmount.goe(1)), PageRequest.of(page, size, sort));
        Function<String, Page<Product>> supplierForBlankQuery = (q) -> productRepository.findAll(QProduct.product.stockAmount.goe(1), PageRequest.of(page, size, sort));

        return StringUtils.isBlank(query) ? supplierForBlankQuery.apply(query) : supplierForNotBlankQuery.apply(query);

    }

    private void mockProduct(String title, BigDecimal price, int stockAmount, Long catId, String pictureURL) {
        PhysicalProduct physicalProduct = new PhysicalProduct();
        physicalProduct.setTitle(title);
        physicalProduct.setPrice(price);
        physicalProduct.setStockAmount(stockAmount);
        physicalProduct.setProductType(ProductType.BOOK);
        physicalProduct.setPictureURL(pictureURL);
        physicalProduct.setCategory(categoriesService.getCategoryById(catId).orElse(null));
        productRepository.save(physicalProduct);
    }

    @PostConstruct
    public void addMockProducts() {
        List<CategoryDTO> categories = categoriesService.getCategories();
        if (productRepository.findAll().isEmpty()) {
            mockProduct("Pan tadeusz", BigDecimal.ONE, 2, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://s.tvp.pl/images2/6/4/a/uid_64aa1b2bc5669b9633cc08ed1d78f0ae1510510680699_width_1920_play_0_pos_0_gs_0_height_1080.jpg");
            mockProduct("Ogniem i mieczem", BigDecimal.valueOf(23.3), 4, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://s.tvp.pl/images2/6/4/a/uid_64aa1b2bc5669b9633cc08ed1d78f0ae1510510680699_width_1920_play_0_pos_0_gs_0_height_1080.jpg");
            mockProduct("Ogniem", BigDecimal.valueOf(2.3), 4, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://s.tvp.pl/images2/6/4/a/uid_64aa1b2bc5669b9633cc08ed1d78f0ae1510510680699_width_1920_play_0_pos_0_gs_0_height_1080.jpg");
            mockProduct("Pan Wołodyjowki", BigDecimal.valueOf(21.3), 4, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://s.tvp.pl/images2/6/4/a/uid_64aa1b2bc5669b9633cc08ed1d78f0ae1510510680699_width_1920_play_0_pos_0_gs_0_height_1080.jpg");
            mockProduct("Dżuma", BigDecimal.valueOf(23.3), 4, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://s.tvp.pl/images2/6/4/a/uid_64aa1b2bc5669b9633cc08ed1d78f0ae1510510680699_width_1920_play_0_pos_0_gs_0_height_1080.jpg");
            mockProduct("HR Business Partner", BigDecimal.ONE, 2, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://emp-scs-uat.img-osdw.pl/img-p/1/kipwn/c0aac775/std/e6-172/750532609o.jpg");
            mockProduct("Jak ocenić dojrzałość dziecka do nauki", BigDecimal.valueOf(23.3), 4, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://emp-scs-uat.img-osdw.pl/img-p/1/kipwn/c0aac775/std/e6-172/106421307o.jpg");
            mockProduct("Słownik ortograficzny PWN", BigDecimal.valueOf(2.3), 4, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://emp-scs-uat.img-osdw.pl/img-p/1/kipwn/c0aac775/std/e6-172/769634306o.jpg");
            mockProduct("Pan Wołodyjowki", BigDecimal.valueOf(21.3), 4, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://s.tvp.pl/images2/6/4/a/uid_64aa1b2bc5669b9633cc08ed1d78f0ae1510510680699_width_1920_play_0_pos_0_gs_0_height_1080.jpg");
            mockProduct("Dżuma", BigDecimal.valueOf(23.3), 4, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://s.tvp.pl/images2/6/4/a/uid_64aa1b2bc5669b9633cc08ed1d78f0ae1510510680699_width_1920_play_0_pos_0_gs_0_height_1080.jpg");
            mockProduct("Pan tadeusz", BigDecimal.ONE, 2, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://s.tvp.pl/images2/6/4/a/uid_64aa1b2bc5669b9633cc08ed1d78f0ae1510510680699_width_1920_play_0_pos_0_gs_0_height_1080.jpg");
            mockProduct("Ogniem i mieczem", BigDecimal.valueOf(23.3), 4, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://s.tvp.pl/images2/6/4/a/uid_64aa1b2bc5669b9633cc08ed1d78f0ae1510510680699_width_1920_play_0_pos_0_gs_0_height_1080.jpg");
            mockProduct("Ogniem", BigDecimal.valueOf(2.3), 4, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://s.tvp.pl/images2/6/4/a/uid_64aa1b2bc5669b9633cc08ed1d78f0ae1510510680699_width_1920_play_0_pos_0_gs_0_height_1080.jpg");
            mockProduct("Pan Wołodyjowki", BigDecimal.valueOf(21.3), 4, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://s.tvp.pl/images2/6/4/a/uid_64aa1b2bc5669b9633cc08ed1d78f0ae1510510680699_width_1920_play_0_pos_0_gs_0_height_1080.jpg");
            mockProduct("Dżuma", BigDecimal.valueOf(23.3), 4, categories.stream().skip(RandomUtils.nextInt(1, categories.size() - 2)).findFirst().get().getId(), "https://s.tvp.pl/images2/6/4/a/uid_64aa1b2bc5669b9633cc08ed1d78f0ae1510510680699_width_1920_play_0_pos_0_gs_0_height_1080.jpg");
        }
    }
}
