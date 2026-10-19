package com.example.mediremind;

public class Medicine {

    private int id;
    private String name;
    private int quantity;
    private double price;
    private String expiryDate;

    // Used when creating a new medicine before saving it.
    public Medicine(String name, int quantity, double price, String expiryDate) {
        this(0, name, quantity, price, expiryDate);
    }

    // Used when loading a medicine from SQLite.
    public Medicine(int id, String name, int quantity,
                    double price, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getTotalValue() {
        return quantity * price;
    }
}