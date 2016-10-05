/* Philip Huang & Jimmy Xu
 * Connect 4 Player vs AI
 * December 2nd, 2015
 * Description: Connect 4 game player vs AI with minimax algorithm and GUI interface.
 */
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

public class Connect4Single extends JFrame implements ActionListener {
	// declare the variable for row and column
	public static final int row = 6, column = 7;
	// declare variables that record score of player and computer and the
	// colour;
	public static int score1 = 0, score2 = 0, currentcolour = 0;
	// declare the name for player
	public static String name1;
	// declare the variable depth of search
	public static int searchdepth = 9;
	// declare the variable infinity
	public static final int INF = 0x7fffffff;
	// declare the variable that record the move by ai
	public static int move = 0;
	// declare two boolean that record the state
	public static boolean playeroneturn = true, inprogress = false;
	// declare a 2 d array that record the state of the grid
	// the gridstate[row][i] record the number of pieces in that column i.
	public static int gridstate[][] = new int[row + 1][column];
	// the array that record the height of column;
	public static int gridheight[] = new int[column];
	// the label 2-d array that display the pieces
	public static JLabel labelgrid[][] = new JLabel[row][column];
	// the array of input buttons that record the input
	public static JButton inputButton[] = new JButton[column];
	// declare a message label that tells the user instructions
	public static JLabel message = new JLabel("Welcome to Connect 4!");
	// surrender button
	public static JButton surrenderButton = new JButton("Surrender?");
	// colour change button
	public static JButton colourchange = new JButton("Change Colour Theme");
	// score label
	public static JLabel p1score = new JLabel(), p2score = new JLabel();
	// declare and initialize board Panel and button panel
	public static JPanel boardPanel = new JPanel();
	public static JPanel buttonPanel = new JPanel();
	// declare all the image icon
	public static ImageIcon empty, p1, p2, empty1, red1, yellow1, empty2, red2,
			yellow2;

	/*
	 * constructor
	 */
	public Connect4Single() {

		// board images for the three themes and three states(total of 9 images)
		empty = new ImageIcon("empty.jpg");
		p1 = new ImageIcon("red.jpg");
		p2 = new ImageIcon("yellow.jpg");
		empty1 = new ImageIcon("empty1.jpg");
		red1 = new ImageIcon("red1.jpg");
		yellow1 = new ImageIcon("yellow1.jpg");
		empty2 = new ImageIcon("empty2.jpg");
		red2 = new ImageIcon("red2.jpg");
		yellow2 = new ImageIcon("yellow2.jpg");

		// add the panels to frame
		add(boardPanel);
		add(buttonPanel);
		// set layout of frame and panels
		setLayout(new FlowLayout());
		boardPanel.setLayout(new GridLayout(row + 1, column));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		// set scoreboard
		p1score.setText(name() + ": " + score1);
		p2score.setText("Computer: " + score2);

		// initializethe input buttons and add actionlistener
		for (int i = 0; i < column; i++) {
			inputButton[i] = new JButton(i + "");
			boardPanel.add(inputButton[i]);
			inputButton[i].addActionListener(this);
			inputButton[i].setEnabled(true);
		}

		// initialize the label grid 2-d array
		for (int i = row - 1; i >= 0; i--) {
			for (int j = 0; j < column; j++) {
				labelgrid[i][j] = new JLabel();
				labelgrid[i][j].setIcon(empty);
				boardPanel.add(labelgrid[i][j]);
			}
		}
		/*
		 * add all the buttons and labels and scoreboard into buttonpanel
		 */
		buttonPanel.add(surrenderButton);
		surrenderButton.setEnabled(false);
		buttonPanel.add(colourchange);
		buttonPanel.add(message);
		buttonPanel.add(p1score);
		buttonPanel.add(p2score);

		// set font and colour
		message.setFont(new Font("Arial Black", Font.PLAIN, 20));
		p1score.setFont(new Font("Arial Black", Font.PLAIN, 20));
		p1score.setForeground(Color.RED);
		p2score.setFont(new Font("Arial Black", Font.PLAIN, 20));
		p2score.setForeground(Color.YELLOW);

		// add actionlistener
		surrenderButton.addActionListener(this);
		colourchange.addActionListener(this);

		// set title and size
		setTitle("Connect 4");
		setSize(1100, 850);
		setResizable(true);
		setVisible(false);
		// ask for who goes first
		whogoesfirst();

		// then display the gui
		setVisible(true);
	}

