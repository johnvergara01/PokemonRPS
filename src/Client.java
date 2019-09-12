import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
/**
 * Client class, creates GUI and sends user's option to server, determines winner and calculates score
 * @author John
 *
 */
public class Client extends JPanel implements ActionListener, Runnable {
	/**
	 * Socket to connect to server
	 */
	private Socket sock;
	
	/**
	 * read in data sent from server
	 */
	private BufferedReader read;
	
	/**
	 * write data to server
	 */
	private PrintStream write;
	
	/**
	 * button for fire
	 */
	private JButton fire;
	
	/**
	 * button for water
	 */
	private JButton water;
	
	/**
	 * button for grass
	 */
	private JButton grass;

	/**
	 * player's score
	 */
	private int playerScore = 0;
	
	/**
	 * computer's score
	 */
	private int compScore = 0;
	
	/**
	 * number of ties
	 */
	private int ties = 0;
	
	/**
	 * player input (button press)
	 */
	private String playerInput;
	
	/**
	 * enemy's input sent from server
	 */
	private String enemyInput;
	
	/**
	 * check if player pressed a button
	 */
	private boolean buttonPressed = false;
	
	/**
	 * image of player's choice
	 */
	private BufferedImage playerImg;
	
	/**
	 * image of computer's choice
	 */
	private BufferedImage enemyImg;
	
	/**
	 * default constructor, connects to server and creates GUI
	 */
	public Client()
	{
		// attempt to connect to server
		try
		{
			sock = new Socket("localhost", 1235);
			read = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			write = new PrintStream(sock.getOutputStream());
			System.out.println("Connected");
		}
		catch(IOException e){}
		
		// create GUI
		setBackground(Color.WHITE);
		setLayout(null);
		fire = new JButton("Fire");
		water = new JButton("Water");
		grass = new JButton("Grass");
		fire.addActionListener(this);
		water.addActionListener(this);
		grass.addActionListener(this);
		ImageIcon fireIcon = new ImageIcon("fire.png");
		ImageIcon waterIcon = new ImageIcon("water.png");
		ImageIcon grassIcon = new ImageIcon("grass.png");
		Image fireImg = fireIcon.getImage();
		Image newFireImg = fireImg.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		fireIcon = new ImageIcon(newFireImg);
		Image waterImg = waterIcon.getImage();
		Image newWaterImg = waterImg.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		waterIcon = new ImageIcon(newWaterImg);
		Image grassImg = grassIcon.getImage();
		Image newGrassImg = grassImg.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		grassIcon = new ImageIcon(newGrassImg);
		fire.setIcon(fireIcon);
		water.setIcon(waterIcon);
		grass.setIcon(grassIcon);
		fire.setBounds(170, 450, 150, 100);
		water.setBounds(325, 450, 150, 100);
		grass.setBounds(480, 450, 150, 100);
		add(fire);
		add(water);
		add(grass);
	}
	
	/**
	 * paints the components on GUI
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.drawString("Player score: " + playerScore, 5, 15);
		g.drawString("Ties: " + ties, 5, 30);
		g.drawString("Computer score: " + compScore, 5, 45);
		try
		{
			// displays image based on user and computer choices
			if(playerInput.equals("Fire"))
			{
				playerImg = ImageIO.read(new File("fire.png"));
			}
			else if(playerInput.equals("Water"))
			{
				playerImg = ImageIO.read(new File("water.png"));
			}
			else
			{
				playerImg = ImageIO.read(new File("grass.png"));
			}
			
			if(enemyInput.equals("Fire"))
			{
				enemyImg = ImageIO.read(new File("fire.png"));
			}
			else if(enemyInput.equals("Water"))
			{
				enemyImg = ImageIO.read(new File("water.png"));
			}
			else
			{
				enemyImg = ImageIO.read(new File("grass.png"));
			}
		}
		catch(Exception e){}
		g.drawImage(playerImg, 50, 150, this);
		g.drawImage(enemyImg, 500, 150, this);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.drawString("Player", 125, 135);
		g.drawString("Computer", 575, 135);
		g.drawString("VS", 375, 200);
	}
	
	/**
	 * plays a sound file
	 * @param file file to be played
	 */
	public static void play(String file)
	{
		try
		{
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(file)));
			clip.start();
		}
		catch(LineUnavailableException e)
		{
			System.out.println("LUE");
		}
		catch(IOException e)
		{
			System.out.println("IOE");
		}
		catch(UnsupportedAudioFileException e)
		{
			System.out.println("UAFE");
		}
	}

	@Override
	/**
	 * sets user's input based on button pressed
	 * @param a checks for actions performed
	 */
	public void actionPerformed(ActionEvent a) {
		if(a.getSource() == fire)
		{
			playerInput = "Fire";
			buttonPressed = true;
		}
		if(a.getSource() == water)
		{
			playerInput = "Water";
			buttonPressed = true;
		}
		if(a.getSource() == grass)
		{
			playerInput = "Grass";
			buttonPressed = true;
		}
	}

	@Override
	/**
	 * main thread for client, send data to server and determines winner of round
	 */
	public void run() {
		try
		{
			while(true)
			{
				repaint();
				// wait for user to press a button
				while(!buttonPressed)
				{
					System.out.println();
				}
				// send data to server
				write.println(playerInput);
				// read data from server
				enemyInput = read.readLine();
				repaint();
				
				// determine winner of round
				if(playerInput.equals(enemyInput))
				{
					ties++;
					//play("tie.wav");
					JOptionPane.showMessageDialog(this, "Tie", "Results", JOptionPane.PLAIN_MESSAGE);
				}
				else if(playerInput.equals("Fire"))
				{
					if(enemyInput.equals("Water"))
					{
						compScore++;
						//play("defeat.wav");
						JOptionPane.showMessageDialog(this, "You lose this round", "Results", JOptionPane.PLAIN_MESSAGE);
					}
					else
					{
						playerScore++;
						play("victory.wav");
						JOptionPane.showMessageDialog(this, "You win this round", "Results", JOptionPane.PLAIN_MESSAGE);
					}
				}
				else if(playerInput.equals("Water"))
				{
					if(enemyInput.equals("Grass"))
					{
						compScore++;
						//play("defeat.wav");
						JOptionPane.showMessageDialog(this, "You lose this round", "Results", JOptionPane.PLAIN_MESSAGE);
					}
					else
					{
						playerScore++;
						play("victory.wav");
						JOptionPane.showMessageDialog(this, "You win this round", "Results", JOptionPane.PLAIN_MESSAGE);
					}
				}
				else if(playerInput.equals("Grass"))
				{
					if(enemyInput.equals("Fire"))
					{
						compScore++;
						//play("defeat.wav");
						JOptionPane.showMessageDialog(this, "You lose this round", "Results", JOptionPane.PLAIN_MESSAGE);
					}
					else
					{
						playerScore++;
						play("victory.wav");
						JOptionPane.showMessageDialog(this, "You win this round", "Results", JOptionPane.PLAIN_MESSAGE);
					}
				}
				buttonPressed = false;
			}
		}
		catch (IOException e){}
	}
}
