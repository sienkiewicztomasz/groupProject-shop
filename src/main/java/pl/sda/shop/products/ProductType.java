package pl.sda.shop.products;

import lombok.Getter;


public enum ProductType {

    BOOK("książka", false), PRESS("prasa", false), EBOOK("ebook", true);

    private String type;
    private boolean electronic;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isElectronic() {
        return electronic;
    }

    public void setElectronic(boolean electronic) {
        this.electronic = electronic;
    }

    ProductType(String type, boolean electronic) {

        this.type = type;
        this.electronic = electronic;
    }
}
