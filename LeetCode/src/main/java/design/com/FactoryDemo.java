package design.com;


public class FactoryDemo {

    public static void main(String[] args) {
        Factory factory = new Factory();
        Product a = factory.getProduct("A");
        Product b = factory.getProduct("B");
        a.info();
        b.info();
    }
}

class Factory{
    public Product getProduct(String productName){
        return switch (productName) {
            case "A" -> new ProductA();
            case "B" -> new ProductB();
            default -> null;
        };
    }
}

interface Product{
    void info();
}

class ProductA implements Product{
    @Override
    public void info(){
        System.out.println("A....");
    }
}

class ProductB implements Product{
    @Override
    public void info(){
        System.out.println("B....");
    }
}



