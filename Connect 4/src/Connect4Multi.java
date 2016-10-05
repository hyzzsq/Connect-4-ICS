import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//importing various java utilities

/*
 * Name: Jimmy Xu and Philip Huang
 * Date: December 2, 2015
 * Description: A connect 4 application in which two human players play against each other
 */

public class Connect4Multi extends JFrame implements ActionListener {
	// number of rows, columns and the delay time of the dropping animation
	public static final int row = 6, column = 7, delaytime = 75;
	// declaring the various GUI components
	public static JFrame frame = new JFrame();
	public static JLabel labelgrid[][] = new JLabel[row][column];
	public static JLabel p1score = new JLabel(), p2score = new JLabel();
	public static JButton inputButton[] = new JButton[column];
	public static JLabel message = new JLabel("Welcome to Connect 4!");
	public static JButton surrenderButton = new JButton("Surrender?");
	public static JButton colourchange = new JButton("Change Colour Theme");
	public static JPanel boardPanel = new JPanel();
	public static JPanel buttonPanel = new JPanel();
	public static ImageIcon empty, p1, p2, empty1, red1, yellow1, empty2, red2,
			yellow2; // images of the pieces for various states of the board

	public static boolean playeroneturn;// keeps track of whose turn it is
	// either a 0,1 or 2 for empty, red or yellow
	public static int gridstate[][] = new int[row][column];
	// keeps track of the height of each column
	public static int gridheight[] = new int[column];
	public static String name1, name2; // the 2 player's names
	// keeps track of player scores, current colour theme, number of full
	// columns, and position that a piece is dropped
	public static int score1 = 0, score2 = 0, currentcolour = 0, full = 0,
			curr, curc;

