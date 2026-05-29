// Expected output:
// A.foo : 0        (foo with no args)
// A.bar : 8
// A.foo : 16       (foo with int arg - overload)
// B.baz : 24
// B.bar : 32       (bar with int arg - overload, not override)
// B.foo : 40       (foo with int,int args - overload)

class OffsetOverload {
  public static void main(String[] args) {
  }
}

class A {
  public int foo() {
    return 1;
  }

  public int bar() {
    return 2;
  }

  public int foo(int x) {
    return x;
  }
}

class B extends A {
  public int baz() {
    return 3;
  }

  public boolean bar(int x) {
    return true;
  }

  public int foo(int x, int y) {
    return x + y;
  }
}
