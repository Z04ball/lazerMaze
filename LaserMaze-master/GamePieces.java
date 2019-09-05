import java.awt.Color;
import java.awt.image.BufferedImage;
/**
 * Models a game piece to be placed on the board.
 *
 * @author Ben, Tyler, Zach, Sean, Kaley
 * @version 3/15
 */
public class GamePieces
{
    private Color color;
    private CardFunction function;
    private boolean canRotate;
    private int direction; 
    private BufferedImage image;
    
    /**
     *          3
     *          |
     *      2 <- -> 0
     *          |
     *          1
     */
    

    /**
     * Constructor for objects of class GamePieces
     */
    public GamePieces(Color color, CardFunction function, boolean canRotate, int direction)
    {
        this.color = color;
        this.function = function;
        this.canRotate = canRotate;
        this.direction = direction;
        image = null;
    }
     public GamePieces(Color color, CardFunction function, boolean canRotate)
    {
        this.color = color;
        this.function = function;
        this.canRotate = canRotate;
        this.direction = 0;
       image = null;
    }
    public Color getColor(){
        return color;
    }
    public CardFunction getFunction(){
        return function;
    }
    public boolean getCanRotate(){
        return canRotate;
    }
    public int getDirection(){
        return direction;
    }
    public void setImage(BufferedImage img){
        image = img;
    }
}
