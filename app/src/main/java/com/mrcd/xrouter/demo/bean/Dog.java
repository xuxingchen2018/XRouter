package com.mrcd.xrouter.demo.bean;

public class Dog {

    String name;

    int mSpeed;

    public String getName() {
        return name;
    }

    public Dog setName(String name) {
        this.name = name;
        return this;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public Dog setSpeed(int speed) {
        mSpeed = speed;
        return this;
    }

    public Dog(String name, int speed) {
        this.name = name;
        mSpeed = speed;
    }
}
