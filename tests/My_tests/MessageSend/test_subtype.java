class TestSubtype {
  public static void main(String[] args) {
  }
}

class A {
  public int getValue() {
    return 1;
  }
}

class B extends A {
  public int getValue() {
    return 2;
  }
}

class C {
  public int test(A a) {
    return a.getValue();
  }
}

class D {
  C myC;
  B myB;

  public int run() {
    int result;
    result = myC.test(myB);
    return result;
  }
}
