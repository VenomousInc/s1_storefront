package com.github.venomousinc.studies.storefront;

import com.github.venomousinc.studies.storefront.store.ProductItem;
import com.github.venomousinc.studies.storefront.store.ProductType;
import com.github.venomousinc.studies.storefront.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger( Main.class );

    /**
     * Override the user login.
     */
    private static final boolean ADMIN_OVERRIDE = true;

    public static void main(String[] args) {
        LOGGER.info(">Storefront");
        try(Scanner sc = new Scanner(System.in)) {
            User user = null;
            if(ADMIN_OVERRIDE) {
                user = new User("ADMIN", "admin");
            }

            while(user == null) {
                System.out.print("Username: ");
                String _username = sc.nextLine();
                System.out.print("Password: ");
                String _password = sc.nextLine();

                /**
                 * Attempt to verify the user, and hash the password.
                 */
                user = User.createUser(_username, _password);
                if (user == null) {
                    System.out.format("Invalid Username!%n%s%n",User.usernameHelper());
                } else {
                    System.out.format("> Logged in as: %s with the password: [%s]%n", user.getUsername(), user.getPassword());
                }
            }
            /**
             * TODO: Load storefronts
             */
            Storefront storefront = new Storefront("Javamart 24/7");

            /** Add stock to the storefront **/
            storefront.addItem("JavaPrice Banana", "That's Java Price!", ProductType.FRUIT_AND_VEGETABLE, new BigDecimal(0.60), BigDecimal.valueOf(15));
            storefront.addItem("Java Undressed", "You won't believe what this programmer did on page 21!",
                    ProductType.MAGAZINES_AND_NEWSPAPERS, new BigDecimal(0.60), BigDecimal.valueOf(20));
            storefront.addItem("Java Cigarettes (20)", null, ProductType.SMOKING_PRODUCT, new BigDecimal(7.55));
            storefront.addItem("Java Cigarettes (50)", null, ProductType.SMOKING_PRODUCT, new BigDecimal(18.87), BigDecimal.valueOf(20));
            storefront.addItem("Python Cigarettes (20)", null, ProductType.SMOKING_PRODUCT, new BigDecimal(8.15));

            /**
             * If the scanner is still alive, continue to ask for input.
             */

            EnumSet<ProductType> stockTypes = storefront.getInventory().getStockTypes();
            System.out.format("You are browsing the %s Store.%n", storefront.getName());

            if(stockTypes == null || stockTypes.size() == 0) {
                System.out.println("The sadly store is currently OUT OF STOCK!");
                return;
            }

            int selector = -1;

            while (sc != null) {
                if(selector == -1) {
                    System.out.println("'exit' to stop shopping, 'checkout' to purchase your items.");
                    System.out.println("Please choose a Product Category");
                    for(ProductType pt : stockTypes) {
                        System.out.format("%03d. %s%n", pt.ordinal(), pt.fixedName());
                    }

                    if(sc.hasNextInt()) {
                        selector = sc.nextInt();
                    }
                    System.out.println(selector);

                } else {
                    /**
                     * If the user isn't using the Category override code, entered an invalid category.
                     */
                    if(selector != 999 && selector >= ProductType.values().length) {
                        selector = -1;
                        continue;
                    }

                    ArrayList<ProductItem> products = null;

                    /**
                     * If the user isn't using the Category override, tell them which category they're in.
                     */
                    if(selector != 999) {
                        ProductType productType = ProductType.values()[selector];
                        System.out.format("Selected: [%d] %s%n", selector, productType.fixedName());
                        products = storefront.getInventory().getProducts(productType);
                    } else {
                        products = storefront.getInventory().getProducts();
                    }

                    /**
                     * Throws the user back to the category selector.
                     */
                    if(products == null || products.size() == 0) {
                        System.out.println("We're currently out of stock in this section!");
                        selector = -1;
                        continue;
                    }

                    System.out.println("Type '-1' to return to the categories, 'exit' to stop shopping, or 'checkout' to purchase the selected products.");
                    for(int i = 0; i < products.size(); i++) {
                        System.out.format("%03d.  %-30s  %s%n", i, products.get(i).NAME, products.get(i).getFormattedPrice());
                    }

                    if(sc.hasNextInt()) {
                        int productID = sc.nextInt();

                        if(productID >= 0 && productID < products.size()) {
                            if(user.addToBasket(products.get(productID))) {
                                System.out.format("Adding Item: %s for $%s to Basket.%n", products.get(productID).NAME, products.get(productID).getFormattedPrice());
                            } else {
                                System.out.println("Failed to add Item to Basket!");
                            }
                            System.out.format(" * Basket Total: %s [%d]%n", user.getFormattedBasketTotal(true), user.getBasket().size());
                        } else if(productID == -1) {
                            selector = -1;
                        }
                    }
                }

                if(sc.hasNextLine()) {
                    switch(sc.nextLine().toLowerCase()) {
                        case "exit":
                            System.out.println("Goodbye!");
                            System.exit(0);
                        break;

                        case "checkout":
                            System.out.println("Printing your Receipt...");
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.format("**RECEIPT** %15s - NO REFUNDS! **%n", storefront.getName());
                            if(user != null && user.getBasket() != null && user.getBasket().size() > 0) {
                                for(ProductItem item : user.getBasket()) {
                                    System.out.format("** %-26s %-6s (%s)%n",
                                            item.NAME,
                                            item.getFormattedPrice(),
                                            NumberFormat.getCurrencyInstance().format(item.TYPE.getTaxCost(item.getPrice(true, false))));
                                }

                                System.out.format(" *** Total: %s%n", user.getFormattedBasketTotal(false));
                                System.out.format(" *** VAT: %s%n", user.getFormattedBasketVATOnly());
                                System.out.format(" *** Total with VAT: %s%n", user.getFormattedBasketTotal(true));
                                return;
                            } else {
                                System.out.println("You have nothing to checkout.");
                            }
                            user.getBasket().clear();
                        break;
                    }
                }

            }


        } catch(InputMismatchException | NullPointerException e) {
            LOGGER.error( String.format("Scanner ERROR: %s", e.getMessage()));
            e.printStackTrace();
        }
    }

}
