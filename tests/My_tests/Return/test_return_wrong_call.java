class TestReturnWrongCall {
  public static void main(String[] args) {
  }
}

class A {
  public boolean getFlag() {
    return true;
  }

  public int bad() {
    return this.getFlag();
  }
}
