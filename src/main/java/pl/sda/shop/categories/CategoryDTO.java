package pl.sda.shop.categories;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryDTO {
    private Long id;
    private Long parentId;
    private Integer depth;
    private String name;
    private CategoryDTO parentCat;
    private CategoryState state;

    public String getText() {
        return id + ". " + name;
    }

    public String getParent() {
        return parentId == null ? "#" : parentId.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryDTO getParentCat() {
        return parentCat;
    }

    public void setParentCat(CategoryDTO parentCat) {
        this.parentCat = parentCat;
    }

    public CategoryState getState() {
        return state;
    }

    public void setState(CategoryState state) {
        this.state = state;
    }
}
