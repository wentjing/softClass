package ch21_singletonpattern.privateSingletonFactory;

public abstract class Client {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        if (s1 == s2) {
            System.out.println("两个示例对象相同");
        }
        System.out.println(s1);
        System.out.println(s2);
    }

}
