package main.flask.net;

import static main.flask.net.SimpleLogger.log;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetWorker_old {

	private ExecutorService threadPool = Executors.newFixedThreadPool(4);
	private Object readerLock = new Object();
	private Object senderLock = new Object();
	private List<OnReceiveListener> listenerList = new ArrayList<OnReceiveListener>();

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private boolean isConnecting = false;
	private boolean isAccepting = false;
	private boolean isListening = false;

	public void connect(String host, int port) {
		close();
		isConnecting = true;
		log("[connect] connect... " + host + ":" + port);

		try {
			clientSocket = new Socket(host, port);
			clientSocket.setTcpNoDelay(true);
			log("[connect] connected!");
			setIOStream();
			fireOnReceiveEvent(OnReceiveEvent.CONNECTED);
			startSocketListening();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			fireOnReceiveEvent(OnReceiveEvent.ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			fireOnReceiveEvent(OnReceiveEvent.ERROR);
		}
		isConnecting = false;
	}

	public void accept(int port) {
		close();
		isAccepting = true;
		log("[accept] listening at " + port);

		try {
			serverSocket = new ServerSocket(port);
			log("[accept] accepting...");
			clientSocket = serverSocket.accept();
			log("[accept] accepted!");
			setIOStream();
			fireOnReceiveEvent(OnReceiveEvent.CONNECTED);
			startSocketListening();
		} catch (IOException e) {
			e.printStackTrace();
			fireOnReceiveEvent(OnReceiveEvent.ERROR);
		}
		isAccepting = false;
	}

	public void disconnect() {
		send(OnReceiveEvent.CLOSED);
		isListening = false;
		close();
		fireOnReceiveEvent(OnReceiveEvent.DISCONNECTED);
	}

	protected void setIOStream() {
		try {
			log("[setIOStream] get IO Streams...");
			writer = new ObjectOutputStream(clientSocket.getOutputStream());
			reader = new ObjectInputStream(clientSocket.getInputStream());
			log("[setIOStream] Complete!");
		} catch (IOException e) {
			e.printStackTrace();
			fireOnReceiveEvent(OnReceiveEvent.ERROR);
		}
	}

	public boolean send(Object data) {
		try {
			synchronized (senderLock) {
				log("[send] sending...[" + data.hashCode() + "] " + data.toString());
				if (writer != null) {
					// writer.reset();
					writer.writeObject(data);
					writer.flush();
					log("[send] sent!   [" + data.hashCode() + "]");
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			fireOnReceiveEvent(OnReceiveEvent.ERROR);
			fireOnReceiveEvent(OnReceiveEvent.DISCONNECTED);
		}
		return false;
	}

	public void startSocketListening() {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				isListening = true;
				Object data;
				try {
					synchronized (readerLock) {
						log("[startSocketListening] listening...");
						while (isListening) { // http://stackoverflow.com/questions/8630494/optionaldataexception-in-java
							// 일단 레퍼패킷에 그냥 메타데이터만 담아서 보내기로하자.....아 짱나,ㅅㅂㄴ
							while (clientSocket.getInputStream().available() > 0 && (data = reader.readObject()) != null) {// +
								log("[startSocketListening] data readed : '" + data.toString() + "'");

								if (data instanceof WrapperPacket) fireOnReceiveEvent(new OnReceiveEvent(((WrapperPacket) data).state, data));
								else fireOnReceiveEvent(new OnReceiveEvent(OnReceiveEvent.STATE.RECEIVED, data));
							}
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					fireOnReceiveEvent(OnReceiveEvent.ERROR);
				} catch (OptionalDataException e) {
					e.printStackTrace();
					System.out.println("ODE e.length : " + e.length);
					try {
						System.out.println("clientSocket.getInputStream().available() : " + clientSocket.getInputStream().available());
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					try {
						int i = 0, p;
						while ((p = reader.read()) >= 0) {
							System.out.println(String.format("reader.read()[%02d] : %d ", i++, p));
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					fireOnReceiveEvent(OnReceiveEvent.ERROR);
				} catch (EOFException e) {
					e.printStackTrace();
					fireOnReceiveEvent(OnReceiveEvent.ERROR);
				} catch (IOException e) {
					e.printStackTrace();
					fireOnReceiveEvent(OnReceiveEvent.ERROR);
				}

				try {
					System.out.println("clientSocket.getInputStream().available() : " + clientSocket.getInputStream().available());
				} catch (IOException e) {
					e.printStackTrace();
				}
				fireOnReceiveEvent(OnReceiveEvent.DISCONNECTED);
				close();
				isListening = false;
			}
		});
	}

	public void close() {
		log("[close] Closing sockets");

		try {
			synchronized (readerLock) {
				fireOnReceiveEvent(OnReceiveEvent.CLOSED);
				if (clientSocket != null) clientSocket.close();
				if (serverSocket != null) serverSocket.close();
				if (reader != null) reader.close();
				if (writer != null) writer.close();

				log("[close] Closed properly!");
			}
		} catch (IOException e) {
			fireOnReceiveEvent(OnReceiveEvent.ERROR);
			e.printStackTrace();
		} finally {
			isListening = isConnecting = isAccepting = false;
			clientSocket = null;
			serverSocket = null;
			reader = null;
			writer = null;
		}
	}

	// /////////////////////////////////////

	public boolean isConnected() {
		return clientSocket != null && clientSocket.isConnected();
	}

	public boolean isConnecting() {
		return isConnecting;
	}

	public boolean isAccepting() {
		return isAccepting;
	}

	public boolean isListening() {
		return isListening;
	}

	public void addOnReceiveListener(OnReceiveListener listener) {
		listenerList.add(listener);
	}

	public void removeReceiveListener(OnReceiveListener listener) {
		listenerList.remove(listener);
	}

	public void removeAll() {
		listenerList.clear();
	}

	protected void fireOnReceiveEvent(OnReceiveEvent e) {
		synchronized (listenerList) {
			for (OnReceiveListener listener : listenerList) {
				listener.onReceive(e);
			}
		}
	}
}
