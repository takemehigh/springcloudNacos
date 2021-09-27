package DesignPattern.factoryPattern;

public class FactoryTest {

    public static void main(String[] args) {
        ComputerFactory2 computerFactory2 = new ComputerFactory2();
        CPU cpu2=computerFactory2.makeCPU();
        MainBoard mainBoard2=computerFactory2.makeMainBoard();
        Computer computer = new Computer(cpu2,mainBoard2);
        System.out.println(computer);
    }

}





