class TestArrayBoolIndex {
  public static void main(String[] args) {
  }
}

class A {
  public int bad() {
    int[] a;
    int x;
    a = new int[10];
    x = a[true];
    return x;
  }
}
