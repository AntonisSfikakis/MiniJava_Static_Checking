// Expected output:
// A.x : 0
// A.flag : 4
// A.arr : 5
// A.other : 13
// A.getX : 0
// A.getFlag : 8
// B.y : 21       (continues from A: 0+4+1+8+8=21)
// B.b : 25       (21+4=25)
// B.getY : 16    (continues from A's methods: 0+8+8=16)

class OffsetMixed {
  public static void main(String[] args) {
  }
}

class A {
  int x;
  boolean flag;
  int[] arr;
  A other;

  public int getX() {
    return x;
  }

  public boolean getFlag() {
    return flag;
  }
}

class B extends A {
  int y;
  boolean b;

  public int getY() {
    return y;
  }
}
