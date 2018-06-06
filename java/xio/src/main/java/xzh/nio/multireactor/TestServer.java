package xzh.nio.multireactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

public class TestServer {

	public static void main(String[] args) throws IOException {
		// 负责监听和分发Accept事件
		Reactor mainReactor = new Reactor();
		
		// 负责监听和分发Read和Write事件
		int nSubReactor = 2; 
		Reactor[] subReactors = new Reactor[nSubReactor];
		for (int i = 0; i < subReactors.length; i++) {
			subReactors[i] = new Reactor();
		}

		ServerSocketChannel serverSocket;
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(8001));
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(mainReactor.getSelector(),
				SelectionKey.OP_ACCEPT);
		// Acceptor负责处理Accept事件
		sk.attach(new Acceptor(subReactors, serverSocket));

	}

}
