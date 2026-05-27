class TestOverrideClassReturnWrong {
  public static void main(String[] args) {
  }
}

class X {
  public int val() {
    return 1;
  }
}

class Y {
  public int val() {
    return 2;
  }
}

class A {
  public X getObj() {
    return new X();
  }
}

class B extends A {
  public Y getObj() {
    return new Y();
  }
}
