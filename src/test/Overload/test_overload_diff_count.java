class TestOverloadDiffCount {
  public static void main(String[] args) {
  }
}

class A {
  public int foo(int x) {
    return x;
  }
}

class B extends A {
  public int foo(int x, int y) {
    return x + y;
  }
}
