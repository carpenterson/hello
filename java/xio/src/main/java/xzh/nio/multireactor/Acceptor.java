package xzh.nio.multireactor;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {
	private final Reactor[] subReactors;
	private final ServerSocketChannel serverSocket;
	private int next = 0;

	public Acceptor(Reactor[] subReactors, ServerSocketChannel serverSocket) {
		this.subReactors = subReactors;
		this.serverSocket = serverSocket;
	}

	public void run() {
		try {
			// chrome浏览器默认会在网站的根目录下获取favicon.ico。也就是说在浏览器输入http://localhost:8000，chrome会发送两个http请求。
			// 这两个http请求有可能会创建两个socket，也可能是在一个socket中串行发送
			SocketChannel c = serverSocket.accept();
			if (c != null) {
				new Handler(subReactors[next].getSelector(), c);
				if (++next == subReactors.length) {
					next = 0;
				}
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
