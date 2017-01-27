package motacojo.mbds.fr.easyorder30.utils;

import android.app.Application;

import java.util.HashMap;

import motacojo.mbds.fr.easyorder30.entities.Order;
import motacojo.mbds.fr.easyorder30.entities.Person;
import motacojo.mbds.fr.easyorder30.entities.Product;

/**
 * Created by cojoc on 27/01/2017.
 */

public class GlobalVariables extends Application {

    private HashMap<String, Person> allUsers = new HashMap<>();
    private HashMap<String, Product> allProducts = new HashMap<>();
    private HashMap<String, Order> allOrders = new HashMap<>();

    public HashMap<String, Person> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(HashMap<String, Person> allUsers) {
        this.allUsers = allUsers;
    }

    public HashMap<String, Product> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(HashMap<String, Product> allProducts) {
        this.allProducts = allProducts;
    }

    public HashMap<String, Order> getAllOrders() {
        return allOrders;
    }

    public void setAllOrders(HashMap<String, Order> allOrders) {
        this.allOrders = allOrders;
    }
}
