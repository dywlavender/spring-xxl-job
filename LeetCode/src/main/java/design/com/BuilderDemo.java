package design.com;

import java.util.ArrayList;
import java.util.List;

public class BuilderDemo {
    public static void main(String[] args) {
        Director director = new Director();

        Builder builder1 = new Builder1();
        director.construct(builder1);
        CarProduct result = builder1.getResult();
        result.show();

        Builder builder2 = new Builder2();
        director.construct(builder2);
        CarProduct result2 = builder2.getResult();
        result2.show();
    }
}

class Director{
    public void construct(Builder builder){
        builder.buildPart();
    }
}

abstract class Builder{
    public abstract void buildPart();
    public abstract CarProduct getResult();
}

class Builder1 extends Builder{
    CarProduct product = new CarProduct();
    @Override
    public void buildPart() {
        product.add("A");
        product.add("B");
        product.add("C");
    }

    @Override
    public CarProduct getResult() {
        return product;
    }
}

class Builder2 extends Builder{
    CarProduct product = new CarProduct();
    @Override
    public void buildPart() {
        product.add("A2");
        product.add("B2");
        product.add("C2");
    }

    @Override
    public CarProduct getResult() {
        return product;
    }
}


class CarProduct{
    List<String> parts = new ArrayList<String>();

    public void add(String part){
        parts.add(part);
    }
    public void show(){
        parts.forEach(System.out::println);
    }
}
