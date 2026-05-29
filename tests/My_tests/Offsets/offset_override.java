// Expected output:
// A.x : 0
// A.foo : 0
// A.bar : 8
// B.y : 4
// (B.foo is override, not printed, offset stays 0)
// C.z : 8
// (C.bar is override, not printed, offset stays 8)

class OffsetOverride {
  public static void main(String[] args) {
  }
}

class A {
  int x;

  public int foo() {
    return 1;
  }

  public int bar() {
    return 2;
  }
}

class B extends A {
  int y;

  public int foo() {
    return 3;
  }
}

class C extends B {
  int z;

  public int bar() {
    return 4;
  }
}
