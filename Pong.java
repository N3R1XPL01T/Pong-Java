import java.awt.event.*;
import javax.swing.*;
import java.awt.*;


public class Pong extends JFrame {
    int WIDTH = 600, HEIGHT = 400, WIDTH_POINT = 5, HEIGHT_POINT = 70, height_var = 180, player1_points = 0, player2_points = 0;
    
	String ball_X_Direction = "RIGHT";
    String ball_Y_Direction = "UP";
    
    Point ball;
    Point racket1;
    Point racket2;
    Point divide_point;
    
	double fps = 25.5;

    HertzRate hertz = new HertzRate();

    GameScreen screen = new GameScreen(); 


    public Pong() {
        ball = new Point(WIDTH/2, HEIGHT/2);
        setTitle("Pong");
        add(screen);
	    setResizable(false);
        addKeyListener(new KeyboardListener());
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addPlayersInScreen();
        Thread trid = new Thread(hertz);
        trid.start();
    }
    


    public void addPlayersInScreen() {
        racket1 = new Point(20, height_var);
        racket2 = new Point(WIDTH-40, height_var);
        divide_point = new Point(WIDTH/2, 0);
    }
    

    
    public void ballAnimation() {
        if (ball_X_Direction == "LEFT") {
            ball.x -= 4;
        }
        if (ball_X_Direction == "RIGHT") {
            ball.x += 4;
        }
        if (ball_Y_Direction == "UP") {
            ball.y -= 2;
        }
        if (ball_Y_Direction == "DOWN") {
            ball.y += 2;
        }
    }
    


    public void incrementDificulty() {
        if (fps <= 10) {
            fps = 10;
        } else {
            fps -= 0.5;
            System.out.println(fps);
        }
    }



	public void ballCollideEvaluator() {
        // <---
        if ((ball.x > (racket2.x - 3)) && (ball.x < (racket2.x + 3)) && (ball.y > (racket2.y - 60)) && (ball.y < (racket2.y + 60))) {
            ball_X_Direction = "LEFT";
            incrementDificulty();
            player2_points++;
            System.out.println("Player2:----> "+player2_points);
            ballAnimation();
        }

        //  --->
        if ((ball.x > (racket1.x - 4)) && (ball.x < (racket1.x + 4)) && (ball.y > (racket1.y - 60)) && (ball.y < (racket1.y + 60))) {
            ball_X_Direction = "RIGHT";
            incrementDificulty();
            player1_points++;
            System.out.println("Player1: "+player1_points);
            ballAnimation();
        }

        // UP Limits
        if (ball.y > (HEIGHT-40)) {
            ball_Y_Direction = "UP";
            incrementDificulty();
            ballAnimation();
        }

        // DOWN Limits
        if (ball.y < 0) {
            ball_Y_Direction = "DOWN";
            incrementDificulty();
            ballAnimation();
        }
		ballLeavesTable();
        ballAnimation();
    }




	public void ballLeavesTable() {
		if (ball.x > WIDTH) {
			ball_X_Direction = "RIGHT";ball_Y_Direction = "UP";
			ball.x = WIDTH/2;ball.y = ((HEIGHT/2) + 180);
			fps = 25;
		}
		if (ball.x < 0) {
			ball_X_Direction = "LEFT";ball_Y_Direction = "DOWN";
			ball.x = WIDTH/2;ball.y = 0;
			fps = 25;
		}
	}



    public class GameScreen extends JPanel {
        
        public GameScreen() {
            setBackground(Color.BLACK);
        }
        
        @Override 
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(ball.x, ball.y, 8, 8);
            g.fillRect(racket1.x, racket1.y, WIDTH_POINT, HEIGHT_POINT);
            g.fillRect(racket2.x, racket2.y, WIDTH_POINT, HEIGHT_POINT);
            g.fillRect(divide_point.x, divide_point.y, 5, 500);

			g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 35));
			g.drawString(""+player1_points, 150, 180);
			g.drawString(""+player2_points, 420, 180);
        }
    }

    

    public class KeyboardListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                // Player2 Controls
                case KeyEvent.VK_UP:
                    racket2.y -= 15;
                    break;
                case KeyEvent.VK_DOWN:
                    racket2.y += 15;
                    break;
                // Player1 Controls
                case KeyEvent.VK_W:
                    racket1.y -= 15;
                    break;
                case KeyEvent.VK_S:
                    racket1.y += 15;
                    break;
            } 
        }
    }


    
    public class HertzRate extends Thread {
        long last = 0;
        
        public HertzRate() {

        }

        public void run() {
            while (true) {
                if ((java.lang.System.currentTimeMillis() - last) > fps) {
                    last = java.lang.System.currentTimeMillis();
					ballCollideEvaluator();
                    screen.repaint();
                }
            }
        }
    }



    // Main
    public static void main(String [] args) {
        Pong p = new Pong();
    }

}
