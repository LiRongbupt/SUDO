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

import com.sun.corba.se.impl.logging.InterceptorsSystemException;

class Myframe extends JFrame 
{
	/**
	 * ����ܣ����������Ź������
	 */
	private static final long serialVersionUID = 4753076496951651267L;
	public static Object obj = new Object();
	
	//�����Ź������
	public final static JTextField[][] filed = new JTextField[9][9];
    
	public Myframe() 
	{
		/**
		 * ���ܣ���ʼ�����棬��81�����Ӷ���Ϊ��
		 */
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				filed[i][j] = new JTextField();
				filed[i][j].setText("");
			}
		}
		
		JPanel jpan = new JPanel();
		jpan.setLayout(new GridLayout(9, 9)); //9*9�����񲼾�
        //��textfield��ӵ�������
		for(int i = 0; i < 9; i++)
		{
			for(int j=0;j<9;j++)
			{
				jpan.add(filed[i][j]);
			}
		}
		 
		//���沼�־���
		add(jpan, BorderLayout.CENTER);
		
		//���������ť��������˳�
		JPanel jpb = new JPanel();
		JButton button1 = new JButton("����");
		JButton button2 = new JButton("�˳�");
		//����ť��ӵ�������
		jpb.add(button1);
		jpb.add(button2);
		
		//����ť���ʱ����Ӧ����
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				synchronized (obj) {
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {
							int value = 0;
							if (!(filed[i][j].getText().trim().equals(""))) {
								value = Integer.parseInt(filed[i][j].getText().trim());
								Calculate.b[i][j] = value;  //��ȡ�������������ֵ������������������ 
							}
						}
					}
				}
				synchronized (obj) {
					//�����̼߳����
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

public class Sudoku 
{
	public static void main(String[] args) 
	{
		Myframe myf = new Myframe();
		myf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myf.setTitle("sudoku");
		myf.setSize(500, 500);
		myf.setVisible(true);
	}
}


class Calculate implements Runnable 
{
	/**
	 *  Calculate ��ʵ���� Runnable �ӿڣ�ʵ���˶��̲߳������ڼ���ÿ�����ݵ�ʱ��������Ч�ʡ�
	 *  java �ж��߳�ʵ�� Runnable �� 
	 *  ���裺
	 *		1.����ʵ��Runnable�ӿ�
	 * 		2.����Runnable�ӿ��е�run���������߳�Ҫ���еĴ�������run�����С�
	 *		3.ͨ��Thread�ཨ���̶߳���
	 * 		4.��Runnable�ӿڵ����������Ϊʵ�ʲ������ݸ�Thread��Ĺ��캯����
	 *		5.����Thread���start���������̲߳�����Runnable�ӿ�����run������
	 */
	public static boolean[][] boo = new boolean[9][9];  
	//��ά���� boo �����жϸø��Ƿ�Ϊ�գ�����Ѿ���������ֵ���Ͳ��������ˡ�
	public static int upRow = 0;//����ָ���е�ֵ                       
	public static int upColumn = 0; //����ָ���е�ֵ
	public static int[][] b = new int[9][9];    //��ά���� b ���洢�Ź����е�����

	public static void flyBack(boolean[][] judge, int row, int column) {
		/*
		 * flyBack �������ڲ���û��������ֵ�Ŀո�
		 * ���ܣ�����ͬ�е���һ��Ԫ��ֵ�������Ϊ�գ���ֵ��upRow����upColumn
		 * �����Ϊ�գ������ݹ�
		 */
		int s = column * 9 + row;  //��ʱ����
		s--;
		
		int quotient = s / 9;  //ȡ�̵�ֵ��ʵ�ʾ���column��ֵ
		int remainder = s % 9; //ȡ����ֵ��ʵ����ȡ(row-1)%9
		if (judge[remainder][quotient]) { //�ж��Ƿ���������
			flyBack(judge, remainder, quotient);
		} else {
			upRow = remainder;
			upColumn = quotient;
		}
	}

	public static void arrayAdd(ArrayList<Integer> array, TreeSet<Integer> tree) {
		/*�������п��ܵ�ֵ
		 * arrayAdd ��������µ���ֵ��1~9����һ���У���������Ѿ����ˣ�������û�оͼ�����ֵ��
		 * ���������Ĺ���ÿ��ÿ��ÿ��С�Ź���1~9�����ظ���������д arrayAdd() ���������tree��û�е�Ԫ�أ�������˾�������
		 */
		
		for (int i = 1; i < 10; i++) 
		{
			boolean flag = true;  //�ж��Ƿ����������־
			Iterator<Integer> ite = tree.iterator(); //������
			//����tree
			while (ite.hasNext()) 
			{// 10
				int value = ite.next().intValue();
				if (i == value) 
				{
					flag = false;
					break;
				}
			}
			if (flag) 
			{ //��iû�г�����tree�У�������ӽ�tree
				array.add(new Integer(i));
			}
			flag = true;
		}
	}
	public static ArrayList<Integer> assume(int row, int column)
	{
		//��������array
		ArrayList<Integer> array = new ArrayList<Integer>();
		TreeSet<Integer> tree = new TreeSet<Integer>();
		
		//���ͬһ��������Ԫ��ֵ
		for(int i=0;i<9;i++)
		{
			//����ø�Ϊ�գ��ͽ�����ӵ�tree��
			if(i!=column&&b[row][i]!=0)
			{
				tree.add(new Integer(b[row][i]));
			}
		}
		
		//���ͬ�е�����Ԫ��
		for(int i=0;i<9;i++)
		{
			//����ø�������ӣ�����ӵ�tree��
			if(i!=row&&b[i][column]!=0)
			{
				tree.add(new Integer(b[i][column]));
			}
		}
		
		//��ȡԪ����ͬһ���Ź������
		for(int i=(row/3)*3;i<(row/3+1)*3;i++)
		{
			//��ȡԪ����ͬһ���Ź������
			for(int j=(column/3)*3;j<(column/3+1)*3;j++)
			{
				//���Ԫ��������������ӵ�tree��
				if((!(i==row&&j==column))&&b[i][j]!=0)
				{
					tree.add(new Integer(b[i][j]));
				}
			}
		}
		arrayAdd(array, tree);
		return array;
	}
	
	public void run()
	{
		//��ʼ�������У���
		int row =0, column =0;
		//��flag���жϸ����Ƿ�������ȷ
		boolean flag = true;
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				//boo���������ҳ��û��������ݵĿո�
				if(b[i][j]!=0)
				{
					boo[i][j]=true;
				}
				else
				{ //�ո�����Ҫ��������
					boo[i][j]=false;
				}
			}
		}
		
		/*
		 * arraylist��һ����ά���У�����ÿһ��ֵ����һ������ָ��
		 * �����ĳ�����ӿ��ܵĽ⣬��һ�������ʱ��������һ����
		 */
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[][] solution = new ArrayList[9][9];
		while(column<9)
		{
			if(flag==true)
			{
				row=0;
			}
			while(row<9)
			{
				if(b[row][column]==0)
				{
					if(flag)
					{
						ArrayList<Integer> list = assume(row, column);
						solution[row][column] = list;
					}
					
					//��û���ҵ����ܵĽ⣬��ǰ��Ľ���󣬻��ݵ�֮��ĸ���������
					if(solution[row][column].isEmpty())
					{
						//����flyback����Ѱ�Һ��ʵ�row��column
						flyBack(boo, row, column);
						//��row���ص����ʵ�λ��
						row = upRow;
						column = upColumn;
						
						//��ʼ��������ĸ���
						b[row][column] = 0;
						column--;
						flag = false;
						break;
					}
					else 
					{
						//����ѡ�����еĵ�һ��ֵ����b
						b[row][column] = solution[row][column].get(0);
						//��Ϊ�����Ѿ���ֵ���ˣ����Ծ�ɾ������һ����ֵ
						solution[row][column].remove(0);
						flag=true;
						
						//�ж��Ƿ����еĸ��Ӷ�������ȷ��Ȼ����ȷ�Ľ���������Ļ��
						judge();
					}
				}
				else
				{
					//���rΪfalse������ӻ�û����������
					flag = true;
				}
				row++;
			}
			column++;
		}
		
	}
	public void judge()
	{
		/**
		 * �жϾŹ����Ƿ����
		 */
		
		boolean r = true;
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				if(r==false)
				{
					break;
				}
				
				//���b[i][j]��Ҫ���㣬������ȡ����
				if(b[i][j]==0)
				{
					r=false;
				}
			}
		}
		
		//���rΪtrue�������еĸ��Ӷ�������
		if(r)
		{
			for(int i=0;i<9;i++)
			{
				for(int j=0;j<9;j++)
				{
					Myframe.filed[i][j].setText(b[i][j]+"");
				}
			}
		}
	}
}