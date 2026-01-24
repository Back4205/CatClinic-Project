package com.mycompany.catclinicproject.model;

public class Cat {
    private String name;
    private int age;
    private String breed;
    private String gender;
    private String id ;

    public Cat() {

    }
    public Cat(String id ,String name, int age, String breed, String gender) {
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.gender = gender;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Cat setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }
    public Cat setAge(int age) {
        this.age = age;
        return this;
    }
    public String getBreed() {
        return breed;
    }
    public Cat setBreed(String breed) {
        this.breed = breed;
        return this;
    }
    public String getGender() {
        return gender;
    }
    public Cat setGender(String gender) {
        this.gender = gender;
        return this;
    }
    public String getId() {
        return id;
    }
    public Cat setId(String id) {
        this.id = id;
        return this;
    }

}
