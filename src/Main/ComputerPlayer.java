package Main;

import java.nio.channels.ShutdownChannelGroupException;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.security.auth.callback.TextOutputCallback;
import javax.swing.RootPaneContainer;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.table.TableColumnModel;
import javax.xml.transform.Templates;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.IOP.TAG_ORB_TYPE;
import org.omg.PortableServer.ServantRetentionPolicyOperations;

//这是电脑方根据uct算法下棋的类
/**
 *author: Qiu Zh 
 *date: 04/03/2017
 *
 * */


class UctTree{
	
	private int x,y;  //节点的横纵坐标
	static final int blackplayer = 1;
	static final int whiteplayer = -1;
	public Vector<Chessindex> sun = null;
	private int[][] chessIndex = new int[8][8];
	int value; //节点表示黑色还是白色,即该步是属于哪个颜色下的
	private int total = 0, win = 0 ;  //分别表示此节点上赢了的次数和总共模拟的次数
	private int nextPlayer;  //记住下一步是谁下棋
	private int currentSunNums = 0;
	public int blackNum = 0, whiteNum = 0;
	
	public boolean isTerminal()
	{
		if(blackNum == 0 || whiteNum ==0 || blackNum + whiteNum == 64)
			return true;
		else 
			return false;
	}
	
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public int getCurrentSunNums()
	{
		return currentSunNums;
	}
	public void setFather(UctTree node)
	{
		this.father = node;
	}
	public boolean IssimulationOver()
	{
		if(blackNum == 0 || whiteNum ==0 || blackNum + whiteNum == 64)
			return true;
		else 
			return false;
	}
	public int getWinner()
	{
		if(blackNum > whiteNum)
			return blackplayer;
		else if(blackNum == whiteNum)
			return 0;
		else 
			return whiteplayer; //平局

	}
	
	public void changePlayer()
	{
		nextPlayer = -nextPlayer;
	}
	public int getNextPlayer()
	{
		return nextPlayer;
	}
// 计算回报公式如下
// x + sqrt(2*ln(n)/nj)
// nj是被选中的次数，n是总共选的次数
// 
	private UctTree leftMostChild = null;
	private UctTree rightSibling = null;
	private UctTree father = null;

//
	
