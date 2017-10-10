package edu.cmps121.app.model;

/**
 * Created by Payton on 10/10/17.
 */

public class Team {
    public int id;

    public String name;

    public String[] cars;

    public Team() {

    }

    public Team(int id, String name, String[] cars) {
        this.id = id;
        this.name = name;
        this.cars = cars;
    }

    public int getId(){ return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String[] getCars() { return cars; }
    public void setCars(String[] cars) { this.cars = cars; }
}
