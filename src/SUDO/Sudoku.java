package SUDO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Myframe extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4753076496951651267L;
	public static Object obj = new Object();
	public final static JTextField[][] filed = new JTextField[9][9];

	public Myframe() {
		for (int a = 0; a < 9; a++) {
			for (int b = 0; b < 9; b++) {
				filed[a][b] = new JTextField();
				filed[a][b].setText("");
			}
		}
		JPanel jpan = new JPanel();
		jpan.setLayout(new GridLayout(9, 9));
		for (int a = 8; a > -1; a--) {
			for (int b = 0; b < 9; b++) {
				jpan.add(filed[b][a]);
			}
		}
		add(jpan, BorderLayout.CENTER);
		JPanel jpb = new JPanel();
		JButton button1 = new JButton("calc");
		JButton button2 = new JButton("close");
		jpb.add(button1);
		jpb.add(button2);
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				synchronized (obj) {
					for (int a = 0; a < 9; a++) {
						for (int b3 = 0; b3 < 9; b3++) {
							int pp = 0;
							if (!(filed[a][b3].getText().trim().equals(""))) {
								pp = Integer.parseInt(filed[a][b3].getText().trim());
								Calculate.b[a][b3] = pp;
							}
						}
					}
				}
				synchronized (obj) {
					new Thread(new Calculate()).start();
				}
			}
		});

		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		add(jpb, BorderLayout.SOUTH);
	}
}

public class Sudoku {
	public static void main(String[] args) {
		Myframe myf = new Myframe();
		myf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myf.setTitle("sudoku");
		myf.setSize(500, 500);
		myf.setVisible(true);
	}
}


class Calculate implements Runnable {
	/*
	 *  Calculate ��ʵ���� Runnable �ӿڣ�ʵ���˶��̲߳������ڼ���ÿ�����ݵ�ʱ��������Ч�ʡ�
	 *  java �ж��߳�ʵ�� Runnable �� 
	 *  ���裺
	 *		1.����ʵ��Runnable�ӿ�
	 * 		2.����Runnable�ӿ��е�run���������߳�Ҫ���еĴ�������run�����С�
	 *		3.ͨ��Thread�ཨ���̶߳���
	 * 		4.��Runnable�ӿڵ����������Ϊʵ�ʲ������ݸ�Thread��Ĺ��캯����
	 *		5.����Thread���start���������̲߳�����Runnable�ӿ�����run������
	 */
	public static boolean[][] boo = new boolean[9][9];  //��ά���� boo �����жϸø��Ƿ�Ϊ�գ�����Ѿ���������ֵ���Ͳ��������ˡ�
	public static int upRow = 0;                       
	public static int upColumn = 0;
	public static int[][] b = new int[9][9];    //��ά���� b ���洢�Ź����е�����

	public static void flyBack(boolean[][] judge, int row, int column) {
		/*
		 * flyBack �������ڲ���û��������ֵ�Ŀո�
		 */
		int s = column * 9 + row;
		s--;
		int quotient = s / 9;
		int remainder = s % 9;
		if (judge[remainder][quotient]) {
			flyBack(judge, remainder, quotient);
		} else {
			upRow = remainder;
			upColumn = quotient;
		}
	}

	public static void arrayAdd(ArrayList<Integer> array, TreeSet<Integer> tree) {
		/*
		 * arrayAdd ���������µ���ֵ��1~9����һ���У���������Ѿ����ˣ�������û�оͼ�����ֵ��
		 * ���������Ĺ���ÿ��ÿ��ÿ��С�Ź���1~9�����ظ���������д arrayAdd() ����������tree��û�е�Ԫ�أ�������˾�������
		 */
		for (int z = 1; z < 10; z++) {
			boolean flag3 = true;
			Iterator<Integer> it = tree.iterator();
			while (it.hasNext()) {// 10
				int b = it.next().intValue();
				if (z == b) {
					flag3 = false;
					break;
				}
			}
			if (flag3) {
				array.add(new Integer(z));
			}
			flag3 = true;
		}
	}