	public UctTree(int across, int vertical, int value ,int [][] BoardStatus) {
		// TODO Auto-generated constructor stub
		this.x = across;
		this.y = vertical;
		this.value = value;
		for(int i = 0; i < 8 ; i++)
		{
			for(int j = 0; j < 8; j ++)
			{
				chessIndex[i][j] = BoardStatus[i][j];
				if(chessIndex[i][j] == blackplayer)
					blackNum ++;
				else if(chessIndex[i][j] == whiteplayer)
					whiteNum ++;
			}
		}
		if(across >=0 && across < 8 && vertical < 8 && vertical >= 0 && BoardStatus[across][vertical] == 0) //原来没有棋子
		{
			chessIndex[across][vertical] = value;
			if(value == blackplayer)
				blackNum ++;
			else
				whiteNum ++;
		
			if(this.playabaleRun(across, vertical, value));  //do nothing
		}
		nextPlayer = -value; //
		sun = this.findsun();
		if(sun.size() == 0 && this.isTerminal() == false)  //没有达到终止状态但是对方棋子没有可以下了
		{
			this.changePlayer();
			sun = findsun();
		}
	}
	public void showStatus()
	{
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
				System.out.print(chessIndex[i][j]+" ");
			System.out.println("\n");;
		}
	}
	public int wintimes() {
		return win;
	}
	public UctTree leftSon()
	{
		return leftMostChild;
	}
	public UctTree Sibiling() {
		return this.rightSibling;
	}
	public UctTree Father()
	{
		return this.father;
	}
	public void increaseWin()
	{
		this.win++;
	}
	public void descreaseWin()
	{
		win --;
	}
	public void increaseTotal()
	{
		this.total++;
	}
	public int[][] getChessStatus()
	{
		return this.chessIndex;
	}
	public int getTotal()
	{
		return total;
	}

	public int getvalue()
	{
		return value;
	}
	
	public UctTree findSonWithIndex(int x,int y) {
		System.out.println("running with finding son by index " + x + " "+y);
//		System.out.println(this.sun.size());
		if(leftMostChild == null)
			{
				System.err.println("son is null");
				return null;
			}
		else{
			UctTree tmp = this.leftMostChild;
			while(tmp != null)
			{
				System.out.println(tmp.getX() + "----" + tmp.getY());
				if(tmp.getX() == x &&tmp.getY() == y)
					break;
				tmp = tmp.Sibiling();
			}
		//	System.err.println("found with "+ tmp.getX() + " "+tmp.getY());
			return tmp;
		}
	}
	// 需要注意的是这里的x的低8位包含着翻转的信息
	//使用这一步的时候必定是可以下棋的位置
	public void play(int x, int y) {
		int tmp = x >> 8;
		chessIndex[tmp][y] = nextPlayer;
		int x1 = tmp;
		int y1 = y;
		int totalchange = 0;                                                                                                                                                                                           
		if( (x & 1) == 1)
		{
			//最低位为1，正面头顶上的棋子有需要翻转的
			x1 -- ;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				x1--;
				totalchange ++;
			}while(x1 >= 0 && chessIndex[x1][y1] != nextPlayer);
		}
		x1 = tmp;
		// 右移1位如果最后一位为1证明右上角有需要翻转的棋子
		if( ( x>> 1 & 1) == 1)
		{
			x1--;
			y1++;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				x1 --;
				y1 ++;
			}while(x1 >=0 && y1 < 8 && chessIndex[x1][y1] != nextPlayer);
		}
		x1 = tmp;
		y1 = y;
		if( (x >> 2 & 1) == 1)
		{
			//右边有需要翻转的棋子
			y1 ++;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				y1 ++;
			}while(y1 < 8 && chessIndex[x1][y1] != nextPlayer);
		}
		y1 = y;
		if( (x >> 3 & 1) == 1)
		{
			//右下角有需要翻转的棋子
			x1 ++;
			y1 ++;
			do{

				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				x1 ++;
				y1 ++;
			}while(x1 < 8 &&y1 < 8 && chessIndex[x1][y1] != nextPlayer);
		}
		x1 = tmp;
		y1 = y;
		if(( x >> 4 & 1) == 1)
		{
			//往下方看
			x1 ++;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				x1 ++;
			}while(x1 < 8 && chessIndex[x1][y1] != nextPlayer);
		}
		x1 = tmp;
		if(( x >> 5 & 1) == 1)
		{
			//左下角有棋子需要翻转
			x1++;
			y1--;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				x1++;
				y1--;
			}while(x1 < 8 && y1 >= 0 && chessIndex[x1][y1] != nextPlayer);
		}
		x1 = tmp;
		y1 = y;
		if(( x >> 6 & 1) == 1)
		{
			//左边有棋子需要翻转
			y1--;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				y1--;
			}while( y1 >0 && chessIndex[x1][y1] != nextPlayer);
		}
		y1 = y;
		if(( x >> 7 & 1) == 1)
		{
			//左上角有棋子需要翻转
			x1--;
			y1--;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				x1--;
				y1--;
			}while( x1 > 0 && y1 >0 && chessIndex[x1][y1] != nextPlayer);
		}
	
		if(nextPlayer == blackplayer)
		{
			blackNum += totalchange;
			blackNum ++;
			whiteNum -= totalchange;
		}
		else{
			whiteNum += totalchange;
			whiteNum ++;
			blackNum -= totalchange;
		}
	}
	public void StimulatePlay(int x,int y)
	{
		int tmp = x >> 8;
		chessIndex[tmp][y] = nextPlayer;
		int x1 = tmp;
		int y1 = y;
		int totalchange = 0;                                                                                                                                                                                           
		if( (x & 1) == 1)
		{
			//最低位为1，正面头顶上的棋子有需要翻转的
			x1 -- ;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				x1--;
				totalchange ++;
			}while(x1 >= 0 && chessIndex[x1][y1] != nextPlayer);
		}
		x1 = tmp;
		// 右移1位如果最后一位为1证明右上角有需要翻转的棋子
		if( ( x>> 1 & 1) == 1)
		{
			x1--;
			y1++;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				x1 --;
				y1 ++;
			}while(x1 >=0 && y1 < 8 && chessIndex[x1][y1] != nextPlayer);
		}
		x1 = tmp;
		y1 = y;
		if( (x >> 2 & 1) == 1)
		{
			//右边有需要翻转的棋子
			y1 ++;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				y1 ++;
			}while(y1 < 8 && chessIndex[x1][y1] != nextPlayer);
		}
		y1 = y;
		if( (x >> 3 & 1) == 1)
		{
			//右下角有需要翻转的棋子
			x1 ++;
			y1 ++;
			do{

				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				x1 ++;
				y1 ++;
			}while(x1 < 8 &&y1 < 8 && chessIndex[x1][y1] != nextPlayer);
		}
		x1 = tmp;
		y1 = y;
		if(( x >> 4 & 1) == 1)
		{
			//往下方看
			x1 ++;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				x1 ++;
			}while(x1 < 8 && chessIndex[x1][y1] != nextPlayer);
		}
		x1 = tmp;
		if(( x >> 5 & 1) == 1)
		{
			//左下角有棋子需要翻转
			x1++;
			y1--;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				x1++;
				y1--;
			}while(x1 < 8 && y1 >= 0 && chessIndex[x1][y1] != nextPlayer);
		}
		x1 = tmp;
		y1 = y;
		if(( x >> 6 & 1) == 1)
		{
			//左边有棋子需要翻转
			y1--;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				y1--;
			}while( y1 >0 && chessIndex[x1][y1] != nextPlayer);
		}
		y1 = y;
		if(( x >> 7 & 1) == 1)
		{
			//左上角有棋子需要翻转
			x1--;
			y1--;
			do{
				chessIndex[x1][y1] = -chessIndex[x1][y1];
				totalchange ++;
				x1--;
				y1--;
			}while( x1 > 0 && y1 >0 && chessIndex[x1][y1] != nextPlayer);
		}
	
	}
	public Vector<Chessindex> findsun() 
	{
		Vector<Chessindex> result = new Vector<Chessindex>();
		int temp;
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j ++)
			{
				if(chessIndex[i][j] == 0)  //没有空的位置
				{
					// 首先考察落子周围是否有对方的旗子，至少棋子周围8个角落中有一个需要是对方的棋子
					temp = playable(i, j, nextPlayer);
					if(temp != -1)
					{
						result.add(new Chessindex(temp, j));
					}
				}
			}
		}
		return result;
	}	
	public boolean isleaf()
	{
		if(leftMostChild == null)
		{
			return true;
		}
		return false;	
	}
	public boolean isFullyExpand()
	{
		if(this.sun.size() == currentSunNums)
			return true;
		else 
			return false;
	}
	public UctTree expand()
	{
	//	System.out.println("running with expand");
		UctTree tmp = new UctTree(sun.get(currentSunNums).x >> 8, sun.get(currentSunNums).y, nextPlayer,chessIndex);
		this.insertNode(tmp);
		currentSunNums++;
		return tmp;
	}
