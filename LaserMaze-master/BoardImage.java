import javax.swing.*;
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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
/**
 * Write a description of class BoardImage here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
abstract class BoardImage
{
    protected BufferedImage image;
    protected int x;
    protected int y;
    public BoardImage(BufferedImage image, int x, int y){
        this.image = image;
        this.x = x;
        this.y = y;
        
    }
    public abstract void setX(int x);
    
    public abstract void setY(int y);
    
    public abstract int getX();
    
    public abstract int getY();
    
}
