class TestOverrideClassReturn {
  public static void main(String[] args) {
  }
}

class X {
  public int val() {
    return 1;
  }
}

class A {
  public X getX() {
    return new X();
  }
}

class B extends A {
  public X getX() {
    return new X();
  }
}
