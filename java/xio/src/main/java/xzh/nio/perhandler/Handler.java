package xzh.nio.perhandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Date;

class Handler {

	final SocketChannel socket;
	final SelectionKey sk;
	ByteBuffer input = ByteBuffer.allocate(2048);
	ByteBuffer output = ByteBuffer.allocate(2048);
	Reader reader = new Reader();
	Sender sender = new Sender();

	Handler(Selector sel, SocketChannel c) throws IOException {
		socket = c;
		c.configureBlocking(false);
		// Optionally try first read now
		sk = socket.register(sel, 0);
		sk.attach(reader);
		sk.interestOps(SelectionKey.OP_READ);
		sel.wakeup();
	}

	void handleException(IOException ex) {
		ex.printStackTrace();
		sk.cancel();
		try {
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	boolean inputIsComplete() {
		return true;
	}

	boolean outputIsComplete() {
		return true;
	}

	void process() {
		System.out.println(new Date().toString() + " receive msg:\r\n"
				+ new String(Arrays.copyOf(input.array(), input.position())));
		output.put(new String("HTTP/1.1 200 OK\r\n" + "Context-Type:text/plain\r\n"
				+ "Content-Length:5\r\n" + "\r\n" + "Hello").getBytes());
	}

	void read() throws IOException {
		socket.read(input);
		if (inputIsComplete()) {
			System.out.println(socket.getRemoteAddress() + ">> read");
			process();

			// 在SelectionKey上绑定下一个handler，并切换感兴趣的事件。以此来实现各handler之间有序协作。
			sk.attach(sender);
			// Normally also do first write now
			sk.interestOps(SelectionKey.OP_WRITE);
		}
	}

	void send() throws IOException {
		output.flip();
		socket.write(output);
		if (outputIsComplete()) {
			System.out.println(socket.getRemoteAddress() + ">> send");
			// 方式一：继续用这个socket，不关闭
			output.clear();
			input.clear();
			// 在SelectionKey上绑定下一个handler，并切换感兴趣的事件。以此来实现各handler之间有序协作。
			sk.attach(reader);
			sk.interestOps(SelectionKey.OP_READ);

			// 方式二：用一次就关闭掉socket
			// sk.cancel();
			// socket.close();
		}
	}

	// 用内部类比较方便。如果不用内部类，需要把input\output\sender的引用放到Reader类中。
	public class Reader implements Runnable {
		@Override
		public void run() {
			try {
				read();
			}
			catch (IOException ex) {
				handleException(ex);
			}
		}
	}

	public class Sender implements Runnable {
		@Override
		public void run() {
			try {
				send();
			}
			catch (IOException ex) {
				handleException(ex);
			}
		}
	}
}