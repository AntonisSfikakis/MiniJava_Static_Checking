class TestOverloadDiffReturn {
  public static void main(String[] args) {
  }
}

class A {
  public int foo(int x) {
    return x;
  }

  public boolean foo(boolean x) {
    return x;
  }
}
