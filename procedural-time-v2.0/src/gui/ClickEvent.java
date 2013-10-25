package gui;

public class ClickEvent {
	public static enum EventType{BUTTON, MENU};
	private String action;
	private GComponent source;
	private EventType type;
	
	public ClickEvent(String action, GComponent source, EventType type){
		this.action = action;
		this.source = source;
		this.type = type;
	}
	
	public String getAction(){
		return this.action;
	}
	public GComponent getSource(){
		return this.source;
	}
	public EventType getType(){
		return this.type;
	}
}
