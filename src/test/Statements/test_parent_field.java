class TestParentField {
  public static void main(String[] args) {
  }
}

class A {
  int x;

  public int getX() {
    return x;
  }
}

class B extends A {
  public int useParentField() {
    x = 42;
    return x;
  }
}
