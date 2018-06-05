package xzh.nio.workthread;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

class Acceptor implements Runnable { 
	final Selector selector;
	final ServerSocketChannel serverSocket;
	
	public Acceptor(Selector selector, ServerSocketChannel serverSocket) {
		super();
		this.selector = selector;
		this.serverSocket = serverSocket;
	}

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