//根据棋盘状态和玩家计算下一步能下棋的位置坐标
//
//将自己的模拟的新的结果反馈上去
	void backup()
	{
	//	System.err.println("backing up");
	//	int player = this.value;
		UctTree tmp = this.father;
		while(tmp != null)
		{
			tmp.increaseTotal();
			if(tmp.getvalue() == value && this.win > 0 || tmp.getvalue() == -value && win < 0)  //节点与某一个祖宗节点颜色相同，并且模拟节点赢了
				tmp.increaseWin();
			else if(tmp.getvalue() == value && this.win < 0 || tmp.getvalue() == -value && win > 0)
				tmp.descreaseWin();
			tmp = tmp.Father();
		}
		
	}
	/*function: 在该节点的下一层插入新的节点
	 *author: Qiu Zenghui
	 *date: 04/05/2017 00:30
	 * */
	public void insertNode(UctTree tmp) {
		if(this.leftMostChild == null)
			{
				this.leftMostChild = tmp;
				tmp.father = this;
			}
			
		else{
			UctTree tail = this.leftMostChild;
			while(tail!=null && tail.rightSibling != null)
			{
				tail = tail.rightSibling;
			}
			tail.rightSibling = tmp;
			tmp.father = this;
		}
	}
	
	void setStatus(int chess[][])
	{
		int i,j;
		for(i = 0; i < 8; i++)
		{
			for(j = 0; j < 8; j++)
			{
				chessIndex[i][j] = chess[i][j];
			}
		}
	}
