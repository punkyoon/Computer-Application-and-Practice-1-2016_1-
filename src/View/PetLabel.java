package View;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import Model.AdultCat;
import Model.Animal;
import Model.Degrade_IF;
import Model.User;

public class PetLabel extends JLabel implements Runnable, MouseListener
{
	protected static Animal pet;
	private User myUser;
	private Place place;
	private JPopupMenu menu;
	private JPopupMenu placeMenu;
	private int flag;
	private int moveFlag;
	private int msgFlag;
	protected int o_x, o_y;
	public BufferedImage speechBubbleImg = null;
	
	private Drappings drap[];
	public JMenuItem behaviorItem7;
	
	Thread t;
	
	public PetLabel(){}
	
	public PetLabel(Animal pet, Place place, User myUser)
	{	
		this.drap = new Drappings[5];
		this.pet = pet;
		this.pet.setLabel(this);
		this.place = place;
		this.myUser = myUser;
		this.menu = new JPopupMenu();
		this.setText(pet.getName());
		//System.out.println(this.getX() +" "+ this.getY());
		
		this.flag = 0;
		this.moveFlag = 0;
		
		JMenuItem item1 = new JMenuItem("move");
		item1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				flag = 1;
				// TODO Auto-generated method stub
				place.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
					{
						if(flag == 1)
						{
							moveThread(e.getX(), e.getY());
							flag = 0;		
						}
					}
				});
			}
		});
		
		JMenu item2 = new JMenu("Talk");
		JMenu item3 = new JMenu("Change map");
		JMenu item4 = new JMenu("Map behavior");
		JMenu item5 = new JMenu("Pet behavior");
		
		menu.add(item1);
		menu.add(item2);
		JMenuItem talkItem1 = new JMenuItem("인사하기");
		talkItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				makeMessageBox(pet.talk(1));
			}
			
		});
		JMenuItem talkItem2 = new JMenuItem("다정하게 말하기");
		talkItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				makeMessageBox(pet.talk(2));
			}
			
		});
		JMenuItem talkItem3 = new JMenuItem("설교하기");
		talkItem3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				makeMessageBox(pet.talk(3));
			}
			
		});
		
		/* undeveloped
		talkItem1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				pet.talk();
			}
		});
		*/
		
		item2.add(talkItem1);
		item2.add(talkItem2);
		item2.add(talkItem3);
		
		menu.add(item3);
		JMenuItem mapItem1 = new JMenuItem("Livingroom");
		mapItem1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				place.c.changeMap(pet, 0);	//0 : livingroom
			}		
		});
		JMenuItem mapItem2 = new JMenuItem("Bathroom");
		mapItem2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				place.c.changeMap(pet, 1);	//1 : bathroom
			}		
		});
		JMenuItem mapItem3 = new JMenuItem("Yard");
		mapItem3.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				place.c.changeMap(pet, 2);	//2 : yard
			}		
		});
		menu.add(item4);
		JMenuItem behaviorItem1 = new JMenuItem("잠자기");
		behaviorItem1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				makeMessageBox(pet.sleep());
			}
		});
		JMenuItem behaviorItem2 = new JMenuItem("먹이주기");
		behaviorItem2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				makeMessageBox(pet.feed());
			}
		});		
		JMenuItem behaviorItem3 = new JMenuItem("놀아주기");
		behaviorItem3.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				makeMessageBox(pet.play());
			}
		});
		JMenuItem behaviorItem4 = new JMenuItem("대전하기");
		/* undeveloped
		behaviorItem4.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
					
			}
		});
		*/
		
		JMenuItem behaviorItem5 = new JMenuItem("똥치우기");
		behaviorItem5.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				
				makeMessageBox(pet.ridDrappings());
				//grow();
			}
		});
		JMenuItem behaviorItem6 = new JMenuItem("옷벗기");
		/* undeveloped
		behaviorItem6.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
					
			}
		});
		*/
		behaviorItem7 = new JMenuItem("스탯확인");
		/*
		behaviorItem7.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String []list=pet.getPetStatList();
				
			}
		});
		*/
		
		if(this.place instanceof LivingroomPage)
		{
			item3.add(mapItem2);
			item3.add(mapItem3);
			item4.add(behaviorItem1);
			item4.add(behaviorItem2);
		}
		else if(this.place instanceof BathroomPage)
		{
			item3.add(mapItem1);
			item3.add(mapItem3);
			item4.add(behaviorItem5);
			item4.add(behaviorItem6);
			item4.add(behaviorItem7);
		}
		else if(this.place instanceof YardPage)
		{
			item3.add(mapItem1);
			item3.add(mapItem2);
			item4.add(behaviorItem3);
			item4.add(behaviorItem4);
		}
		
		menu.add(item5);
		this.addMouseListener(this);
		this.setVisible(true);
	}
	//메시지 박스
	/*speechList 번호로 받아오는 건 사용 안하는 것 같아서 일단 주석처리
	public void makeMessageBox(int num) {
		//JLabel myMessageLabel = new JLabel(pet.getMessage(num));
		//this.pet.getMyLocation().add(myMessageLabel);
		//myMessageLabel.setBounds(this.getX(), this.getY() - 40, 100, 50);
		//myMessageLabel.setVisible(true);
		try
		{
			speechBubbleImg = ImageIO.read(new File("Img\\speechBubble.png"));;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
		}
		
		JPanel speechBubble;
		JTextArea text;
		
		speechBubble = new JPanel(){
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				g.drawImage(speechBubbleImg, 0, 0, null);
			}
		};
		this.pet.getMyLocation().add(speechBubble);
		speechBubble.setBounds(this.getX()+100, this.getY()-120, 180, 138);
		speechBubble.setOpaque(false);
		speechBubble.setLayout(null);
		speechBubble.setVisible(true);
		
		text = new JTextArea(pet.getMessage(num));
		text.setBounds(28, 32, 130, 84);
		text.setOpaque(false);
		text.setEditable(false);
		text.setVisible(true);
		text.setFont(new Font("고딕", Font.HANGING_BASELINE, 15));
		text.setLineWrap(true);		//줄바꿈 활성화
		speechBubble.add(text);
		
		Thread th = new Thread(new Runnable() {
		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						//removeDialog(myMessageLabel);
						speechBubble.setVisible(false);
					}
				};
				Timer timer = new Timer();
				timer.schedule(task, 3000);
				//task.run();
				System.out.println("작업");
			}
			
		});
		th.start();
	}
	*/
	public void makeMessageBox(String msg) {
		//JLabel myMessageLabel = new JLabel(msg);
		//this.pet.getMyLocation().add(myMessageLabel);
		//myMessageLabel.setBounds(this.getX(), this.getY() - 90, 2000, 100);
		//myMessageLabel.setVisible(true);
		try
		{
			speechBubbleImg = ImageIO.read(new File("Img\\speechBubble.png"));;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
		}
		System.out.println("msgFlag:"+ msgFlag);
		JPanel speechBubble;
		JTextArea text;
		
		speechBubble = new JPanel(){
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				setOpaque(false);
				g.drawImage(speechBubbleImg, 0, 0, null);
			}
		};
		this.pet.getMyLocation().add(speechBubble);
		speechBubble.setBounds(this.getX()+100, this.getY()-120, 180, 138);
		speechBubble.setOpaque(false);
		speechBubble.setLayout(null);
		speechBubble.setVisible(true);
		
		text = new JTextArea(msg);
		text.setBounds(28, 32, 130, 84);
		text.setOpaque(false);
		text.setEditable(false);
		text.setVisible(true);
		text.setFont(new Font("고딕", Font.HANGING_BASELINE, 15));
		text.setLineWrap(true);		//줄바꿈 활성화
		speechBubble.add(text);
		
		Thread th = new Thread(new Runnable() {
		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//removeDialog(myMessageLabel);
						speechBubble.setVisible(false);
					}
				};
				Timer timer = new Timer();
				timer.schedule(task, (2500));
				//task.run();
				System.out.println("메시지박스");
			}
			
		});
		th.start();
		
	}
	//퇴화 소스
	public void degrade() {
		pet.getMyLocation().deleteIcon(pet);
		
		pet.degrade(myUser, pet.getIndex());
		System.out.println(pet.getIndex() + "나의 원래 인덱스 퇴화");
		
		System.out.println(myUser.getPet(pet.getIndex()));
		pet = myUser.getPet(pet.getIndex());
		System.out.println(pet + "퇴화해라");
		pet.getMyLocation().createPetIcon(pet);
		pet.getMyLocation().repaint();
	}
	//진화 소스
	public void grow() {
		pet.getMyLocation().deleteIcon(pet);
		
		pet.grow(myUser, pet.getIndex());
		System.out.println(pet.getIndex() + "나의 원래 인덱스");
		
		System.out.println(myUser.getPet(pet.getIndex()));
		pet = myUser.getPet(pet.getIndex());
		System.out.println(pet + "진화해라");
		pet.getMyLocation().createPetIcon(pet);
		
	}
	public void setPet(Animal p) {
		this.pet = p;
	}
	
	//똥 소스
	public void makeDrapping() {
		for(int i = 0; i < 5; i++) {
			if(this.drap[i] == null) {
				this.drap[i] = new Drappings(this, i);
				System.out.println(this.place.getName());
				this.place.add(this.drap[i]);
				this.drap[i].setVisible(true);
				break;
			}
		}
	}
	public void ridDrapping() {
		for(int i = 0; i < 5; i++) {
			if(this.drap[i] != null) {
				this.drap[i].setVisible(false);
				this.drap[i] = null;
			}
		}
	}
	public void removeDrappingFromMap() {
		int drapNum = this.pet.getDrappings();
		for(int i = 0; i < drapNum; i++) {
			this.drap[i].setVisible(false);
			this.drap[i] = null;
		}
	}
	
	public int getMoveflag() {
		return this.moveFlag;
	}
	//똥 소스 끝
	public void makeThread()
	{
		this.t = new Thread(this);
		this.t.start();
	}
	public void moveThread(int x, int y)
	{
		this.t = new Thread(this);
		moveFlag = 1;
		this.o_x = x;
		this.o_y = y;
		this.t.start();
	}
	
	@Override
	public void run()
	{
		
		this.pet.setXY(this.getX(), this.getY());
		
		double x = (double)this.pet.getX();
		double y = (double)this.pet.getY();
		System.out.println(x + "  " + y);
		double x_cal = x;
		double y_cal = y;
		double speed_x = Math.abs(o_x - x) / 30;
		double speed_y = Math.abs(o_y - y) / 30;
		for(int i = 0; i < pet.getDrappings(); i++) {
			this.drap[i].moveThread(o_x, o_y, speed_x, speed_y);
		}
		// TODO Auto-generated method stub
		System.out.println("speed : x y" + speed_x + " " + speed_y);
		while(this.moveFlag == 1)
		{
			//System.out.println(x + "   " + y);
			if(x < this.o_x && y < this.o_y)
			{
				x_cal += speed_x;
				y_cal += speed_y;
				if(x_cal > this.o_x || y_cal > this.o_y)
				{
					x_cal = this.o_x;
					y_cal = this.o_y;
					moveFlag = 0;
				}
			}
			else if(x < this.o_x && y > this.o_y)
			{
				x_cal += speed_x;
				y_cal -= speed_y;
				if(x_cal > this.o_x || y_cal < this.o_y)
				{
					x_cal = this.o_x;
					y_cal = this.o_y;
					moveFlag = 0;
				}
			}
			else if(x > this.o_x && y < this.o_y)
			{
				x_cal -= speed_x;
				y_cal += speed_y;
				if(x_cal < this.o_x || y_cal > this.o_y)
				{
					x_cal = this.o_x;
					y_cal = this.o_y;
					moveFlag = 0;
				}
			}
			else if(x > this.o_x && y > this.o_y)
			{
				x_cal -= speed_x;
				y_cal -= speed_y;
				if(x_cal < this.o_x || y_cal < this.o_y)
				{
					x_cal = this.o_x;
					y_cal = this.o_y;
					moveFlag = 0;
				}
			}			
			
			try
			{
				Thread.sleep(70);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.setLocation((int)x_cal % 780, (int)y_cal % 490);
			//System.out.println(x_cal + "  " + y_cal);
		}
		
		this.pet.setXY((int)x_cal,(int)y_cal);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
		if(e.getSource() == this)
		{
			menu.show(this, this.getWidth(), 0);
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		/*
		if(e.isPopupTrigger()) {
			menu.show(e.getComponent(), e.getX(), e.getY());
		}*/
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	/*	// TODO Auto-generated method stub
		if(e.isPopupTrigger()) {
			menu.show(e.getComponent(), e.getX(), e.getY());
		}*/
	}
}