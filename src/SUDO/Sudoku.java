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
	 * 类介绍：创建数独九宫格界面
	 */
	private static final long serialVersionUID = 4753076496951651267L;
	public static Object obj = new Object();
	
	//创建九宫格界面
	public final static JTextField[][] filed = new JTextField[9][9];
    
	public Myframe() 
	{
		/**
		 * 功能：初始化界面，将81个格子都置为空
		 */
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				filed[i][j] = new JTextField();
				filed[i][j].setText("");
			}
		}
		
		JPanel jpan = new JPanel();
		jpan.setLayout(new GridLayout(9, 9)); //9*9的网格布局
        //将textfield添加到布局中
		for(int i = 0; i < 9; i++)
		{
			for(int j=0;j<9;j++)
			{
				jpan.add(filed[i][j]);
			}
		}
		 
		//界面布局居中
		add(jpan, BorderLayout.CENTER);
		
		//添加两个按钮：计算和退出
		JPanel jpb = new JPanel();
		JButton button1 = new JButton("计算");
		JButton button2 = new JButton("退出");
		//将按钮添加到界面上
		jpb.add(button1);
		jpb.add(button2);
		
		//给按钮添加时间响应函数
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				synchronized (obj) {
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {
							int value = 0;
							if (!(filed[i][j].getText().trim().equals(""))) {
								value = Integer.parseInt(filed[i][j].getText().trim());
								Calculate.b[i][j] = value;  //读取界面中填入的数值，将其填入数独表中 
							}
						}
					}
				}
				synchronized (obj) {
					//开启线程计算答案
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
	 *  Calculate 类实现了 Runnable 接口，实现了多线程操作，在计算每格数据的时候可以提高效率。
	 *  java 中多线程实现 Runnable ： 
	 *  步骤：
	 *		1.定义实现Runnable接口
	 * 		2.覆盖Runnable接口中的run方法，将线程要运行的代码存放在run方法中。
	 *		3.通过Thread类建立线程对象。
	 * 		4.将Runnable接口的子类对象作为实际参数传递给Thread类的构造函数。
	 *		5.调用Thread类的start方法开启线程并调用Runnable接口子类run方法。
	 */
	public static boolean[][] boo = new boolean[9][9];  
	//二维数组 boo 用于判断该格是否为空，如果已经填入了数值，就不用再填了。
	public static int upRow = 0;//计算指定行的值                       
	public static int upColumn = 0; //计算指定列的值
	public static int[][] b = new int[9][9];    //二维数据 b 将存储九宫格中的数据

	public static void flyBack(boolean[][] judge, int row, int column) {
		/*
		 * flyBack 函数用于查找没有填入数值的空格
		 * 功能：计算同列的上一行元素值，如果它为空，则赋值给upRow，和upColumn
		 * 如果不为空，继续递归
		 */
		int s = column * 9 + row;  //临时变量
		s--;
		
		int quotient = s / 9;  //取商的值，实际就是column的值
		int remainder = s % 9; //取余数值，实际是取(row-1)%9
		if (judge[remainder][quotient]) { //判断是否满足条件
			flyBack(judge, remainder, quotient);
		} else {
			upRow = remainder;
			upColumn = quotient;
		}
	}

	public static void arrayAdd(ArrayList<Integer> array, TreeSet<Integer> tree) {
		/*遍历所有可能的值
		 * arrayAdd 函数添加新的数值（1~9）到一行中，如果数据已经有了，跳过，没有就继续赋值。
		 * 由于数独的规则，每行每列每个小九宫格1~9不能重复，所以填写 arrayAdd() 函数来添加tree中没有的元素，如果有了就跳过。
		 */
		
		for (int i = 1; i < 10; i++) 
		{
			boolean flag = true;  //判断是否符合条件标志
			Iterator<Integer> ite = tree.iterator(); //迭代器
			//遍历tree
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
			{ //若i没有出现在tree中，则将其添加进tree
				array.add(new Integer(i));
			}
			flag = true;
		}
	}
	public static ArrayList<Integer> assume(int row, int column)
	{
		//创建数组array
		ArrayList<Integer> array = new ArrayList<Integer>();
		TreeSet<Integer> tree = new TreeSet<Integer>();
		
		//添加同一列其他的元素值
		for(int i=0;i<9;i++)
		{
			//如果该格不为空，就将其添加到tree中
			if(i!=column&&b[row][i]!=0)
			{
				tree.add(new Integer(b[row][i]));
			}
		}
		
		//添加同行的其他元素
		for(int i=0;i<9;i++)
		{
			//如果该格满足添加，就添加到tree中
			if(i!=row&&b[i][column]!=0)
			{
				tree.add(new Integer(b[i][column]));
			}
		}
		
		//获取元素在同一个九宫格的行
		for(int i=(row/3)*3;i<(row/3+1)*3;i++)
		{
			//获取元素在同一个九宫格的列
			for(int j=(column/3)*3;j<(column/3+1)*3;j++)
			{
				//如果元素满足条件都添加到tree中
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
		//初始化变量行，列
		int row =0, column =0;
		//用flag来判断格子是否填入正确
		boolean flag = true;
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				//boo的作用是找出用户填入数据的空格
				if(b[i][j]!=0)
				{
					boo[i][j]=true;
				}
				else
				{ //空格子需要填入数据
					boo[i][j]=false;
				}
			}
		}
		
		/*
		 * arraylist是一个二维序列，它的每一个值都是一个数组指针
		 * 存放了某个格子可能的解，当一个解错误时，调用下一个解
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
					
					//若没有找到可能的解，则前面的解错误，回溯到之间的格子修正答案
					if(solution[row][column].isEmpty())
					{
						//调用flyback函数寻找合适的row和column
						flyBack(boo, row, column);
						//将row返回到合适的位子
						row = upRow;
						column = upColumn;
						
						//初始化有问题的格子
						b[row][column] = 0;
						column--;
						flag = false;
						break;
					}
					else 
					{
						//将备选数组中的第一个值赋给b
						b[row][column] = solution[row][column].get(0);
						//因为上面已经赋值过了，所以就删除掉第一个数值
						solution[row][column].remove(0);
						flag=true;
						
						//判断是否所有的格子都填入正确，然后将正确的结果输出到屏幕上
						judge();
					}
				}
				else
				{
					//如果r为false，则格子还没有填满数据
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
		 * 判断九宫格是否完成
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
				
				//如果b[i][j]需要计算，则将它提取出来
				if(b[i][j]==0)
				{
					r=false;
				}
			}
		}
		
		//如果r为true，则所有的格子都填满了
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