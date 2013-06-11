package flask.utils;

import java.lang.reflect.Array;

public class FixedQueue<T extends Object> {
	protected int size;
	protected int cur;
	protected T[] data;

	@SuppressWarnings("unchecked")
	public FixedQueue(int $size) {
		size = $size;
		data = (T[]) new Object[$size];
		cur = $size - 1;
		inits();
	}

	@SuppressWarnings("unchecked")
	public FixedQueue(Class<T> type, int $size) {
		size = $size;
		data = (T[]) Array.newInstance(type, $size);
		cur = $size - 1;
		inits();
	}

	protected void inits() {
		int i = 0;
		for (; i < size; i++) {
			data[i] = null;
		}
	}

	public void initializeWith(T init_value) {
		for (int i = 0; i < size; i++) {
			data[i] = (T) init_value;
		}
	}

	public T enQueue(T o) {
		T temp = data[cur];
		data[cur] = (T) o;
		if (cur <= 0)
			cur = size - 1;
		else
			cur--;
		return temp;
	}

	public T deQueue() {
		T temp = data[cur];
		data[cur] = null;
		if (cur <= 0)
			cur = size - 1;
		else
			cur--;
		return temp;
	}

	public T get() {
		try {
			return data[cur];
		} finally {
			if (cur <= 0)
				cur = size - 1;
			else
				cur--;
		}
	}

	public int size() {
		return size;
	}

	public T[] getData() {
		return data;
	}

	public void printData() {
		trace(data);
	}

	protected void set_cur() {
		cur--;
		if (cur < 0)
			cur = size - 1;
	}

	protected static void trace(Object... args) {
		int l = args.length - 1, i = l;
		for (; i > 0; i--) {
			System.out.print(args[i] + ", ");
		}
		System.out.println(args[0]);
	}
}