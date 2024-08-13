package life.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseManager implements MouseListener, MouseMotionListener {
	
	private boolean left, right;
	private int mouseX, mouseY;
	
	public MouseManager() {
		left = false;
		right = false;
		mouseX = 0;
		mouseY = 0;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			left = true;
		}
		
		if (e.getButton() == MouseEvent.BUTTON3) {
			right = true;
		}
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			left = true;
		}
		
		if (e.getButton() == MouseEvent.BUTTON3) {
			right = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			left = false;
		}
		
		if (e.getButton() == MouseEvent.BUTTON3) {
			right = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	public int getMouseX() {
		return mouseX;
	}

	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

}
