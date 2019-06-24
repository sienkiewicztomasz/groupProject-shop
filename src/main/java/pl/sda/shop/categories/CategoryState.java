package pl.sda.shop.categories;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryState {
    boolean opened;
    boolean disabled;
    boolean selected;
}