	public static ArrayList<Integer> assume(int row, int column) {
		/*
		 * assume ��������Ź���ֳ�9��С�Ź�����Ҫ���ж���ͬ��ͬ��ͬһ��С�Ź�������Щ��ֵ�Ѿ�������ˣ����Ӹø�ѡ����ֵ�����Ǻ�ѡ����˼�롣
		 * Ϊ������㷨��Ч�ʣ����ǽ���Ź���ֳ�9��С�Ź���
		 * ��Ҫ�Ƿ�����ͬ��ͬ��ͬһ��С�Ź�������Щ��ֵ�Ѿ�������ˣ�Ȼ��ص��� arrayAdd() ���������Ӹø�ѡ����ֵ��
		 */
		ArrayList<Integer> array = new ArrayList<Integer>();
		TreeSet<Integer> tree = new TreeSet<Integer>();
		if (0 <= row && row <= 2 && 0 <= column && column <= 2) {
			for (int a = 0; a < 9; a++) {
				if (a != column && b[row][a] != 0) {
					tree.add(new Integer(b[row][a]));
				}
			}
			for (int b1 = 0; b1 < 9; b1++) {
				if (b1 != row && b[b1][column] != 0) {
					tree.add(new Integer(b[b1][column]));
				}
			}
			for (int a2 = 0; a2 < 3; a2++) {
				for (int b4 = 0; b4 < 3; b4++) {
					if ((!(a2 == row && b4 == column)) && b[a2][b4] != 0) {
						tree.add(new Integer(b[a2][b4]));
					}
				}
			}
			arrayAdd(array, tree);
		} else if (0 <= row && row <= 2 && 3 <= column && column <= 5) {
			for (int a = 0; a < 9; a++) {
				if (a != column && b[row][a] != 0) {
					tree.add(new Integer(b[row][a]));
				}
			}
			for (int b1 = 0; b1 < 9; b1++) {
				if (b1 != row && b[b1][column] != 0) {
					tree.add(new Integer(b[b1][column]));
				}
			}
			for (int a2 = 0; a2 < 3; a2++) {
				for (int b4 = 3; b4 < 6; b4++) {
					if ((!(a2 == row && b4 == column)) && b[a2][b4] != 0) {
						tree.add(new Integer(b[a2][b4]));
					}
				}
			}
			arrayAdd(array, tree);
		} else if (0 <= row && row <= 2 && 6 <= column && column <= 8) {
			for (int a = 0; a < 9; a++) {
				if (a != column && b[row][a] != 0) {
					tree.add(new Integer(b[row][a]));
				}
			}
			for (int b1 = 0; b1 < 9; b1++) {
				if (b1 != row && b[b1][column] != 0) {
					tree.add(new Integer(b[b1][column]));
				}
			}
			for (int a2 = 0; a2 < 3; a2++) {
				for (int b4 = 6; b4 < 9; b4++) {
					if ((!(a2 == row && b4 == column)) && b[a2][b4] != 0) {
						tree.add(new Integer(b[a2][b4]));
					}
				}
			}
			arrayAdd(array, tree);
		} else if (3 <= row && row <= 5 && 0 <= column && column <= 2) {
			for (int a = 0; a < 9; a++) {
				if (a != column && b[row][a] != 0) {
					tree.add(new Integer(b[row][a]));
				}
			}
			for (int b1 = 0; b1 < 9; b1++) {
				if (b1 != row && b[b1][column] != 0) {
					tree.add(new Integer(b[b1][column]));
				}
			}
			for (int a2 = 3; a2 < 6; a2++) {
				for (int b4 = 0; b4 < 3; b4++) {
					if ((!(a2 == row && b4 == column)) && b[a2][b4] != 0) {
						tree.add(new Integer(b[a2][b4]));
					}
				}
			}
			arrayAdd(array, tree);
		} else if (3 <= row && row <= 5 && 3 <= column && column <= 5) {
			for (int a = 0; a < 9; a++) {
				if (a != column && b[row][a] != 0) {
					tree.add(new Integer(b[row][a]));
				}
			}
			for (int b1 = 0; b1 < 9; b1++) {
				if (b1 != row && b[b1][column] != 0) {
					tree.add(new Integer(b[b1][column]));
				}
			}
			for (int a2 = 3; a2 < 6; a2++) {
				for (int b4 = 3; b4 < 6; b4++) {
					if ((!(a2 == row && b4 == column)) && b[a2][b4] != 0) {
						tree.add(new Integer(b[a2][b4]));
					}
				}
			}
			arrayAdd(array, tree);
		} else if (3 <= row && row <= 5 && 6 <= column && column <= 8) {
			for (int a = 0; a < 9; a++) {
				if (a != column && b[row][a] != 0) {
					tree.add(new Integer(b[row][a]));
				}
			}
			for (int b1 = 0; b1 < 9; b1++) {
				if (b1 != row && b[b1][column] != 0) {
					tree.add(new Integer(b[b1][column]));
				}
			}
			for (int a2 = 3; a2 < 6; a2++) {
				for (int b4 = 6; b4 < 9; b4++) {
					if ((!(a2 == row && b4 == column)) && b[a2][b4] != 0) {
						tree.add(new Integer(b[a2][b4]));
					}
				}
			}
			arrayAdd(array, tree);
		} else if (6 <= row && row <= 8 && 0 <= column && column <= 2) {
			for (int a = 0; a < 9; a++) {
				if (a != column && b[row][a] != 0) {
					tree.add(new Integer(b[row][a]));
				}
			}
			for (int b1 = 0; b1 < 9; b1++) {
				if (b1 != row && b[b1][column] != 0) {
					tree.add(new Integer(b[b1][column]));
				}
			}
			for (int a2 = 6; a2 < 9; a2++) {
				for (int b4 = 0; b4 < 3; b4++) {
					if ((!(a2 == row && b4 == column)) && b[a2][b4] != 0) {
						tree.add(new Integer(b[a2][b4]));
					}
				}
			}
			arrayAdd(array, tree);
		} else if (6 <= row && row <= 8 && 3 <= column && column <= 5) {
			for (int a = 0; a < 9; a++) {
				if (a != column && b[row][a] != 0) {
					tree.add(new Integer(b[row][a]));
				}
			}
			for (int b1 = 0; b1 < 9; b1++) {
				if (b1 != row && b[b1][column] != 0) {
					tree.add(new Integer(b[b1][column]));
				}
			}
			for (int a2 = 6; a2 < 9; a2++) {
				for (int b4 = 3; b4 < 6; b4++) {
					if ((!(a2 == row && b4 == column)) && b[a2][b4] != 0) {
						tree.add(new Integer(b[a2][b4]));
					}
				}
			}
			arrayAdd(array, tree);
		} else if (6 <= row && row <= 8 && 6 <= column && column <= 8) {
			for (int a = 0; a < 9; a++) {
				if (a != column && b[row][a] != 0) {
					tree.add(new Integer(b[row][a]));
				}
			}
			for (int b1 = 0; b1 < 9; b1++) {
				if (b1 != row && b[b1][column] != 0) {
					tree.add(new Integer(b[b1][column]));
				}
			}
			for (int a2 = 6; a2 < 9; a2++) {
				for (int b4 = 6; b4 < 9; b4++) {
					if ((!(a2 == row && b4 == column)) && b[a2][b4] != 0) {
						tree.add(new Integer(b[a2][b4]));
					}
				}
			}
			arrayAdd(array, tree);
		}
		return array;
	}

