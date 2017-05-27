package Main;

import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JFrame;

public class JFrameTestor extends JFrame {
	public JFrameTestor() {
		setSize(480, 320);
		setLocation(new Point(400, 150));
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		new JFrameTestor();
	}
	
	public void paint(Graphics g) {
	for(int i = 30; i <= 310; i += 20) {
			g.drawLine(i, 30, i, 310);
	}
	
	for(int i = 10; i <= 310; i += 20) {
		g.drawLine(30, i, 310, i);
		}
	}
}