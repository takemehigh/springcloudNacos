package DesignPattern.factoryPattern;

public class Computer {

    CPU cpu;

    MainBoard mainBoard;

    public Computer(CPU cpu, MainBoard mainBoard) {
        this.cpu = cpu;
        this.mainBoard = mainBoard;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public MainBoard getMainBoard() {
        return mainBoard;
    }

    public void setMainBoard(MainBoard mainBoard) {
        this.mainBoard = mainBoard;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "cpu=" + cpu +
                ", mainBoard=" + mainBoard +
                '}';
    }
}
