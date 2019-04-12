package com.github.venomousinc.studies.storefront.user;

import com.github.venomousinc.studies.storefront.store.ProductItem;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * A Storefront User session.
 */

public class User {

    private String username;
    private String password;

    private LinkedList<ProductItem> basket;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.basket = new LinkedList<>();
    }

    /**
     * @return this {@link User}'s {@link #basket} of {@link ProductItem}'s.
     */
    public LinkedList<ProductItem> getBasket() {
        return basket;
    }

    /**
     * A formatted version of {@link #getBasketTotal(boolean)}
     *
     * @see #getBasket()
     * @see #getBasketTotal(boolean)
     * @param withVAT Should VAT be calculated into the output?
     * @return
     */
    public String getFormattedBasketTotal(boolean withVAT) {
        return NumberFormat.getCurrencyInstance().format(getBasketTotal(withVAT));
    }
    /**
     * Gathers up all the {@link #basket} items, producing a total price for all of the {@link ProductItem}'s
     * <p>
     * This is not formatted for currency.
     * @see #getFormattedBasketTotal(boolean)
     * @param withVAT Should VAT be calculated into the output?
     * @return
     */
    public BigDecimal getBasketTotal(boolean withVAT) {
        BigDecimal total = BigDecimal.ZERO;

        for(ProductItem item : basket) {
            total = total.add(item.getPrice(true, withVAT));
        }

        return total;
    }


    /**
     * The total costs in purely VAT of the users basket.
     *
     * @see ProductItem
     * @see #getFormattedBasketTotal(boolean)
     * @see #getBasketTotal(boolean)
     * @return
     */
    public String getFormattedBasketVATOnly() {
        BigDecimal total = BigDecimal.ZERO;

        for(ProductItem item : basket) {
            total = total.add(item.TYPE.getTaxCost(item.getPrice(true, false)));
        }

        return NumberFormat.getCurrencyInstance().format(total);
    }

    public boolean addToBasket(ProductItem item) {
        return basket.add(item);
    }

    /**
     * Attempt to create or load the user with the specified Username and Password.
     * Validates the username, and hashes the password.
     * @param username
     * @param password
     * @return
     */
    public static User createUser(String username, String password) {
        /**
         * TODO: Mock Verify Password & Username, Mock Hash
         */
        if(validUsername(username) == false)
            return null;
        
        return new User(username, mockHash(username, password));
    }

    /**
     * Hash the users password.
     * Warning: Mock-up, not for actual use case!
     * @param username
     * @param password
     * @return
     */
    private static String mockHash(String username, String password) {
        StringBuilder hw = new StringBuilder();

        for(char c : password.toCharArray()) {
            if(c != 0) {
                hw.append((char) (c - 1));
                hw.append(c / username.length());
            }
        }

        return hw.toString();
    }

    /**
     * Characters allowed within the username.
     */
    private static Pattern usernamePattern = Pattern.compile("^[\\w\\d]{2,40}$");

    /**
     * Passes the Username through a Pattern Matching Regex, then returns if it's valid or not.
     * @param username
     * @return
     */
    public static boolean validUsername(String username) {
        return usernamePattern.matcher(username).matches();
    }

    public static int _USERNAME_MINIMUM_LENGTH = 2;
    public static int _USERNAME_MAXIMUM_LENGTH = 40;

    /**
     * Returns a string with Username rules laid out for the user.
     * @return
     */
    public static String usernameHelper() {
        StringBuilder sb = new StringBuilder("Username Guidelines:\n");
        sb.append( String.format("* Length: %d-%d", _USERNAME_MINIMUM_LENGTH, _USERNAME_MAXIMUM_LENGTH))
            .append("\n* Characters: a-z, A-Z, 0-9");
        return sb.toString();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
