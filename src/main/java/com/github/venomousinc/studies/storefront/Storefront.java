package com.github.venomousinc.studies.storefront;

import com.github.venomousinc.studies.storefront.store.ProductInventory;
import com.github.venomousinc.studies.storefront.store.ProductItem;
import com.github.venomousinc.studies.storefront.store.ProductType;

import java.math.BigDecimal;

public class Storefront {

    private String name;

    /**
     * The inventory/stock of the Storefront.
     */
    private final ProductInventory productInventory;

    public Storefront(String name) {
        this.name = name;
        this.productInventory = new ProductInventory(this);
    }

    /**
     * This is the inventory/stock of the Storefront.
     * @return {@link #productInventory} of this {@link Storefront}
     */
    public ProductInventory getInventory() {
        return productInventory;
    }

    public String getName() {
        return name;
    }

    /**
     * Attempts to add a new {@link ProductItem} to the {@link ProductInventory}, without a discount.
     *
     * @see #addItem(String, String, ProductType, BigDecimal, BigDecimal)
     * @param name
     * @param description
     * @param type
     * @param recommendedRetailPrice
     * @return
     */
    public ProductItem addItem(String name, String description, ProductType type, BigDecimal recommendedRetailPrice) {
        return addItem(name,description,type,recommendedRetailPrice,null);
    }

    /**
     * Attempts to add a new {@link ProductItem} to the {@link ProductInventory}, with a set discount.
     *
     * @param name
     * @param description
     * @param type
     * @param recommendedRetailPrice
     * @return
     */
    public ProductItem addItem(String name, String description, ProductType type, BigDecimal recommendedRetailPrice, BigDecimal discount) {
        ProductItem item = new ProductItem(name, description, type, recommendedRetailPrice);

        if(discount != null) {
            item.setDiscount(discount);
        }

        return getInventory().createProduct(item) ? item : null;
    }

}
