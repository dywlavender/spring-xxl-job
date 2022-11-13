package design.com;


public class AbstractFactoryDemo {
    public static void main(String[] args) {
        AbstractFactory factory1 = new Factory1();
        AbstractFactory factory2 = new Factory2();

        AbsProductA productA = factory1.createProductA();
        AbsProductB productB = factory1.createProductB();

        productA.infoA();
        productB.infoB();

        AbsProductA productA1 = factory2.createProductA();
        AbsProductB productB1 = factory2.createProductB();

        productA1.infoA();
        productB1.infoB();
    }
}


interface AbsProductA{
    void infoA();
}

interface AbsProductB{
    void infoB();
}

class ProductA1 implements AbsProductA{

    @Override
    public void infoA() {
        System.out.println("A1");
    }
}

class ProductA2 implements AbsProductA{

    @Override
    public void infoA() {
        System.out.println("A2");
    }
}

class ProductB1 implements AbsProductB{

    @Override
    public void infoB() {
        System.out.println("B1");
    }
}

class ProductB2 implements AbsProductB{

    @Override
    public void infoB() {
        System.out.println("B2");
    }
}


interface AbstractFactory{
    AbsProductA createProductA();
    AbsProductB createProductB();
}

class Factory1 implements AbstractFactory{

    @Override
    public AbsProductA createProductA() {
        return new ProductA1();
    }

    @Override
    public AbsProductB createProductB() {
        return new ProductB1();
    }
}

class Factory2 implements AbstractFactory{

    @Override
    public AbsProductA createProductA() {
        return new ProductA2();
    }

    @Override
    public AbsProductB createProductB() {
        return new ProductB2();
    }
}
