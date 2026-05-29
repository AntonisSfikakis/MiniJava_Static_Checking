// Expected output:
// A.foo : 0
// A.bar : 8
// B.foo : 0     (override, NOT printed)
// So just:
// B is empty in output (foo is override)
// C.bar : 8     (override of A.bar, NOT printed)
// So C is empty too
//
// Actually wait - B.foo overrides A.foo (same name, same params) -> not printed
// C.bar overrides A.bar (same name, same params) -> not printed
// Expected:
// A.foo : 0
// A.bar : 8

class OffsetPiazza {
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
}

class B extends A {
  public int foo() {
    return 3;
  }
}

class C extends B {
  public int bar() {
    return 4;
  }
}