	public Connect4Multi() {
		// board images
		empty = new ImageIcon("empty.jpg");
		p1 = new ImageIcon("red.jpg");
		p2 = new ImageIcon("yellow.jpg");
		empty1 = new ImageIcon("empty1.jpg");
		red1 = new ImageIcon("red1.jpg");
		yellow1 = new ImageIcon("yellow1.jpg");
		empty2 = new ImageIcon("empty2.jpg");
		red2 = new ImageIcon("red2.jpg");
		yellow2 = new ImageIcon("yellow2.jpg");
		// creating the GUI
		frame.add(boardPanel);
		frame.add(buttonPanel);
		frame.setLayout(new FlowLayout());
		boardPanel.setLayout(new GridLayout(row + 1, column));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		// labels that display each player's score
		p1score.setText(name(1) + ": " + score1);
		p2score.setText(name(2) + ": " + score2);
		for (int i = 0; i < column; i++) {
			// creating the row of buttons
			inputButton[i] = new JButton(i + "");
			boardPanel.add(inputButton[i]);
			inputButton[i].addActionListener(this);
			inputButton[i].setEnabled(true);
		}
		for (int i = row - 1; i >= 0; i--) {
			for (int j = 0; j < column; j++) {
				// creating 2d array of labels for board spaces
				labelgrid[i][j] = new JLabel();
				labelgrid[i][j].setIcon(empty);
				boardPanel.add(labelgrid[i][j]);
			}
		}
		// adding components to frame and panels
		buttonPanel.add(surrenderButton);
		surrenderButton.setEnabled(false);
		buttonPanel.add(colourchange);
		buttonPanel.add(message);
		buttonPanel.add(p1score);
		buttonPanel.add(p2score);
		// setting the message and score fonts and colours
		message.setFont(new Font("Arial Black", Font.PLAIN, 20));
		message.setForeground(Color.BLACK);
		p1score.setFont(new Font("Arial Black", Font.PLAIN, 20));
		p1score.setForeground(Color.RED);
		p2score.setFont(new Font("Arial Black", Font.PLAIN, 20));
		p2score.setForeground(Color.YELLOW);
		// adding actionlisteners to the buttons
		surrenderButton.addActionListener(this);
		colourchange.addActionListener(this);
		// frame properties
		setTitle("Connect 4 Multiplayer");
		frame.setSize(1100, 850);
		frame.setTitle("Connect 4 Multiplayer");
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public static int check(int[][] checkBoard, int x, int y) {
		/*
		 * check if the the current player wins or not x and y are the row and
		 * column of current move
		 */
		boolean win = true;
		int temp = checkBoard[x][y];
		// temp stands for the current player

		// if in a row , there is a connected 4
		for (int i = x - 3; i <= x; i++) {
			if (i < 0)// make sure not out of bound
				continue;
			win = true;
			// check the next 4
			for (int j = i; j <= i + 3; j++) {
				if (j >= row) {// make sure not out of bound
					win = false;
					break;
				}
				if (checkBoard[j][y] != temp)
					win = false;// if the value is from temp, set win to false
			}
			if (win)
				return temp;
			// return temp if win is true
		}

		// if in a column , there is a connected 4
		for (int i = y - 3; i <= y; i++) {
			if (i < 0)
				continue;
			win = true;
			for (int j = i; j <= i + 3; j++) { // check the next 4
				if (j >= column) {// make sure not out of bound
					win = false;
					break;
				}
				if (checkBoard[x][j] != temp)
					win = false;// if the value is from temp, set win to false
			}
			if (win)
				return temp;
			// return temp if win is true
		}

		// if in a rising diagonal , there is a connected 4
		for (int i = -3; i <= 0; i++) {
			if ((x + i) < 0)// make sure not out of bound
				continue;
			if ((y + i) < 0)// make sure not out of bound
				continue;
			win = true;
			for (int j = i; j <= i + 3; j++) { // check the next 4
				if ((x + j) >= row || (y + j) >= column) {// make sure not out
															// of bound
					win = false;
					break;
				}
				if (checkBoard[x + j][y + j] != temp)
					win = false;// if the value is from temp, set win to false
			}
			if (win)
				return temp;
			// return temp if win is true
		}

		// if in a falling diagonal , there is a connected 4

		for (int i = -3; i <= 0; i++) {
			if ((x + i) < 0)// make sure not out of bound
				continue;
			if ((y - i) >= column)// make sure not out of bound
				continue;
			win = true;
			for (int j = i; j <= i + 3; j++) {// check the next 4
				if ((x + j) >= row || (y - j) < 0) {// make sure not out of
													// bound
					win = false;
					break;
				}
				if (checkBoard[x + j][y - j] != temp)
					win = false;// if the value is from temp, set win to false
			}
			if (win)
				return temp;
			// return temp if win is true
		}
		return 0;
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Surrender?")) {
			// confirms if the player wants to concede
			int reply;// stores the user input from the JOptionPane
			if (playeroneturn) {
				reply = JOptionPane.showConfirmDialog(null, name(1)
						+ ", do you wish to concede?", name(1) + " Give Up?",
						JOptionPane.YES_NO_OPTION);
			} else {
				reply = JOptionPane.showConfirmDialog(null, name(2)
						+ ", do you wish to concede?", name(2) + " Give Up?",
						JOptionPane.YES_NO_OPTION);
			}
			if (reply == JOptionPane.YES_OPTION) {
				// adds one to the opponent's score and resets the board
				if (playeroneturn) {
					message.setText(name(1) + " has surrendered!");
					p2score.setText(name(2) + ": " + ++score2);
				} else {
					message.setText(name(2) + " has surrendered!");
					p1score.setText(name(1) + ": " + ++score1);
				}
				reset();
			}
		} else if (command.equals("Change Colour Theme")) {
			// changes the colour of the board and pieces
			changecolour();
		} else {
			// this happens when any of the numbered buttons are clicked
			surrenderButton.setEnabled(true);
			int num = Integer.parseInt(command);// identifies which button was
												// clicked
			curr = gridheight[num];// the row that the piece will fall to
			curc = num;// the column clicked
			if (playeroneturn) {
				// delaythread to animate piece drop
				Thread delaythread = new Thread() {
					public void run() {
						drop(1);
						try {
							// disable buttons, prevent interrupting thread
							for (int i = 0; i < column; i++)
								inputButton[i].setEnabled(false);
							Thread.sleep(delaytime
									* (column - gridheight[curc]));
							// reenable buttons
							for (int i = 0; i < column; i++)
								// unless the column is already full
								if (gridheight[i] != row)
									inputButton[i].setEnabled(true);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				delaythread.start();
				gridstate[gridheight[num]][num] = 1;
				if (check(gridstate, gridheight[num], num) > 0) {
					// if player 1 wins, display message, change score, reset
					message.setText(name(1) + " wins!");
					JOptionPane.showMessageDialog(null, name(1) + " wins!",
							"Victory", JOptionPane.INFORMATION_MESSAGE);
					p1score.setText(name(1) + ": " + ++score1);
					reset();
					return;
				}
				// if player 1 did not win, change the message
				message.setText(name(2) + "'s turn");
			} else {
				Thread delaythread = new Thread() {
					// delaythread to animate falling piece
					public void run() {
						drop(2);
						try {
							// disable buttons to prevent interruption
							for (int i = 0; i < column; i++)
								inputButton[i].setEnabled(false);
							Thread.sleep(delaytime
									* (column - gridheight[curc]));
							// re-enable buttons after animation
							for (int i = 0; i < column; i++)
								if (gridheight[i] != row)
									inputButton[i].setEnabled(true);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				delaythread.start();
				gridstate[gridheight[num]][num] = 2;
				if (check(gridstate, gridheight[num], num) > 0) {
					// if player 2 wins, display message, change score, reset
					message.setText(name(2) + " wins!");
					JOptionPane.showMessageDialog(null, name(2) + " wins!",
							"Victory", JOptionPane.INFORMATION_MESSAGE);
					p2score.setText(name(2) + ": " + ++score2);
					reset();
					return;
				}
				// if player 2 did not win, change message
				message.setText(name(1) + "'s turn");
			}
			gridheight[num]++;
			if (gridheight[num] >= row) {
				// if a column is full, disable the button
				inputButton[num].setEnabled(false);
				full++;
				if (full == row) {
					// if all columns are full, it's a tie
					JOptionPane.showMessageDialog(null, "It's a tie!", "Tie!",
							JOptionPane.INFORMATION_MESSAGE);
					reset();
					return;
				}
			}
			playeroneturn = !playeroneturn; // change turns
		}
	}

	public static void drop(int player) {
		// method to animate the pieces
		final int p = player;// keeps track of which player is going
		Thread droppiece = new Thread() {
			public void run() {
				for (int i = 5; i > curr; i--) {// from the top to the lowest
												// unoccupied space
					if (p == 1) {// player 1
						// change the colour, delay for delaytime ms, then
						// change it back to empty
						if (currentcolour == 0) {
							labelgrid[i][curc].setIcon(p1);
							try {
								Thread.sleep(delaytime);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							labelgrid[i][curc].setIcon(empty);
						} else if (currentcolour == 1) {
							labelgrid[i][curc].setIcon(red1);
							try {
								Thread.sleep(delaytime);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							labelgrid[i][curc].setIcon(empty1);
						} else {
							labelgrid[i][curc].setIcon(red2);
							try {
								Thread.sleep(delaytime);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							labelgrid[i][curc].setIcon(empty2);
						}
					} else {// player 2
						// change the colour, delay for delaytime ms, then
						// change it back to empty
						if (currentcolour == 0) {
							labelgrid[i][curc].setIcon(p2);
							try {
								Thread.sleep(delaytime);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							labelgrid[i][curc].setIcon(empty);
						} else if (currentcolour == 1) {
							labelgrid[i][curc].setIcon(yellow1);
							try {
								Thread.sleep(delaytime);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							labelgrid[i][curc].setIcon(empty1);
						} else {
							labelgrid[i][curc].setIcon(yellow2);
							try {
								Thread.sleep(delaytime);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							labelgrid[i][curc].setIcon(empty2);
						}
					}
				}
				// change the colour back to solid at the end
				if (p == 1) {
					if (currentcolour == 0)
						labelgrid[curr][curc].setIcon(p1);
					else if (currentcolour == 1)
						labelgrid[curr][curc].setIcon(red1);
					else
						labelgrid[curr][curc].setIcon(red2);
				} else {
					if (currentcolour == 0)
						labelgrid[curr][curc].setIcon(p2);
					else if (currentcolour == 1)
						labelgrid[curr][curc].setIcon(yellow1);
					else
						labelgrid[curr][curc].setIcon(yellow2);
				}
			}
		};
		droppiece.start();
	}

	public static String name(int player) {
		// this method simplifies name statements in the main code
		// don't need to code 2 ifs every time when using a name
		if (player == 1) {
			if (name1.equals("")) {
				return "Player 1";
			} else {
				return name1;
			}
		} else if (player == 2) {
			if (name2.equals("")) {
				return "Player 2";
			} else {
				return name2;
			}
		}
		return "";
	}

	public static void changecolour() {
		// this method goes through the board with 2 for loops and toggles the
		// colour of the pieces
		if (currentcolour == 2) {
			currentcolour = 0;
		} else {
			currentcolour++;
		}
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				if (gridstate[i][j] == 0)
					if (currentcolour == 0)
						labelgrid[i][j].setIcon(empty);
					else if (currentcolour == 1)
						labelgrid[i][j].setIcon(empty1);
					else
						labelgrid[i][j].setIcon(empty2);
				else if (gridstate[i][j] == 1)
					if (currentcolour == 0)
						labelgrid[i][j].setIcon(p1);
					else if (currentcolour == 1)
						labelgrid[i][j].setIcon(red1);
					else
						labelgrid[i][j].setIcon(red2);
				else if (currentcolour == 0)
					labelgrid[i][j].setIcon(p2);
				else if (currentcolour == 1)
					labelgrid[i][j].setIcon(yellow1);
				else
					labelgrid[i][j].setIcon(yellow2);
			}
		}
		// this changes the message colour
		if (currentcolour == 0) {
			p1score.setForeground(Color.RED);
			p2score.setForeground(Color.YELLOW);
		} else if (currentcolour == 1) {
			p1score.setForeground(Color.MAGENTA);
			p2score.setForeground(Color.ORANGE);
		} else {
			p1score.setForeground(Color.BLUE);
			p2score.setForeground(Color.YELLOW);
		}
	}

	public static void reset() {
		// this clears the board
		full = 0;
		for (int i = 0; i < column; i++) {
			// re-enables all buttons
			gridheight[i] = 0;
			inputButton[i].setEnabled(true);
		}
		for (int i = 0; i < row; i++)
			for (int j = 0; j < column; j++) {
				gridstate[i][j] = 0;
				if (currentcolour == 0)
					labelgrid[i][j].setIcon(empty);
				else if (currentcolour == 1)
					labelgrid[i][j].setIcon(empty1);
				else
					labelgrid[i][j].setIcon(empty2);
			}
		surrenderButton.setEnabled(false);
		whogoesfirst(); // prompts the user to decide which player goes first
	}

	public static void playernames() {
		// get user names
		// this code prevents the user from clicking a cancel option that would
		// otherwise be present
		String[] options = { "OK" };
		JPanel rpan = new JPanel();
		JLabel rlabel = new JLabel("What is player 1's name?");
		JTextField rtext = new JTextField(10);
		rpan.add(rlabel);
		rpan.add(rtext);
		int ok = JOptionPane.showOptionDialog(null, rpan, "Player 1's name?",
				JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
		if (ok == 0) {
			name1 = (rtext.getText());
		}
		rtext.setText(null);
		JPanel cpan = new JPanel();
		JLabel clabel = new JLabel("What is player 2's name?");
		cpan.add(clabel);
		cpan.add(rtext);
		ok = JOptionPane.showOptionDialog(null, cpan, "Player 2's name?",
				JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
		if (ok == 0) {
			name2 = (rtext.getText());
			if (!name1.equals(""))
				if (name2.equals(name1)) {
					// prevents duplicate names
					name2 = name1 + "(2)";
				}
		}
		whogoesfirst();
	}

	public static void whogoesfirst() {
		// simple JOptionPane with 2 options
		int n;
		n = JOptionPane.showConfirmDialog(null, "Is " + name(1)
				+ " going first?", "Whos Goes First?",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			playeroneturn = true;
			message.setText(name(1) + "'s turn");
		} else if (n == JOptionPane.NO_OPTION) {
			playeroneturn = false;
			message.setText(name(2) + "'s turn");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		playernames();
		Connect4Multi a = new Connect4Multi();
	}
}
