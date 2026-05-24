class TestReturnCorrect {
  public static void main(String[] args) {
  }
}

class A {
  public int returnInt() {
    return 1;
  }

  public boolean returnBool() {
    return true;
  }

  public int returnVar() {
    int x;
    x = 5;
    return x;
  }

  public int returnExpr() {
    return 1 + 2;
  }

  public boolean returnCompare() {
    return 1 < 2;
  }

  public int[] returnArray() {
    int[] a;
    a = new int[5];
    return a;
  }
}
