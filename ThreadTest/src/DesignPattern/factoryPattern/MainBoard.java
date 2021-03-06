package DesignPattern.factoryPattern;

public class MainBoard {

    String name;

    String type;

    public MainBoard(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MainBoard{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
