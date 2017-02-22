

public class Jewel{
  private Sprite picture;
  public Enum JewelType{
    KOCH(1, false), SIERPINSKI(2, false), CARPET(3, false), DRAGON(4, false), TREE(5, false), APOLONIAN(6, false),
      HILBERT(7, false), MANDLEBROT(8, true), JULIA(9, true), NEWTON(10, true);
    private int number;
    private boolean special;
    public Jewel(int num, boolean cool){
      number = num;
      special = cool;
    }
  }
  public Jewel(int type, Bitmap b, int row, int col){
  
  
  }





}
