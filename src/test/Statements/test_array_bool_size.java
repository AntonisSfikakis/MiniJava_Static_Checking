class TestArrayBoolSize {
  public static void main(String[] args) {
  }
}

class A {
  public int bad() {
    int[] a;
    a = new int[true];
    return 1;
  }
}
