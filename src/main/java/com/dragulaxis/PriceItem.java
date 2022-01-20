package com.dragulaxis;

import javax.persistence.*;

@Entity
@Table(name = "PriceItems")
public class PriceItem {

    @Column(columnDefinition = "VARCHAR(64)")
    private String vendor;

    @Column(columnDefinition = "VARCHAR(64)")
    private String number;

    @Column(columnDefinition = "VARCHAR(64)")
    private String search_vendor;

    @Column(columnDefinition = "VARCHAR(64)")
    private String search_number;

    @Column(columnDefinition = "VARCHAR(512)")
    private String description;

    @Column(columnDefinition = "DECIMAL(18,2)")
    private Double price;

    @Column
    private int count;

    public PriceItem() {
    }

    public PriceItem(String vendor, String number, String search_vendor, String search_number, String description, Double price, int count) {
        this.vendor = vendor;
        this.number = number;
        this.search_vendor = search_vendor;
        this.search_number = search_number;
        this.description = description;
        this.price = price;
        this.count = count;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSearch_vendor() {
        return search_vendor;
    }

    public void setSearch_vendor(String search_vendor) {
        this.search_vendor = search_vendor;
    }

    public String getSearch_number() {
        return search_number;
    }

    public void setSearch_number(String search_number) {
        this.search_number = search_number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "PriceItem{" +
                "vendor='" + vendor + '\'' +
                ", number='" + number + '\'' +
                ", search_vendor='" + search_vendor + '\'' +
                ", search_number='" + search_number + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}
