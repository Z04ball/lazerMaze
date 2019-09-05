/**
 * Helps determine where a piece will go on the board
 *
 * @author Kaley, Tyler, Sean, Ben, Zach
 * @version 4/5/18
 */
public class Helper
{
    /**
     * finds the x coordinate of a piece at a given space
     * 
     * @param space, the space where the piece is 
     * @return the x coordinate number
     */
    public int spaceX(int space)
    {
        if(space == 1 || space == 6 || space == 11 || space == 16 || space == 21)
        {
            return 112;
        }           
        else if(space == 2 || space == 7 || space == 12 || space == 17 || space == 22)
        {
            return 252;
        }    
        else if(space == 3 || space == 8 || space == 13 || space == 18 || space == 23)
        {
            return 392;
        }
        else if(space == 4 || space == 9 || space == 14 || space == 19 || space == 24)
        {
            return 532;
        }
        else if(space == 5 || space == 10 || space == 15 || space == 20 || space == 25)
        {
            return 672;
        }
        return 0;
    }

    /**
     * finds the y coordinate of a piece at a given space
     * 
     * @param space, the space where the piece is 
     * @return the y coordinate number
     */
    public int spaceY(int space)
    {
        if(space == 1 || space == 2 || space == 3 || space == 4 || space == 5)
        {
            return 92;
        }           
        else if(space == 6 || space == 7 || space == 8 || space == 9 || space == 10)
        {
            return 232;
        }    
        else if(space == 11 || space == 12 || space == 13 || space == 14 || space == 15)
        {
            return 374;
        }
        else if(space == 16 || space == 17 || space == 18 || space == 19 || space == 20)
        {
            return 514;
        }
        else if(space == 21 || space == 22 || space == 23 || space == 24 || space == 25)
        {
            return 655;
        }
        return 0;
    }

    /**
     * This method is a helper method used to find where a piece will go. Based on where the click is, 
     * it will determine the space
     * 1 2 3 4 5
     * 6 7 8 9 10
     * etc...
     * Every 5 spaces repeats the same x-coordinates with different shared Y-coordinates
     *               X1= 111-> 218    X2= 252-> 359    X3= 393-> 500   X4= 534-> 641   X5= 675-> 782
     *  Y1= 90-197
     *  Y2= 231-338
     *  Y3= 372-479
     *  Y4= 513-620
     *  Y5= 654-761
     *  
     *  @param x The x coordinate
     *  @param y The Y coordinate
     *  @return The space that is currently being looked at
     */
    public int whatSpace(int x, int y)
    {
        if(x >=111 && x <=218 && y >= 90 && y <= 197)// 1st piece Y-Range 90 to 197 (difference 107)
        {
            return 1;
        }
        else if(x >=252 && x <=359 && y >= 90 && y <= 197)// 2nd piece
        {
            return 2;
        }
        else if(x >=393 && x <=500 && y >= 90 && y <= 197)//3rd
        {
            return 3;
        }
        else if(x >=534 && x <=641 && y >= 90 && y <= 197)//4th
        {
            return 4;
        }
        else if(x >=675 && x <=782 && y >= 90 && y <= 197)//5th
        {
            return 5;
        }
        else if(x >=111 && x <=218 && y >= 231 && y <= 338)//6th piece Y-Range 231 to 338 (difference 107)
        {
            return 6;
        }
        else if(x >=252 && x <=359 && y >= 231 && y <= 338)//7th
        {
            return 7;
        }
        else if(x >=393 && x <=500 && y >= 231 && y <= 338)//8th
        {
            return 8;
        }
        else if(x >=534 && x <=641 && y >= 231 && y <= 338)//9th
        {
            return 9;
        }
        else if(x >=675 && x <=782 && y >= 231 && y <= 338)//10th
        {
            return 10;
        }
        else if(x >=111 && x <=218 && y >= 372 && y <= 479)//11th piece Y-Range 372 to 479 (difference 107)
        {
            return 11;
        }
        else if(x >=252 && x <=359 && y >= 372 && y <= 479)//12th
        {
            return 12;
        }
        else if(x >=393 && x <=500 && y >= 372 && y <= 479)//13th
        {
            return 13;
        }
        else if(x >=534 && x <=641 && y >= 372 && y <= 479)//14th
        {
            return 14;
        }
        else if(x >=675 && x <=782 && y >= 372 && y <= 479)//15th
        {
            return 15;
        }
        else if(x >=111 && x <=218 && y >= 513 && y <= 620)//16th piece Y-Range 513 to 620 (difference 107)
        {
            return 16;
        }
        else if(x >=252 && x <=359 && y >= 513 && y <= 620)//17th
        {
            return 17;
        }
        else if(x >=393 && x <=500 && y >= 513 && y <= 620)//18th
        {
            return 18;
        }
        else if(x >=534 && x <=641 && y >= 513 && y <= 620)//19th
        {
            return 19;
        }
        else if(x >=675 && x <=782 && y >= 513 && y <= 620)//20th
        {
            return 20;
        }
        else if(x >=111 && x <=218 && y >= 654 && y <= 761)//21st piece Y-Range 654 to 761 (difference 107)
        {
            return 21;
        }
        else if(x >=252 && x <=359 && y >= 654 && y <= 761)//22nd
        {
            return 22;
        }
        else if(x >=393 && x <=500 && y >= 654 && y <= 761)//23rd
        {
            return 23;
        }
        else if(x >=534 && x <=641 && y >= 654 && y <= 761)//24th
        {
            return 24;
        }
        else if(x >=675 && x <=782 && y >= 654 && y <= 761)//25th
        {
            return 25;
        }
        else 
        {
            return 0;
        }
    }
}
