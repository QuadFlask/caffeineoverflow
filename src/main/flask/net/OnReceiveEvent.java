package main.flask.net;

public class OnReceiveEvent {
	
	public static enum STATE {
		NONE,
		CONNECTED,
		REFUSED,
		DISCONNECTED,
		RECEIVED,
		CLOSED,
		ERROR,
		ACK,
		ACK_PING,
		REQ_PING
	}
	
	public static final OnReceiveEvent CONNECTED = new OnReceiveEvent(STATE.CONNECTED, null);
	public static final OnReceiveEvent DISCONNECTED = new OnReceiveEvent(STATE.DISCONNECTED, null);
	public static final OnReceiveEvent REFUSED = new OnReceiveEvent(STATE.REFUSED, null);
	public static final OnReceiveEvent CLOSED = new OnReceiveEvent(STATE.CLOSED, null);
	public static final OnReceiveEvent ERROR = new OnReceiveEvent(STATE.ERROR, null);
	
	public STATE state;
	public Object data;
	
	public OnReceiveEvent(STATE s, Object d) {
		this.state = s;
		this.data = d;
	}
	
	@Override
	public String toString() {
		return "OnReceiveEvent [state=" + state + ", data=" + data + "]";
	}
}
