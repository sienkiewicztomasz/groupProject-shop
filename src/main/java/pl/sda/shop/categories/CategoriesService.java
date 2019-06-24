package pl.sda.shop.categories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CategoriesService {

    private CategoryRepository categoryRepository;

    public CategoriesService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public abstract List<CategoryDTO> filterCategories(String searchText);

    public List<CategoryDTO> getCategories(){
        List<Category> categoryList = categoryRepository.getCategories();
        return categoryList.stream()
                .map(c -> buildCategoryDTO(c)).collect(Collectors.toList());
    }

    public Optional<Category> getCategoryById(Long movedId) {
        return categoryRepository.findCategoryById(movedId);
    }

    public void moveCategory(String newParentId, String movedId) {
        Category movedCategory = getCategoryById(Long.valueOf(movedId)).get();
        movedCategory.setParentId(Long.valueOf(newParentId));
        categoryRepository.updateCategory(movedCategory);
    }


    protected CategoryDTO buildCategoryDTO(Category c) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(c.getId());
        categoryDTO.setParentId(c.getParentId());
        categoryDTO.setName(c.getName());
        categoryDTO.setDepth(c.getDepth());
        categoryDTO.setState(new CategoryState());
        return categoryDTO;
    }

    protected CategoryDTO populateStateAndOpenParents(CategoryDTO dto, String searchText) {
        if (searchText != null && dto.getName().equals(searchText.trim())) {
            dto.getState().setOpened(true);
            dto.getState().setSelected(true);
            openParent(dto);
        }
        return dto;
    }

    protected void openParent(CategoryDTO child) {
        CategoryDTO parentCat = child.getParentCat();
        if (parentCat == null) {
            return;
        }
        parentCat.getState().setOpened(true);
        openParent(parentCat);
    }

    public void addCategory(String catName, Long parentId) {
        categoryRepository.save(new Category(parentId == 0 ? null : parentId, catName));
    }
}
