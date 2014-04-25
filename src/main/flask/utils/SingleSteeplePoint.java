package main.flask.utils;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

public class SingleSteeplePoint {
	
	/*
	 * _ _ ( + _ ( = - ( + * * * �ܼ��� ���� Ƣ���,,,
	 */
	
	private Vec2D position;
	private Vec2D a0, b0, c0;
	private Vec2D a1, b1, c1;
	private double theta = 0;
	private double dTheta = 0;
	private double r = 1;
	private Shape s;
	private Path2D path;
	
	public SingleSteeplePoint(Vec2D postion, double theta, double dTheta, double r) {
		setFactors(postion, theta, dTheta, r);
	}
	
	private void calc() {
		a0 = new Vec2D(r, 0);
		a0.rotate(dTheta);
		
		double l = r / Math.cos(dTheta);
		double d = r * Math.tan(dTheta);
		
		b0 = new Vec2D(l, 0);
		
		c0 = new Vec2D(l + d / 4, 0);
		
		//////////////// 
		
		a1 = new Vec2D(r, 0);
		a1.rotate(-dTheta);
		
		b1 = new Vec2D(l, 0);
		
		c1 = new Vec2D(l + d / 4, 0);
		
		////////////////
		
		a0.rotate(theta);
		b0.rotate(theta);
		c0.rotate(theta);
		
		a1.rotate(theta);
		b1.rotate(theta);
		c1.rotate(theta);
		
		a0.add(position);
		b0.add(position);
		c0.add(position);
		a1.add(position);
		b1.add(position);
		c1.add(position);
	}
	
	public void setFactors(Vec2D position, double theta, double dTheta, double r) {
		this.position = position;
		this.theta = theta;
		this.dTheta = dTheta;
		this.r = r;
		
		s = new Ellipse2D.Double(position.x - r, position.y - r, r * 2, r * 2);
		path = new GeneralPath();
		
		calc();
		makePath();
	}
	
	public void setTarget(Vec2D target) {
		Vec2D diff = position.getSub(target);
		this.theta = Math.atan2(diff.y, diff.x);
		calc();
	}
	
	public Path2D makePath() {
		path.reset();
		
		path.moveTo(position.x, position.y);
		path.lineTo(a0.x, a0.y);
		path.curveTo(a0.x, a0.y, b0.x, b0.y, c0.x, c0.y);
		path.curveTo(c1.x, c1.y, b1.x, b1.y, a1.x, a1.y);
		path.lineTo(position.x, position.y);
		path.closePath();
		
		return path;
	}
	
	public void draw(Graphics2D g) {
		g.draw(path);
		g.draw(s);
	}
	
	public void fill(Graphics2D g) {
		g.fill(path);
		g.fill(s);
	}
}
