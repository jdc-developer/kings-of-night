package jdc.kings.state.objects;

public class Option {
	
	private String description;
	private float x;
	private float y;
	private float width;
	private float height;
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public Option(String description, float width, float height) {
		super();
		this.description = description;
		this.width = width;
		this.height = height;
	}

	public Option() {
		super();
	}

}
