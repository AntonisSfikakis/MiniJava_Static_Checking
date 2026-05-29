// Expected output:
// A.i : 0
// A.flag : 4
// A.j : 5
// A.foo : 0
// A.bar : 8

class OffsetBasic {
  public static void main(String[] args) {
  }
}

class A {
  int i;
  boolean flag;
  int j;

  public int foo() {
    return i;
  }

  public boolean bar() {
    return flag;
  }
}
