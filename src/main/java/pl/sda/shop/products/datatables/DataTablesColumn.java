package pl.sda.shop.products.datatables;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataTablesColumn {

    private String name;
    private boolean searchable;
    private boolean orderable;
    private DataTablesSearch search;
}