package xzh.nio.multireactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {
	private final Selector selector;

	public Reactor() throws IOException {
		selector = Selector.open();
		new Thread(this).start();
	}

	public Selector getSelector() {
		return selector;
	}

	public void run() {
		try {
			while (!Thread.interrupted()) {
				// 不能用selector.select();因为select和register都会锁住publicKeys；无入参的select方法会一直等待，使得register被阻塞。
				if (selector.select(500) < 0) {
					continue;
				}
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
