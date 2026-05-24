class TestReturnArrayAsBool {
  public static void main(String[] args) {
  }
}

class A {
  public boolean bad() {
    return new int[5];
  }
}
