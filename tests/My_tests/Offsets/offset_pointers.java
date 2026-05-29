// Expected output:
// A.x : 0
// A.arr : 4
// A.other : 12
// A.flag : 20
// A.foo : 0

class OffsetPointers {
  public static void main(String[] args) {
  }
}

class A {
  int x;
  int[] arr;
  A other;
  boolean flag;

  public int foo() {
    return x;
  }
}
