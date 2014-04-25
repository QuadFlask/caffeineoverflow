package main.flask.net;

import java.io.Serializable;

import main.flask.net.OnReceiveEvent.STATE;

public class WrapperPacket implements Serializable {
	private static final long serialVersionUID = -2891154439940139421L;
	
	public STATE state;
	public int seq;
	
	public WrapperPacket(STATE state, int seq) {
		this.state = state;
		this.seq = seq;
	}
	
	public WrapperPacket(STATE state) {
		this.state = state;
	}
	
}
