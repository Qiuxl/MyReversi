package Main;

import java.awt.Dialog;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.channels.SelectableChannel;
import java.sql.Time;
import java.time.format.TextStyle;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import layout.TableLayout;
import javax.swing.JComboBox;
public class WBGOGame extends JFrame{

	//����˫��˼����ʱ�䳤�� 10��
	static long timelimit = 3000;  
	
	volatile static int playerturn ; // 1��ʾ�ڷ�ִ�壬-1��ʾ�׷�ִ��,�����������ǰ׷������Ǻڷ�
	static int blockwidth;
	static int blockheight;
	static int blackplayer = 1;
	static int whiteplayer = -1;
	private ComputerPlayer computerPlayer;
	private static board gameboard = new board();
	private boolean isGameRunning = false;
	private Font TextStyle = new Font("΢���ź�", Font.BOLD, 20);
	private JButton black;
	private JButton white;
	
	static public JLabel blackLabel =null;
	static public JLabel whiteLabel = null;
	
	private JLabel first;
	private JComboBox<String> select;
	private JButton start;
	private JButton reset;
	
	private JComboBox<Integer> timeSelect = new JComboBox<Integer>();
	private Thread game;
	
	private JLabel thinkTime;
	private int firstPlayer;
	private boolean computerFisrt = false;
	private int comColor = whiteplayer;
	private int poersonColor = blackplayer;
	public WBGOGame() {
		// TODO Auto-generated constructor stub
		computerFisrt = false;
		timeSelect.addItem(1);
		timeSelect.addItem(2);
		timeSelect.addItem(3);
		timeSelect.addItem(4);
		timeSelect.addItem(5);
		timeSelect.addItem(6);
		timeSelect.addItem(7);
		timeSelect.addItem(8);
		timeSelect.addItem(9);
		timeSelect.addItem(10);
		timeSelect.addItem(15);
<<<<<<< HEAD
		timeSelect.addItem(30);
		
=======
		timeSelect.addItem(20);
>>>>>>> 0df94aed59b0cdbdc2c1734ae4eeead9a75b3226
		initializeMain();
		start.addActionListener(e ->
		{
			blackLabel.setText("2");
			whiteLabel.setText("2");
			initializeGame();
			game = new Thread(()->{
				beginGame();
			});
			game.start();
		});
		
		reset.addActionListener(e ->{
			isGameRunning = false;
			try {
				game.join(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			gameboard.reset();
			initializeGame();
			blackLabel.setText("2");
			whiteLabel.setText("2");
			timeSelect.setSelectedItem(3);
			select.setSelectedItem("��");
			
		});
	   // initializeSurface();
		gameboard.addMouseListener(new MouseListener() {

			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(playerturn == comColor)
					return ;  //�׷����壬�����Ч
				int xmouse = e.getX();
				int ymouse = e.getY();
				blockwidth = gameboard.getEachWidth();
				blockheight = gameboard.getEachHeight();
				int x = ymouse / blockwidth; //�����x�Ƕ�ά�����е��У�����֮������������������Կ�ͷ��ƫ�Ƶ�λ��
				int y = xmouse / blockheight;
				if( x < 1 || x > 8 || y < 1 ||y > 8)
					return;
				
				if(isGameRunning &&playerturn == poersonColor && gameboard.getSatus()[x-1][y-1]==0)
				{
					//System.out.println("black:"+gameboard.blackNums+" white: "+gameboard.whiteNums);
					//��Ҫ����û�����ӿ����µ������˫����Ҫ����
<<<<<<< HEAD
					
					if(gameboard.playable(x-1, y-1, poersonColor,true)) //�������������
					{
						gameboard.setValueByindex(x-1, y-1, poersonColor);
						if(poersonColor == blackplayer)
							gameboard.increaseBlack();
						else
							gameboard.inceaseWhite();
=======
					if(gameboard.playable(x-1, y-1, poersonColor,true)) //�������������
					{
						gameboard.setValueByindex(x-1, y-1, poersonColor);
						gameboard.increaseBlack();
						
>>>>>>> 0df94aed59b0cdbdc2c1734ae4eeead9a75b3226
						gameboard.setNewChessIndex(x-1, y-1);
					//	System.out.println("black:"+gameboard.blackNums+" white: "+gameboard.whiteNums);
						gameboard.repaint();
						playerturn = comColor;
						gameboard.IncreaseStep();
						blackLabel.setText(String.valueOf(gameboard.blackNums));
						whiteLabel.setText(String.valueOf(gameboard.whiteNums));
						//�Լ��º���,���߶Է�����������
						if(computerPlayer != null)
						{
							ComputerPlayer.lock1.lock();
							computerPlayer.setXY(x-1, y-1);
							ComputerPlayer.lock1.unlock();
							computerPlayer.setStartTime(System.currentTimeMillis());
						}
					}

				}
			}
		});
		
	}
	public void initializeGame() {
		//�м��ĸ��ӷֱ�Ϊ 
		System.err.println(16 >> 4 & 1);
		playerturn = blackplayer;
		gameboard.setValueByindex(3, 3, whiteplayer);
		gameboard.setValueByindex(3, 4, blackplayer);
		gameboard.setValueByindex(4, 3, blackplayer);
		gameboard.setValueByindex(4, 4, whiteplayer);
		gameboard.setWBnums(2, 2);  //��ʼ��ʱ��ڷ��Ͱ׷���Ϊ2��
		gameboard.repaint();
<<<<<<< HEAD
=======

>>>>>>> 0df94aed59b0cdbdc2c1734ae4eeead9a75b3226
	}
	public void beginGame() {
		int flag = 0;
		initializeGame();
		select.setEnabled(false);
		timeSelect.setEnabled(false);
		isGameRunning = true;
<<<<<<< HEAD
		computerPlayer = new ComputerPlayer(gameboard);
		// ����Ϸ����������ݽ�ȥ
=======
		computerPlayer = new ComputerPlayer(gameboard);// ����Ϸ����������ݽ�ȥ
>>>>>>> 0df94aed59b0cdbdc2c1734ae4eeead9a75b3226
		playerturn = blackplayer;
		//8*8 = 64 
		computerPlayer.setTimeLimit(timelimit);
		if(computerFisrt)
<<<<<<< HEAD
		{
			computerPlayer.setStartTime(System.currentTimeMillis());
			computerPlayer.setColor(blackplayer);
		}
		else{
			computerPlayer.setColor(whiteplayer);
		}
		Thread computer = new Thread(()->{
			computerPlayer.play();
		});
		computer.start();
		while(!gameboard.isGameOver() && isGameRunning )
		{
			if(playerturn == poersonColor)
			{
			//	System.out.println("its preson playing");
				//�жϺڷ��Ƿ����������
				if(!gameboard.checkPlayable(poersonColor))
				{
					System.out.println("black unable to play");
					if(gameboard.checkPlayable(comColor))
						playerturn = comColor;
					else
					{
						flag = 1;
						break;
					}
				}
				
			}
		}
		System.err.println("winner is " + gameboard.GetWinner());
		if(gameboard.isGameOver() == true || flag == 1){
					
			if(gameboard.GetWinner() == 0)
			{
=======
		{
			computerPlayer.setStartTime(System.currentTimeMillis());
			computerPlayer.setColor(blackplayer);
		}
		else{
			computerPlayer.setColor(whiteplayer);
		}
		Thread computer = new Thread(()->{
			computerPlayer.play();
		});
		computer.start();
		while(!gameboard.isGameOver() && isGameRunning )
		{
			if(playerturn == poersonColor)
			{
			//	System.out.println("its preson playing");
				//�жϺڷ��Ƿ����������
				if(!gameboard.checkPlayable(poersonColor))
				{
					System.out.println("black unable to play");
					if(gameboard.checkPlayable(comColor))
						playerturn = comColor;
					else
					{
						flag = 1;
						break;
					}
				}
				
			}
		}
		System.err.println("winner is " + gameboard.GetWinner());
		if(gameboard.isGameOver() == true || flag == 1){
					
			if(gameboard.GetWinner() == 0)
			{
>>>>>>> 0df94aed59b0cdbdc2c1734ae4eeead9a75b3226
				System.err.println("��ƽ�ˣ�����һ�����");
				new MessegeDialog(WBGOGame.this, new String("��ƽ�ˣ�����һ�����"));
			}
			else if(gameboard.GetWinner() == comColor)
			{//����Ӯ��
				
				System.err.println("������,��������!");
				new MessegeDialog(WBGOGame.this, new String("������,��������!"));
			}
			else if(gameboard.GetWinner() == poersonColor){
			//	System.err.println("66666,��Ӯ��!");
				new MessegeDialog(this, "66666,��Ӯ��!");
			}
		}
		computerPlayer.SetGameOver(true);  //������֮�����˼���߳��˳�
		isGameRunning = false;
		select.setEnabled(true);
		timeSelect.setEnabled(true);
		
		try {
			computer.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	void initializeSurface()
	{
		JPanel left = new JPanel();
		
		JButton black = new JButton(new ImageIcon(this.getClass().getResource("/image/Black.png")));
		black.setBorderPainted(false);
		JButton white = new JButton(new ImageIcon(this.getClass().getResource("/image/White.png")));
		white.setBorderPainted(false);
		
		black.setBounds(10, 10, 40, 40);
		left.add(black);
		this.add(left,"0,0");
		
	}
	
	void initializeMain(){
		double size[][] ={
				{0.25,TableLayout.FILL}, 
				{TableLayout.FILL}};
		Random r1 = new Random();
		int index = r1.nextInt(10);
		for(int i= 0; i < 10 ; i++)
		{
			index = r1.nextInt(10);
			System.out.print(index + " ");
		}
		TableLayout mtTableLayout = new TableLayout(size);
		JPanel left = new JPanel();
		left.setLayout(null);
<<<<<<< HEAD
	//	black = new JButton(new ImageIcon(this.getClass().getResource("/image/Black.png")));
		black = new JButton(new ImageIcon("./image/Black.png"));
		black.setBorderPainted(false);
		black.setBounds(10, 10, 35, 35);
	//	white = new JButton(new ImageIcon(this.getClass().getResource("/image/White.png")));
		
		white = new JButton(new ImageIcon("./image/White.png"));
		
=======
		black = new JButton(new ImageIcon(this.getClass().getResource("/image/Black.png")));
		black.setBorderPainted(false);
		black.setBounds(10, 10, 35, 35);
		white = new JButton(new ImageIcon(this.getClass().getResource("/image/White.png")));
>>>>>>> 0df94aed59b0cdbdc2c1734ae4eeead9a75b3226
		white.setBorderPainted(false);
		white.setBounds(10, 60, 35, 35);
		
		blackLabel = new JLabel("2");
		blackLabel.setFont(TextStyle);
		blackLabel.setBounds(70, 10, 50, 35);
		
		whiteLabel = new JLabel("2");
		whiteLabel.setFont(TextStyle);
		whiteLabel.setBounds(70, 60, 50, 30);
		
		select = new JComboBox<String>();
		
		select.addItem("����");
		select.addItem("��");
		select.setBounds(70, 110, 60, 40);
		select.setSelectedItem("��");
		select.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(select.getSelectedItem().equals("����"))
				{
					computerFisrt = true;
					comColor = blackplayer;
					poersonColor = whiteplayer;
				}
				else{
					computerFisrt = false;
					comColor = whiteplayer;
					poersonColor = blackplayer;
				}
			}
		});
		
		first = new JLabel("Who first: ");
		first.setFont(new Font("SanSerif", Font.BOLD, 14));
		first.setBounds(0, 110, 80, 40);
		
		thinkTime = new JLabel("time(s):");
		thinkTime.setFont(new Font("SanSerif", Font.BOLD, 14));
		thinkTime.setBounds(0, 160, 80, 40);
		
		timeSelect.setBounds(60, 160, 60, 40);
		timeSelect.setSelectedItem(3);
		timeSelect.addActionListener(e->{
			timelimit = (Integer) timeSelect.getSelectedItem() * 1000;
		});
		
		start = new JButton("start");
		start.setBorderPainted(false);
	//	start.setFont(new Font("΢���ź�", Font.BOLD, 8));
		start.setBounds(10, 210, 100, 40);
		
		reset = new JButton("Reset");
		reset.setBorderPainted(false);
		reset.setBounds(10, 260, 100, 40);
		left.add(select);
		left.add(first);
		left.add(black);
		left.add(blackLabel);
		left.add(white);
		left.add(whiteLabel);
		left.add(thinkTime);
		left.add(timeSelect);
		left.add(start);
		left.add(reset);
		
		this.setBounds(200, 200, 500, 410);
		this.setResizable(true);
		this.setLayout(mtTableLayout);
		this.add(gameboard,"1,0");
		this.add(left,"0,0");
		this.setResizable(false);
		this.setTitle("�ڰ����˻���ս");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	}
	public static void main(String[] args) {
	
//		double size[][] ={
//				{0.25,TableLayout.FILL}, 
//				{TableLayout.FILL}};
//		Random r1 = new Random();
//		int index = r1.nextInt(10);
//		for(int i= 0; i < 10 ; i++)
//		{
//			index = r1.nextInt(10);
//			System.out.print(index + " ");
//		}
//		TableLayout mtTableLayout = new TableLayout(size);
//		WBGOGame newWbgoGame = new WBGOGame();
//		
//		newWbgoGame.setBounds(200, 200, 500, 410);
//		newWbgoGame.setResizable(true);
//		newWbgoGame.setLayout(mtTableLayout);
//		
//		newWbgoGame.add(gameboard,"1,0");
//		newWbgoGame.setResizable(false);
//		newWbgoGame.setTitle("�ڰ����˻���ս");
//		newWbgoGame.setVisible(true);
//		newWbgoGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		newWbgoGame.initializeGame();
//		newWbgoGame.beginGame();
		
		WBGOGame newWbgoGame = new WBGOGame();
		newWbgoGame.initializeGame();
//		newWbgoGame.beginGame();
	}
}
