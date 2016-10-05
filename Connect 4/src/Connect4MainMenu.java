/* Philip Huang & Jimmy Xu
 * Connect 4 Main Menu
 * December 2nd, 2015
 * Description: Connect 4 game main menu including multiplayer and singleplayer
 */

//imports
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager; 

//-----------main class-------extends JFrame
public class Connect4MainMenu extends JFrame {
	public Connect4MainMenu() {
		/*
		 * Remove window border
		 */
		this.setUndecorated(true);
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		//set size
		this.setSize(400, 300);
		//set layout
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);//set close operation
		this.setVisible(true);//set visibility

		/*
		 * Centering the current window
		 */
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);

		/*
		 * Using the third party GUI look and feel, looks much better than the
		 * Java metal look and feel
		 */ 
		/*
		 * Constructing title bar
		 */
		//declare and initialize title label
		JLabel titleLabel = new JLabel("Connect4");
		//set font
		titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 55));

		/*
		 * Removes the window border
		 */
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setVerticalAlignment(SwingConstants.CENTER);

		this.add(titleLabel, BorderLayout.NORTH);

		JPanel controlPanel = new JPanel();
		//set layout of control panel
		controlPanel.setLayout(new GridLayout(5, 1));

		/*
		 * The buttons on the main panel
		 */
		JButton singlePlayer = new JButton("Single Player");
		JButton localMultiplayer = new JButton("Local Multiplayer"); 
		JButton about = new JButton("About");
		JButton option =new JButton("Option");
		JButton exit = new JButton("Exit");

		/*
		 * set font for the buttons
		 */
		singlePlayer.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		localMultiplayer.setFont(new Font("Times New Roman", Font.PLAIN, 20)); 
		about.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		option.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		exit.setFont(new Font("Times New Roman", Font.PLAIN, 20));

		/*
		 * ActionListeners
		 */

		/*
		 * Single Player, where a player is playing the AI.
		 */
		singlePlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {  
				Connect4Single.main(null); //run main method of connect 4 single player
			}

		});
		/*
		 * Add in local Multiplayer 
		 */
		localMultiplayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) { 
				Connect4Multi.playernames();//ask for 2 player names
				new Connect4Multi();//run the gui of connect 4 multiplayer
			}

		});
 

		/*
		 * action listener for about button
		 */
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// show the author
				String aboutString = "<html><h2>Connect4</h2><b><br>By: Philip Huang, Jimmy Xu</b></html>";
				JOptionPane.showMessageDialog(null, aboutString);
				/*
				 * show the rules of connect 4
				 */
				String rule="                                                           Rule\n\n"
						+ "Each player in his turn drops one of his pieces "
						+ "down any of the slots in the top of the grid. \n\n"
						+ "The play alternates until one of the players "
						+ "gets four checkers of his colour in a row. \n\n"
						+ "The four in a row can be horizontal, vertical,"
						+ "or diagonal. \n\n"
						+ "The first player to get four in a row wins.";
				JOptionPane.showMessageDialog(null, rule);

			}

		});
		/*
		 * action listener for option
		 */
		option.addActionListener(new ActionListener(){
			@Override
			/*
			 * set level of difficulty of AI
			 */
			public void actionPerformed(ActionEvent arg0){
				String difficulty;
				String levels[]={"easy", "medium", "hard"};
				do{
					difficulty = (String) JOptionPane.showInputDialog(null,
							"Diffuculty of AI?", "", JOptionPane.QUESTION_MESSAGE,
						null, levels, "");
				} while(difficulty == null);
				//exit until there is an answer for difficulty
				
				//if difficulty is easy
				if(difficulty.equals("easy"))
				{
					//set search depth to 4
					Connect4Single.searchdepth=4;
				}
				//if difficulty is medium
				else if(difficulty.equals("medium")){
					//set search depth to 6
					Connect4Single.searchdepth=6;
				}
				//if difficulty is hard
				else
				{
					//set search depth to 9
					Connect4Single.searchdepth=9;
				}
			}
		});

		/*
		 * action listener for exit button
		 */
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}

		});

		// add buttons to the control Panel
		controlPanel.add(singlePlayer);
		controlPanel.add(localMultiplayer); 
		controlPanel.add(about);
		controlPanel.add(option);
		controlPanel.add(exit);

		// set layout for main menu
		this.add(controlPanel, BorderLayout.CENTER);

		this.repaint();
		this.revalidate();

	}

	public static void main(String[] args) {
		new Connect4MainMenu();
		// main method
	}

}
