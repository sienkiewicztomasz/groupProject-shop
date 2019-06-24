package pl.sda.shop.categories;

import lombok.*;
import org.assertj.core.internal.bytebuddy.dynamic.DynamicType;
import pl.sda.shop.BaseEntity;

import javax.persistence.Entity;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity  {

    private Long parentId;
    private Integer depth;
    private String name;

    public Category(Long parentId, String name) {
        this.parentId = parentId;
        this.name = name;
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
}
