package com.devroic.jsonlite.model;

import java.util.List;

public class Person {

    private String id;
    private String name;
    private String city;
    private List<String> cars;
    private List<String> brands;
    private String job;

    public Person() {    //Empty Constructor to be used from the Jackson ObjectMapper
    }

    // Constructor
    public Person(String id, String name, String city, List<String> cars, List<String> brands, String job) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.cars = cars;
        this.brands = brands;
        this.job = job;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getCars() {
        return cars;
    }

    public void setCars(List<String> cars) {
        this.cars = cars;
    }

    public List<String> getBrands() {
        return brands;
    }

    public void setBrands(List<String> brands) {
        this.brands = brands;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    // Optional: Override toString() for easy printing of the object
    @Override
    public String toString() {
        return "Person{" +
                "id='" + (id != null ? id : "N/A") + '\'' +
                ", name='" + (name != null ? name : "N/A") + '\'' +
                ", city='" + (city != null ? city : "N/A") + '\'' +
                ", cars=" + (cars != null ? cars : "N/A") +
                ", brands=" + (brands != null ? brands : "N/A") +
                ", job='" + (job != null ? job : "N/A") + '\'' +
                '}';
    }
}
