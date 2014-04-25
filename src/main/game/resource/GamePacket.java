package main.game.resource;

import java.io.Serializable;

public class GamePacket implements Serializable {
	private static final long serialVersionUID = -4517640171095929776L;

	private static int SEQNUMBER = 0;
	private int seqNo = SEQNUMBER++;

	public GamePacket() {
		
	}

}


// TODO
// game packet to do....
/*

seq no    -> check sequence of packets (is it necessary?)
ping info -> every 2 seconds.
data      -> when change event occur 
  - hp
  - mob send
  - wave sync
  -  




*/