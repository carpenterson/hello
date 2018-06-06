package xzh.nio.multireactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Reader负责读，Sender负责写，Processor负责业务逻辑处理。这个Handler类用来存数据。
 * 读和写都是在subReactor的线程中，业务逻辑处理是在工作线程中。
 */
public class Handler {
	private final SocketChannel socket;
	private final SelectionKey sk;
	private ByteBuffer input = ByteBuffer.allocate(2048);
	private ByteBuffer output = ByteBuffer.allocate(2048);
	private Reader reader = new Reader();
	private Sender sender = new Sender();

	private static ExecutorService pool = Executors.newFixedThreadPool(3);

	public Handler(Selector sel, SocketChannel c) throws IOException {
		socket = c;
		c.configureBlocking(false);
		// Optionally try first read now
		sk = socket.register(sel, 0);
		sk.attach(reader);
		sk.interestOps(SelectionKey.OP_READ);
		sel.wakeup();
	}

	private void handleException(IOException ex) {
		ex.printStackTrace();
		closeSocket();
	}

	private void closeSocket() {
		System.out.println("close socket.");
		sk.cancel();
		try {
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean inputIsComplete() {
		return true;
	}

	private boolean outputIsComplete() {
		return true;
	}

	private void process() {
		System.out.println(new Date().toString() + " receive msg:\r\n"
				+ new String(Arrays.copyOf(input.array(), input.position())));
		output.put(new String("HTTP/1.1 200 OK\r\n" + "Context-Type:text/plain\r\n"
				+ "Content-Length:5\r\n" + "\r\n" + "Hello").getBytes());
	}

	private void read() throws IOException {
		int n = socket.read(input);
		// 如何判断socket关闭？如果socket被关闭，read操作返回-1。
		if (n < 0) {
			closeSocket();
			return;
		}
		if (inputIsComplete()) {
			System.out.println(socket.getRemoteAddress() + ">> read");
			pool.execute(new Processer());
		}
	}

	private void send() throws IOException {
		output.flip();
		socket.write(output);
		if (outputIsComplete()) {
			System.out.println(socket.getRemoteAddress() + ">> send");
			// 继续用这个socket，不关闭
			output.clear();
			input.clear();
			// 在SelectionKey上绑定下一个handler，并切换感兴趣的事件。以此来实现各handler之间有序协作。
			sk.attach(reader);
			sk.interestOps(SelectionKey.OP_READ);
		}
	}

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

	public class Processer implements Runnable {
		@Override
		public void run() {
			process();

			// 在SelectionKey上绑定下一个handler，并切换感兴趣的事件。以此来实现各handler之间有序协作。
			sk.attach(sender);
			// Normally also do first write now
			sk.interestOps(SelectionKey.OP_WRITE);
			// 这里必须调用一次wakeup，否则reactor线程的select一直在等待READ
			sk.selector().wakeup();
		}

	}
}