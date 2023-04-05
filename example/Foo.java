class Foo {
    public static void main(String[] args) {
        int a = 5;
        f(a);
    }

    public static void f1(int a) {
        System.out.println("A is greater than 1: " + a);
    }

    public static void f2(int a) {
        System.out.println("A is less than or equal to 1: " + a);
    }

    public static void f(int a) {
        if (a > 1) {
            f1(a);
        }
        else {
            f2(a);
        }
    }
}
