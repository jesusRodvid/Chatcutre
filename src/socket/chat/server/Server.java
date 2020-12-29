package socket.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import socket.chat.messages.Message;

public class Server implements Runnable {

	private int port;
	private Queue<Message> queue = new ConcurrentLinkedQueue<>();
	private ServerSocket serverSocket;
	private Thread thread;

	public Server(int port) {
		this.port = port;
	}
	
	public Server() {
		this(9999);
	}
		
	@Override
	public void run() {
		try {

			while (true) {

				Socket misocket = serverSocket.accept();
				new Thread(() -> onClientConnected(misocket)).start();

			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void start() throws IOException {
		System.out.println("Starting server...");
		serverSocket = new ServerSocket(port);
		thread = new Thread(this);
		thread.start();
		System.out.println("Server started!");
	}

	public void stop() {
		System.out.println("Stopping server...");
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server stopped!");
	}
	
	public Queue<Message> getQueue() {
		return queue;
	}
	
	private void onClientConnected(Socket socket) {

		System.out.println("Client connected: " + socket.getInetAddress() + ":" + socket.getLocalPort());
		
		try {
			
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			while (socket.isConnected()) {

				Message message = (Message) ois.readObject();
				System.out.println("Message received: " + message + "!");
				queue.add(message);
				System.out.println("Message queued!");
					
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("Client disconnected!");
		
		
	}
	
	public static void main(String[] args) throws IOException {
		new Server().start();
	}

}