// 这个 int 数字中必须保存哪些方向上是可以颠覆的
	int playable(int x,int y,int player)
    {
    	boolean isable = false;
    	int blankcount = 0;
    	int opcount =0;
    	// 往右查看
    	int i,j;
    	int turnRecord = 0; //用于记录存在颜色反转的方向
    	if( x < 0 || x > 7 || y < 0 || y > 7)
    	{
    		return -1;
    	}
    	//右边查看
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
    		turnRecord += (1 << 2); 
    	}
    	//往左查看
    	blankcount = 0;
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
    		turnRecord += (1 << 6);    //左边是左移动 6
    	}
    	blankcount = 0;
    	//往上查看
    	// 从上方开始顺时针，用8个bit依次表示  1 1 1 1 1 1 1 1
    	// 
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
    		turnRecord += 1;
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
    	if(blankcount == 0 && i < 8 && i != x+1) //这里得到 (i,y)到(x,y)之间的对方棋子都被翻转
    	{
    		isable = true;
    		turnRecord += (1 << 4);
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
    		turnRecord += ( 1 << 3);
    	}
    	
    	//往左上角查看
    	blankcount = 0;
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
    		turnRecord += (1 << 7);
    	}
    	
    	//往右上角查看
    	blankcount = 0;
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
    		turnRecord += (1 << 1);
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
    		turnRecord += (1 << 5);
    	}
    	
    	if(turnRecord == 0)
    		return -1;
    	else
    		return (x << 8) + turnRecord;   //因此翻转记录保存在低8位中
    }
    boolean playabaleRun(int x,int y,int player)
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
    		turnStatus(i, j, x, y);
    	}
    	
    	return isable;
    	
    }
    public void turnStatus(int oldx,int oldy, int newx,int newy)
    {
    	//将 (oldx,oldy) 与 (newx,newy) 之间的棋子翻转
    //	System.out.print("("+oldx +" "+ oldy+ ")");
  //  	System.out.print("("+newx +" "+ newy+ ")");
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
    			 blackNum ++;
    			 whiteNum --;
    		 }
    		 else{
    			 blackNum --;
    			 whiteNum ++;
    		 }
    		 oldx += xincre;
    		 oldy += yincre;
    		 
    	}while(oldx != newx || oldy != newy);
    	
    }
}

class UCT{
	private UctTree root = null;
	private int timeLeft = 0;
	private Vector<Integer> v1 = new Vector<Integer>();
	private Vector<Integer> v2 = new Vector<Integer>();
	private Vector<Integer> v3 = new Vector<Integer>();
	private Vector<Integer> v4 = new Vector<Integer>();
	private Vector<Integer> v5 = new Vector<Integer>();
	private Vector<Integer> v6 = new Vector<Integer>();
	private Vector<Integer> v7 = new Vector<Integer>();
	private Vector<Integer> v8 = new Vector<Integer>();
	private Vector<Integer> v9 = new Vector<Integer>();
	private Vector<Integer> prefer = new Vector<Integer>();
	private Vector<Integer> bad = new Vector<Integer>();
	//权重矩阵
	public static final int[][] weightMatrix = {
	        {99, -8, 8, 6, 6, 8, -8, 99},
	        {-8, -24, -4, -3, -3, -4, -24, -8},
	        {8, -4, 7, 4, 4, 7, -4, 8},
	        {6, -3, 4, 0, 0, 4, -3, 6},
	        {6, -3, 4, 0, 0, 4, -3, 6},
	        {8, -4, 7, 4, 4, 7, -4, 8},
	        {-8, -24, -4, -3, -3, -4, -24, -8},
	        {99, -8, 8, 6, 6, 8, -8, 99},
	};

