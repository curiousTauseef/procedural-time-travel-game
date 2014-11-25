package core.display;

import gui.GUtil.SpriteSheetType;

import java.util.ArrayList;
import java.util.HashMap;

public class SpriteManager {
	
	private static SpriteManager sm;
	private HashMap<SpriteSheetType, SpriteSheet> spriteSheets;
	
	private  SpriteManager() {
		spriteSheets = new HashMap<>();
	}
	
	public static SpriteManager get(){
		if (sm == null){
			sm = new SpriteManager();
		}
		return sm;
	}
	
	public void loadSpriteSheet(SpriteSheet ss){
		spriteSheets.put(ss.getType(), ss);
		PTTGSTDataParser.load(ss, this);
	}
	
	public void addImage(Image img){
		img.getSpriteSheet().addImage(img);
	}
	
	public void addAnim(Animation2 anim){
		anim.getSpriteSheet().addAnim(anim);
	}
	
	public Image getImage(SpriteSheet ss, int id){
		return ss.getImage(id);
	}
	
}
