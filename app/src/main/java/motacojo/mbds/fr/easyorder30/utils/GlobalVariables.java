package motacojo.mbds.fr.easyorder30.utils;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;

import motacojo.mbds.fr.easyorder30.entities.Order;
import motacojo.mbds.fr.easyorder30.entities.Person;
import motacojo.mbds.fr.easyorder30.entities.Product;

/**
 * Created by cojoc on 27/01/2017.
 */

public class GlobalVariables extends Application {

    private Person connectedUser;
    private HashMap<String, Person> allUsers = new HashMap<>();
    private HashMap<String, Product> allProducts = new HashMap<>();
    private HashMap<String, Order> allOrders = new HashMap<>();
    private HashMap<String, Integer> orderInProgress =  new HashMap<>();
    private String token = "";

    public HashMap<String, Person> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(HashMap<String, Person> allUsers) {
        this.allUsers = allUsers;
    }

    public void removeUserFromAllUsers(String id) {
        allUsers.remove(id);
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

    public HashMap<String, Integer> getOrderInProgress() {
        return orderInProgress;
    }

    public void setOrderInProgress(HashMap<String, Integer> orderInProgress) {
        this.orderInProgress = orderInProgress;
    }

    public boolean isOnOrderInProgress(String productId) {
        return orderInProgress.containsKey(productId);
    }

    public int getOrderInProgressQty(String productId) {
        if (orderInProgress.containsKey(productId)) {
            return orderInProgress.get(productId);
        } else {
            return 0;
        }

    }

    public void addProductToOrder(String productId) {
        if (orderInProgress.containsKey(productId)) {
            int qty = orderInProgress.get(productId);
            orderInProgress.put(productId, qty + 1);
        } else {
            orderInProgress.put(productId, 1);
        }
    }

    public void removeProductFromOrder(String productId) {
        if (orderInProgress.containsKey(productId)) {
            int qty = orderInProgress.get(productId);
            if (qty - 1 == 0) {
                orderInProgress.remove(productId);
            } else {
                orderInProgress.put(productId, qty - 1);
            }
        } else {
            Log.e("removeProductFromOrder", "Produit innexistant");
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Person getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(Person connectedUser) {
        this.connectedUser = connectedUser;
    }
}
