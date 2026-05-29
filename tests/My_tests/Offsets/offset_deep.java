// Expected output:
// A.x : 0
// A.foo : 0
// A.bar : 8
// B.y : 4
// B.baz : 16
// C.z : 12
// C.qux : 24

class OffsetDeep {
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

  public int baz() {
    return 3;
  }
}

class C extends B {
  int z;

  public int qux() {
    return 4;
  }
}
