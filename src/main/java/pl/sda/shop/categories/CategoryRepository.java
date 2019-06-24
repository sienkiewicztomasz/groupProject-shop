package pl.sda.shop.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByNameLike(String text);

    @Query("select count(c) from Category c")
    Long checkSize();

    default List<Category> getCategories() {
        initializeCategories();
        return findAll();
    }

    //opcjonalne
    default void initializeCategories() {
        if (checkSize() != 0) {
            return;
        }
        List<Category> categories = MockedCategoriesSource.getInstance().getMockedCategories()
                .stream()
                .sorted(Comparator.comparingLong(e -> e.getId()))
                .collect(toList());
        for (Category category : categories) {
            Long temp = category.getId();
            Category saved = save(category);
            if (saved.getParentId() == null) {
                continue;
            }
            List<Category> categoryStream = categories
                    .stream()
                    .filter(e -> e.getParentId() != null)
                    .filter(e -> temp.equals(Long.valueOf(e.getParentId())))
                    .collect(toList());
            categoryStream.forEach(e -> e.setParentId(saved.getId()));
        }
    }

    default Optional<Category> findCategoryById(Long id) {
        return findById(id);
    }


    default List<Category> findCategoriesByName(String searchText) {
        return searchText.isEmpty() ? findAll() : findByNameLike(searchText);
    }

    @Transactional
    default void updateCategory(Category categoryInMemoryDto) {
        save(categoryInMemoryDto);
    }
}