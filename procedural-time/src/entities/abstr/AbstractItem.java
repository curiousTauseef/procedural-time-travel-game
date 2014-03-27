package entities.abstr;

import core.Game;
import entities.interfaces.Item;

public abstract class AbstractItem extends AbstractEntity implements Item
{
//	public static enum ItemState {LOOSE, PLACED, HELD, STOWED}
//	private ItemState state;
	public AbstractItem(float x, float y){
		super(x, y);
	}
	
	public AbstractItem(){
		super(0, 0);
	}
	
	public void removeFromMap(){
		this.x = 0;
		this.y = 0;
		Game.getMap().getTile(getTileX(), getTileY()).removeEntity(this);
	}
	
	public void addToMap(float x, float y){
		this.x = x;
		this.y = y;
		Game.getMap().getTile(getTileX(), getTileY()).addEntity(this);
	}
	
	@Override
	public void draw(float x, float y){
//		System.out.println("test");
		facing = Facing.NORTH;
		super.draw(x, y);
//		System.out.println("Drew item " + this.toString());
	}
}