// Expected output:
// A.i : 0
// A.flag : 4
// A.j : 5
// A.foo : 0
// A.fa : 8
// B.type : 9
// B.k : 17
// B.bla : 16

class OffsetSpec {
  public static void main(String[] args) {
  }
}

class A {
  int i;
  boolean flag;
  int j;

  public int foo() {
    return 1;
  }

  public boolean fa() {
    return true;
  }
}

class B extends A {
  A type;
  int k;

  public int foo() {
    return 2;
  }

  public boolean bla() {
    return false;
  }
}
