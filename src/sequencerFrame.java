import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.MouseInfo;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class sequencerFrame
{

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					sequencerFrame window = new sequencerFrame();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public sequencerFrame()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.getContentPane().addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				//System.out.println("Click at "+ MouseInfo.getPointerInfo().getLocation());
			}
		});
		frame.setBounds(100, 100, 535, 510);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final Grid panel = new Grid(8,8);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setBounds(100,40,300,200);
		panel.setBackground(new java.awt.Color(255,255,255));
		

	}
	private class GPanel extends JPanel
	{
		public void paintComponent(Graphics g)
		{

			super.paintComponent(g);

			/*g.setColor(Color.black);
			g.drawRect(0, 0, 50, 50);
			g.drawRect(0, 50, 50, 50);
			g.drawRect(0, 100, 50, 50);
			g.drawRect(0, 150, 50, 50);
			g.drawRect(0, 200, 50, 50);
			g.drawRect(0, 250, 50, 50);
			g.drawRect(0, 300, 50, 50);
			g.drawRect(0, 350, 50, 50);
			g.drawRect(0, 400, 50, 50);

			g.drawRect(50, 0, 50, 50);
			g.drawRect(50, 50, 50, 50);
			g.drawRect(50, 100, 50, 50);
			g.drawRect(50, 150, 50, 50);
			g.drawRect(50, 200, 50, 50);
			g.drawRect(50, 250, 50, 50);
			g.drawRect(50, 300, 50, 50);
			g.drawRect(50, 350, 50, 50);
			g.drawRect(50, 400, 50, 50);

			g.drawRect(100, 0, 50, 50);
			g.drawRect(100, 50, 50, 50);
			g.drawRect(100, 100, 50, 50);
			g.drawRect(100, 150, 50, 50);
			g.drawRect(100, 200, 50, 50);
			g.drawRect(100, 250, 50, 50);
			g.drawRect(100, 300, 50, 50);
			g.drawRect(100, 350, 50, 50);
			g.drawRect(100, 400, 50, 50);

			g.drawRect(150, 0, 50, 50);
			g.drawRect(150, 50, 50, 50);
			g.drawRect(150, 100, 50, 50);
			g.drawRect(150, 150, 50, 50);
			g.drawRect(150, 200, 50, 50);
			g.drawRect(150, 250, 50, 50);
			g.drawRect(150, 300, 50, 50);
			g.drawRect(150, 350, 50, 50);
			g.drawRect(150, 400, 50, 50);

			g.drawRect(200, 0, 50, 50);
			g.drawRect(200, 50, 50, 50);
			g.drawRect(200, 100, 50, 50);
			g.drawRect(200, 150, 50, 50);
			g.drawRect(200, 200, 50, 50);
			g.drawRect(200, 250, 50, 50);
			g.drawRect(200, 300, 50, 50);
			g.drawRect(200, 350, 50, 50);
			g.drawRect(200, 400, 50, 50);

			g.drawRect(250, 0, 50, 50);
			g.drawRect(250, 50, 50, 50);
			g.drawRect(250, 100, 50, 50);
			g.drawRect(250, 150, 50, 50);
			g.drawRect(250, 200, 50, 50);
			g.drawRect(250, 250, 50, 50);
			g.drawRect(250, 300, 50, 50);
			g.drawRect(250, 350, 50, 50);
			g.drawRect(250, 400, 50, 50);

			g.drawRect(300, 0, 50, 50);
			g.drawRect(300, 50, 50, 50);
			g.drawRect(300, 100, 50, 50);
			g.drawRect(300, 150, 50, 50);
			g.drawRect(300, 200, 50, 50);
			g.drawRect(300, 250, 50, 50);
			g.drawRect(300, 300, 50, 50);
			g.drawRect(300, 350, 50, 50);
			g.drawRect(300, 400, 50, 50);

			g.drawRect(350, 0, 50, 50);
			g.drawRect(350, 50, 50, 50);
			g.drawRect(350, 100, 50, 50);
			g.drawRect(350, 150, 50, 50);
			g.drawRect(350, 200, 50, 50);
			g.drawRect(350, 250, 50, 50);
			g.drawRect(350, 300, 50, 50);
			g.drawRect(350, 350, 50, 50);
			g.drawRect(350, 400, 50, 50);

			g.drawRect(400, 0, 50, 50);
			g.drawRect(400, 50, 50, 50);
			g.drawRect(400, 100, 50, 50);
			g.drawRect(400, 150, 50, 50);
			g.drawRect(400, 200, 50, 50);
			g.drawRect(400, 250, 50, 50);
			g.drawRect(400, 300, 50, 50);
			g.drawRect(400, 350, 50, 50);
			g.drawRect(400, 400, 50, 50);*/
		}
	}
	
	
}
