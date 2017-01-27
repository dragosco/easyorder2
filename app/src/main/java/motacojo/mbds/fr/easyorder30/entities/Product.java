package motacojo.mbds.fr.easyorder30.entities;

import org.json.JSONObject;

import java.util.Date;

import motacojo.mbds.fr.easyorder30.utils.GlobalVariables;

public class Product {

    private String name;
    private String description;
    private int price;
    private int calories;
    private String type;
    private String picture;
    private int discount;
    private Date createdAt;
    private Date updatedAt;
    private String id;

    public Product(String name, String description, int price, int calories, String type, String picture, int discount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.calories = calories;
        this.type = type;
        this.picture = picture;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static Product getById(GlobalVariables globalVariables, String id) {
        return globalVariables.getAllProducts().get(id);
    }

    public static Product parseJSON(JSONObject product) {
        Product p = new Product(
                product.optString("name", "Produit Inconnu"),
                product.optString("description", "Pas de description"),
                Integer.parseInt(product.optString("price", "0")),
                Integer.parseInt(product.optString("calories", "0")),
                product.optString("type", "Inconnu"),
                product.optString("picture", "none"),
                Integer.parseInt(product.optString("discount", "0")));
        p.setId(product.optString("id", "99999999999999999999999"));
        return p;
    }
}

