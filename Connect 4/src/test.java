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

//import com.jgoodies.looks.plastic.PlasticLookAndFeel;

public class test extends JFrame implements ActionListener {
	public static final int row = 6, column = 7, delaytime = 75;
	public static int score1 = 0, score2 = 0, currentcolour = 0, full = 0,
			curr, curc;
	public static String name1;
	public static final int searchdepth = 9;
	public static final int INF = 0x7fffffff;
	public static int move = 0;
	public static boolean playeroneturn = true;
	public static int gridstate[][] = new int[row + 1][column];
	public static int gridheight[] = new int[column];
	public static JFrame frame = new JFrame();
	public static JLabel labelgrid[][] = new JLabel[row][column];
	public static JButton inputButton[] = new JButton[column];
	public static JLabel message = new JLabel("Welcome to Connect 4!");
	public static JButton surrenderButton = new JButton("Surrender?");
	public static JButton colourchange = new JButton("Change Colour Theme");
	public static JLabel p1score = new JLabel(), p2score = new JLabel();
	public static JPanel boardPanel = new JPanel();
	public static JPanel buttonPanel = new JPanel();
	public static ImageIcon empty, p1, p2, empty1, red1, yellow1, empty2, red2,
			yellow2;

	public test() {
		/*
		 * Using the third party GUI look and feel, looks much better than the
		 * Java metal look and feel
		 */
		// try {
		// UIManager.setLookAndFeel(new PlasticLookAndFeel());
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
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

		frame.add(boardPanel);
		frame.add(buttonPanel);
		frame.setLayout(new FlowLayout());
		boardPanel.setLayout(new GridLayout(row + 1, column));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		p1score.setText(name(1) + ": " + score1);
		p2score.setText("Computer: " + score2);
		for (int i = 0; i < column; i++) {
			inputButton[i] = new JButton(i + "");
			boardPanel.add(inputButton[i]);
			inputButton[i].addActionListener(this);
			inputButton[i].setEnabled(true);
		}
		for (int i = row - 1; i >= 0; i--) {
			for (int j = 0; j < column; j++) {
				labelgrid[i][j] = new JLabel();
				labelgrid[i][j].setIcon(empty);
				boardPanel.add(labelgrid[i][j]);
			}
		}
		buttonPanel.add(surrenderButton);
		surrenderButton.setEnabled(false);
		buttonPanel.add(colourchange);
		buttonPanel.add(message);
		buttonPanel.add(p1score);
		buttonPanel.add(p2score);
		message.setFont(new Font("Arial Black", Font.PLAIN, 20));
		p1score.setFont(new Font("Arial Black", Font.PLAIN, 20));
		p1score.setForeground(Color.RED);
		p2score.setFont(new Font("Arial Black", Font.PLAIN, 20));
		p2score.setForeground(Color.YELLOW);
		surrenderButton.addActionListener(this);
		colourchange.addActionListener(this);
		setTitle("Connect 4 Singleplayer");
		frame.setSize(1100, 850);
		frame.setResizable(true);
		frame.setVisible(false);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Surrender?")) {
			int reply = JOptionPane.showConfirmDialog(null, name(1)
					+ ", do you wish to concede?", name(1) + " Give Up?",
					JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				message.setText(name(1) + " has surrendered!");
				p2score.setText("Computer: " + ++score2);
				reset();
			}
			// philip i dont know what the point of this is, take it out if you
			// dont need it
			// if (playeroneturn)
			// message.setText(name(1) + "'s turn");
			// else {
			// message.setText("Computer has moved. " + name(1) + "s turn");
			// gridheight[3]++;
			// gridstate[0][3] = 2;
			// labelgrid[0][3].setText("2");
			// labelgrid[0][3].setForeground(Color.red);
			// playeroneturn = true;
			// }
		} else if (command.equals("Change Colour Theme")) {
			changecolour();
		} else {
			surrenderButton.setEnabled(true);
			int num = Integer.parseInt(command);
			curr = gridheight[num];
			curc = num;
			gridstate[gridheight[num]][num] = 1;
			if (check(gridstate, gridheight[num], num) > 0) {
				message.setText(name(1) + " wins!");
				JOptionPane.showMessageDialog(null, name(1) + " wins!",
						"Victory", JOptionPane.INFORMATION_MESSAGE);
				p1score.setText(name(1) + ": " + ++score1);
				reset();
				return;
			} else if (fullgrid(gridstate)) {
				JOptionPane.showMessageDialog(null, "It's a tie!", "Tie!",
						JOptionPane.INFORMATION_MESSAGE);
				reset();
			} else {
				gridheight[num]++;
				if (gridheight[num] >= row)
					inputButton[num].setEnabled(false);
				message.setText("Computer's turn");
				for (int i = 0; i < column; i++) {
					gridstate[row][i] = gridheight[i];
				}
				int moveVal = minimax(gridstate, gridheight[num] - 1, num,
						-INF, INF, searchdepth, false);
				Thread delaythread = new Thread() {
					public void run() {
						drop(1);
						try {
							for (int i = 0; i < column; i++) {
								inputButton[i].setEnabled(false);
							}
							Thread.sleep(delaytime * (column - gridheight[curc]));
							for (int i = 0; i < column; i++) {
								if (gridheight[i] != row)
									inputButton[i].setEnabled(true);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				delaythread.start();
				try {
					delaythread.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (currentcolour == 0)
					labelgrid[gridheight[move]][move].setIcon(p2);
				else if (currentcolour == 1)
					labelgrid[gridheight[move]][move].setIcon(yellow1);
				else
					labelgrid[gridheight[move]][move].setIcon(yellow2);
				gridstate[gridheight[move]][move] = 2;

				if (check(gridstate, gridheight[move], move) == 2) {
					message.setText("Computer wins!");
					JOptionPane.showMessageDialog(null, "The computer won!",
							"Victory!", JOptionPane.INFORMATION_MESSAGE);
					p2score.setText("Computer: " + ++score2);
					reset();
				} else if (fullgrid(gridstate)) {
					JOptionPane.showMessageDialog(null, "It's a tie!", "Tie!",
							JOptionPane.INFORMATION_MESSAGE);
					reset();
				} else {
					message.setText("Player one's turn");
					gridheight[move]++;
					if (gridheight[move] >= row)
						inputButton[move].setEnabled(false);
				}
			}
		}
	}

	public static void drop(int player) {
		final int p = player;
		Thread droppiece = new Thread() {
			public void run() {
				for (int i = 5; i > curr; i--) {
					if (p == 1) {
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
					} else {
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

	public static String name(int player) {
		if (name1.equals("")) {
			return "Player";
		} else {
			return name1;
		}
	}

	public static void reset() {
		for (int i = 0; i < column; i++) {
			inputButton[i].setEnabled(true);
			gridheight[i] = 0;
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
		whogoesfirst();
		for (int i = 0; i < column; i++)
			gridstate[row][i] = gridheight[i];
	}

	public static int minimax(int[][] checkBoard, int x, int y, int alpha,
			int beta, int depth, boolean playerturn) {
		int temp = check(checkBoard, x, y);
		if (temp == 1) {
			return -INF;
		}
		if (temp == 2) {
			return INF;
		}
		if (fullgrid(checkBoard))
			return 0;
		if (depth == 0) {
			return boardState(checkBoard, playerturn);
			// return 50;
		}

		if (depth == searchdepth)
			move = 0;
		if (playerturn) {
			for (int i = 0; i < column; i++) {
				if (checkBoard[row][i] >= row)
					continue;
				int[][] newBoard = new int[row + 1][column];
				for (int k = 0; k <= row; k++) {
					for (int j = 0; j < column; j++) {
						newBoard[k][j] = checkBoard[k][j];
					}
				}
				newBoard[checkBoard[row][i]][i] = 1;
				newBoard[row][i]++;
				int val = minimax(newBoard, checkBoard[row][i], i, alpha, beta,
						depth - 1, !playerturn);
				if (val < beta) {
					beta = val;
					if (depth == searchdepth) {
						int closeness = Math.abs(i - (int) column / 2);
						int closeness2 = Math.abs(move - (int) column / 2);
						if (closeness <= closeness2)
							move = i;
					}
				}
				if (beta <= alpha) {
					return beta;
				}
			}
			return beta;
		} else {
			for (int i = 0; i < column; i++) {
				if (checkBoard[row][i] >= row) {
					continue;
				}
				int[][] newBoard = new int[row + 1][column];
				for (int k = 0; k <= row; k++) {
					for (int j = 0; j < column; j++) {
						newBoard[k][j] = checkBoard[k][j];
					}
				}
				newBoard[checkBoard[row][i]][i] = 2;
				newBoard[row][i]++;
				int val = minimax(newBoard, checkBoard[row][i], i, alpha, beta,
						depth - 1, !playerturn);
				if (val > alpha) {
					alpha = val;
					if (depth == searchdepth) {
						int closeness = Math.abs(i - (int) column / 2);
						int closeness2 = Math.abs(move - (int) column / 2);
						if (closeness <= closeness2)
							move = i;
					}
				}
				if (beta <= alpha) {
					return alpha;
				}
			}
			return alpha;
		}
	}

	public static int boardState(int[][] checkboard, boolean playerturn) {
		int finalVal = 0;
		int num0, num1, num2;
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

	public static int check(int[][] checkBoard, int x, int y) {
		boolean win = true;
		int temp = checkBoard[x][y];
		for (int i = x - 3; i <= x; i++) {
			if (i < 0)
				continue;
			win = true;
			for (int j = i; j <= i + 3; j++) {
				if (j >= row) {
					win = false;
					break;
				}
				if (checkBoard[j][y] != temp)
					win = false;
			}
			if (win)
				return temp;
		}
		for (int i = y - 3; i <= y; i++) {
			if (i < 0)
				continue;
			win = true;
			for (int j = i; j <= i + 3; j++) {
				if (j >= column) {
					win = false;
					break;
				}
				if (checkBoard[x][j] != temp)
					win = false;
			}
			if (win)
				return temp;
		}
		for (int i = -3; i <= 0; i++) {
			if ((x + i) < 0)
				continue;
			if ((y + i) < 0)
				continue;
			win = true;
			for (int j = i; j <= i + 3; j++) {
				if ((x + j) >= row || (y + j) >= column) {
					win = false;
					break;
				}
				if (checkBoard[x + j][y + j] != temp)
					win = false;
			}
			if (win)
				return temp;
		}
		for (int i = -3; i <= 0; i++) {
			if ((x + i) < 0)
				continue;
			if ((y - i) >= column)
				continue;
			win = true;
			for (int j = i; j <= i + 3; j++) {
				if ((x + j) >= row || (y - j) < 0) {
					win = false;
					break;
				}
				if (checkBoard[x + j][y - j] != temp)
					win = false;
			}
			if (win)
				return temp;
		}
		return 0;
	}

	public static boolean fullgrid(int grid[][]) {
		for (int i = 0; i < column; i++) {
			for (int j = 0; j < row; j++) {
				if (grid[j][i] == 0)
					return false;
			}
		}
		return true;
	}

	public static void playernames() {
		// get user names
		String[] options = { "OK" };
		JPanel rpan = new JPanel();
		JLabel rlabel = new JLabel("What is your name?");
		JTextField rtext = new JTextField(10);
		rpan.add(rlabel);
		rpan.add(rtext);
		int ok = JOptionPane.showOptionDialog(null, rpan, "Your name?",
				JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
		if (ok == 0) {
			name1 = (rtext.getText());
		}
		whogoesfirst();
	}

	public static void whogoesfirst() {
		int reply = JOptionPane
				.showConfirmDialog(null, "Do you wish to play first?", "Turn",
						JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			playeroneturn = true;
		} else {
			playeroneturn = true;
			gridstate[0][3] = 2;
			gridheight[3] = 1;
			gridstate[row][3] = 1;
			if (currentcolour == 0)
				labelgrid[0][3].setIcon(p2);
			else if (currentcolour == 1)
				labelgrid[0][3].setIcon(yellow1);
			else
				labelgrid[0][3].setIcon(yellow2);

		}
	}

	public static void main(String[] args) {
		playernames();
		test a = new test();
		a.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
