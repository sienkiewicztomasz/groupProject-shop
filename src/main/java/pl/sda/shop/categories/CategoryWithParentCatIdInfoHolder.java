package pl.sda.shop.categories;

public interface CategoryWithParentCatIdInfoHolder extends CategoryInfoHolder {
    void setParentCatId(String id);
    String getParentCatId();
}