	private static Random randomSeed = new Random();
	public UctTree getRoot()
	{
		return root;
	}
	public void setroot(UctTree root)
	{
		this.root = root;
	}
	// 这里是uct策略中expand的结合，即是一次性把所有能扩展的子节点全部扩展掉
	// 这里只会在选中该节点并且节点被模拟过了或者节点是根节点的时候才会执行expand
	//这个函数并没有使用到
	public void createSun(UctTree tmproot){
		
		if(!tmproot.isleaf())
			return;
		Vector<Chessindex> tmp = tmproot.findsun();
		for(Chessindex index : tmp)
		{
			tmproot.insertNode(new UctTree(index.x >> 8, index.y, -tmproot.getvalue(),tmproot.getChessStatus()));
		}
		
	}
	int selectPolicy(Vector<Chessindex> tmp,UctTree node)
	{
		// 从可以下的位置中选出最后的棋子
		int index;
		int tempWeight;
		v1.clear();
		v2.clear();
		v3.clear();
		v4.clear();
		v5.clear();
		v6.clear();
		v7.clear();
		v8.clear();
		v9.clear();
		prefer.clear();
		bad.clear();
		int result;
		for(index = 0; index < tmp.size(); index ++)
		{
			tempWeight = weightMatrix[tmp.get(index).x>>8][tmp.get(index).y];
			if(tempWeight == 99)
			{
				v1.add(index);
			}
			else if(tempWeight == 8)
			{
				v2.add(index);
			}
			else if(tempWeight == 7)
			{
				v3.add(index);	
			}
			else if(tempWeight == 6)
			{
				v4.add(index);	
			}
			else if(tempWeight == 4)
			{
				v5.add(index);	
			}
			else if(tempWeight == -3)
			{
				v6.add(index);
			}
			else if(tempWeight == -4)
			{
				v7.add(index);	
			}
			else if(tempWeight == -8)
			{
				v8.add(index);	
			}
			else if(tempWeight == -24)
			{
				v9.add(index);	
			}
		}
		if(v1.size() > 0)
		{
			result = v1.get(randomSeed.nextInt(v1.size()));
			return result;
		}
		int [][] tempChess = new int [8][8];
		for(int i = 0; i < 8 ; i++)
		{
			for(int j = 0; j < 8 ; j++)
			{
				tempChess[i][j] = node.getChessStatus()[i][j];
			}
		}
		if(v2.size() > 0 )
		{ //边上的
			for(Integer t : v2)
			{
	//			node.play(tmp.get(t).x, tmp.get(t).y);
				node.StimulatePlay(tmp.get(t).x, tmp.get(t).y);
				if(isPrefer(node.getChessStatus(), tmp.get(t).x >> 8, tmp.get(t).y))
					prefer.add(t);
				else
					bad.add(t);
				node.setStatus(tempChess);
			//	node.changePlayer();
			}
		}
		if(v4.size() > 0)
		{
			for(Integer t : v4)
			{
				node.StimulatePlay(tmp.get(t).x, tmp.get(t).y);
				if(isPrefer(node.getChessStatus(), tmp.get(t).x >> 8, tmp.get(t).y))
					prefer.add(t);
				else
					bad.add(t);
				node.setStatus(tempChess);
			//	node.changePlayer();
			}
		}
		if(v8.size() > 0)
		{
			for(Integer t : v8)
			{
				node.StimulatePlay(tmp.get(t).x, tmp.get(t).y);
				if(isPrefer(node.getChessStatus(), tmp.get(t).x >> 8, tmp.get(t).y))
					prefer.add(t);
				else
					bad.add(t);
				node.setStatus(tempChess);
			}
		}
		if(prefer.size() > 0)
		{
			return prefer.get(randomSeed.nextInt(prefer.size()));
		}
		if(v3.size() > 0)
		{
			result = v3.get(randomSeed.nextInt(v3.size()));
			return result;
		}
		if(v5.size() > 0)
		{
			result = v5.get(randomSeed.nextInt(v5.size()));
			return result;
		}
		if(v6.size() > 0)
		{
			result = v6.get(randomSeed.nextInt(v6.size()));
			return result;
		}
		if(v7.size() > 0)
		{
			result = v7.get(randomSeed.nextInt(v7.size()));
			return result;
		}
		if(v9.size() > 0)
		{
			result = v9.get(randomSeed.nextInt(v9.size()));
			return result;
		}
		if(bad.size() > 0)
		{
			result = bad.get(randomSeed.nextInt(bad.size()));
			return result;
		}
		return -1;
	}
	
