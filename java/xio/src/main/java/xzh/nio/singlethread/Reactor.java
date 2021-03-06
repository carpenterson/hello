package xzh.nio.singlethread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * 单线程版本。这里把所有的类放到一个文件中。
 */
class Reactor implements Runnable { // 没有public修饰就是包访问权限
	final Selector selector;
	final ServerSocketChannel serverSocket;
	
	public static void main(String[] args) throws IOException {
		new Thread(new Reactor(8000)).start();
	}
	
	Reactor(int port) throws IOException {
		// 这一个selector负责监听所有的事件：Accept,Read,Write
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor());
	}

	public void run() { // normally in a new Thread
		try {
			while (!Thread.interrupted()) {
				selector.select();
				Set<SelectionKey> selected = selector.selectedKeys();
				Iterator<SelectionKey> it = selected.iterator();
				while (it.hasNext())
					dispatch((SelectionKey) (it.next()));
				// 必须clear。否则，SelectionKey一直在这个列表中，会一直进入read()代码（第一次之后，read的结果都是0）
				selected.clear();
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	void dispatch(SelectionKey k) {
		Runnable r = (Runnable) (k.attachment());
		if (r != null)
			r.run();
	}

	class Acceptor implements Runnable { // inner
		public void run() {
			try {
				// chrome浏览器默认会在网站的根目录下获取favicon.ico。也就是说在浏览器输入http://localhost:8000，chrome会发送两个http请求。
				// 这两个http请求有可能会创建两个socket，也可能是在一个socket中串行发送
				SocketChannel c = serverSocket.accept();
				if (c != null)
					new Handler(selector, c);
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}

final class Handler implements Runnable { //同一个java文件中可以定义多个类，只能有一个public类
	final SocketChannel socket;
	final SelectionKey sk;
	ByteBuffer input = ByteBuffer.allocate(2048);
	ByteBuffer output = ByteBuffer.allocate(2048);
	static final int READING = 0, SENDING = 1;
	int state = READING;

	Handler(Selector sel, SocketChannel c) throws IOException {
		socket = c;
		c.configureBlocking(false);
		// Optionally try first read now
		sk = socket.register(sel, 0);
		sk.attach(this);
		sk.interestOps(SelectionKey.OP_READ);
		sel.wakeup();
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

	public void run() {
		try {
			if (state == READING)
				read();
			else if (state == SENDING)
				send();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			sk.cancel();
			try {
				socket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void read() throws IOException {
		socket.read(input);
		if (inputIsComplete()) {
			System.out.println(socket.getRemoteAddress() + ">> read");
			process();
			state = SENDING;
			// Normally also do first write now
			sk.interestOps(SelectionKey.OP_WRITE);
			// 如果不wakeup一下，selector.select()是否会阻塞在等待READ事件？这里是单线程版本，不存在这个问题。
			// sk.selector().wakeup();
		}
	}

	void send() throws IOException {
		output.flip();
		socket.write(output);
		if (outputIsComplete()) {
			System.out.println(socket.getRemoteAddress() + ">> send");
			// 方式一：继续用这个socket，不关闭
			state = READING;
			output.clear();
			input.clear();
			sk.interestOps(SelectionKey.OP_READ);

			// 方式二：用一次就关闭掉socket
			// sk.cancel();
			// socket.close();
		}
	}
}