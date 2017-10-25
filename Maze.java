package mazeRunner;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import javax.swing.*;

public class Maze {
	public static JFrame outerFrame;
	public static long startTime, endTime;
	public static int[][] hardMaze, hardMaze1;
	public static boolean flag = false;
	public static void main(String[] args) {
		int width = 693;
		int height = 545;
		outerFrame = new JFrame("MAZE RUNNER");
		outerFrame.setContentPane(new Panel(width, height));
		outerFrame.pack();
		outerFrame.setResizable(false);
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		double widthDesc = size.getWidth();
		double heightDesc = size.getHeight();
		int x = ((int) widthDesc - width) / 2;
		int y = ((int) heightDesc - height) / 2;
		outerFrame.setLocation(x, y);
		outerFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		outerFrame.setVisible(true);
	}

	public static class Panel extends JPanel {
		private static final long serialVersionUID = 1L;

		private class MazeCell {
			int row, column, f, g, h;
			MazeCell previous;

			public MazeCell(int row, int col) {
				this.row = row;
				this.column = col;
			}
		}

		private class CellComparatorByF implements Comparator<MazeCell> {
			@Override
			public int compare(MazeCell cell1, MazeCell cell2) {
				return cell1.f - cell2.f;
			}
		}

		private class ActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent event) {
				String command = event.getActionCommand();
				if (command.equals("Clear Search")) {
					fillGrid();
					findPath.setEnabled(true);
					dfs.setEnabled(true);
					bfs.setEnabled(true);
					aStar.setEnabled(true);
					aStar1.setEnabled(true);

				} else if (command.equals("Find Path")) {
					searching = true;

					dfs.setEnabled(false);
					bfs.setEnabled(false);
					aStar.setEnabled(false);
					aStar1.setEnabled(false);

					startTime = System.currentTimeMillis();
					timer.start();
				} else if (command.equals("Genetic Algorithm")) {
					
					findPath.setEnabled(false);
					dfs.setEnabled(false);
					bfs.setEnabled(false);
					aStar.setEnabled(false);
					aStar1.setEnabled(false);
					
					int nodes = Integer.parseInt(JOptionPane.showInputDialog("Please input number of rows/columns(< 248): "));
					int mode = Integer.parseInt(JOptionPane.showInputDialog("Please select mode: \n0 - Nodes Expanded\n 1 - Path Length\n 2 - Fringe Length "));
					hardMaze1 = (new GA(nodes, 100 , 100)).genetic(mode);
					if(hardMaze1 == null)
					{
						JOptionPane.showMessageDialog(outerFrame, "No solvable mazes found in 100 generations");
					}
					else
					{
						System.out.println(hardMaze1.length);
						for(int i = 0 ; i < hardMaze1.length ; i++)
						{
							for(int j = 0 ; j < hardMaze1.length ; j++)
							{
								System.out.print(hardMaze1[i][j]);
							}
							System.out.println();
						}
						int k = hardMaze1.length + 2;
						System.out.println(k);
						hardMaze = new int[k][k];
						System.out.println(hardMaze.length);
						int i = k - 3, j = 0;
						for(int r = 0; r < k ; r++)
						{
							for(int c = 0 ; c < k ; c++)
							{
								if(r == 0 || r == k - 1 || c == 0 || c == k - 1)
								{
									hardMaze[r][c] = 1;
								}
								else
								{
									hardMaze[r][c] = hardMaze1[i][j];
									if(j < hardMaze1.length - 1)
									{
										j++;
									}
									else
									{
										i--;
										j = 0;
									}
								}
								 
							}
						}
						for(i = 0 ; i < k ; i++)
						{
							for(j = 0 ; j < k ; j++)
							{
								System.out.print(hardMaze[i][j]);
							}
							System.out.println();
						}
						flag = true;
						
						repaint();
											
					}
					
				
				} else if (command.equals("About Maze")) {
					Description desc = new Description(outerFrame, true);
					desc.setVisible(true);
				}
			}
		}

		private class Repaint implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent event) {
				checkGoal();
				if (found) {
					timer.stop();
				}
				repaint();
			}
		}

		public void checkGoal() {

			expandNode();
			if (found) {
				endOfSearch = true;
				plotRoute();
				findPath.setEnabled(false);
				repaint();

				endTime = System.currentTimeMillis();
				System.out.println((double) (endTime - startTime) / 1000);
			}
		}

		private class Description extends JDialog {

			private static final long serialVersionUID = 1L;

			public Description(Frame parent, boolean modal) {
				super(parent, modal);
				Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
				double widthDesc = size.getWidth();
				double heightDesc = size.getHeight();
				int width = 900;
				int height = 400;
				int x = ((int) widthDesc - width) / 2;
				int y = ((int) heightDesc - height) / 2;
				setSize(width, height);
				setLocation(x, y);

				setResizable(false);
				setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

				JLabel title = new JLabel("Maze", JLabel.CENTER);
				title.setFont(new Font("Helvetica", Font.PLAIN, 24));
				title.setForeground(new java.awt.Color(255, 153, 102));

				JLabel version = new JLabel("Version:1.0", JLabel.CENTER);
				version.setFont(new Font("Helvetica", Font.BOLD, 14));

				JLabel programmer = new JLabel("Designer: Mudita Tandon", JLabel.CENTER);
				programmer.setFont(new Font("Helvetica", Font.PLAIN, 16));

				JLabel email = new JLabel("E-mail:mt909", JLabel.CENTER);
				email.setFont(new Font("Helvetica", Font.PLAIN, 14));

				JLabel dummy = new JLabel("");

				add(title);
				add(version);
				add(programmer);
				add(email);

				add(dummy);

				title.setBounds(5, 10, 330, 30);
				version.setBounds(5, 30, 330, 20);
				programmer.setBounds(5, 55, 330, 20);
				email.setBounds(5, 80, 330, 20);

				dummy.setBounds(5, 155, 330, 20);
			}

		}

		private class MyMaze {
			private int dimRow, dimColumn;
			private int mazeX, mazeY;
			private char[][] mazeMatrix;
			private MazeCell[][] cells;
			private Random random = new Random();

			public MyMaze(int xDimension, int yDimension) {
				dimRow = xDimension;
				dimColumn = yDimension;
				mazeX = xDimension * 2 + 1;
				mazeY = yDimension * 2 + 1;
				mazeMatrix = new char[mazeX][mazeY];

				init();
				create(0, 0);
			}

			private void init() {
				cells = new MazeCell[dimRow][dimColumn];
				for (int x = 0; x < dimRow; x++) {
					for (int y = 0; y < dimColumn; y++) {
						cells[x][y] = new MazeCell(x, y, false);
					}
				}
			}

			private class MazeCell {

				int x, y;
				ArrayList<MazeCell> adjacentMazeCell = new ArrayList<>();
				boolean wall = true;
				boolean open = true;

				MazeCell(int x, int y) {
					this(x, y, true);
				}

				MazeCell(int x, int y, boolean isWall) {
					this.x = x;
					this.y = y;
					this.wall = isWall;
				}

				void addAdjacent(MazeCell other) {
					if (!this.adjacentMazeCell.contains(other)) {
						this.adjacentMazeCell.add(other);
					}
					if (!other.adjacentMazeCell.contains(this)) {
						other.adjacentMazeCell.add(this);
					}
				}

				boolean isBelow() {
					return this.adjacentMazeCell.contains(new MazeCell(this.x, this.y + 1));
				}

				boolean isAbove() {
					return this.adjacentMazeCell.contains(new MazeCell(this.x, this.y - 1));
				}

				boolean isLeft() {
					return this.adjacentMazeCell.contains(new MazeCell(this.x - 1, this.y));
				}

				boolean isRight() {
					return this.adjacentMazeCell.contains(new MazeCell(this.x + 1, this.y));
				}

				@Override
				public boolean equals(Object other) {
					if (!(other instanceof MazeCell))
						return false;
					MazeCell otherCell = (MazeCell) other;
					return (this.x == otherCell.x && this.y == otherCell.y);
				}
			}

			private void create(int x, int y) {
				create(getMazeCell(x, y));
			}

			private void create(MazeCell startAt) {
				if (startAt == null)
					return;
				startAt.open = false;
				ArrayList<MazeCell> cellsList = new ArrayList<>();
				cellsList.add(startAt);

				while (!cellsList.isEmpty()) {
					MazeCell cell;
					cell = cellsList.remove(cellsList.size() - 1);

					ArrayList<MazeCell> adjacentMazeCell = new ArrayList<>();
					MazeCell[] potentialAdjacentMazeCell = new MazeCell[] { getMazeCell(cell.x + 1, cell.y),
							getMazeCell(cell.x, cell.y + 1), getMazeCell(cell.x - 1, cell.y),
							getMazeCell(cell.x, cell.y - 1) };

					for (MazeCell other : potentialAdjacentMazeCell) {
						if (other == null || other.wall || !other.open)
							continue;
						adjacentMazeCell.add(other);

					}
					if (adjacentMazeCell.isEmpty())
						continue;
					MazeCell selected = adjacentMazeCell.get(random.nextInt(adjacentMazeCell.size()));
					selected.open = false;
					cell.addAdjacent(selected);
					cellsList.add(cell);
					cellsList.add(selected);
				}
				updateMaze();
			}

			public MazeCell getMazeCell(int x, int y) {
				try {
					return cells[x][y];
				} catch (ArrayIndexOutOfBoundsException e) {
					return null;
				}
			}

			public void updateMaze() {
				char black = 'X', white = ' ';

				double c = Math.random();
				for (int x = 0; x < mazeX; x++) {
					for (int y = 0; y < mazeY; y++) {
						if (x % 2 == 0 || y % 2 == 0)
							mazeMatrix[x][y] = black;
					}
				}
				for (int x = 0; x < dimRow; x++) {
					for (int y = 0; y < dimColumn; y++) {
						MazeCell current = getMazeCell(x, y);
						int mazeX1 = x * 2 + 1;
						int mazeY1 = y * 2 + 1;
						mazeMatrix[mazeX1][mazeY1] = white;

						if (c < 0.5 && current.isBelow()) {

							mazeMatrix[mazeX1][mazeY1 + 1] = white;
						}
						if (c < 0.5 && current.isRight()) {
							mazeMatrix[mazeX1 + 1][mazeY1] = white;
						}
						if (c >= 0.5 && current.isAbove()) {

							mazeMatrix[mazeX1][mazeY1 - 1] = white;
						}
						if (c >= 0.5 && current.isLeft()) {
							mazeMatrix[mazeX1 - 1][mazeY1] = white;
						}

					}
				}

				searching = false;
				endOfSearch = false;
				fillGrid();
				double total;
				for (int x = 0; x < mazeX; x++) {
					grid[x][0] = grid[x][mazeX - 1] = OBST;
				}
				for (int y = 0; y < mazeY; y++) {
					grid[0][y] = grid[mazeY - 1][y] = OBST;
				}

				total = (mazeX - 2) * (mazeY - 2);

				System.out.println(mazeX + "  " + mazeY + "  " + total);
				double count = 0;
				double p = Double.parseDouble(probIP.getText());
				System.out.println(p);
				double prob = p * total;

				if (p <= 0.35) {
					while (count < (int) prob) {
						Random random = new Random();
						int x = random.nextInt(mazeX - 2) + 1;
						int y = random.nextInt(mazeY - 2) + 1;

						if (mazeMatrix[x][y] == black && grid[x][y] != ROBOT && grid[x][y] != TARGET && count <= prob
								&& grid[x][y] != OBST) {

							count++;
							grid[x][y] = OBST;
							// System.out.println(count+" "+x+" "+y);
						}
					}

					System.out.println(count);
				}

				else {

					count = 0;
					while (count < prob) {
						Random random = new Random();
						int x = random.nextInt(mazeX - 2) + 1;
						int y = random.nextInt(mazeY - 2) + 1;

						if (count <= prob && grid[x][y] != OBST) {
							count++;
							System.out.println(x + " " + y + " " + count);
							if (grid[x][y] != ROBOT && grid[x][y] != TARGET) {
								grid[x][y] = OBST;

							}
						}

					}

					System.out.println("...." + prob + "   " + count);
				}
			}
		}

		private final static int EMPTY = 0, OBST = 1, ROBOT = 2, TARGET = 3, FRONTIER = 4, CLOSED = 5, ROUTE = 6;

		private final static String msgDrawAndSelect = "Generate Maze, then choose 'Algorithm', then click 'Find Path'";

		int rows = 5, columns = 5, squareSize = 500 / rows;

		ArrayList<MazeCell> open = new ArrayList<>();
		ArrayList<MazeCell> closed = new ArrayList<>();
		ArrayList<MazeCell> graph = new ArrayList<>();

		MazeCell start;
		MazeCell goal;

		JLabel message;

		JButton maze, clear, findPath, geneticAlgo;

		JRadioButton dfs, bfs, aStar, aStar1;

		JTextField nodes = new JTextField(20);

		JTextField probIP = new JTextField(20);

		int[][] grid;
		int expanded;

		int delay;
		boolean found, searching, endOfSearch;

		Repaint action = new Repaint();

		Timer timer;

		public Panel(int width, int height) {

			setLayout(null);
			setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.orange));
			setPreferredSize(new Dimension(width, height));
			grid = new int[rows][columns];
			message = new JLabel(msgDrawAndSelect, JLabel.CENTER);
			message.setForeground(Color.DARK_GRAY);
			message.setFont(new Font("Helvetica", Font.BOLD, 16));

			JLabel nodesLbl = new JLabel("Enter nodes in maze(< 248)", JLabel.LEFT);
			nodesLbl.setFont(new Font("Helvetica", Font.PLAIN, 13));

			JLabel pLbl = new JLabel("Enter probability(p* = 0.35)", JLabel.LEFT);
			pLbl.setFont(new Font("Helvetica", Font.PLAIN, 13));

			maze = new JButton("Generate Maze");
			maze.addActionListener(new ActionHandler());
			maze.setBackground(Color.lightGray);
			maze.setToolTipText("Creates a random maze");
			maze.addActionListener(this::mazeActionPerformed);

			clear = new JButton("Clear Search");
			clear.addActionListener(new ActionHandler());
			clear.setBackground(Color.lightGray);
			clear.setToolTipText("First click: clears search, Second click: clears obstacles");

			findPath = new JButton("Find Path");
			findPath.addActionListener(new ActionHandler());
			findPath.setBackground(Color.lightGray);
			findPath.setToolTipText("The search is performed automatically");

			geneticAlgo = new JButton("Genetic Algorithm");
			geneticAlgo.addActionListener(new ActionHandler());
			geneticAlgo.setBackground(Color.lightGray);
			geneticAlgo.setToolTipText("The search is performed automatically");

			JLabel velocity = new JLabel("Speed", JLabel.CENTER);
			velocity.setFont(new Font("Helvetica", Font.PLAIN, 10));

			delay = 0;

			ButtonGroup algos = new ButtonGroup();

			dfs = new JRadioButton("DFS");
			dfs.setToolTipText("Depth First Search algorithm");
			algos.add(dfs);
			dfs.addActionListener(new ActionHandler());

			bfs = new JRadioButton("BFS");
			bfs.setToolTipText("Breadth First Search algorithm");
			algos.add(bfs);
			bfs.addActionListener(new ActionHandler());

			aStar = new JRadioButton("A*");
			aStar.setToolTipText("A* algorithm");
			algos.add(aStar);
			aStar.addActionListener(new ActionHandler());

			aStar1 = new JRadioButton("A*1");
			aStar1.setToolTipText("A*1 algorithm");
			algos.add(aStar1);
			aStar1.addActionListener(new ActionHandler());

			JPanel algoPanel = new JPanel();
			algoPanel.setBorder(
					javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(),
							"Algorithms", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
							javax.swing.border.TitledBorder.TOP, new java.awt.Font("Helvetica", 0, 14)));

			dfs.setSelected(true);

			JLabel robot = new JLabel("Start", JLabel.CENTER);
			robot.setForeground(Color.red);
			robot.setFont(new Font("Helvetica", Font.PLAIN, 14));

			JLabel target = new JLabel("Goal", JLabel.CENTER);
			target.setForeground(Color.GREEN);
			target.setFont(new Font("Helvetica", Font.PLAIN, 14));

			JLabel frontier = new JLabel("Open Set", JLabel.CENTER);
			frontier.setForeground(Color.blue);
			frontier.setFont(new Font("Helvetica", Font.PLAIN, 14));

			JLabel closed = new JLabel("Closed set", JLabel.CENTER);
			closed.setForeground(Color.CYAN);
			closed.setFont(new Font("Helvetica", Font.PLAIN, 14));

			JButton descButon = new JButton("About Maze");
			descButon.addActionListener(new ActionHandler());
			descButon.setBackground(Color.lightGray);

			add(message);
			add(nodesLbl);
			add(nodes);
			add(pLbl);
			add(probIP);
			add(maze);
			add(clear);
			add(findPath);
			add(geneticAlgo);
			add(dfs);
			add(bfs);
			add(aStar);
			add(aStar1);
			add(algoPanel);
			add(robot);
			add(target);
			add(frontier);
			add(closed);
			add(descButon);

			message.setBounds(165, 515, 500, 23);
			nodesLbl.setBounds(5, 5, 180, 25);
			nodes.setBounds(5, 35, 130, 25);
			pLbl.setBounds(5, 65, 180, 25);
			probIP.setBounds(5, 95, 130, 25);
			maze.setBounds(5, 125, 170, 25);
			clear.setBounds(5, 155, 170, 25);
			findPath.setBounds(5, 185, 170, 25);
			geneticAlgo.setBounds(5, 215, 170, 25);
			algoPanel.setLocation(5, 245);
			algoPanel.setSize(170, 75);
			dfs.setBounds(15, 265, 70, 25);
			bfs.setBounds(85, 265, 70, 25);
			aStar.setBounds(15, 285, 70, 25);
			aStar1.setBounds(85, 285, 70, 25);
			robot.setBounds(5, 465, 80, 25);
			target.setBounds(90, 465, 80, 25);
			frontier.setBounds(5, 485, 80, 25);
			closed.setBounds(90, 485, 80, 25);
			descButon.setBounds(5, 515, 170, 25);

			timer = new Timer(delay, action);
			fillGrid();

		}

		private void mazeActionPerformed(java.awt.event.ActionEvent event) {
			findPath.setEnabled(true);
			initializeGrid(true);
		}

		private void initializeGrid(Boolean makeMaze) {

			rows = Integer.parseInt(nodes.getText()) + 2;
			columns = rows;
			if (rows % 2 == 0) {
				rows--;
				columns--;
			}
			squareSize = 500 / (rows);

			grid = new int[rows][columns];
			start = new MazeCell(rows - 2, 1);
			goal = new MazeCell(1, columns - 2);
			dfs.setEnabled(true);
			bfs.setEnabled(true);
			aStar.setEnabled(true);
			aStar1.setEnabled(true);
			if (makeMaze) {
				MyMaze maze = new MyMaze(rows / 2, columns / 2);
			} else {
				fillGrid();
			}
		}

		private void expandNode() {

			MazeCell current;
			if (dfs.isSelected() || bfs.isSelected()) {
				current = open.remove(0);
			} else {
				Collections.sort(open, new CellComparatorByF());
				current = open.remove(0);
			}
			closed.add(0, current);
			grid[current.row][current.column] = CLOSED;
			if (current.row == goal.row && current.column == goal.column) {
				MazeCell last = goal;
				last.previous = current.previous;
				closed.add(last);
				found = true;
				return;
			}
			expanded++;
			ArrayList<MazeCell> succesors;
			succesors = createSuccesors(current, false);
			succesors.stream().forEach((cell) -> {
				if (dfs.isSelected()) {
					open.add(0, cell);
					grid[cell.row][cell.column] = FRONTIER;
				} else if (bfs.isSelected()) {
					open.add(cell);
					grid[cell.row][cell.column] = FRONTIER;
				} else if (aStar.isSelected()) {
					int dxg = current.column - cell.column;
					int dyg = current.row - cell.row;
					int dxh = goal.column - cell.column;
					int dyh = goal.row - cell.row;
					cell.g = current.g + Math.abs(dxg) + Math.abs(dyg);
					cell.h = Math.abs(dxh) + Math.abs(dyh);
					cell.f = cell.g + cell.h;
					int openIndex = isInList(open, cell);
					int closedIndex = isInList(closed, cell);
					if (openIndex == -1 && closedIndex == -1) {
						open.add(cell);
						grid[cell.row][cell.column] = FRONTIER;
					} else {
						if (openIndex > -1) {
							if (open.get(openIndex).f <= cell.f) {
							} else {
								open.remove(openIndex);
								open.add(cell);
								grid[cell.row][cell.column] = FRONTIER;
							}
						} else {
							if (closed.get(closedIndex).f <= cell.f) {
							} else {
								closed.remove(closedIndex);
								open.add(cell);
								grid[cell.row][cell.column] = FRONTIER;
							}
						}
					}
				} else {

					int dxg = current.column - cell.column;
					int dyg = current.row - cell.row;
					int dxh = goal.column - cell.column;
					int dyh = goal.row - cell.row;
					cell.g = (int) (current.g + Math.sqrt(((dxg) * (dxg)) + ((dyg) * (dyg))));
					cell.h = (int) Math.sqrt((dxh * dxh) + (dyh * dyh));
					cell.f = cell.g + cell.h;
					int openIndex = isInList(open, cell);
					int closedIndex = isInList(closed, cell);
					if (openIndex == -1 && closedIndex == -1) {
						open.add(cell);
						grid[cell.row][cell.column] = FRONTIER;

					} else {
						if (openIndex > -1) {
							if (open.get(openIndex).f <= cell.f) {
							} else {
								open.remove(openIndex);
								open.add(cell);
								grid[cell.row][cell.column] = FRONTIER;
							}
						} else {
							if (closed.get(closedIndex).f <= cell.f) {
							} else {
								closed.remove(closedIndex);
								open.add(cell);
								grid[cell.row][cell.column] = FRONTIER;
							}
						}
					}
				}

			});

		}

		private ArrayList<MazeCell> createSuccesors(MazeCell current, boolean makeConnected) {
			int r = current.row;
			int c = current.column;
			ArrayList<MazeCell> temp = new ArrayList<>();
			if (r > 0 && grid[r - 1][c] != OBST && ((aStar.isSelected() || aStar1.isSelected()) ? true
					: isInList(open, new MazeCell(r - 1, c)) == -1 && isInList(closed, new MazeCell(r - 1, c)) == -1)) {
				MazeCell cell = new MazeCell(r - 1, c);
				cell.previous = current;
				temp.add(cell);

			}

			if (c < columns - 1 && grid[r][c + 1] != OBST &&

					((aStar.isSelected() || aStar1.isSelected()) ? true
							: isInList(open, new MazeCell(r, c + 1)) == -1
									&& isInList(closed, new MazeCell(r, c + 1)) == -1)) {
				MazeCell cell = new MazeCell(r, c + 1);
				cell.previous = current;
				temp.add(cell);
			}

			if (r < rows - 1 && grid[r + 1][c] != OBST && ((aStar.isSelected() || aStar1.isSelected()) ? true
					: isInList(open, new MazeCell(r + 1, c)) == -1 && isInList(closed, new MazeCell(r + 1, c)) == -1)) {
				MazeCell cell = new MazeCell(r + 1, c);
				cell.previous = current;
				temp.add(cell);
			}
			if (c > 0 && grid[r][c - 1] != OBST && ((aStar.isSelected() || aStar1.isSelected()) ? true
					: isInList(open, new MazeCell(r, c - 1)) == -1 && isInList(closed, new MazeCell(r, c - 1)) == -1)) {
				MazeCell cell = new MazeCell(r, c - 1);

				cell.previous = current;
				temp.add(cell);

			}
			if (dfs.isSelected()) {
				Collections.reverse(temp);
			}
			return temp;
		}

		private int isInList(ArrayList<MazeCell> list, MazeCell current) {
			int index = -1;
			for (int i = 0; i < list.size(); i++) {
				if (current.row == list.get(i).row && current.column == list.get(i).column) {
					index = i;
					break;
				}
			}
			return index;
		}

		private void plotRoute() {
			searching = false;
			endOfSearch = true;
			int steps = 0;
			double distance = 0;
			int index = isInList(closed, goal);
			MazeCell cur = closed.get(index);
			grid[cur.row][cur.column] = TARGET;
			do {
				steps++;

				distance++;
				cur = cur.previous;
				grid[cur.row][cur.column] = ROUTE;
			} while (!(cur.row == start.row && cur.column == start.column));
			grid[start.row][start.column] = ROBOT;
			String msg;
			endTime = System.currentTimeMillis();
			double time = (double) (endTime - startTime) / 1000;
			System.out.println(time);
			msg = String.format("Expanded: %d, Steps: %d, Dist: %.0f, Time(Secs): %.3f", expanded, steps, distance,
					time);
			message.setText(msg);

		}

		private void fillGrid() {
			if (searching || endOfSearch) {
				for (int r = 0; r < rows; r++) {
					for (int c = 0; c < columns; c++) {
						if (grid[r][c] == FRONTIER || grid[r][c] == CLOSED || grid[r][c] == ROUTE) {
							grid[r][c] = EMPTY;
						}
						if (grid[r][c] == ROBOT) {
							start = new MazeCell(r, c);
						}
						if (grid[r][c] == TARGET) {
							goal = new MazeCell(r, c);
						}
					}
				}
				searching = false;
			} else {
				for (int r = 0; r < rows; r++) {
					for (int c = 0; c < columns; c++) {
						grid[r][c] = EMPTY;
					}
				}
				start = new MazeCell(rows - 2, 1);
				goal = new MazeCell(1, columns - 2);
			}
			if (aStar.isSelected() || aStar1.isSelected()) {
				start.g = 0;
				start.h = 0;
				start.f = 0;
			}
			expanded = 0;
			found = false;
			searching = false;
			endOfSearch = false;

			open.removeAll(open);
			open.add(start);
			closed.removeAll(closed);

			grid[goal.row][goal.column] = TARGET;
			grid[start.row][start.column] = ROBOT;
			message.setText(msgDrawAndSelect);
			timer.stop();
			repaint();

		}

		@Override
		public void paintComponent(Graphics g) {

			/*StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			System.out.println(stackTraceElements[2].getClass());*/
			
			//System.out.println(flag);
			
			if(flag)
			{
				super.paintComponent(g);
				int columns = rows = hardMaze.length;
				int squareSize = 500 / rows;
				//g.setColor(Color.CYAN);
				//g.fillRect(180, 10, 5 * squareSize + 1, 5 * squareSize + 1);
				
				for(int r = 0 ; r < rows; r++)
				{
					for(int c = 0 ; c < columns ; c++)
					{
						if(r == rows - 2 && c == 1)
						{
							g.setColor(Color.RED);
						}
						else if(hardMaze[r][c] == 1)
						{
							System.out.println(r);
							g.setColor(Color.BLACK);
						}
						else if(hardMaze[r][c] == 0)
						{
							g.setColor(Color.WHITE);
						}
						else if(hardMaze[r][c] == 9)
						{
							g.setColor(Color.GREEN);
						}
						g.fillRect(181 + c * squareSize, 11 + r * squareSize, squareSize - 1, squareSize - 1);
					}
				}
				
				flag = false;
			}
			else
			{
				super.paintComponent(g);
				g.setColor(Color.DARK_GRAY);
				g.fillRect(180, 10, columns * squareSize + 1, rows * squareSize + 1);

				for (int r = 0; r < rows; r++) {
					for (int c = 0; c < columns; c++) {
						if (grid[r][c] == EMPTY) {
							g.setColor(Color.WHITE);
						} else if (grid[r][c] == ROBOT) {
							g.setColor(Color.RED);
						} else if (grid[r][c] == TARGET) {
							g.setColor(Color.GREEN);
						} else if (grid[r][c] == OBST) {
							g.setColor(Color.BLACK);
						} else if (grid[r][c] == FRONTIER) {
							g.setColor(Color.BLUE);
						} else if (grid[r][c] == CLOSED) {
							g.setColor(Color.CYAN);
						} else if (grid[r][c] == ROUTE) {
							g.setColor(Color.YELLOW);
						}
						g.fillRect(181 + c * squareSize, 11 + r * squareSize, squareSize - 1, squareSize - 1);
					}
				}
			}
			

		}

	}

}