	Boolean isPrefer(int chess[][],int x,int y)
	{
		int player = chess[x][y];
		int flag;
		int i,j;
		flag = 0;
		if(x == 0 || x == 7)  //出现在第一行或者最后一行
		{
			for( j = y-1; j >= 0; j--)
			{
				if(chess[x][j] == player)
					continue;
				if(chess[x][j] == -player)
				{ 
					flag ++;
					break;
				}
				if(chess[x][j] == 0)  //空的跳出
					break;
			}
			if(j == -1) //一直到角上都是自己的，那么直接返回true
				return true;
			for( j = y + 1; j < 8; j++)
			{
				if(chess[x][j] == player)
					continue;
				if(chess[x][j] == -player)
				{ 
					flag ++;
					break;
				}
				if(chess[x][j] == 0)  //空的跳出
					break;
			}
			if( j == 8)
				return true;
			if(flag == 2 || flag == 0) //两边都没有对方棋子
				return true;
			else //一边有
				return false;
		}
		//第一列或者最后一列
		else if(y == 0 || y == 7)
		{
			flag = 0;
			for( i = x-1; i >= 0; i--)
			{
				if(chess[i][y] == player)
					continue;
				if(chess[i][y] == -player)
				{ 
					flag ++;
					break;
				}
				if(chess[i][y] == 0)  //空的跳出
					break;
			}
			if(i == -1) //一直到角上都是自己的，那么直接返回true
				return true;
			for( i = x+1; i < 8; i++)
			{
				if(chess[i][y] == player)
					continue;
				if(chess[i][y] == -player)
				{ 
					flag ++;
					break;
				}
				if(chess[i][y] == 0)  //空的跳出
					break;
			}
			if(i == 8) //一直到角上都是自己的，那么直接返回true
				return true;
			if(flag == 2 || flag == 0) //两边都没有对方棋子
				return true;
			else //一边有
				return false;
		}
		return false;
	}
	// 从一个节点开始模拟
	public void defultPolicy(UctTree node)
	{
		//考察可以下的位置
	//	node.showStatus();
	//	System.out.println("stimulation begin");
		int i,index,x,index_1;
		Vector<Chessindex> tmp = null;
		int changeTimes = 0;
		//System.err.println(node.getX() +" +++ "+node.getY());
	//	if(node.playabaleRun(node.getX(), node.getY(), -node.getNextPlayer()));
		//	System.out.println("initial success"); 
		Random r1 = new Random(randomSeed.nextLong());  //用一串随机数作为种子，保证每次的随机数不一样
		int  tempWeight;
		Vector<Integer> position = new Vector<Integer>();
		while(node.IssimulationOver() == false)
		{

			tmp = node.findsun();
			index = -1;
			index_1 = -1;
			tempWeight = -100;
			if(tmp!=null && tmp.size() != 0)
			{ //如果有棋子可以下
				position.clear();
				for(index = 0; index < tmp.size(); index ++)
				{
					if(weightMatrix[tmp.get(index).x >> 8][tmp.get(index).y] > tempWeight)
					{
						position.clear();
						position.add(index);
						tempWeight = weightMatrix[tmp.get(index).x>>8][tmp.get(index).y];
					//	index_1 = index;
					}
					if(weightMatrix[tmp.get(index).x >> 8][tmp.get(index).y] == tempWeight)
					{
						position.add(index);
					}
				}
				index = r1.nextInt(position.size());
				index = position.get(index);
//				index = selectPolicy(tmp, node);
				if(index == -1)
				{
					System.err.println("error happen in defaul policy");
					System.exit(0);
				}
				node.play(tmp.get(index).x, tmp.get(index).y);
				
				node.changePlayer();
				changeTimes = 0;
			}
			else
				{  
					changeTimes ++;
				//	System.out.println(changeTimes);
					node.changePlayer();
				}
			if(changeTimes >= 2)
			{
			//	System.err.println(node.blackNum + "####" + node.whiteNum);
			//	System.err.println(node.getNextPlayer());
			//	node.showStatus();
				break;
			}
		}
		
		//模拟次数加1
		//在该处的模拟结果胜者为自己下棋一方
	//	System.out.println(node.getChessStatus());
	//	System.out.println("stimulation over");
		//求出落子位置，下棋，改变棋盘状态
	} 
	public UctTree TreePolicy(UctTree node)
	{
		UctTree tmp = node;
		while( tmp != null && tmp.isTerminal() == false )
		{
			if(tmp.isFullyExpand() == false)
				return tmp.expand();
			else 
				tmp = bestChild(tmp, Math.sqrt(2));
		}
	//	System.out.println("running tree policy");
		return tmp;
	}
	// 计算回报公式如下
	// x + sqrt(2*ln(n)/nj) * c
	// nj是被选中的次数，n是总共选的次数
	public UctTree bestChild(UctTree node,double c) {
		
	//	System.out.println("running selecting best child");
		if(node == null || node.isleaf())
			return null;
		int totaltimes = node.getTotal();  //得到父节点总的选中次数
		UctTree tmp = node.leftSon();
		UctTree targetNode = null;
		double max = -10000000, result;
		while(tmp != null)
		{
			//优先选中那些没有被模拟过的节点
			if(tmp.getTotal() == 0)
			{
				targetNode = tmp;
				break;
			}
		//	System.out.println(tmp.getTotal() + "++" + tmp.wintimes());
			result = 1.0*tmp.wintimes() / tmp.getTotal() + c * Math.sqrt(2 * Math.log(totaltimes) / tmp.getTotal());
		//	System.out.println(result);
			if(result > max)
			{
				max = result;
				targetNode = tmp;
			}
			tmp = tmp.Sibiling();
		}
		return targetNode;
	}
	
