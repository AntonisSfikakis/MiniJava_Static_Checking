class TestArrayCorrect {
  public static void main(String[] args) {
  }
}

class A {
  int[] arr;

  public int testArray() {
    int[] a;
    int x;
    a = new int[10];
    x = a[0];
    a[0] = 5;
    return x;
  }
}