	// action performed method
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();// get command

		/*
		 * surrender button is pressed
		 */
		if (command.equals("Surrender?")) {// inform the user
			int reply = JOptionPane.showConfirmDialog(null, name()
					+ ", do you wish to concede?", name() + " Give Up?",
					JOptionPane.YES_NO_OPTION);
			// if it is a confirmed surrender
			if (reply == JOptionPane.YES_OPTION) {
				p2score.setText("Computer: " + ++score2);
				reset();
				// update score and reset
			}
		/*
		* change colour button pressed
		*/
		} else if (command.equals("Change Colour Theme")) {
			changecolour();
			
		} else {/*
				* user engage in a move
				*/
			surrenderButton.setEnabled(true);
			int num = Integer.parseInt(command);
			//get the command
			inprogress = true;
			
			/*
			 * set the colour of grid according the move
			 */
			if (currentcolour == 0)
				labelgrid[gridheight[num]][num].setIcon(p1);
			else if (currentcolour == 1)
				labelgrid[gridheight[num]][num].setIcon(red1);
			else
				labelgrid[gridheight[num]][num].setIcon(red2);
			
			///update gridstate
			gridstate[gridheight[num]][num] = 1;
			/*
			 * check if player1 win
			 */
			if (check(gridstate, gridheight[num], num) > 0) {
				message.setText(name() + " wins!");
				JOptionPane.showMessageDialog(null, name() + " wins!",
						"Victory", JOptionPane.INFORMATION_MESSAGE);
				p1score.setText(name() + ": " + ++score1);
				//if player one does win, pop message, reset and update
				reset();
				return;
				/*
				 * if it is a tie
				 */
			} else if (fullgrid(gridstate)) {
				JOptionPane.showMessageDialog(null, "The game is a tie", "Tie",
						JOptionPane.INFORMATION_MESSAGE);
				reset();
			} else {
				gridheight[num]++;//add gridheight by 1
				if (gridheight[num] >= row)
					inputButton[num].setEnabled(false);
				for (int i = 0; i < column; i++) {
					gridstate[row][i] = gridheight[i];
				}
				
				//call the minimax function
				//-------------------2-d array   x-coor             y-coor
				int moveVal = minimax(gridstate, gridheight[num] - 1, num,
						-INF, INF, searchdepth, false);
				//      max   min   depth     playerturn
				
				//if the computer will lose for sure, make a random move
				if (move == -1) {
					for (int i = 0; i < column; i++)
						if (gridheight[i] < row)
							move = i;
				}
				// change colour for the move the computer make
				if (currentcolour == 0)
					labelgrid[gridheight[move]][move].setIcon(p2);
				else if (currentcolour == 1)
					labelgrid[gridheight[move]][move].setIcon(yellow1);
				else
					labelgrid[gridheight[move]][move].setIcon(yellow2);
				gridstate[gridheight[move]][move] = 2;

				/*
				 * if the computer win, output, update score, and reset.
				 */
				if (check(gridstate, gridheight[move], move) == 2) {
					message.setText("Computer win");
					JOptionPane.showMessageDialog(null, "Computer win",
							"Victory", JOptionPane.INFORMATION_MESSAGE);
					p2score.setText("Computer: " + ++score2);
					reset();
					/*
					 * if it is a tie, output and reset
					 */
				} else if (fullgrid(gridstate)) {
					JOptionPane.showMessageDialog(null, "The game is a tie",
							"Tie", JOptionPane.INFORMATION_MESSAGE);
					reset();
				} else {
					/*
					 * switch to player1's turn
					 * reset the message to player one
					 */
					message.setText("Player one's turn");
					gridheight[move]++;
					if (gridheight[move] >= row)
						inputButton[move].setEnabled(false);
				}
			}
		}
	}
	
	//change colour function
	public static void changecolour() {
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

	// get the name of user
	public static String name() {
		if (name1.equals("")) {
			// if no name is entered return 1
			return "Player 1";
		} else {//else return the inputed name
			return name1;
		}
	}

	///reset function
	public static void reset() {
		for (int i = 0; i < column; i++) {
			inputButton[i].setEnabled(true);
			gridheight[i] = 0;
			//enable all the buttons, and reset the gridheight to 0
		}
		inprogress = false;//reset inprogress
		
		//reset the value of the grid state 2-d array
		for (int i = 0; i < row; i++)
			for (int j = 0; j < column; j++) {
				gridstate[i][j] = 0;
				//reset labelgrid to empty image
				if (currentcolour == 0)
					labelgrid[i][j].setIcon(empty);
				else if (currentcolour == 1)
					labelgrid[i][j].setIcon(empty1);
				else
					labelgrid[i][j].setIcon(empty2);
			}
		//disable surrender function
		surrenderButton.setEnabled(false);
		
		//determine who goes first
		whogoesfirst();
	
		//reset grid height
		for (int i = 0; i < column; i++)
			gridstate[row][i] = gridheight[i];
	}

	/*
	 * minimax algorithm with alpha beta pruning
	 * whenever the maximum point(beta) that the minimizing player is assured of
	 * is less than the minimum point(alpha) that the  maximizing player is assured of in that node
	 * there is no point of keep searching that node
	 * beta starts with inf
	 * alpha starts with -inf
	 */
	public static int minimax(int[][] checkBoard, int x, int y, int alpha,
			int beta, int depth, boolean playerturn) {
		int temp = check(checkBoard, x, y);
		//check the board if there is anyone winning
		if (temp == 1) {
			return -INF;
		// return negative infinity if player win
		}
		if (temp == 2) {
			return INF;
			//return positive infinity if computer win
		}
		if (fullgrid(checkBoard))
			return 0;
		//if tie return 0
	
		// if the depth reaches 0, check board state to get the point
		if (depth == 0) {
			return boardState(checkBoard, playerturn);
		}

		//if this node is the root, set move to -1
		if (depth == searchdepth)
			move = -1;
		
		// if player turn
		if (playerturn) {
			/*
			 * look through all the columns
			 */
			for (int i = 0; i < column; i++) {
				if (checkBoard[row][i] >= row)// if this column is no longer available 
					continue;//skip it
				int[][] newBoard = new int[row + 1][column];//declare and initialize new board
				
				//copy the new board from the current board
				for (int k = 0; k <= row; k++) {
					for (int j = 0; j < column; j++) {
						newBoard[k][j] = checkBoard[k][j];
					}
				}
				// change the board at the current column
				newBoard[checkBoard[row][i]][i] = 1;
				newBoard[row][i]++;//change the height of that column
				
				// get the value of that number from calling another minimax
				int val = minimax(newBoard, checkBoard[row][i], i, alpha, beta,
						depth - 1, !playerturn);
				
				//if value is smaller than the current beta value
				if (val < beta) {
					beta = val;
					//reset the beta
					/*
					 * if it is the root
					 */
					if (depth == searchdepth) {
					//set the move to the current column
							move = i;
					}
				}
				// break the search if keep searching is meaningless(as explained before)
				if (beta <= alpha) {
					return beta;
				}
			}
			//return beta
			return beta;
			/*
			 * if it is the computer's turn
			 */
		} else {
			/*
			 * go through all the columns
			 */
			for (int i = 0; i < column; i++) {
				if (checkBoard[row][i] >= row) {//skip the column that is full
					continue;
				}
				//initialize a new board with same value of current board
				int[][] newBoard = new int[row + 1][column];
				for (int k = 0; k <= row; k++) {
					for (int j = 0; j < column; j++) {
						newBoard[k][j] = checkBoard[k][j];
					}
				}
				//change the state of board at the current column
				newBoard[checkBoard[row][i]][i] = 2;
				newBoard[row][i]++;
				//change the height
				
				//run minimax algorithm again to get the value
				int val = minimax(newBoard, checkBoard[row][i], i, alpha, beta,
						depth - 1, !playerturn);
				// if the value is bigger than the alpha value
				if (val > alpha) {
					alpha = val;//reset the alpha
					if (depth == searchdepth) { 
						//reset move if at root
							move = i;
					}
				}
				//stop searching when unnecessary as explained before
				if (beta <= alpha) {
					return alpha;
				}
			}
			
			//return alpha
			return alpha;
		}
	}

	/*
	 * board state function
	 */
	public static int boardState(int[][] checkboard, boolean playerturn) {
		int finalVal = 0;//value
		int num0, num1, num2;
		/*
		 * check the connected 3's and 2's in a row by one player that has not been blocked
		 */
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column - 3; j++) {
				num0 = 0;
				num1 = 0;
				num2 = 0;
				for (int k = j; k < j + 4; k++) {
					if (checkboard[i][k] == 0)
						num0++;
					if (checkboard[i][k] == 1)
						num1++;
					if (checkboard[i][k] == 2)
						num2++;
				}
				/*
				 * give the point based on the user and the position of the connected 3 or 2
				 * if it is lower, than it has more point.
				 */
				if (num0 == 1) {
					if (num1 == 3)
						finalVal -= (50 * (row - i));
					else if (num2 == 3)
						finalVal += (50 * (row - i));
				} else if (num0 == 2) {
					if (num1 == 2)
						finalVal -= (20 * (row - i));
					else if (num2 == 2)
						finalVal += (20 * (row - i));
				}
			}
		}
		/*
		 * check the connected 3's and 2's in a column by one player that has not been blocked
		 */
		for (int i = 0; i < row - 3; i++) {
			for (int j = 0; j < column; j++) {
				num0 = 0;
				num1 = 0;
				num2 = 0;
				for (int k = i; k < i + 4; k++) {
					if (checkboard[k][j] == 0)
						num0++;
					if (checkboard[k][j] == 1)
						num1++;
					if (checkboard[k][j] == 2)
						num2++;
				}
				/*
				 * give the point based on the user
				 */
				if (num0 == 1) {
					if (num1 == 3)
						finalVal -= 50;
					else if (num2 == 3)
						finalVal += 50;
				} else if (num0 == 2) {
					if (num1 == 2)
						finalVal -= 20;
					else if (num2 == 2)
						finalVal += 20;
				}
			}
		}
		/*
		 * check the connected 3's and 2's in a rising diagonal by one player that has not been blocked
		 */
		for (int i = 0; i < row - 3; i++) {
			for (int j = 0; j < column - 3; j++) {
				num0 = 0;
				num1 = 0;
				num2 = 0;
				int loc0 = 0;
				for (int k = 0; k < 4; k++) {
					if (checkboard[i + k][j + k] == 0) {
						loc0 = i + k;
						num0++;
					}
					if (checkboard[i + k][j + k] == 1)
						num1++;
					if (checkboard[i + k][j + k] == 2)
						num2++;
				}
				if (num0 == 1) {
					/*
					 * give the point based on the user and the position of the connected 3 or 2
					 * if it is lower, than it has more point.
					 */
					if (num1 == 3)
						finalVal -= (50 * (row - loc0));
					else if (num2 == 3)
						finalVal += (50 * (row - loc0));
				} else if (num0 == 2) {
					if (num1 == 2)
						finalVal -= (20 * (row - loc0));
					else if (num2 == 2)
						finalVal += (20 * (row - loc0));
				}
			}
		}
		/*
		 * check the connected 3's and 2's in a falling diagonal by one player that has not been blocked
		 */
		for (int i = 0; i < row - 3; i++) {
			for (int j = 3; j < column; j++) {
				num0 = 0;
				num1 = 0;
				num2 = 0;
				int loc0 = 0;
				for (int k = 0; k < 4; k++) {
					if (checkboard[i + k][j - k] == 0) {
						loc0 = i + k;
						num0++;
					}
					if (checkboard[i + k][j - k] == 1)
						num1++;
					if (checkboard[i + k][j - k] == 2)
						num2++;
				}
				/*
				 * give the point based on the user and the position of the connected 3
				 * if it is lower, than it has more point.
				 */
				if (num0 == 1) {
					if (num1 == 3)
						finalVal -= (50 * (row - loc0));
					else if (num2 == 3)
						finalVal += (50 * (row - loc0));
				} else if (num0 == 2) {
					if (num1 == 2)
						finalVal -= (20 * (row - loc0));
					else if (num2 == 2)
						finalVal += (20 * (row - loc0));
				}
			}
		}
		return finalVal;
	}

	/*
	 * check if the the current player wins or not
	 * x and y are the row and column of current move
	 */
	public static int check(int[][] checkBoard, int x, int y) {
		boolean win = true;
		int temp = checkBoard[x][y];
		//temp stands for the current player
		
		// if in a row , there is a connected 4
		for (int i = x - 3; i <= x; i++) {
			if (i < 0)// make sure not out of bound
				continue;
			win = true;
			// check the next 4
			for (int j = i; j <= i + 3; j++) {
				if (j >= row) {//make sure not out of bound
					win = false;
					break;
				}
				if (checkBoard[j][y] != temp)
					win = false;//if the value is from temp, set win to false
			}
			if (win)
				return temp;
			//return temp if win is true
		}
		
		// if in a column , there is a connected 4
		for (int i = y - 3; i <= y; i++) {
			if (i < 0)
				continue;
			win = true;
			for (int j = i; j <= i + 3; j++) {	// check the next 4
				if (j >= column) {//make sure not out of bound
					win = false;
					break;
				}
				if (checkBoard[x][j] != temp)
					win = false;//if the value is from temp, set win to false
			}
			if (win)
				return temp;
			//return temp if win is true
		}
		
		// if in a rising diagonal , there is a connected 4
		for (int i = -3; i <= 0; i++) {
			if ((x + i) < 0)//make sure not out of bound
				continue;
			if ((y + i) < 0)//make sure not out of bound
				continue;
			win = true;
			for (int j = i; j <= i + 3; j++) {	// check the next 4
				if ((x + j) >= row || (y + j) >= column) {//make sure not out of bound
					win = false;
					break;
				}
				if (checkBoard[x + j][y + j] != temp)
					win = false;//if the value is from temp, set win to false
			}
			if (win)
				return temp;
			//return temp if win is true
		}
		
		// if in a falling diagonal , there is a connected 4

		for (int i = -3; i <= 0; i++) {
			if ((x + i) < 0)//make sure not out of bound
				continue;
			if ((y - i) >= column)//make sure not out of bound
				continue;
			win = true;
			for (int j = i; j <= i + 3; j++) {// check the next 4
				if ((x + j) >= row || (y - j) < 0) {//make sure not out of bound
					win = false;
					break;
				}
				if (checkBoard[x + j][y - j] != temp)
					win = false;//if the value is from temp, set win to false
			}
			if (win)
				return temp;
			//return temp if win is true
		}
		return 0;
	}

	/*
	 * method that checks if the grid is full
	 */
	public static boolean fullgrid(int grid[][]) {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				if (grid[i][j] == 0)
					return false;
			}
		}
		return true;
	}
	/*
	 * method that gets user name
	 */

	public static void playernames() { 
		String[] options = { "OK" };
		JPanel rpan = new JPanel();
		JLabel rlabel = new JLabel("What is player 1's name?");
		JTextField rtext = new JTextField(10);
		rpan.add(rlabel);
		rpan.add(rtext);
		// use a java option panel to get user inputs
		int ok = JOptionPane.showOptionDialog(null, rpan, "Player 1's name?",
				JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
		if (ok == 0) {
			name1 = (rtext.getText());
		}
	}

	/*
	 * method that determine who goes first
	 */
	public static void whogoesfirst() {
		//use a option pane to ask the user input
		int reply = JOptionPane
				.showConfirmDialog(null, "Do you wish to play first?", "Turn",
						JOptionPane.YES_NO_OPTION);
		//if the user choose yes, set playerone turn to true
		if (reply == JOptionPane.YES_OPTION) {
			playeroneturn = true;
		} else {
			//if the compuetr is going first instead, AI will make the first move in the middle
			gridstate[0][3] = 2;
			gridheight[3] = 1;
			gridstate[row][3] = 1;
			if (currentcolour == 0)
				labelgrid[0][3].setIcon(p2);
			else if (currentcolour == 1)
				labelgrid[0][3].setIcon(yellow1);
			else
				labelgrid[0][3].setIcon(yellow2);
			playeroneturn = true;
		}
	}

	/*
	 * main method
	 */
	public static void main(String[] args) {
		//ask for player names
		playernames();
		//run the GUI constructor
		Connect4Single a = new Connect4Single();
		a.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
