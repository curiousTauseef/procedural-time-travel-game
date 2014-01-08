package core;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import gui.GUtil;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

 
public abstract class Core {
	public static final int SCREEN_WIDTH = 1000;
	public static final int SCREEN_HEIGHT = 800;
	
	public static enum GameState {READY, RUNNING, PAUSED};

	private GameState state = GameState.READY;
	private long lastFrame;

    public Core() {
    	init();
    	System.gc();
    	state = GameState.RUNNING;
    	gameLoop();
    }
    	
    @SuppressWarnings("static-method")
	public void init() {
        try {
            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
            Display.setTitle("Procedural Time");
            Display.create();
        } catch (LWJGLException e) {
            System.err.println("Display wasn't initialized correctly.");
            System.exit(1);
        }
        
        
        
        // GL init code
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-0.5, SCREEN_WIDTH, SCREEN_HEIGHT, -0.5, 1, -1);
        glMatrixMode(GL_MODELVIEW);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
    }
    
    private void gameLoop(){
        while (!Display.isCloseRequested()) {
        	// Render
        	long deltaTime = getDelta();
        	update(deltaTime);
        	switch (state){
	        	case READY:
	        		System.out.println("Error, game not running.");
	        		exit();
	        		break;
	        	case RUNNING:
		        	gameUpdate(deltaTime);
	        		break;
	        	case PAUSED:
	        		pauseUpdate(deltaTime);
	        		break;
        	}
//        	glClear(GL_COLOR_BUFFER_BIT);
        	GUtil.begin();
//        	System.out.println("Drawing");
        	draw();
//        	System.out.println("Done Drawing");
        	GUtil.end();
//        	System.out.println("Ended");
        	//int mouse_x = Mouse.getX();
        	//int mouse_y = SCREEN_HEIGHT - Mouse.getY() - 1;
        	
            Display.update();
            Display.sync(60);
        }
 
        exit();
    }

    public abstract void update(long delta);
	public abstract void gameUpdate(long delta);
	public abstract void pauseUpdate(long delta);
	public abstract void draw();

	public void pauseGame(){
		state = GameState.PAUSED;
	}
	public void unpauseGame(){
		state = GameState.RUNNING;
	}
	
	/**
	 * Calculates frame time.
	 * @return Time since last frame.
	 */
	private long getDelta(){
		long currentTime = (Sys.getTime()*1000)/Sys.getTimerResolution();
		long delta = currentTime - lastFrame;
		lastFrame = currentTime;
		return delta;
	}
	
	/**
	 * Safely close the program.
	 */
	public static void exit(){
		Display.destroy();
        System.exit(0);
	}
    

}