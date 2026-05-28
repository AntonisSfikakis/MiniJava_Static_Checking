class TestOverloadAllRelated {
  public static void main(String[] args) {
  }
}

class X {
  public int val() {
    return 1;
  }
}

class Y extends X {
  public int val() {
    return 2;
  }
}

class A {
  public int foo(X a, X b) {
    return 1;
  }

  public int foo(Y a, Y b) {
    return 2;
  }
}
