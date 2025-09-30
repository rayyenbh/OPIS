// Worker.java
package application;

public class Worker {
    private String name;
    private String surname;
    private int id;

    public Worker(String name, String surname, int id) {
        this.name = name;
        this.surname = surname;
        this.id = id;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
