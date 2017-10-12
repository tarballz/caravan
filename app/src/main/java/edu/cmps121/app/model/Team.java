package edu.cmps121.app.model;

import java.util.Arrays;

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

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cars=" + Arrays.toString(cars) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        if (id != team.id) return false;
        if (name != null ? !name.equals(team.name) : team.name != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(cars, team.cars);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(cars);
        //result = 1;
        return result;
    }
}
