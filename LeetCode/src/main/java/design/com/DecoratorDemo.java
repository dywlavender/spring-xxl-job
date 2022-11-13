package design.com;

public class DecoratorDemo {
    public static void main(String[] args) {
        Person zs = new Student("zs");
        zs.operation();

        System.out.println("---------");
        DecoratorA decoratorA = new DecoratorA(zs);
        decoratorA.operation();
    }
}

abstract class Person{
    protected String name;

    public abstract void operation();
}

class Student extends Person{
    public Student(String name){
        this.name = name;
    }

    @Override
    public void operation(){
        System.out.println(name + "：学习");
    }
}

abstract class Decorator extends Person{
    protected Person person;

}

class DecoratorA extends Decorator{
    public DecoratorA(Person person){
        this.person = person;
    }

    @Override
    public void operation(){
        person.operation();
        System.out.println("写作业");
    }
}


