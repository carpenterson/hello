package xzh.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

	private static final int PORT = 8000;
	private static final int MAX_INPUT = 2048;

	public static void main(String[] args) {
		new Thread(new Server()).start();
	}

	public void run() {
		try (ServerSocket ss = new ServerSocket(PORT)) {
			while (!Thread.interrupted())
				new Thread(new Handler(ss.accept())).start();
			// or, single-threaded, or a thread pool
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	static class Handler implements Runnable {
		final Socket socket;

		Handler(Socket s) {
			socket = s;
		}

		public void run() {
			try {
				byte[] input = new byte[MAX_INPUT];
				socket.getInputStream().read(input);
				byte[] output = process(input);
				socket.getOutputStream().write(output);
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		private byte[] process(byte[] cmd) {
			System.out.println(new String(cmd));
			return new String("HTTP/1.1 200 OK\r\n" + 
					"Context-Type:text/plain\r\n" + 
					"Content-Length:5\r\n" + 
					"\r\n" + 
					"Hello").getBytes();
		}
	}
}
