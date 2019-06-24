package pl.sda.shop.categories;

import com.google.common.io.Resources;
import lombok.Getter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class MockedCategoriesSource {

    private static MockedCategoriesSource instance;
    @Getter
    private List<Category> mockedCategories;

    public static void setInstance(MockedCategoriesSource instance) {
        MockedCategoriesSource.instance = instance;
    }

    public List<Category> getMockedCategories() {
        return mockedCategories;
    }

    public void setMockedCategories(List<Category> mockedCategories) {
        this.mockedCategories = mockedCategories;
    }

    private MockedCategoriesSource() {
        mockedCategories = initializeCategories();
    }

    public static MockedCategoriesSource getInstance() {
        if (instance == null) {
            synchronized (MockedCategoriesSource.class) {
                if (instance == null) {
                    instance = new MockedCategoriesSource();
                }
            }
        }
        return instance;
    }

    private List<Category> initializeCategories() {
        try {
            List<String> strings = Resources.readLines(Resources.getResource("kategorie.txt"), Charset.forName("UNICODE"));
            List<Category> categories = new ArrayList<>();

            int counter = 1;
            for (String line : strings) {
                Category category = Category.builder()
                        .name(line.trim())
                        .depth(calculateDepth(line))
                        .build();
                category.setId((long) counter++);
                categories.add(category);
            }

            Map<Integer, List<Category>> categoryMap = new HashMap<>();
            for (Category category : categories) {
                if (categoryMap.containsKey(category.getDepth())) {
                    categoryMap.get(category.getDepth()).add(category);
                } else {
                    List<Category> cats = new ArrayList<>();
                    cats.add(category);
                    categoryMap.put(category.getDepth(), cats);
                }
            }
            populateParentID(categoryMap, 0);
            return categories;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void populateParentID(Map<Integer, List<Category>> categoryMap, Integer depth) {
        if (!categoryMap.containsKey(depth)) {
            return;
        }
        List<Category> children = categoryMap.get(depth);
        for (Category child : children) {
            if (depth != 0) {
                List<Category> potentialParents = categoryMap.get(depth - 1);
                long parentID = 0;
                for (Category potentialParent : potentialParents) {
                    if (potentialParent.getId() < child.getId() && parentID < potentialParent.getId()) {
                        parentID = potentialParent.getId();
                    }
                }
                if (parentID == 0) {
                    throw new RuntimeException();
                }
                child.setParentId(parentID);
            }
        }
        populateParentID(categoryMap, ++depth);
    }


    private int calculateDepth(String line) {
        if (line.startsWith(" ") || line.startsWith("\t")) {
            return line.split("\\S")[0].length();
        }
        return 0;
    }

}