package com.github.venomousinc.studies.storefront.store;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class ProductItem {

    /** Represents an Integer value of 100.*/
    private static BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    public final String NAME;
    public final String DESCRIPTION;
    /**
     * A Generated Serial Number, should always be 10 digits.
     */
    public final long SERIAL_NUMBER;

    /**
     * The Recommended Retail Price, it is important not reference this for the Products actual in-store price.
     */
    private final BigDecimal RRP;

    /**
     * The {@link ProductType} which includes the tax rates.
     */
    public final ProductType TYPE;

    /**
     * {@link BigDecimal} with a value related to Discount Percentage.
     */
    private BigDecimal promotionalDiscount = BigDecimal.ZERO;

    /**
     * An item to be sold in the {@link com.github.venomousinc.studies.storefront.Storefront}
     *
     * @see com.github.venomousinc.studies.storefront.Storefront
     * @see ProductInventory
     * @param name
     * @param description
     * @param type
     * @param RRP
     */
    public ProductItem(String name, String description, ProductType type, BigDecimal RRP) {
        this.NAME = name;
        if(description == null)
            description = name.concat("'s at a Java Price!");
        this.DESCRIPTION = description;
        this.TYPE = type;
        this.RRP = RRP;

        long generatedSN = 5000000000l;
        for(char c : this.NAME.toCharArray()) {
            generatedSN += (long) c + name.length();
        }

        this.SERIAL_NUMBER = generatedSN;
    }

    /**
     * It is not recommended to use this for Product pricing, please use {@link #getPrice(boolean, boolean)}
     *
     * @see #getPrice(boolean, boolean)
     * @see #RRP
     * @return
     */
    public BigDecimal getRRP() {
        return RRP;
    }

    /**
     * @return The {@link com.github.venomousinc.studies.storefront.Storefront} applied discount.
     */
    public BigDecimal getPromotionalDiscount() {
        return promotionalDiscount;
    }

    /**
     * Set the Discount of this Product.
     * @param newDiscount
     * @return
     */
    public boolean setDiscount(BigDecimal newDiscount) {
        promotionalDiscount = newDiscount;
        return true;
    }

    /**
     * Set the discount of this Product to 0 (BigDecimal.ZERO)
     */
    public void removeDiscount() {
        promotionalDiscount = BigDecimal.ZERO;
    }

    /**
     * The price of this item, with the options of applying the store discount & tax.
     *
     * @param withDiscount
     * @param withTax
     * @return
     */
    public BigDecimal getPrice(boolean withDiscount, boolean withTax) {
        BigDecimal newPrice = getRRP();

        if(withDiscount && promotionalDiscount.compareTo(BigDecimal.ZERO) != 0) {
            newPrice.subtract( getRRP().multiply( promotionalDiscount ).divide( ONE_HUNDRED ) );
        }

        if(withTax && TYPE != null && TYPE.getTaxPercentage().compareTo(BigDecimal.ZERO) != 0) {
            newPrice = TYPE.taxedPrice(newPrice);
        }

        return newPrice;
    }

    /**
     * Converts the price into a locale currency Format.
     *
     * @see #getPrice(boolean, boolean)
     * @return
     */
    public String getFormattedPrice() {
        return NumberFormat.getCurrencyInstance().format(getPrice(true, true));
    }

}
