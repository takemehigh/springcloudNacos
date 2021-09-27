package DesignPattern.factoryPattern;

public interface ComputerFactory {
    public CPU makeCPU();
}


class ComputerFactory1{
    CPU makeCPU(){
        return new CPU("英特尔CPU","INTEL");
    }

    MainBoard makeMainBoard(){
        return new MainBoard("英特尔主板","INTEL");
    }
}

class ComputerFactory2{
    CPU makeCPU(){
        return new CPU("AMDCPU","AMD");
    }

    MainBoard makeMainBoard(){
        return new MainBoard("AMD主板","AMD");
    }
}