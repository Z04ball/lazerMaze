import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Window;
/**
 * Write a description of class BoardPanel here.
 *
 * @author Kaley, Tyler, Ben, Sean, Zach
 * @version 4/5/18
 */
public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener
{
    private int width, height;
    protected static String choice = "";
    private static BoardPanel panel;

    private static boolean movingPiece = false;
    private static BoardImage pieceToMove = null;

    private static int laserDirection = 0;
    private static boolean laserFired;

    private static BoardImage boardPicture;
    private static BoardImage piece1;

    private static BoardImage piece2;
    private static BoardImage piece3;

    private static BoardImage piece4;
    private static BoardImage piece5;
    private static BoardImage piece6;

    private static BufferedImage winLoss;
    private static BufferedImage winLossLeft;
    private static BufferedImage winLossRight;
    private static BufferedImage selected;

    private static BoardImage laser; 
    private static ArrayList<BoardImage> pieces = new ArrayList<>();

    protected ArrayList<Integer> lineStartX = new ArrayList<>();
    protected ArrayList<Integer> lineStartY = new ArrayList<>();

    protected ArrayList<Integer> lineEndX = new ArrayList<>();
    protected ArrayList<Integer> lineEndY = new ArrayList<>();

    // for drawing the laser
    private boolean firstLineDrawn; // used to differentiate
    // between drawing from laser vs other piece

    protected int startX , startY;
    protected BoardImage startingPiece;
    int previousDirection = -1; //keep track of where laser is coming from
    private boolean gameOver = false;
    private boolean gameWon = false;
    /**
     * The purpose of this class is to create BoardImage objects. This is so
     * these objects can be updated based on user clicks, and then repainted
     * to reflect those updates.
     */
    public class BoardImage{
        // list of images rotated each way
        protected ArrayList<BufferedImage> images;
        //location of piece
        protected int x;
        protected int y;
        protected int hitboxX, hitboxY;
        protected Rectangle rectangle;
        // keep starting point in case of reset
        protected final int startX, startY;
        // incremented to change image on rotate
        protected int currentImage;
        // image is able to rotate or not
        private boolean canRotate;
        // image is movable
        private boolean canMove;
        // whether the piece is a target or not
        private boolean isTarget;
        // can tell whether a piece has been hit yet or not
        private boolean isHit;
        private boolean selectedPiece;
        private String type; //description of type of piece,
        //mainly used for checkpoint in our case
        /**
         * Creates the board image 
         * 
         * @param images, an arraylist of buffered images that contains the 
         *                images we need on the board
         * @param x, the x coordinate
         * @param y, the y coordinate
         * @param canRotate, true if the piece can rotate, false if not
         * @param canMove, true if the piece can move, false if not
         * @param isTarget, true if ending piece, false if not
         */
        public BoardImage(ArrayList<BufferedImage> images, int x, 
        int y, boolean canRotate, boolean canMove, boolean isTarget){
            this.images = images;
            this.canRotate = canRotate;
            this.x = x;
            this.y = y;
            startX = x;
            startY = y;
            currentImage = 0;
            this.canMove = canMove;
            this.isTarget = isTarget;
            isHit = false;
            rectangle = new Rectangle();
            type = "";
            rectangle.setBounds(this.x, this.y, 107, 107);
            selectedPiece = false;
        }

        /**
         * sets the type of piece
         * 
         * @param type, the type of piece you want to set
         */
        public void setType(String type){
            this.type = type;
        }

        /**
         * returns the type of piece
         * 
         * @return String type, the type of piece 
         */
        public String getType(){
            return type;
        }

        /**
         * sets the x coordinate
         * 
         * @param x, the coordinate you want to set x to
         */
        public  void setX(int x){
            this.x = x;
        }

        /**
         * sets the y coordinate
         * 
         * @param y, the coordinate you want to set y to
         */
        public  void setY(int y){
            this.y = y;
        }

        /**
         * gets the x coordinate 
         * 
         * @return the x coordinate
         */
        public  int getX(){
            return x;
        }

        /** 
         * gets the y coordinate 
         * 
         * @return the y coordinate
         */
        public  int getY(){
            return y;
        }

        /**
         * gets the current image from the arraylist of buffered images
         * 
         * @return the bufferedimage that is currently being used
         */
        public BufferedImage getImage(){
            return images.get(currentImage);
        }

        /**
         * determines whether the piece can rotate
         * 
         * @return true if the piece can rotate, 
         *         false if the piece cannot rotate
         */
        public boolean isRotatable(){
            return canRotate;
        }

        /**
         * determines whether the piece can move
         * 
         * @return true if the piece can move
         *         false if the piece can move
         */
        public boolean isMovable(){
            return canMove;
        }

        /**
         * gets the index of the image being displayed
         * 
         * @return the index of the image
         */
        public int getDirection(){
            return currentImage;
        }

        /**
         * rotates the image around, assuring that it wraps 
         * around when it reaches the beginning 
         */
        public void rotateImage(){
            if(this.images.size() > 4){
                images.remove(0);

            }
            else{
                currentImage = (currentImage + 1) % 4;
            }
        }

        /**
         * updates hit status of piece
         * 
         */
        public void hit(){
            this.isHit = true;
        }

        /**
         * returns hit status of piece
         * 
         * @return whether piece has been hit or not
         */
        public boolean getHit(){
            return isHit;
        }

        /**
         * updates the location of the hit box 
         */
        public void updateHitBox()
        {
            this.rectangle.setLocation(this.x, this.y);
        }
    }

    /**
     * Constructor for objects of class BoardPanel
     */
    public BoardPanel()
    {
        super();
        setPreferredSize(new Dimension(1053, 850));
        width = getPreferredSize().width;
        height = getPreferredSize().height;
        setBackground( Color.WHITE );
        addMouseListener( this );

        //read in background image

        try{
            winLoss = ImageIO.read(new File("WinLoss.png"));
            winLossLeft = ImageIO.read(new File("WinLossLeft.png"));
            winLossRight = ImageIO.read(new File("WinLossRight.png"));
            selected = ImageIO.read(new File("selected.png"));
            if(choice.equals("Beginner")){

                BufferedImage img = ImageIO.read(new File("board.jpg"));
                //create new background
                ArrayList<BufferedImage> temp1 = new ArrayList<>();
                temp1.add(img);

                boardPicture = new BoardImage(temp1, 0, 0, false, false, false);

                String[] laserNames = {"laserstart.png", "laser0.jpg",
                        "laser1.jpg", "laser2.jpg", "laser3.jpg"};
                laser = createPiece(laserNames, 112, 655, true, false, false);
                laser.hit(); // don't need to hit the laser
                pieces.add(laser);

                String[] piece1Names = {"target_no_rotate.png"};
                piece1 = createPiece(piece1Names, 112, 514, false, false, false);
                pieces.add(piece1);

                String[] piece2Names = {"target_end.png"};
                piece2 = createPiece(piece2Names, 672, 92, false, false, true);
                pieces.add(piece2);

                String[] piece3Names = {"mirror_rotate.png","mirrorrightup.jpg", "mirror.jpg", "mirrorleftdown.jpg",
                        "mirrorleftup.jpg"};

                piece3 = createPiece(piece3Names, 897, 88, true, true, false);
                pieces.add(piece3);

                startX = laser.getX() + 58;
                startY = laser.getY() + 58;
                startingPiece = laser;
            }
            else if(choice.equals("Intermediate")){

                BufferedImage img = ImageIO.read(new File("board.jpg"));
                //create new background
                ArrayList<BufferedImage> temp1 = new ArrayList<>();
                temp1.add(img);

                boardPicture = new BoardImage(temp1, 0, 0, false, false, false);

                String[] laserNames = {"laser2.jpg"};
                laser = createPiece(laserNames, 672, 92, false, false, false);
                laser.hit(); // don't need to hit the laser
                pieces.add(laser);

                String[] piece1Names = {"mirror_rotate.png","mirrorrightup.jpg", "mirror.jpg", "mirrorleftdown.jpg",
                        "mirrorleftup.jpg"};
                piece1 = createPiece(piece1Names, 112, 655, true, false, false);
                pieces.add(piece1);
                ///////////// side pieces //////////////////////
                String[] piece2Names = {"mirror_rotate.png","mirrorrightup.jpg", "mirror.jpg", "mirrorleftdown.jpg",
                        "mirrorleftup.jpg"};
                piece2 = createPiece(piece2Names, 897, 88, true, true, false);
                pieces.add(piece2);

                String[] piece3Names = {"mirror_rotate.png","mirrorrightup.jpg", "mirror.jpg", "mirrorleftdown.jpg",
                        "mirrorleftup.jpg"};
                piece3 = createPiece(piece3Names, 897, 200, true, true, false);
                pieces.add(piece3);

                String[] piece4Names = {"mirror_rotate.png","mirrorrightup.jpg", "mirror.jpg", "mirrorleftdown.jpg",
                        "mirrorleftup.jpg"};
                piece4 = createPiece(piece4Names, 897, 315, true, true, false);
                pieces.add(piece4);

                String[] piece5Names = {"chkpt.png"};
                piece5 = createPiece(piece5Names, 532, 655, false, false, false);
                piece5.setType("checkpoint");
                pieces.add(piece5);

                String[] piece6Names = {"target_rotate.png",  "target_down.png", "target_left.png",
                        "target_up.png", "target_right.png"};
                piece6 = createPiece(piece6Names, 112, 374, true, false, false);
                pieces.add(piece6);
                ////////////////////////////////////////////////////////////////////////////////

                startX = laser.getX() + 58;
                startY = laser.getY() + 58;
                startingPiece = laser;
            }
        }
        catch(Exception e){
            System.err.println("Error with popup.");
        }
    }

    /**
     * creates a new board image 
     * 
     * @param images, the string array of images 
     * @param x, the x coordinate
     * @param y, the y coordinate
     * @param rotatable, true if the piece is rotatable
     *                   false if the piece is not rotatable
     * @param movable, true if the piece is movable
     *                 false if the piece is not movable
     * @param target, true if ending piece
     *                false if not
     * @return the new board image
     */
    public BoardImage createPiece(String[] images, int x,int y,
    boolean rotatable, boolean movable, boolean target){
        ArrayList<BufferedImage> temp = new ArrayList<>();
        for(String s: images){
            try{
                temp.add(ImageIO.read(new File(s)));
            }
            catch(IOException e){
                System.err.println("Error reading in file " + s);
            }
        }
        return new BoardImage(temp, x, y, rotatable, movable, target);
    }

    /**
     * invoked when the mouse the mouse enters a component
     * 
     * @param e, the mouse event that happens
     */
    @Override
    public void mouseEntered( MouseEvent e ) { }

    /**
     * invoked when the mouse exits a component
     * 
     * @param e, the mouse event that happens
     */
    @Override
    public void mouseExited( MouseEvent e ) { }

    /**
     * method to carry out if the mouse is clicked
     * 
     * @param e, the mouse event that happens
     */
    @Override
    public void mouseClicked( MouseEvent e ) {
        System.out.println(e.getX() + " " + e.getY());
        //if the mouse click is a left click
        if(e.getButton() == MouseEvent.BUTTON1){
            int x = e.getX();
            int y = e.getY();

            if(x >= 900 && x <= 1000 && y <= 825 && y >= 720){
                laserDirection = laser.currentImage;
                boolean imagesSetAndOnBoard = true;
                for(BoardImage b: pieces){
                    if(b.images.size() > 4 || b.getY() > 800 || b.getY() < 90
                    || b.getX() < 100 || b.getX() > 800) imagesSetAndOnBoard = false;

                }
                laserFired = imagesSetAndOnBoard;
                repaint();
            }
            else if(x >= 881 && x <= 1032 && y <= 657 && y >= 623)
            {
                // reset(displayMessage(true));
                System.err.println("Reset Button");
            }
            else if(x >= 931 && x <= 979 && y <= 701 && y >= 667)
            {
                String[] choices = {"How to play", "Pieces"};
                String which = (String) JOptionPane.showInputDialog(null,
                        "Choose which instruction","Info", 
                        JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

                if(which.equals("How to play"))
                {
                    String[] choices2 = {"Left Click: Changes piece direction", "Right Click: Move a piece by first clicking the piece", "\t and then click on any open space"
                        , "A piece with a ? is a non active piece, left click on it to activate", "How to lose: If the laser goes off the side of the game board you lose."
                        , "\t The game will not work untill you reset the board by clicking Restart", "How to win: Your object is to hit the target while going through all checkpoints"};
                    JOptionPane.showMessageDialog(null, choices2, "Directions", 1);                    
                }
                else if(which.equals("Pieces"))
                {
                    info();
                }
            }
            else if(!laserFired){

                for(BoardImage b : pieces){
                    // 105, 655 
                    if(x >= b.getX() && x <= b.getX() + 115){
                        if(y >= b.getY()  && y <= b.getY() + 115){
                            if(b.isRotatable()){
                                b.rotateImage();
                                repaint();

                            }
                        }
                    }

                }
            }
        }

        //if the mouse click is a right click
        if(e.getButton() == MouseEvent.BUTTON3 && !laserFired){
            if(movingPiece){
                int xCord = e.getX();
                int yCord = e.getY();

                int space = whatSpace(xCord, yCord);
                int setX = spaceX(space);
                int setY = spaceY(space);
                boolean makeMove = true;
                for(BoardImage b : pieces){
                    if(b.getX() == setX && b.getY() == setY)
                    {   
                        makeMove = false;

                    }
                    //b.selectedPiece = false;
                }
                if(makeMove){
                    pieceToMove.setX(setX);
                    pieceToMove.setY(setY);

                }
                repaint();
                movingPiece = false;
                // System.out.println(xCord + " " + yCord);
                // System.out.println(setX + " " + setY);
            }

            else if(!movingPiece){

                for(BoardImage b : pieces){
                    // 105, 655 
                    int x = e.getX();
                    int y = e.getY();
                    if(x >= b.getX() && x <= b.getX() + 115){
                        if(y >= b.getY()  && y <= b.getY() + 115){
                            if(b.isMovable()){
                                b.selectedPiece = true;
                                pieceToMove = b;
                                movingPiece = true;
                                repaint();
                            }
                        }
                    }

                }
            }
        }
        e.consume();
    }

    /**
     * Invoked when the mouse buttons has been pressed on a component
     * 
     * @param e, the mouse event that happens
     */
    @Override
    public void mousePressed( MouseEvent e ) { }

    /**
     * Invoked when a mouse button has been released on a component
     *
     * @param e, the mouse event that happens
     */
    @Override
    public void mouseReleased( MouseEvent e ) { 

    }

    /**
     * Invoked when a mouse cursor has been moved onto a component but no buttons have been pushed.
     *
     * @param e, the mouse event that happens
     */
    @Override
    public void mouseMoved( MouseEvent e ) { }

    /**
     * Invoked when a mouse button is pressed on a component and then dragged.
     *
     * @param e, the mouse event that happens
     */
    @Override
    public void mouseDragged( MouseEvent e ) { }

    /**
     * creates the graphics on the screen 
     * 
     * @param g, the graphic
     */
    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //draw background
        g.drawImage(boardPicture.getImage(),boardPicture.getX(),boardPicture.getY(),this);
        //draw all pieces
        for(BoardImage p : pieces){
            g.drawImage(p.getImage(), p.getX(), p.getY(), this);
            if(p.selectedPiece == true)
            {
                //g.fillRect(0,0,400,400);
                g.drawImage(selected, p.getX()-2,p.getY()-2,this);
            }
            p.selectedPiece = false;
        }
        g.setColor(Color.RED);
        //draw any lasers

        for(int i =0; i < lineStartX.size(); i++){
            if(lineEndX.get(i) <= 790 && lineEndY.get(i) >= 92){

                g.drawLine(lineStartX.get(i), lineStartY.get(i), lineEndX.get(i), lineEndY.get(i));
            }

        }

        if(laserFired && !gameOver){
            int direction = startingPiece.getDirection();
            if(!firstLineDrawn){
                if(choice.equals("Intermediate") && startingPiece == laser){
                    direction = 2;
                    firstLineDrawn = true;
                }
            }
            // image is not a question mark
            if(laser.images.size() > 4){
                laserFired = false;
            }
            else{
                int nextX = startingPiece.getX();
                int nextY = startingPiece.getY();
                boolean foundPiece = false;
                BoardImage closest;
                if(laserFired){
                    // image is not a question mark
                    if(laser.images.size() > 4){
                        laserFired = false;
                    }
                    else{
                        if(choice.equals("Intermediate") && previousDirection == 0 && 
                        direction == 2) direction = 1;
                        if(choice.equals("Intermediate") && previousDirection == 1 && 
                        direction == 3) direction = 2;
                        if(choice.equals("Intermediate") && previousDirection == 2 &&
                        direction == 0) direction = 3;
                        boolean xDirection = false;
                        if(direction == 0 || direction == 2) xDirection = true;
                        closest = closestPiece(xDirection, direction);
                        if(closest != null){
                            foundPiece = true;
                            if(direction == 0){ //draw right
                                while(nextX < closest.getX()){
                                    nextX += 1;
                                    //g.drawLine(startX, startY, nextX, nextY);
                                    if(nextX == closest.getX()){
                                        lineStartX.add(startingPiece.getX() + 58);
                                        lineStartY.add(startingPiece.getY() + 58);

                                        lineEndX.add(nextX + 58);
                                        lineEndY.add(nextY + 58);

                                        closest.hit();
                                        startingPiece = closest;
                                        previousDirection = direction;

                                    }
                                }
                                if(!foundPiece){
                                    g.drawLine(startingPiece.getX()+58, startingPiece.getY()
                                        +58, 850, nextY+58);
                                    gameOver = true; // because drew off board
                                }   
                                if(checkGameStatus()){
                                    gameOver = true;
                                    gameWon = true;

                                }
                            }

                            if(direction == 1){ // draw down

                                foundPiece = true;
                                while(nextY < closest.getY()){
                                    nextY += 1;
                                    //g.drawLine(startX, startY, nextX, nextY);
                                    if(nextY == closest.getY()){
                                        lineStartX.add(startingPiece.getX()+ 58);
                                        lineStartY.add(startingPiece.getY() + 58);

                                        lineEndX.add(nextX + 58);
                                        lineEndY.add(nextY + 58);
                                        // System.out.print(lineStartX.size());
                                        startingPiece = closest;
                                        closest.hit();
                                        previousDirection = direction;

                                    }
                                }

                                if(!foundPiece){
                                    g.drawLine(startingPiece.getX() +58, startingPiece.getY()
                                        +58, nextX+58, 850);
                                    gameOver = true; // because drew off board
                                }   
                                if(checkGameStatus()){
                                    gameOver = true;
                                    gameWon = true;

                                }  

                            }
                            // if(!foundPiece){
                            // g.drawLine(startX, startY, 850, nextY);
                            // }   
                            // }
                            if(direction == 2){ // draw left

                                foundPiece = true;
                                while(nextX > closest.getX()){
                                    nextX -= 1;
                                    //hitting the checkbox
                                    if(piece6 != null){
                                        if(nextX == piece6.getX()) piece6.hit();
                                    }

                                    //g.drawLine(startX, startY, nextX, nextY);
                                    if(nextX == closest.getX()){
                                        lineStartX.add(startingPiece.getX()+ 58);
                                        lineStartY.add(startingPiece.getY() + 58);

                                        lineEndX.add(nextX + 58);
                                        lineEndY.add(nextY + 58);
                                        // System.out.print(lineStartX.size());
                                        startingPiece = closest;
                                        closest.hit();
                                        previousDirection = direction;

                                    }
                                }

                                if(!foundPiece){
                                    g.drawLine(startingPiece.getX(), startingPiece.getY()
                                        +58, 50, nextY+58);
                                    gameOver = true; // because drew off board
                                }   
                                if(checkGameStatus()){
                                    gameOver = true;
                                    gameWon = true;

                                }  

                            }
                            // if(!foundPiece){
                            // g.drawLine(startX, startY, 850, nextY);
                            // }   
                            // }

                            else if(direction == 3){ // draw up

                                foundPiece = true;
                                while(nextY > closest.getY()){
                                    nextY -= 1;
                                    //g.drawLine(startX, startY, nextX, nextY);
                                    if(nextY == closest.getY()){
                                        lineStartX.add(startingPiece.getX()+ 58);
                                        lineStartY.add(startingPiece.getY() + 58);

                                        lineEndX.add(nextX + 58);
                                        lineEndY.add(nextY + 58);

                                        startingPiece = closest;
                                        closest.hit();
                                        previousDirection = direction;
                                        break;

                                    }
                                }
                                if(!foundPiece){
                                    g.drawLine(startingPiece.getX() +58, startingPiece.getY()
                                        +58, nextX+58, 0);
                                    gameOver = true; // because drew off board
                                }   
                                if(checkGameStatus()){
                                    gameOver = true;
                                    gameWon = true;

                                }  

                            }

                        }                       
                        repaint();

                    }

                }
            }
        }
        else if(laserFired && gameOver && !gameWon){
            String win = new String("You Lost!");
            g.drawImage(winLoss, 0, 0, null);
            g.drawImage(winLoss, 860, -248, null);
            g.drawImage(winLoss, 860, 660, null);
            g.drawImage(winLossLeft, 860, 622, null);
            g.drawImage(winLossRight, 1020, 622, null);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
            g.drawString(win,400,400);
        }
        else if(gameWon){
            String win = new String("You Won!");
            g.drawImage(winLoss, 0, 0, null);
            g.drawImage(winLoss, 860, -248, null);
            g.drawImage(winLoss, 860, 660, null);
            g.drawImage(winLossLeft, 860, 622, null);
            g.drawImage(winLossRight, 1020, 622, null);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
            g.drawString(win,400,400);

        }

    }

    public String displayMessage(boolean won){
        // JOptionPane.showMessageDialog(null, "alert", "alert",
        // JOptionPane.ERROR_MESSAGE);
        //choose game board
        String[] choices = {"Beginner", "Intermediate"};
        //produce dialog box to choose board
        choice = (String) JOptionPane.showInputDialog(null, "Choose difficulty",
            "Laser Maze", JOptionPane.QUESTION_MESSAGE, null,
            choices, choices[0]);
        return choice;
    }

    /**
     * Finds and returns the closest BoardImage
     * 
     * @param boolean xOrY  true if searching x direction, false if y
     * @param int     direction     which direction the piece we are 
     *                              drawing from is facing
     * 
     * @return BoardImage   closest on board in specified direction
     * 
     */
    public BoardImage closestPiece(boolean xOrY, int direction){
        BoardImage closest = null;
        int closestDifference = Integer.MAX_VALUE;

        if(xOrY){
            for(BoardImage b: pieces){
                if(!b.getType().equals("checkpoint")){
                    if(b.getY() == startingPiece.getY()){
                        if(direction == 0){
                            if(b.getX() > startingPiece.getX()){
                                if(b.getX() - startingPiece.getX() < closestDifference)
                                {
                                    closest = b;
                                }
                            }
                        }
                        else if(direction == 2){
                            if(b.getX() < startingPiece.getX()){
                                if(startingPiece.getX() -b.getX() < closestDifference)
                                {
                                    closest = b;
                                }
                            }

                        }
                    }
                }
            }
        }

        else{
            for(BoardImage b: pieces){
                if(!b.getType().equals("checkpoint")){
                    if(b.getX() == startingPiece.getX()){
                        if(direction == 1){
                            if(b.getY() > startingPiece.getY()){
                                if(b.getY() - startingPiece.getY() < closestDifference)
                                {
                                    closest = b;
                                }
                            }
                        }
                        else if(direction == 3){
                            if(b.getY() < startingPiece.getY()){
                                if(startingPiece.getY() -b.getY() < closestDifference)
                                {
                                    closest = b;
                                }
                            }

                        }
                    }
                }
            }
        }

        return closest;
    }

    /**
     * returns true if every piece has been hit, false otherwise
     * 
     * @return true if each piece is hit. false otherwise
     */
    public boolean checkGameStatus(){
        for(BoardImage b : pieces){
            if(b.getHit() == false) return false;
        }
        return true;
    }

    /**
     * Creates the graphics of the board
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Laser Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new BoardPanel();
        frame.setResizable(false);

        frame.getContentPane().add(panel);
        //Display the window.
        frame.pack();
        frame.setVisible(true);

    }

    /**
     * main method to call upon the graphics method and 
     * create the board and the graphics
     */
    public static void main(String[] args) {
        //choose game board
        String[] choices = {"Beginner", "Intermediate"};
        //produce dialog box to choose board
        choice = (String) JOptionPane.showInputDialog(null, "Choose difficulty",
            "Laser Maze", JOptionPane.QUESTION_MESSAGE, null,
            choices, choices[0]);

        Button fire = new Button("fire");
        fire.setLocation(900, 720);
        fire.setSize(100, 100);
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });

    }

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

    public void reset(String choice)
    {
        if(choice.equals("Beginner")){
            String[] laserNames = {"laserstart.png", "laser0.jpg",
                    "laser1.jpg", "laser2.jpg", "laser3.jpg"};
            laser = createPiece(laserNames, 112, 655, true, false, false);
            laser.hit(); // don't need to hit the laser
            pieces.add(laser);

            String[] piece1Names = {"target_no_rotate.png"};
            piece1 = createPiece(piece1Names, 112, 514, false, false, false);
            pieces.add(piece1);

            String[] piece2Names = {"target_end.png"};
            piece2 = createPiece(piece2Names, 672, 92, false, false, true);
            pieces.add(piece2);

            String[] piece3Names = {"mirror_rotate.png","mirrorrightup.jpg", "mirror.jpg", "mirrorleftdown.jpg",
                    "mirrorleftup.jpg"};

            piece3 = createPiece(piece3Names, 897, 88, true, true, false);
            pieces.add(piece3);

            startX = laser.getX() + 58;
            startY = laser.getY() + 58;
            startingPiece = laser;
        }
        else if(choice.equals("Intermediate")){
            //672,92 112,655
            //
            ///////////// side pieces //////////////////////
            String[] piece2Names = {"mirror_rotate.png","mirrorrightup.jpg", "mirror.jpg", "mirrorleftdown.jpg",
                    "mirrorleftup.jpg"};
            piece2 = createPiece(piece2Names, 897, 88, true, true, false);
            pieces.add(piece2);

            String[] piece3Names = {"mirror_rotate.png","mirrorrightup.jpg", "mirror.jpg", "mirrorleftdown.jpg",
                    "mirrorleftup.jpg"};
            piece3 = createPiece(piece3Names, 897, 200, true, true, false);
            pieces.add(piece3);

            String[] piece4Names = {"mirror_rotate.png","mirrorrightup.jpg", "mirror.jpg", "mirrorleftdown.jpg",
                    "mirrorleftup.jpg"};
            piece4 = createPiece(piece4Names, 897, 315, true, true, false);
            pieces.add(piece4);

            String[] piece5Names = {"chkpt.png"};
            piece5 = createPiece(piece5Names, 532, 655, false, false, false);
            piece5.setType("checkpoint");
            pieces.add(piece5);

            String[] piece6Names = {"target_rotate.png",  "target_down.png", "target_left.png",
                    "target_up.png", "target_right.png"};
            piece6 = createPiece(piece6Names, 112, 374, true, false, false);
            pieces.add(piece6);
            ////////////////////////////////////////////////////////////////////////////////

            startX = laser.getX() + 58;
            startY = laser.getY() + 58;
            startingPiece = laser;
        }

    }

    public static void info()
    {
        JPanel mirrorInfo = new JPanel();
        JPanel targetInfo = new JPanel();
        JPanel laserInfo = new JPanel();
        JPanel questionInfo = new JPanel();
        JPanel checkPointInfo = new JPanel();

        JLabel mirrorPic = new JLabel(new ImageIcon("mirror.jpg"));
        JLabel targetPic = new JLabel(new ImageIcon("target_down.png"));
        JLabel laserPic = new JLabel(new ImageIcon("laser0.jpg"));
        JLabel QuestionPic1 = new JLabel(new ImageIcon("laserstart.png"));
        JLabel QuestionPic2 = new JLabel(new ImageIcon("mirror_rotate.png"));
        JLabel checkpointPic = new JLabel(new ImageIcon("checkpoint.jpg"));

        JTextArea mirrorLabel = new JTextArea("This is the mirror, it will reflect\nlasers"
                + " at 90 degress",2,20);
        JTextArea targetLabel = new JTextArea("This is like a mirror, in that one side "
                + "reflects.\nThe difference is that the laser needs\n" 
                + "to hit the target to win",3,20);
        JTextArea laserLabel = new JTextArea("Depending on where the arrow is,\n"
                + "the laser will fire from that side.",2,20);
        JTextArea questionLabel = new JTextArea("Before a piece is active, it looks like\n"
                + "one of these. Left clicking on it will change\nit to a working piece",3,25);
        JTextArea checkPointLabel = new JTextArea("This is a checkpoint.\n"
                + "You need to get through this to win!\nThe laser cannot go into the side\n to work"
            ,3,20);
        JTabbedPane tabs = new JTabbedPane();

        mirrorInfo.add(mirrorPic);
        targetInfo.add(targetPic);
        laserInfo.add(laserPic);    
        questionInfo.add(QuestionPic1);
        questionInfo.add(QuestionPic2);
        checkPointInfo.add(checkpointPic);

        mirrorInfo.add(mirrorLabel);
        targetInfo.add(targetLabel);
        laserInfo.add(laserLabel);    
        questionInfo.add(questionLabel);
        checkPointInfo.add(checkPointLabel);

        tabs.add("Mirrors",mirrorInfo);
        tabs.add("Targets",targetInfo);
        tabs.add("lasers",laserInfo);
        tabs.add("Questions Marks",questionInfo);
        tabs.add("Checkpoints",checkPointInfo);            
        JFrame tp= new JFrame();
        tp.add(tabs);
        tp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tp.setSize(500, 500);
        tp.setVisible(true);
        tp.setResizable(false);

    }

}