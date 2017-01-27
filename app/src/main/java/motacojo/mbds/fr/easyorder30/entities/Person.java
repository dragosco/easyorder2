package motacojo.mbds.fr.easyorder30.entities;

import org.json.JSONObject;

import java.util.Date;

import motacojo.mbds.fr.easyorder30.utils.GlobalVariables;

/**
 * Created by thais on 21/10/2016.
 */

public class Person {

    private String id;
    private String nom;
    private String prenom;
    private String sexe;
    private String telephone;
    private String email;
    private String createdBy;
    private String password;
    private boolean connected;
    private Date creadtedAt;
    private Date updatedAt;

    public Person() {
        this.nom = "Etchebest";
        this.prenom = "Philippe";
        this.sexe = "masculin";
        this.telephone = "+3366666666666";
        this.email = "etchebest@topchef.fr";
        this.createdBy = "Thais & Dragos";
        this.password = "pass";
    }

    public Person(String nom, String prenom, String sexe, String telephone, String email, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.telephone = telephone;
        this.email = email;
        this.createdBy = prenom;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getFullName() {
        return this.prenom + " " + this.nom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Date getCreadtedAt() {
        return creadtedAt;
    }

    public void setCreadtedAt(Date creadtedAt) {
        this.creadtedAt = creadtedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static Person getById(GlobalVariables globalVariables, String id) {
        return globalVariables.getAllUsers().get(id);
    }

    public static Person parseJSON(JSONObject person) {
        Person p = new Person(
                person.optString("nom", "John"),
                person.optString("prenom", "John"),
                person.optString("sexe", "John"),
                person.optString("telephone", "John"),
                person.optString("email", "John"),
                person.optString("password", "John"));
        p.setConnected(person.optBoolean("connected", false));
        p.setId(person.optString("id", "99999999999999999999999"));
        return p;
    }
}
