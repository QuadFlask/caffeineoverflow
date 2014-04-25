package main.flask.utils;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiSteeplePoint {
	private Vec2D position;
	private List<Vec2D> a0l, b0l, c0l;
	private List<Vec2D> a1l, b1l, c1l;
	private List<Vec2D> targets;
	
	private double dTheta = 0;
	private double r = 1;
	private Shape s;
	private Path2D path;
	
	public MultiSteeplePoint(Vec2D position, double dTheta, double r) {
		this.position = position;
		this.dTheta = dTheta;
		this.r = r;
		
		path = new GeneralPath();
		s = new Ellipse2D.Double(position.x - r, position.y - r, r * 2, r * 2);
		
		a0l = new ArrayList<Vec2D>();
		b0l = new ArrayList<Vec2D>();
		c0l = new ArrayList<Vec2D>();
		
		a1l = new ArrayList<Vec2D>();
		b1l = new ArrayList<Vec2D>();
		c1l = new ArrayList<Vec2D>();
		
		targets = new ArrayList<Vec2D>();
	}
	
	public void addTarget(Vec2D target) {
		targets.add(target);
		
		Vec2D diff = position.getSub(target);
		addToList(calcTo(Math.atan2(diff.y, diff.x)));
	}
	
	public void removeTarget(Vec2D target) {
		targets.remove(target);
		reCalculate();
	}
	
	public void removeTarget(int index) {
		targets.remove(index);
		reCalculate();
	}
	
	public void clear() {
		targets.clear();
		a0l.clear();
		b0l.clear();
		c0l.clear();
		a1l.clear();
		b1l.clear();
		c1l.clear();
	}
	
	private void addToList(List<Vec2D> list) {
		a0l.add(list.get(0));
		b0l.add(list.get(1));
		c0l.add(list.get(2));
		a1l.add(list.get(3));
		b1l.add(list.get(4));
		c1l.add(list.get(5));
	}
	
	public void reCalculate() {
		Vec2D diff;
		a0l.clear();
		b0l.clear();
		c0l.clear();
		a1l.clear();
		b1l.clear();
		c1l.clear();
		
		for (Vec2D target : targets) {
			diff = position.getSub(target);
			addToList(calcTo(Math.atan2(diff.y, diff.x)));
		}
	}
	
	private List<Vec2D> calcTo(double theta) {
		Vec2D a0 = new Vec2D(r, 0);
		a0.rotate(dTheta);
		
		double l = r / Math.cos(dTheta);
		double d = r * Math.tan(dTheta);
		
		Vec2D b0 = new Vec2D(l, 0);
		
		Vec2D c0 = new Vec2D(l + d / 4, 0);
		
		//////////////// 
		
		Vec2D a1 = new Vec2D(r, 0);
		a1.rotate(-dTheta);
		
		Vec2D b1 = new Vec2D(l, 0);
		
		Vec2D c1 = new Vec2D(l + d / 4, 0);
		
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
		
		return Arrays.asList(a0, b0, c0, a1, b1, c1);
	}
	
	public Path2D makePath() {
		path.reset();
		
		path.closePath();
		return path;
	}
	
}
