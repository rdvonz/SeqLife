import java.awt.Graphics;

import javax.swing.JPanel;




public class Grid extends JPanel
{
	int row;
	int col;
	boolean[][] doa;
	Grid(int row, int col)
	{
		this.row = row;
		this.col = col;
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		for(int i=0;i<(row*50);i+=50)
		{
			for(int k=0;k<(col*50);k+=50)
			{
				g.drawRect((i+25),k,50,50);
			}
		}
		g.drawString("C", 11, 380);
		g.drawString("D", 11, 330);
		g.drawString("E", 11, 280);
		g.drawString("F", 11, 230);
		g.drawString("G", 11, 180);
		g.drawString("A", 11, 130);
		g.drawString("B", 11, 80);
		g.drawString("C", 11, 30);

	}


}

