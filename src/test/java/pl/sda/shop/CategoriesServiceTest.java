package pl.sda.shop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sda.shop.categories.*;

import java.util.List;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(SelectCategoriesService.class)
public class CategoriesServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SelectCategoriesService selectCategoriesService;

    @Test
    public void categoriesFindShouldWork() {
        List<Category> categories = MockedCategoriesSource.getInstance().getMockedCategories();
        for (Category category : categories) {
            entityManager.merge(category);
        }

        List<Category> cats = categoryRepository.findByNameLike("Powieść podróżnicza i przygodowa");
        assertFalse(cats.isEmpty());


        List<CategoryDTO> categoryDtos = selectCategoriesService.filterCategories("Powieść podróżnicza i przygodowa");
        assertFalse(categoryDtos.isEmpty());
    }
}