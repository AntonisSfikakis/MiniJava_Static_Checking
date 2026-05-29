// Expected output:
// A.x : 0
// A.foo : 0
// B.y : 0
// B.flag : 4
// B.bar : 0
// B.baz : 8

class OffsetMulti {
  public static void main(String[] args) {
  }
}

class A {
  int x;

  public int foo() {
    return 1;
  }
}

class B {
  int y;
  boolean flag;

  public int bar() {
    return 2;
  }

  public boolean baz() {
    return true;
  }
}
