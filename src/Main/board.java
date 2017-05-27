package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.crypto.interfaces.PBEKey;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.synth.SynthStyle;

import org.ietf.jgss.Oid;

public class board extends JPanel{
// 画一个棋盘
	static int blackplayer = 1;
	static int whiteplayer = -1;
	static int totalStep = 4;  //一开始只有4步
 	
	private static int interval;  // the space between two lines
	boolean isOver = false;
	boolean isBlack = true;  // black first
	private int count = 0;   // 棋子的数目
	int xindex,yindex;   // the chess present location
	Image img;   // loading the images from local
	Image tmp;   
	private Image backgroud= Toolkit.getDefaultToolkit().getImage("image/棋盘.png");

	private Image whiteChess = Toolkit.getDefaultToolkit().getImage("image/White.png");
	private Image newWhite = Toolkit.getDefaultToolkit().getImage("image/newWhite.png");
	private Image newBlack = Toolkit.getDefaultToolkit().getImage("image/newBlack.png");
	private Image blank = Toolkit.getDefaultToolkit().getImage("image/blank.png");
	private Image blackChess = Toolkit.getDefaultToolkit().getImage("image/Black.png");
	
	
<<<<<<< HEAD
	public volatile int blackNums = 0,whiteNums = 0; 
=======
	int blackNums,whiteNums; 
>>>>>>> 0df94aed59b0cdbdc2c1734ae4eeead9a75b3226
	int imgWidth,imgHeight;
	int chessWidth,chessHeight;
	int winWidth,winHeight;
    int eachWidth;
    int eachHeight;
    
    private int nx = -1, ny = -1;  //对于新下的棋子，应该用带有十字号的棋子表示
	int [] rightUp = new int[2];
	int [] rightDown = new int[2];
	int [] leftup = new int[2];
	int [] leftdown = new int[2];

	
	private int[][] chessIndex = new int[8][8]; 
	
	public void setWBnums(int b,int w) {
		this.blackNums = b;
		this.whiteNums = w;
	}
	public void setNewChessIndex(int x,int y)
	{
		this.nx = x;
		this.ny = y;
	}
	//判断游戏完成与否函数
	public boolean isGameOver() {
		if(totalStep == 64)
			return true;
		else
			return blackNums == 0 || whiteNums == 0; 
	}
	public int GetWinner()
	{
		if(blackNums > whiteNums)
			return blackplayer;
		else if(blackNums == whiteNums)
			return 0;
		else {
			return whiteplayer;
		}
	}
	public void increaseBlack()
	{
		blackNums ++;
	}
	public void inceaseWhite()
	{
		whiteNums ++;
	}
	public int getEachWidth()
	{
		return eachWidth;
	}
	
	public int getEachHeight() {
		return eachHeight;
	}
    public int[][] getSatus()
    {
    	return chessIndex;
    }
    
    
    public void setStatus(int [][] BoardStatus)
    {
		for(int i = 0; i < 8 ; i++)
		{
			for(int j = 0; j < 8; j ++)
			{
				chessIndex[i][j] = BoardStatus[i][j];
			}
		}
    }
	public void IncreaseStep()
	{
		totalStep ++;
	}
	
