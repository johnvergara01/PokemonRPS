import java.util.*;
import java.awt.*;
import javax.swing.*;
/**
 * Networked Fire Water Grass Project 6, user chooses fire, water, or grass on GUI and server predicts user's move
 * @author John
 *
 */
public class Window extends JFrame{
	public static void main(String[] args) 
	{
		Window w = new Window();
	}
	
	/**
	 * default constructor of window, creates client GUI and starts client thread
	 */
	public Window()
	{
		setBounds(100, 100, 800, 600);
		setTitle("Fire Water Grass");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Client c = new Client();
		setContentPane(c);
		Thread t = new Thread(c);
		setVisible(true);
		setResizable(false);
		t.start();
	}
}