	public void run() {
		/*
		 * run ������ʼ�������������������Ľ����
		 * �� run() ��������д�ո�ĵط������ǵ��뷨�ǽ�һ��һ�еķ�����ÿ���㶼�����м���ֵ��
		 * ������һ������ utilization ��������п��ܵ�ֵ�������ֵ�Ļ�������д��һ���ո�
		 * ����д������ʱ����ݵ������дΪ utilization ���������һ��ֵ��
		 */
		for (int a = 0; a < 9; a++) {
			for (int b1 = 0; b1 < 9; b1++) {
				if (b[a][b1] != 0) {
					boo[a][b1] = true;
				} else {
					boo[a][b1] = false;
				}
			}
		}
		boolean flag = true;
		ArrayList<Integer>[][] utilization = new ArrayList[9][9];
		int row = 0;
		int column = 0;
		while (column < 9) {
			if (flag == true) {
				row = 0;
			}
			while (row < 9) {
				if (b[row][column] == 0) {
					if (flag) {
						ArrayList<Integer> list = assume(row, column);
						utilization[row][column] = list;
					}
					if (utilization[row][column].isEmpty()) {
						flyBack(boo, row, column);
						row = upRow;
						column = upColumn;
						b[row][column] = 0;
						column--;
						flag = false;
						break;
					} // if(list.isEmpty())
					else {
						b[row][column] = utilization[row][column].get(0);
						utilization[row][column].remove(0);
						flag = true;
						boolean r = true;
						for (int a1 = 0; a1 < 9; a1++) {
							for (int b1 = 0; b1 < 9; b1++) {
								if (r == false) {
									break;
								}
								if (b[a1][b1] == 0) {
									r = false;
								}
							}
						}
						if (r) {
							for (int a1 = 0; a1 < 9; a1++) {
								for (int b1 = 0; b1 < 9; b1++) {
									System.out.print("b[" + a1 + "][" + b1 + "]" + b[a1][b1] + ",");
									Myframe.filed[a1][b1].setText(b[a1][b1] + "");
								}
							}
						}
					}
				} // if(int[row][column]==0)
				else {
					flag = true;
				}
				row++;
			}
			column++;
		}
	}
}