	public board() {
		// TODO Auto-generated constructor stub
		
		if(backgroud == null)
		{
			System.out.println("loading image success\n");
		}
	    imgWidth = backgroud.getWidth(this);  
	    imgHeight = backgroud.getHeight(this);
	    chessWidth = blank.getWidth(this);
	    chessHeight = blank.getHeight(this);
        winWidth = this.getWidth();
        winHeight = this.getHeight();
        eachWidth = winHeight / 10;
	    eachHeight = winHeight /10;
	  //  System.out.println(eachWidth+" "+eachHeight);
	    for(int i = 0; i < 8; i++)
	    {
	    	for(int j = 0; j < 8; j++)
	    	{
	    		chessIndex[i][j] = 0;
	    	}
	    }
	   
	}
	public void reset()
	{
		for(int i =0; i < 8;i++)
		{
			for(int j = 0; j < 8; j++)
			{
				chessIndex[i][j] = 0;
			}
		}
		totalStep = 4;
	}
	public boolean checkPlayable(int player)
	{
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j ++)
			{
				if(chessIndex[i][j] == 0)  //没有空的位置
				{
					// 首先考察落子周围是否有对方的旗子，至少棋子周围8个角落中有一个需要是对方的棋子
					if(playable(i, j, player,false))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	@Override
	public void repaint() {
			// TODO Auto-generated method stub
			super.repaint();
	}
	public void paint(Graphics g)  
    {  
        super.paint(g);
    //    System.out.println("paint happens");
        winWidth = this.getWidth();
        winHeight = this.getHeight();
	    imgWidth = backgroud.getWidth(this);  
	    imgHeight = backgroud.getHeight(this);
	    chessWidth = blank.getWidth(this);
	    chessHeight = blank.getHeight(this);
	    eachWidth = winWidth/ 10;
	    eachHeight = winHeight /10;
	//    System.out.println(eachWidth+" "+eachHeight);
	//    System.out.println(winWidth+" "+winHeight);
    //   g.drawImage(backgroud, 0, 0,null);
        g.drawImage(backgroud, 0, 0, winWidth, winHeight, 0, 0,imgWidth, imgHeight, null);
 //     g.drawImage(blank, 0, 0, winWidth, winHeight, 0, 0,chessWidth, chessHeight, null);
	    for(int i = 0; i < 8; i++)
	    {
	    	for(int j = 0; j < 8; j++)
	    	{
	    		if(chessIndex[i][j]==0)
	    		{
	    			g.drawImage(blank, eachWidth*(j+1),eachHeight*(i+1), eachWidth*(j+2), eachHeight*(i+2),  0, 0,chessWidth, chessHeight,this);
	    		}
	    		else if(chessIndex[i][j] == blackplayer)
	    		{ //画黑棋
	    			if(i != nx || j != ny)
	    				g.drawImage(blackChess, eachWidth*(j+1),eachHeight*(i+1), eachWidth*(j+2), eachHeight*(i+2),  0, 0,chessWidth, chessHeight,this);
	    			else{
	    			//	System.out.println("newblack comes");
	    				g.drawImage(newBlack, eachWidth*(j+1),eachHeight*(i+1), eachWidth*(j+2), eachHeight*(i+2),  0, 0,chessWidth, chessHeight,this);
	    				
	    			}
	    		}
	    		else if(chessIndex[i][j] == whiteplayer)
	    		{
	    			if(i != nx || j != ny)
	    				g.drawImage(whiteChess, eachWidth*(j+1),eachHeight*(i+1), eachWidth*(j+2), eachHeight*(i+2),  0, 0,chessWidth, chessHeight,this);
	    			else
	    				g.drawImage(newWhite, eachWidth*(j+1),eachHeight*(i+1), eachWidth*(j+2), eachHeight*(i+2),  0, 0,chessWidth, chessHeight,this);
	    		}
	    	}
	    }
	//    this.nx = -1;
	//    this.ny = -1;
    
    }
    //下一步新的棋，调用这个函数更新画布
    public void newChessComes(int x,int y,int player) {
		//更新数组
    	if( x < 0 || x > 7 || y < 0 || y > 7 )
    		return ;
    	chessIndex[x][y] = player;
	}
    
    public void turnStatus(int oldx,int oldy, int newx,int newy)
    {
    	//将 (oldx,oldy) 与 (newx,newy) 之间的棋子翻转
    	int xincre,yincre;
    	if(newx > oldx)
    	{
    		xincre = 1;
    	}
    	else if(oldx == newx){
    		
    		xincre = 0;
    	}
    	else {
			xincre = -1;
		}
    	if(newy > oldy)
    	{
    		yincre = 1;
    	}
    	else if(oldy == newy){
    		
    		yincre = 0;
    	}
    	else {
			yincre = -1;
		}
		 oldx += xincre;
		 oldy += yincre;
    	do{
    		
    		 chessIndex[oldx][oldy] = - chessIndex[oldx][oldy];
    		 if( chessIndex[oldx][oldy] == blackplayer)
    		 {//翻转后是黑色
    			 blackNums ++;
    			 whiteNums --;
    		 }
    		 else{
    			 blackNums --;
    			 whiteNums ++;
    		 }
    		 oldx += xincre;
    		 oldy += yincre;
    	}while(oldx != newx || oldy != newy);
    }
    /*
     * according to the status,返回该位置上是否能下棋
     * 参数 player, 1表示黑方下棋，-1表示白方下棋  
     * author: Qzh
     * */
    boolean playable(int x,int y,int player,boolean change)
    {
    	boolean isable = false;
    	int blankcount = 0;
    	int opcount =0;
    	// 往右查看
    	int i,j;
    	
    	if( x < 0 || x > 7 || y < 0 || y > 7)
    	{
    		return false;
    	}
    	for(i = y + 1; i < 8; i++)
    	{

    		if(chessIndex[x][i] == 0)  //没有棋子跳出，不行
    		{
    			blankcount ++;
    		}
    		if(chessIndex[x][i] == player)
    			break;
    	}
    	if(blankcount == 0 && i < 8 && i != y+1) //这里得到 (x,y)到(x,i)之间的对方棋子都被翻转
    	{
    		isable = true;
    		if(change)
    			turnStatus(x, y, x, i);
    	}
    	blankcount = 0;
    	//往左查看
    	for(i = y - 1; i >= 0; i--)
    	{

    		if(chessIndex[x][i] == 0)  //没有棋子跳出，不行
    		{
    			blankcount ++;
    		}
    		if(chessIndex[x][i] == player)
    			break;
    	}
    	if(blankcount == 0 && i >= 0 && i != y-1) //这里得到 (x,i)到(x,y)之间的对方棋子都被翻转
    	{
    		isable = true;
    		if(change)
    			turnStatus(x, i, x, y);
    	}
    	blankcount = 0;
    	//往上查看
    	for(i = x - 1; i >= 0; i--)
    	{

    		if(chessIndex[i][y] == 0)  //没有棋子跳出，不行
    		{
    			blankcount ++;
    		}
    		if(chessIndex[i][y] == player)
    			break;
    	}
    	if(blankcount == 0 && i >= 0 && i != x-1) //这里得到 (i,y)到(x,y)之间的对方棋子都被翻转
    	{	
    		isable = true;
    		if(change)
    			turnStatus(i, y, x, y);
    	}
    	blankcount = 0;
    	//往下查看
    	for(i = x + 1; i < 8; i++)
    	{

    		if(chessIndex[i][y] == 0)  //没有棋子跳出，不行
    		{
    			blankcount ++;
    		}
    		if(chessIndex[i][y] == player)
    			break;
    	}
    	if(blankcount == 0 && i < 8 && i != x+1) //这里得到 (x,y)到(i,y)之间的对方棋子都被翻转
    	{
    		isable = true;
    		if(change)
    			turnStatus(x, y, i, y);
    	}
    	blankcount = 0;
    	
    	// 往右下角查看
    	for(i = x + 1, j = y + 1; i < 8 && j <8; i++,j++)
    	{
    		if(chessIndex[i][j] == 0)  //没有棋子跳出，不行
    		{
    			blankcount ++;
    		}
    		if(chessIndex[i][j] == player)
    			break;
    	}
    	if(blankcount == 0 && i < 8 && i != x+1 && j < 8 && j != y+1) //这里得到 (x,y)到(i,j)之间的对方棋子都被翻转
    	{	
    		isable = true;
    		if(change)
    			turnStatus(x, y, i, j);
    	}
    	blankcount = 0;
    	//往左上角查看
    	for(i = x - 1, j = y - 1; i >=0  && j >= 0; i--,j--)
    	{
    		if(chessIndex[i][j] == 0)  //没有棋子跳出，不行
    		{
    			blankcount ++;
    		}
    		if(chessIndex[i][j] == player)
    			break;
    	}
    	if(blankcount == 0 && i >=0 && i != x-1 && j >= 0 && j != y-1) //这里得到 (i,j)到(x,y)之间的对方棋子都被翻转
    	{
    		isable = true;
    		if(change)
    			turnStatus(i, j, x, y);
    	}
    	blankcount = 0;
    	//往右上角查看
    	for(i = x - 1, j = y + 1; i >= 0  && j < 8; i --,j ++)
    	{
    		if(chessIndex[i][j] == 0)  //没有棋子跳出，不行
    		{
    			blankcount ++;
    		}
    		if(chessIndex[i][j] == player)
    			break;
    	}
    	if(blankcount == 0 && i >= 0 && i != x - 1 && j < 8 && j != y + 1) //这里得到 (i,j)到(x,y)之间的对方棋子都被翻转
    	{
    		isable = true;
    		if(change)
    			turnStatus(i, j, x, y);
    	}
    	//往左下角查看
    	blankcount = 0;
    	for(i = x + 1, j = y - 1; i < 8  && j >= 0; i++,j--)
    	{
    		if(chessIndex[i][j] == 0)  //没有棋子跳出，不行
    		{
    			blankcount ++;
    		}
    		if(chessIndex[i][j] == player)
    			break;
    	}
    	if(blankcount == 0 && i < 8 && i != x + 1 && j >= 0 && j != y-1) //这里得到 (i,j)到(x,y)之间的对方棋子都被翻转
    	{
    		isable = true;
    		if(change)
    			turnStatus(i, j, x, y);
    	}
    	
    	return isable;
    	
    }
    
    public void setValueByindex(int x,int y,int player) {
		// TODO Auto-generated method stub
     	if( x < 0 || x > 7 || y < 0 || y > 7)
    	{
    		return ;
    	}
     	chessIndex[x][y] = player;
     	nx = x;
     	ny = y;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
	
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
	       JFrame  jf = new JFrame();  
	       board jp = new board();  
	       jp.setBounds(220, 220, 400, 400);
	       JPanel tmPanel = new JPanel();
	 //      tmPanel.setBounds(x, y, width, height);
	        jf.setBounds(200, 200, 405, 430);  
	        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	        jf.add(jp);  
	        jf.setVisible(true);  
		
	}

}
