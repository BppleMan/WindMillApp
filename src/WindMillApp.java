import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RadialGradientPaint;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WindMillApp
{
	public static void main(String[] args)
	{
		DrawFrame aFrame = new DrawFrame();
		aFrame.setTitle("风车");
		aFrame.setVisible(true);
	}
}

class DrawFrame extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4143373256529082544L;
	DrawPanel aPane = new DrawPanel();

	public DrawFrame()
	{
		Toolkit tk = Toolkit.getDefaultToolkit();
		// 获取屏幕size
		Dimension d = tk.getScreenSize();
		System.out.println(d);
		this.setLayout(new BorderLayout());

		int screenWidth = (int) d.getWidth();
		int screenHeight = (int) d.getHeight();

		// 设置Frame的大小
		setSize(screenWidth / 2, screenHeight / 2);
		// 设置Frame的位置
		setLocation(screenWidth / 4, screenHeight / 4);

		final JButton startBtn = new JButton("开始");
		startBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				aPane.Run();
				startBtn.setEnabled(false);
			}
		});
		JButton stopBtn = new JButton("暂停");
		stopBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				aPane.Stop();
				startBtn.setEnabled(true);
			}
		});

		JButton accelerateBtn = new JButton("加速");
		accelerateBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				aPane.speed += 2;
				System.out.println(aPane.speed);
			}
		});

		JButton decelerateBtn = new JButton("减速");
		decelerateBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				aPane.speed -= 2;
				System.out.println(aPane.speed);
			}
		});

		Container conp = new Container();
		conp.setSize(new Dimension(screenWidth, 30));
		conp.setLayout(new GridLayout(2, 2));
		conp.add(startBtn, 0);
		conp.add(accelerateBtn, 1);
		conp.add(stopBtn, 2);
		conp.add(decelerateBtn, 3);

		add(aPane, BorderLayout.CENTER);
		add(conp, BorderLayout.SOUTH);

	}
}

class DrawPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3837457431628892449L;
	public double frameWidth;
	public double frameHeight;
	private static int angle = 0;
	private float colorSpeed1;
	private float colorSpeed2;
	public double speed = 2;
	private Timer timer;

	public void Run()
	{
		timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO 自动生成的方法存根
				angle += speed;
				colorSpeed1 += speed / 10;
				colorSpeed2 += speed / 10;
				repaint();
			}
		}, 0, 5);
	}

	public void Stop()
	{
		timer.cancel();
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		Dimension d = getSize();
		frameWidth = d.getWidth();
		frameHeight = d.getHeight();

		double centerX = frameWidth / 2;
		double centerY = frameHeight / 2;
		double radius;
		if (frameWidth > frameHeight)
			radius = frameHeight / 2;
		else
			radius = frameWidth / 2;
		double cornerX = centerX + radius;
		double cornerY = centerY + radius;

		Ellipse2D circle = new Ellipse2D.Double();
		circle.setFrameFromCenter(centerX, centerY, cornerX, cornerY);

		Point2D centerP = new Point2D.Double(centerX, centerY);
		float[] fractions = new float[] { 0, 1 };
		Color colors[] = new Color[] { new Color(251, 254, 115), new Color(225, 167, 67) };
		RadialGradientPaint rgp = new RadialGradientPaint(centerP, (float) radius, fractions, colors);
		g2d.setPaint(rgp);
		g2d.fill(circle);

		int r = (int) radius;
		for (int j = 0; j < 300; j += 30)
		{
			for (int i = 0; i < 3; i++)
			{
				if (i == 0)
					g2d.setColor(new Color(121, 154, 75));
				else if (i == 1)
					g2d.setColor(new Color(10, 130, 164));
				else if (i == 2)
					g2d.setColor(new Color(214, 114, 116));
				double sita = Math.PI * (240 * i + angle) / 360;
				Arc2D arc = new Arc2D.Double();
				Point2D p1 = new Point2D.Double(centerX, centerY);
				Point2D p = new Point2D.Double(centerX + r / 2 * Math.cos(sita), centerY + r / 2 * Math.sin(sita));
				double theta_x = p1.getX() - p.getX();
				double theta_y = p1.getY() - p.getY();
				double theta = Math.atan(theta_y / theta_x) * 180 / Math.PI;
				theta = 360.0 - theta;
				if (theta > 360)
				{
					theta -= 360;
				}
				if (p.getX() > centerX)
				{
					theta += 180;
				}
				arc.setArcByCenter(p.getX(), p.getY(), r / 2, theta, 180, Arc2D.PIE);
				Line2D line = new Line2D.Double(p1, p);
				g2d.draw(line);
				g2d.fill(arc);
			}
		}
	}
}
