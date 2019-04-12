package com.github.venomousinc.studies.storefront.store;

import java.math.BigDecimal;

/**
 * An enum of Product Types, including the VAT/tax amount for this item.
 * <p>
 * For example: Luxury Food with a 18% VAT/tax rate.
 *
 * @see VAT
 * @see #taxedPrice(BigDecimal)
 * @see #getTaxPercentage()
 * @author Martin Ian Bennett
 */

public enum ProductType {
    /**
     * ZERO-PERCENTAGE PRODUCTS
     */
    CAKE,
    TEA_AND_COFFEE,
    MILK, WATER, FRUIT_JUICE, STANDARD_BEVERAGE,
    MEAT_AND_FISH, FRUIT_AND_VEGETABLE,
    CEREAL, NUT_AND_PULSE, CULINARY_HERB, EGGS,
    SOUP, SAUCE, COOKING_OIL,
    SALT_AND_PEPPER, SWEETENER_AND_FLAVOURING,
    BOOKS_AND_EDUCATIONAL, MAGAZINES_AND_NEWSPAPERS,
    MUSIC,
    BABYWEAR, CHILDRENS_CLOTHES, CHILDRENS_FOOTWEAR, SPORT_GEAR,
    PROTECTIVE_HELMET, PROTECTIVE_BOOTS,
    ANIMAL_FOOD,
    SEEDS,
    ZERO_OTHER,
    /***/
    MICROWAVE_MEAL(VAT.STANDARD_RATE),
    PROCESSED_FOOD(VAT.STANDARD_RATE),
    CLOTHES(VAT.STANDARD_RATE),
    ESSENTIAL_OIL(VAT.STANDARD_RATE),
    STANDARD_OTHER(VAT.STANDARD_RATE),
    CHILDRENS_CAR_SEAT(VAT.CHILD_SAFETY_RATE),
    CHILDRENS_BOOSTER_SEAT(VAT.CHILD_SAFETY_RATE),
    GAMBLING(VAT.GAMBLING_RATE),
    BISCUIT(VAT.STANDARD_RATE),
    ICECREAM(VAT.STANDARD_RATE),
    CANDY(VAT.STANDARD_RATE), CHOCOLATE_BAR(VAT.STANDARD_RATE),
    SPORT_BEVERAGE(VAT.STANDARD_RATE),
    NUTRITIONAL_BEVERAGE(VAT.STANDARD_RATE),
    SUGAR_BEVERAGE(VAT.SUGAR_RATE),
    ALCOHOLIC_BEVERAGE(VAT.ALCOHOL_RATE),
    SMOKING_PRODUCT(VAT.SMOKING_RATE);

    private static BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    /**
     * How much VAT/tax to apply.
     */
    private BigDecimal vatRate;

    ProductType() {
        this(BigDecimal.ZERO);
    }

    ProductType(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    /**
     * The Products tax amount, with a minimum of 0.
     *
     * Equation: {@code (ItemPrice * vatRate) / 100}
     * @return
     */
    public BigDecimal getTaxPercentage() {
        return vatRate;
    }

    /**
     * How much this Product costs in just VAT/tax.
     * @param itemPrice
     * @return
     */
    public BigDecimal getTaxCost(BigDecimal itemPrice) {
        return itemPrice.multiply(vatRate).divide(ONE_HUNDRED);
    }

    /**
     * Using this {@link ProductType}'s {@link #vatRate}age, it returns the price with the addition of VAT/taxes.
     * @param itemPrice
     * @return
     */
    public BigDecimal taxedPrice(BigDecimal itemPrice) {
        return itemPrice.add( getTaxCost(itemPrice) );
    }

    /**
     * @return The Type name, underscores are replaced with spaces.
     */
    public String fixedName() {
        return this.name().replaceAll("_", " ");
    }

    /**
     * The Standardized VAT rates that the Storefront must follow.
     */
    public static class VAT {

        public static final BigDecimal ZERO_RATE = BigDecimal.ZERO;
        public static final BigDecimal ALCOHOL_RATE = BigDecimal.valueOf(25);
        public static final BigDecimal SUGAR_RATE = BigDecimal.valueOf(22);
        public static final BigDecimal STANDARD_RATE = BigDecimal.valueOf(20);
        public static final BigDecimal SMOKING_RATE = BigDecimal.valueOf(30);
        public static final BigDecimal GAMBLING_RATE = BigDecimal.valueOf(15);
        /**
         * A tax applied to Carseats, Booster seats and other such products.
         *
         * @see <a href="https://www.gov.uk/guidance/rates-of-vat-on-different-goods-and-services#clothing-and-footwear-protective-and-safety-equipment">Child Safety tax</a>
         */
        public static final BigDecimal CHILD_SAFETY_RATE = BigDecimal.valueOf(5);
    }

}
