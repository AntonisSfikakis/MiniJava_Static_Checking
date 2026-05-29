class TestMethodInExpr {
  public static void main(String[] args) {
  }
}

class A {
  public int getValue() {
    return 5;
  }

  public boolean getFlag() {
    return true;
  }

  public int testMethodInAssign() {
    int x;
    x = (this.getValue()) + 1;
    return x;
  }

  public int testMethodInIf() {
    int x;
    if (this.getFlag())
      x = 1;
    else
      x = 0;
    return x;
  }

  public int testMethodInPrint() {
    System.out.println(this.getValue());
    return 1;
  }

  public int testMethodInWhile() {
    int x;
    x = 0;
    while (this.getFlag())
      x = x + 1;
    return x;
  }
}
