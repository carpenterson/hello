package xzh.nio.workthread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 引入work thread
 */
class Reactor implements Runnable {
	final Selector selector;
	final ServerSocketChannel serverSocket;

	public static void main(String[] args) throws IOException {
		new Thread(new Reactor(8000)).start();
	}

	Reactor(int port) throws IOException {
		// 这一个selector负责捕获所有的事件：Accept,Read,Write
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor(selector, serverSocket));
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
}

