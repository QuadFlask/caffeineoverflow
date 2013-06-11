package flask.threadpool;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class WorkQueue {
	private LinkedList<Runnable> queue;

	public WorkQueue() {
		queue = new LinkedList<Runnable>();
	}

	public void enqueue(Runnable work) {
		queue.add(work);
	}

	public Runnable dequeue() throws NoSuchElementException {
		return queue.removeFirst();
	}

	public boolean isEmpty(){
		return queue.isEmpty();
	}

	public int size(){
		return queue.size();
	}

	public void clear(){
		queue.clear();
	}
}
