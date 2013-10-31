package gui;

import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;

public class GTextbox extends GComponent{

	private String text;
	private int textLen;
	private Color textColor;
	
	public GTextbox(String name, String text){
		super(name);
		this.text = text;
		this.textLen = text.length();
		this.textColor = new Color(0, 0, 0);
	}
	
	public GTextbox(String name, String text, int posX, int posY) {
		super(name, null, null);
		Rectangle rect = new Rectangle(posX, posY, 16*text.length(), 16);
		setRect(rect);
		this.text = text;
		this.textLen = text.length();
		this.textColor = new Color(0, 0, 0);
	}

	public void setText(String text){
		if (text.length() > textLen){
			this.text = text.substring(0, textLen);
			return;
		}
		this.text = text;
	}
	
	public void setTextColor(Color c){
		this.textColor = c;
	}
	
	public GClickEvent clickDown(int x, int y) {
		return null;
	}

	public GClickEvent clickUp(int x, int y) {
		return null;
	}

	public GClickEvent clickHold(int x, int y) {
		return null;
	}

	public void update(long deltaTime) {
		return;
	}

	public void draw() {
		GUtil.drawText(getX(), getY(), textColor, text);
	}

}
