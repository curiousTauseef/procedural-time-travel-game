package core;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import core.AnimationManager.SpriteSheet;
import entities.Human;
import gui.ClickEvent;
import gui.GButton;
import gui.GPanel;

public class Game extends Core {
	public static int TILE_SIZE = 16;
	public static float SCALE = 3f;
	
	float player_x;
	float player_y;
	Human player;
	
	boolean pauseDown = false;
	GPanel panel;

	private Texture tileSheetTex;
	private Texture peopleTex;
	private Texture guiTex;
	
	private TileMap tileMap;
	private AnimationManager animManager;
	
	public static void main(String[] args){
		new Game();
	}
	
	Game(){
 		super();
		init();
	}
	
	public void init() {
		super.init();
		animManager = new AnimationManager();
		animManager.loadAnims("res/animations.txt", SpriteSheet.MAP);
		animManager.loadAnims("res/peopleAnim.txt", SpriteSheet.PEOPLE);

		panel = new GPanel("panel", null, new Rectangle(10,200,355,200), new Color(95, 158, 160));

		GButton p1 = new GButton("Dr. Seuss", "test", null, 10, 10, new Color(50, 100, 50));
		GButton p2 = new GButton("Eggs and Ham", "other", null, 10, 70, new Color(50, 50, 100));
		GPanel p3 = new GPanel("p4", null, new Rectangle(130, 10, 210, 110), new Color(50, 50, 50));
		GPanel p4 = new GPanel("p4", null, new Rectangle(5, 5, 100, 100), new Color(200, 50, 50));
		GButton p5 = new GButton("Sam I Am", "Really long button", null, 10, 135, new Color(100, 50, 50));
		p3.addChild(p4);
		panel.addChild(p1);
		panel.addChild(p2);
		panel.addChild(p3);
		panel.addChild(p5);

		tileMap = new TileMap(1000, animManager);
		player_x = 300f;
		player_y = 300f;
		player = new Human(300f, 300f, tileMap);
		player.setMovingAnims(animManager.getAnim("main_n_anim"), 
				animManager.getAnim("main_e_anim"),
				animManager.getAnim("main_s_anim"),
				animManager.getAnim("main_w_anim"));
		player.setStandingAnims(animManager.getAnim("main_n"), 
				animManager.getAnim("main_e"),
				animManager.getAnim("main_s"),
				animManager.getAnim("main_w"));
		
		try {
			tileSheetTex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/map.png")), GL11.GL_NEAREST);
			peopleTex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/people.png")), GL11.GL_NEAREST);
			guiTex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/gui.png")), GL11.GL_NEAREST);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			exit();
		} catch (IOException e) {
			e.printStackTrace();
			exit();
		}
	
	}
	
	public void gameUpdate(long deltaTime){
		animManager.update(deltaTime);
		float speed = (float) (0.5*deltaTime);

		if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)){
			player_y -= speed;
			player.move(0f, -speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)){
			player_y += speed;
			player.move(0f, speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)){
			player_x += speed;
			player.move(speed, 0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)){
			player_x -= speed;
			player.move(-speed, 0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q) || 
				Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			exit();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_P)){
			pauseDown = true;
		} else if (pauseDown){
			pauseDown = false;
			pauseGame();
		}
		player.update(deltaTime);
		if (Mouse.next()){
			if (Mouse.getEventButton() == 0 && !Mouse.getEventButtonState()){
				ClickEvent tmp = panel.click(Mouse.getEventX(), SCREEN_HEIGHT - Mouse.getEventY());
				if (tmp != null){
					System.out.println(tmp.getSource());
				}
			}
		}
		
	}
	
	public void pauseUpdate(long deltaTime){
		if (Keyboard.isKeyDown(Keyboard.KEY_P)){
			pauseDown = true;
		} else if (pauseDown){
			pauseDown = false;
			unpauseGame();
		}
		player.update(deltaTime);
	}
	
	
	public void draw(){
		
		glColor3f(1f, 1f, 1f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		// Draw TileMap
		float tileSide = TILE_SIZE * SCALE;
		int playerTile_x = (int) Math.floor(player_x / (tileSide));
		int playerTile_y = (int) Math.floor(player_y / (tileSide));
		glBindTexture(GL_TEXTURE_2D, tileSheetTex.getTextureID());
		for (Tile tile : tileMap.getSurroundingTiles(11, playerTile_x, playerTile_y)){

			drawSprite(tile.getX() * tileSide - player_x + SCREEN_WIDTH/2f,
					tile.getY() * tileSide - player_y + SCREEN_HEIGHT/2f,
					tile.getTexX(), tile.getTexY(), tileSide);
			
		}
		
		glBindTexture(GL_TEXTURE_2D, peopleTex.getTextureID());
		//System.out.println(player.getTexX() + ", " + player.getTexY());
		drawSprite (SCREEN_WIDTH/2f, SCREEN_HEIGHT/2f, player.getTexX(), player.getTexY(), tileSide);
		
		glBindTexture(GL_TEXTURE_2D, guiTex.getTextureID());
		panel.draw();
		
		
	}
	
	public void drawSprite(float x, float y, int texX, int texY, float spriteSide){
		glPushMatrix();
			glTranslatef(x, y, 0);
			glBegin(GL_QUADS);
			glTexCoord2f(texX/16f, texY/16f);				//short,short
			glVertex2f(0, 0);
			glTexCoord2f((texX+1)/16f, texY/16f);			//long, short
			glVertex2f(spriteSide, 0);
			glTexCoord2f((texX+1)/16f, (texY+1)/16f);		//long,  long
			glVertex2f(spriteSide, spriteSide);
			glTexCoord2f(texX/16f, (texY+1)/16f);			//short, long
			glVertex2f(0, spriteSide);
			glEnd();
		glPopMatrix();
	}
}
