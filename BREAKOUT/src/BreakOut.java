import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class BreakOut extends GraphicsProgram {
	//width and height of application window in pixels
	public static final int APPLICATION_WIDTH=432;;
	public static final int APPLICATION_HEIGHT=630;
	
	//Dimensions of the game board
	private static final int WIDTH=APPLICATION_WIDTH;
	private static final int HEIGHT=APPLICATION_HEIGHT;
	
	//Dimensions of the paddle
	private static  int PADDLE_WIDTH=60;
	private static final int PADDLE_HEIGHT=10;
	
	//Offset of the paddle up from the bottom
	private static final int PADDLE_Y_OFFSET=30;
	
	//Number of bricks per row
	private static final int NBRICKS_PER_ROW=10;
	
	//Number of rows of bricks
	private static final int NBRICK_ROW=10;
	
	// separation between bricks
	private static final int BRICK_STEP=4;
	
	// width of a brick
	private static final int BRICK_WIDTH=(WIDTH - (NBRICKS_PER_ROW - 1)*BRICK_STEP)/NBRICKS_PER_ROW;
	
	//height of a brick
	private static final int BRICK_HEIGHT=8;
	
	// Radius of the ball
	private static final int BALL_RADIUS=5;
	
	//offset of the top row from top
	private static final int BRICK_Y_OFFSET=70;
	
	//Number of turns
	private static final int NTURNS=3;
	
	private static final int PAUSE_TIME=5;
	
	private static final int MENU_AREA_HEIGHT=80;
	
	
	//velocity variables
	
	private double vx,vy;
	private GBall ball;
	private GBall blackBall;
	private GBall blueBall;
	private GBall pinkBall;
	private GRect paddle,back_ground,mback_ground;
	private GRect sideWall_L,sideWall_R,topWall,bottomWall,M_LWall,M_RWall,MBWall,water;
    private int numberOfTurns,numberOfBricks;

	
//	private JLabel start_label,exit_label,pnp_label,sound_label;
    private JLabel p_label; 
	private GLabel score_label,points_label;
	private JButton start_button,exit_button,pnp_button,sound_button;
	private int points=0;
	private ActionListener listener1,listener2,listener3,listener4;
	private boolean started=false;
	private boolean play =true;
	private GLabel congrats, play_again;
	private GRect[][] brick;
	private GObject objdl,objdr,objul,objur,obj;
	private GImage play_image;
	private AudioClip breakClip,bounceClip,waterClip;
    private URL breakClipURL,bounceClipURL,waterClipURL;
	private ImageIcon play_icon,pause_icon,exit_icon,sound_on_icon,sound_off_icon,start_icon,penguine_icon;
	
	
	// set up game
	public void init(){
		setSize(WIDTH+22,HEIGHT+140);
		
		this.setName("Break Out Game");
		
		play_image =new GImage("player_play.png");
		
		pause_icon = new ImageIcon(getClass().getClassLoader().getResource("Button-Pause-icon.png"));
		exit_icon = new ImageIcon(getClass().getClassLoader().getResource("Button-Close-icon.png"));
				
		sound_on_icon =new ImageIcon(getClass().getClassLoader().getResource("soundonicon.png"));
				
		sound_off_icon = new ImageIcon(getClass().getClassLoader().getResource("soundofficon.png"));
				
		start_icon =new ImageIcon(getClass().getClassLoader().getResource("Start-icon.png"));
				
		penguine_icon = new ImageIcon(getClass().getClassLoader().getResource("penguine.png"));
				
		
		
		back_ground=new GRect(WIDTH+BRICK_STEP,HEIGHT+BRICK_HEIGHT);
		mback_ground=new GRect(WIDTH+4,MENU_AREA_HEIGHT);
		back_ground.setFilled(true);
		back_ground.setColor(Color.BLACK);
		mback_ground.setFilled(true);
		mback_ground.setColor(Color.BLACK);
		add(back_ground,4,4);
		add(mback_ground,4,HEIGHT+4);
		makeWalls();
		setUpBricks();
		makePaddle();
		ball = new GBall(BALL_RADIUS);
	///	ball.setFilled(true);
		ball.setColor(new Color(176,196,222));
		add(ball,(WIDTH-BALL_RADIUS)/2,HEIGHT-PADDLE_Y_OFFSET-(1.5)*BALL_RADIUS);
		numberOfTurns = NTURNS;
		numberOfBricks = NBRICKS_PER_ROW*NBRICK_ROW;
		breakClipURL= getClass().getResource("Glass Breaking.wav");
		breakClip=Applet.newAudioClip(breakClipURL);
		
		bounceClipURL= getClass().getResource("Ball_Bounce.wav");
		bounceClip=Applet.newAudioClip(bounceClipURL);
		
		waterClipURL= getClass().getResource("Water Balloon.wav");
		waterClip=Applet.newAudioClip(waterClipURL);
		
		addMouseListeners();
		
		listener1 = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(numberOfTurns>0){
					started =true;
					updateBalls();
				}
				
			}
		};
		start_button.addActionListener(listener1);
		
		listener2 = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(pnp_button.getIcon()==pause_icon){
					pnp_button.setIcon(play_icon);
					
				}
				else{
					pnp_button.setIcon(pause_icon);
				}
			}
		};
		
		pnp_button.addActionListener(listener2);
		
		listener3 = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		};
		
		exit_button.addActionListener(listener3);
		
		listener4 = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(sound_button.getIcon()==sound_off_icon){
					play =true;
					sound_button.setIcon(sound_on_icon);
				}
				else{
					play =false;
					sound_button.setIcon(sound_off_icon);
				}
			}
		};
		
		sound_button.addActionListener(listener4);
	}

	
	
	private void updateBalls(){
		switch(numberOfTurns){
		case 1:
			pinkBall.setVisible(false);
			break;
			
		case 2:
			blueBall.setVisible(false);
			break;
			
		case 3:
			blackBall.setVisible(false);
			break;
			
		}
	}
	
	private void reSetUpBricks(){
		
		//brick=new GRect[NBRICKS_PER_ROW][NBRICK_ROW];
		
		for(int i=0;i<NBRICKS_PER_ROW;i++){
			for(int j=0;j<NBRICK_ROW;j++){
				if(brick[i][j]!=null){
					remove(brick[i][j]);
				}
			}
		}
		
		for(int i=0;i<NBRICKS_PER_ROW;i++){
			for(int j=0;j<NBRICK_ROW;j++){
				brick[i][j] = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				brick[i][j].setFilled(true);
				switch(i){
				case 0:
				case 1:
					brick[i][j].setColor(Color.RED);
					break;
				case 2:
				case 3:
					brick[i][j].setColor(Color.ORANGE);
					break;
				case 4:
				case 5:
					brick[i][j].setColor(Color.YELLOW);
					break;
				case 6:
				case 7:
					brick[i][j].setColor(Color.GREEN);
					break;
				case 8:
				case 9:
					brick[i][j].setColor(Color.CYAN);
					break;
				}
				add(brick[i][j],8+j*(BRICK_WIDTH+BRICK_STEP),BRICK_Y_OFFSET+i*(BRICK_HEIGHT+BRICK_STEP));
			}
		}
	}
	
	
	void game_climax( String s){
		congrats = new GLabel(s);
		congrats.setColor(Color.MAGENTA);
		congrats.setFont("Serif-BOLD-20");
		add(congrats,WIDTH/4,HEIGHT/2);
		start_button.setEnabled(false);
		pause(3000);
		remove(congrats);
		play_again =new GLabel("PLAY AGAIN!");
		play_image =new GImage("player_play.png");
		
		play_again.setColor(Color.GREEN);
		play_again.setFont("Serif-BOLD-20");
		add(play_again,WIDTH/3,HEIGHT/2);
		add(play_image,WIDTH/3,HEIGHT/2+30);
		
	}
	
	public void mouseClicked(MouseEvent e){
		double ex = e.getX();
		double ey = e.getY();
		GObject ob = getElementAt(ex,ey);
		if((ob == play_again)||(ob==play_image)){
			remove(play_again);
			remove(play_image);
			start_button.setEnabled(true);
			reSetUpBricks();
			blackBall.setVisible(true);
			blueBall.setVisible(true);
			pinkBall.setVisible(true);
			ball.setColor(new Color(176,196,222));
			ball.setVisible(true);
			numberOfTurns = NTURNS;
			numberOfBricks = NBRICKS_PER_ROW*NBRICK_ROW;
			points=0;
			points_label.setLabel(""+points);
		}
		
	}
	
	private void play_Complete_Game(){
		while(true){
			playGame();
			if(numberOfBricks == 0){
				
				game_climax("Congratulations! You won!");
				started = false;
				ball.setLocation((WIDTH-BALL_RADIUS)/2,HEIGHT-PADDLE_Y_OFFSET-(1.5)*BALL_RADIUS);
				PADDLE_WIDTH=60;
				
			}
			else{
				game_climax("      Sorry! You lost!");
				started = false;
				ball.setLocation((WIDTH-BALL_RADIUS)/2,HEIGHT-PADDLE_Y_OFFSET-(1.5)*BALL_RADIUS);
				PADDLE_WIDTH=60;
				
			}
		}
	}
	
	public void run(){
		while(true){
			if((pnp_button.getIcon()==pause_icon)&& started){
				break;
			}
			pause(100);
		}
		play_Complete_Game();

	}

	
	public void mouseMoved(MouseEvent e){
		if(started){
			double mx = e.getX();
			if(mx>WIDTH-PADDLE_WIDTH+6){
				paddle.setLocation(WIDTH-PADDLE_WIDTH+6, HEIGHT-PADDLE_Y_OFFSET);
			}
			else if(mx<6){
				paddle.setLocation(6, HEIGHT-PADDLE_Y_OFFSET);
			}
			else{
				paddle.setLocation(mx, HEIGHT-PADDLE_Y_OFFSET);
			}
		}
		
	}

	// get score
	private void updateScore(GObject obj){
		if(obj.getColor() == Color.CYAN){
			points+=10;
		}
		else if (obj.getColor() == Color.GREEN){
			points+=15;
		}
		else if (obj.getColor() == Color.YELLOW){
			points+=20;
		}
		else if (obj.getColor() == Color.ORANGE){
			points+=25;
		}
		else if (obj.getColor() == Color.RED){
			points+=30;
		}
		else{
			
		}
	}
	
	private boolean collisionWithBrick(GObject obj){
		if((obj.getColor()==Color.CYAN)||(obj.getColor()==Color.GREEN)||(obj.getColor()==Color.YELLOW)||(obj.getColor()==Color.ORANGE)||(obj.getColor()==Color.RED)){
			return true;
		}
		return false;
	}
	
	private void breakBrick(GObject obj) {
		
		updateScore(obj);	
		points_label.setLabel(""+points);
		numberOfBricks--;
		vy=-vy;
		double x=obj.getX();
		double y = obj.getY();
		Color clr= obj.getColor();
		remove(obj);
		if(play){
			breakClip.play();
		}
		brickBreakAnimation(x,y,clr);
		
	}
	
	private GObject getColloidBrick() {
		double bx = ball.getX();
		double by = ball.getY();
		objdl= getElementAt(bx-BALL_RADIUS,by+BALL_RADIUS);
		objdr= getElementAt(bx+BALL_RADIUS,by+BALL_RADIUS);
		objul= getElementAt(bx-BALL_RADIUS,by-BALL_RADIUS);
		objur= getElementAt(bx+BALL_RADIUS,by-BALL_RADIUS);
		
		if(objdl!=null) {
			return objdl;
		}
		else if(objdr!=null) {
			return objdr;
		}
		else if(objul!=null) {
			return objul;
		}
		else if(objur!=null) {
			return objur;
		}
		else {
			return null;
		}
		
	}
	
	private void playTurn(){
		
		while((numberOfBricks > 0)  ){
			double bx = ball.getX();
			double by = ball.getY();
		//	obj= getElementAt(bx+BALL_RADIUS,by+BALL_RADIUS);
			objdl= getElementAt(bx-BALL_RADIUS,by+BALL_RADIUS);
			objdr= getElementAt(bx+BALL_RADIUS,by+BALL_RADIUS);
			objul= getElementAt(bx-BALL_RADIUS,by-BALL_RADIUS);
			objur= getElementAt(bx+BALL_RADIUS,by-BALL_RADIUS);
			
			while(true){
				if((pnp_button.getIcon()==pause_icon)&& started){
					break;
				}
				pause(100);
			}
		     GObject obj=getColloidBrick();
		     if(obj!=null)

			
			/* if(((objdl != null)||(objdr != null)||(objul != null)||(objur != null))&&(bx>4+BALL_RADIUS)&&(bx+BALL_RADIUS<WIDTH)&&(by>4+BALL_RADIUS)&&(by<HEIGHT-PADDLE_Y_OFFSET)){
				if((objul != null)&&collisionWithBrick(objul)){
					breakBrick(objul);
					
				}
				else if((objur != null)&&collisionWithBrick(objur)) {
					breakBrick(objul);
					
				}
				else if((objdl != null)&&collisionWithBrick(objdl)) {
					breakBrick(objul);
				}
				else if ((objdr != null)&&collisionWithBrick(objdr)) {
					breakBrick(objul);
				} */
				
				if((obj != null)&&(obj == paddle)){	
					vy=-vy;
					double px = paddle.getX()+paddle.getWidth()/2;
					double py = paddle.getY();
					double rr = GMath.distance(bx,by,px,py);
					if((py+paddle.getHeight()>by)&&rr<(paddle.getWidth()/2)){
						if(vx!=0){
							vx=-vx;
							if(play){
								bounceClip.play();
							}
						}
						else{
							vx=vy;
						}
				}
				else if((rr>7*PADDLE_WIDTH/16)&&(rr<9*PADDLE_WIDTH/16)){
					vx=0;
						
				}
				else if(vx == 0){
					vx=vy;
				}
				if(play){
					bounceClip.play();
				}
					
					
				}
				ball.move(vx, vy);
				
			}
			else{
				if(by<4){
					vy=-vy;
					if(play){
						bounceClip.play();
					}
				}
				else if(by+BALL_RADIUS>HEIGHT-5){
					numberOfTurns--;
					started = false;
					if(play){
						waterClip.play();
					}
					double x=ball.getX();
					double y = ball.getY()+(4/3.0)*BALL_RADIUS;
					
					
					WaterSpill spill=new WaterSpill();
					add(spill,x,y);
					pause(20*PAUSE_TIME);
					for(int i=0;i<20;i++){
						spill.scale(0.9);
						pause(10*PAUSE_TIME);
					}
					pause(50*PAUSE_TIME);
					remove(spill);
					ball.setLocation((WIDTH-BALL_RADIUS)/2,HEIGHT-PADDLE_Y_OFFSET-(1.5)*BALL_RADIUS);
					setBallColor();
					break;
				}
				else if(bx-BALL_RADIUS/2<4||bx+BALL_RADIUS/2>WIDTH+4){
					vx=-vx;
					if(play){
						bounceClip.play();
					}
					
				}
				
				ball.move(vx, vy);
			}
			
			pause(PAUSE_TIME);
		}
		
	}
	
	public void brickBreakAnimation(double x, double y,Color clr) {
		BrickBreak brk=new BrickBreak(clr);
		add(brk,x,y);
		pause(20);
		for(int i=0;i<20;i++){
			pause(5);
			brk.move(0,3);
		}
		remove(brk);
	}
	
	
	private void setBallColor(){
		switch(numberOfTurns){
		case 1:
			ball.setColor(new Color(255,182,193));
			break;
			
		case 2:
			ball.setColor(new Color(221,160,221));
			break;
			
		case 3:
			ball.setColor(new Color(176,196,222));
			break;
			
		}
	}
	
	private void playGame(){
		RandomGenerator rgen = RandomGenerator.getInstance();
		numberOfTurns = NTURNS;
		numberOfBricks = NBRICKS_PER_ROW*NBRICK_ROW;
		
		
		while((numberOfTurns > 0)&&(numberOfBricks>0)){
			vx=rgen.nextDouble(1.0, 2.0);
			if(rgen.nextBoolean(0.5)){
				vx=-vx;
			}
			switch(numberOfTurns){
			case 1:
				vy=-2.5;
				PADDLE_WIDTH=PADDLE_WIDTH-13;
				updatePaddle();
				break;
			case 2:
				vy=-2;
				PADDLE_WIDTH=PADDLE_WIDTH-13;
				updatePaddle();
				break;
			case 3:
				vy =-1.5;
				//PADDLE_WIDTH=PADDLE_WIDTH-15;
				updatePaddle();
				break;
		}

			playTurn();	
			paddle.setLocation((WIDTH-PADDLE_WIDTH+4)/2, HEIGHT-PADDLE_Y_OFFSET);
		}
		ball.setVisible(false);
	}
	
	private void makeWalls(){
		water = new GRect(WIDTH+4,5);
		water.setFilled(true);
		water.setColor(new Color(100,149,237));
		add(water,4,HEIGHT-5);
		sideWall_L=new GRect(4,HEIGHT);
		sideWall_L.setFilled(true);
		sideWall_L.setColor(Color.GRAY);
		sideWall_R=new GRect(4,HEIGHT);
		sideWall_R.setFilled(true);
		sideWall_R.setColor(Color.GRAY);
		topWall=new GRect(WIDTH+8,4);
		bottomWall=new GRect(WIDTH+8,4);
		topWall.setFilled(true);
		topWall.setColor(Color.GRAY);
		bottomWall.setFilled(true);
		bottomWall.setColor(Color.GRAY);
		add(sideWall_L,0,0);
		add(sideWall_R,WIDTH+2*BRICK_STEP,0);
		add(topWall,0,0);
		add(bottomWall,0,HEIGHT);
		
		MBWall=new GRect(WIDTH+12,4);
		MBWall.setFilled(true);
		MBWall.setColor(Color.GRAY);
		add(MBWall,0,HEIGHT+MENU_AREA_HEIGHT);
		
		M_LWall=new GRect(4,MENU_AREA_HEIGHT);
		M_LWall.setFilled(true);
		M_LWall.setColor(Color.GRAY);
		add(M_LWall,0,HEIGHT);
		
		M_RWall=new GRect(4,MENU_AREA_HEIGHT);
		M_RWall.setFilled(true);
		M_RWall.setColor(Color.GRAY);
		add(M_RWall,WIDTH+8,HEIGHT);
		

		
		sound_button = new JButton();
		sound_button.setSize(30,30);
		sound_button.setIcon(sound_on_icon);
		add(sound_button,94,HEIGHT+12);
		
		start_button = new JButton();
		start_button.setSize(30,30);
		start_button.setIcon(start_icon);
		add(start_button,14,HEIGHT+12);
		
		pnp_button = new JButton();
		pnp_button.setSize(30,30);
		pnp_button.setIcon(pause_icon);
		add(pnp_button,54,HEIGHT+12);
		
		exit_button = new JButton();
		exit_button.setSize(30, 30);
		exit_button.setIcon(exit_icon);
		add(exit_button,134,HEIGHT+12);
		
		score_label = new GLabel("SCORE:");
		score_label.setFont("*-bold-*");
		score_label.setColor(Color.lightGray);
	
		add(score_label,(WIDTH-5*score_label.getHeight()),HEIGHT+70);
		
		p_label = new JLabel();
		p_label.setIcon(penguine_icon);
		add(p_label,(WIDTH-7*score_label.getHeight()),HEIGHT+46);
		
		
		points_label = new GLabel("");
		points_label.setLabel(""+points);
		points_label.setFont("*-bold-*");
		points_label.setColor(Color.WHITE);;
		add(points_label,WIDTH-35,HEIGHT+70);
		
		blackBall = new GBall(BALL_RADIUS);
		//blackBall.setFilled(true);
		blackBall.setColor(new Color(176,196,222));
		blueBall = new GBall(BALL_RADIUS);
		//blueBall.setFilled(true);
		blueBall.setColor(new Color(221,160,221));
		pinkBall = new GBall(BALL_RADIUS);
		//pinkBall.setFilled(true);
		pinkBall.setColor(new Color(255,182,193));
		add(blackBall,2*BALL_RADIUS,HEIGHT+60);
		add(blueBall,5*BALL_RADIUS,HEIGHT+60);
		add(pinkBall,8*BALL_RADIUS,HEIGHT+60);
		
	}

	
	// setting up bricks
	private void setUpBricks(){
		brick=new GRect[NBRICKS_PER_ROW][NBRICK_ROW];
		for(int i=0;i<NBRICKS_PER_ROW;i++){
			for(int j=0;j<NBRICK_ROW;j++){
				brick[i][j] = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				brick[i][j].setFilled(true);
				switch(i){
				case 0:
				case 1:
					brick[i][j].setColor(Color.RED);
					break;
				case 2:
				case 3:
					brick[i][j].setColor(Color.ORANGE);
					break;
				case 4:
				case 5:
					brick[i][j].setColor(Color.YELLOW);
					break;
				case 6:
				case 7:
					brick[i][j].setColor(Color.GREEN);
					break;
				case 8:
				case 9:
					brick[i][j].setColor(Color.CYAN);
					break;
				}
				add(brick[i][j],2*BRICK_STEP+j*(BRICK_WIDTH+BRICK_STEP),BRICK_Y_OFFSET+i*(BRICK_HEIGHT+BRICK_STEP));
			}
		}
	}
	
	//making paddle
	private void makePaddle(){
		paddle = new GRect(PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(new Color(165,42,42));
		add(paddle,(WIDTH-PADDLE_WIDTH+4)/2,HEIGHT-PADDLE_Y_OFFSET);
	}
	
	private void updatePaddle() {
		paddle.setSize(PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setLocation((WIDTH-PADDLE_WIDTH+4)/2, HEIGHT-PADDLE_Y_OFFSET);
	}
	
	

}
