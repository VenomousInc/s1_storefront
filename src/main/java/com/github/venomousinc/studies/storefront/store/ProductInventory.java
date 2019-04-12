package com.github.venomousinc.studies.storefront.store;

import com.github.venomousinc.studies.storefront.Storefront;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * The Stores Inventory/Stock of Product Items.
 */

public class ProductInventory {

    private final Storefront parent;
    /**
     * A list of all the Products currently in store.
     */
    private ArrayList<ProductItem> products;

    public ProductInventory(final Storefront parent) {
        this.parent = parent;
        this.products = new ArrayList<>();
    }

    /**
     * Loops through all current Products, and creates an EnumSet based on these products.
     * @see ProductType
     * @see #products
     * @see ProductItem
     * @return An EnumSet of ProductTypes represented in this Inventory.
     */
    public EnumSet<ProductType> getStockTypes() {
        if(products == null || products.size() == 0)
            return null;

        EnumSet<ProductType> set = EnumSet.noneOf(ProductType.class);

        for(ProductItem item : products) {
            if(!set.contains(item.TYPE))
                set.add(item.TYPE);
        }

        return set;
    }

    /**
     * @see ProductItem
     * @return an ArrayList of ProductItems
     */
    public ArrayList<ProductItem> getProducts() {
        return products;
    }

    /**
     * Get all products of a specific {@link ProductType}
     * @see #getProducts()
     * @param productType Specified filter.
     * @return A list of Products with the specified filter.
     */
    public ArrayList<ProductItem> getProducts(ProductType productType) {
        ArrayList<ProductItem> productList = new ArrayList<>();

        for(ProductItem item : products) {
            if(item.TYPE.equals(productType)) {
                productList.add(item);
            }
        }

        return productList;
    }

    /**
     * Adds a new Product to the {@link #products} {@link ArrayList<ProductItem>}
     * @see ProductItem
     * @see #products
     * @see #getProducts()
     * @see #getStockTypes()
     * @param item The Item to add to the Producsts list.
     * @return The success of adding the ProductItem.
     */
    public boolean createProduct(ProductItem item) {
        return products.add(item);
    }
}
