class TestReturnExpr {
  public static void main(String[] args) {
  }
}

class A {
  int x;
  int y;
  boolean flag;
  int[] arr;

  public int returnAdd() {
    return x + y;
  }

  public int returnMul() {
    return x * y;
  }

  public int returnSub() {
    return x - y;
  }

  public boolean returnCompare() {
    return x < y;
  }

  public boolean returnAnd() {
    return flag && (x < y);
  }

  public boolean returnNot() {
    return !flag;
  }

  public int returnArrayLookup() {
    arr = new int[5];
    return arr[0];
  }

  public int returnArrayLength() {
    arr = new int[5];
    return arr.length;
  }

  public int returnMethodCall() {
    return this.returnAdd();
  }

  public int returnNewMethodCall() {
    return (new A()).returnAdd();
  }

  public int returnComplex() {
    return (x + y) * (x - y);
  }

  public int returnBracket() {
    return (x);
  }
}