	public UctTree serach()
	{
		//System.out.println("serching");
		if(root == null)
			return null;
		UctTree v1 = null;
		UctTree v2 = null;
		v1 = TreePolicy(root);
		if(v1 == null)
			return bestChild(root, 0);
		//v1是指向博弈树上的一个节点，为了不改变其中数据，应该复制一份进行模拟
		v2 = new UctTree(v1.getX(),v1.getY(), v1.getvalue(), v1.getChessStatus());
		defultPolicy(v2);
		if(v2.getWinner() == v1.getvalue())
			v1.increaseWin();
		else if(v2.getWinner() == -v1.getvalue())
			v1.descreaseWin();  //减掉
		v1.increaseTotal(); //总的模拟次数加1
		v1.backup();
		v2 = null;
		return bestChild(root, 0);
	}
}
public class ComputerPlayer {
	public static int record = 0;
	volatile int SearchNum = 0;
	public static Lock lock1 = new ReentrantLock(); 
	public static board gameboard;
	private UCT searchTree = null;
	private long timelimit = -100;
	volatile private long startTime = -1;
	private boolean GameOver = false;
	volatile private int opx = -1, opy = -1; //记录对方新下的棋子的位置
	private int myColor = UctTree.whiteplayer;
	private long opstart = 0;
	public ComputerPlayer(board tmp) {
		// TODO Auto-generated constructor stub
		this.gameboard = tmp;
		if(searchTree == null)
		{
			searchTree = new UCT();
			searchTree.setroot(new UctTree(-1,-1, UctTree.whiteplayer, gameboard.getSatus())); //开始时候对方在下棋
		}
	}
	public void setColor(int color)
	{
		myColor = color;
	}
	public void SetGameOver(boolean tmp)
	{
		GameOver = tmp;
	}
	public void setStartTime(long start)
	{
		startTime = start;
	}
	public void setXY(int x, int y)
	{
		opx = x;
		opy = y;
	//	System.out.println("setting new x " + x + " " + y);
	}
	public void setTimeLimit(long time)
	{
		this.timelimit = time;
	}
	public void showSun(UctTree father)
	{
		System.err.println("showing sun");
		if(father==null)
			return;
		System.err.println(father.getX()+" " +father.getY());
		UctTree temp = father.leftSon();
		while(temp!=null)
		{
			
			System.err.println(temp.getX()+" " +temp.getY());
			temp = temp.Sibiling();
		}
	}
	public void testReward(UctTree t)
	{
		if(t == null)
			return;
		UctTree node = t.leftSon();
		while( node != null)
		{
			System.out.println("total："+node.getTotal() + " " + node.wintimes());
			node = node.Sibiling();
		}
	}
	public void play() {
		long ct;
		UctTree result = null;
		UctTree nRoot = null;
		int count = 0;
		System.out.println("computer is thinking");
		
		while(GameOver == false)
		{
			opstart = System.currentTimeMillis();
			while( startTime < 0 ) //这个是在当前棋局下，对方下棋时间内的自我模拟，当时间设置好之后，那么需要在子节点总寻找一个，节点(如果该局面存在了)
			{
				if(System.currentTimeMillis() - opstart <= 5000)
					searchTree.serach();  //不断模拟		
			}
			nRoot = searchTree.getRoot();
			if(searchTree.getRoot().getNextPlayer() == -myColor)
			{
				lock1.lock(); //保证被写完再读取
				nRoot = searchTree.getRoot().findSonWithIndex(opx, opy);
				lock1.unlock();
				searchTree.setroot(nRoot);
			}
			else  ;
			//	nRoot = searchTree.getRoot();  //下一个玩家是白色棋子
		//	System.out.println("finding new root "+ nRoot.getX() +" "+nRoot.getY());
		//	showSun(searchTree.getRoot());
			if(nRoot.getNextPlayer() == -myColor)
			{
				System.err.println("----------------");
				setStartTime(-1);
				WBGOGame.playerturn = -myColor;
				continue;
			}
			//System.err.println("running out of time1");
			while(System.currentTimeMillis() - startTime < timelimit)
			{
				result = searchTree.serach();  //不断模拟
			}

			if(gameboard != null && gameboard.playable(result.getX(), result.getY(), myColor,true))
			{
				//下棋之后应该，应该把这个节点当作根节点了
				gameboard.setValueByindex(result.getX(),result.getY(),myColor);
				gameboard.setNewChessIndex(result.getX(), result.getY());
				gameboard.repaint();
				if(myColor == UctTree.blackplayer)
					gameboard.increaseBlack();
				else
					gameboard.inceaseWhite();
				gameboard.IncreaseStep();

				searchTree.setroot(result);
			
			     
				if(searchTree.getRoot().getNextPlayer() == myColor  &&  searchTree.getRoot().value == myColor)
					this.setStartTime(System.currentTimeMillis());
				else
					this.setStartTime(-1);
				System.out.println("stimulation times: " + result.getTotal() + " " + result.wintimes() + " " + result.Father().getTotal() + " " + result.Father().wintimes());
			//	testReward(searchTree.getRoot());
				
				
				result.setFather(null);
				System.out.println("blacks: "+ gameboard.blackNums);
				System.out.println("white: "+ gameboard.whiteNums);
				WBGOGame.blackLabel.setText(String.valueOf(gameboard.blackNums));
				WBGOGame.whiteLabel.setText(String.valueOf(gameboard.whiteNums));
				WBGOGame.playerturn = -myColor;
				System.err.println("change player to" + WBGOGame.playerturn);
				
			}
			else{
				System.err.println("finding a wrong place " + result.getX() + " "+result.getY());
			}
		}
	}

}
