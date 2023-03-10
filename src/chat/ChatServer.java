package chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Vector;

public class ChatServer {

	// 1. ���� ��Ʈ ��ȣ ����(1024~65535) - ����� ����
	public static final int PORT = 5698;

	public static void main(String[] args) {

		String ip = null;

		// 2. ���� IP(��ſ�)�ּ� Ȯ��(InetAddress) (172.30.1.89) �ּҸ� �����´�.
		try {
			ip = InetAddress.getLocalHost().getHostAddress();

		} catch (UnknownHostException e1) {
			System.out.println("getAddress Error : " + e1.getMessage());
		}

		ServerSocket serverSocket = null;

		/*
		 * 3. ���� Ŭ���̾�Ʈ���� ���� ��Ʈ��(�帧)�� ����Ʈȭ�Ͽ� ���� �� ��Ʈ��(�帧)�� Ŭ���̾�Ʈ�� �������� ����ϴ� ���� �ڿ�(�÷��� ������
		 * ��ũ)
		 */
		/*
		 * PrintWriter: Writer�� ����Ͽ� Writer ��� �پ��� ��� ��� ���� (print/println/printf ��, Ư��
		 * println()(���� ����)���� ����� ������ readLine()(���� �ձ��� ����)�� ������ ��) Ŭ���̾�Ʈ�� 2byte ������
		 * ���(���� ��� ��� ��Ʈ���� �����)
		 */
		Vector<PrintWriter> printWriterList = new Vector<>();

		try {
			// 4. ���� ���� ���� (�� ��ȭ��)
			serverSocket = new ServerSocket();

			// 5. ���� ���� ���ε�(���Ͽ� IP(��ȭ��ȣ), port ��ȣ�� �ο�) / (�� ��ȭ�� : ��ȭ��ȣ�� �ο���) �� �Ȱ���
			serverSocket.bind(new InetSocketAddress(ip, PORT));
			// ���� �������� ������ �ּ�(��ȭ��ȣ) port ��ȣ�� �ο�
			System.out.println("[������ ��ٸ��ϴ�] " + Thread.currentThread().getId() + "�� IP: " + ip + ", "
					+ Thread.currentThread().getId() + "�� ��Ʈ��ȣ: " + PORT);
			/*
			 * Thread.currentThread().getName(): 'main' ���� Thread.currentThread().getId():
			 * '1' ����
			 */

			// 6. Ŭ���̾�Ʈ�κ��� ���� ��û �ޱ�(���� �ݺ� / ���� �� ����)
			while (true) {
				// 7. Ŭ���̾�Ʈ ���� ��û ��� �� ���� �� ��ſ� ���� ����(��� ����)
				Socket socket = serverSocket.accept();

				// 8. (���ÿ� ���� ����� ��ȭ�ϱ� ����) Ŭ���̾�Ʈ���� ����� ������� ����(����, ��Ʈ��(�帧) Ȱ��)
				ChatServerThread chatServerThread = new ChatServerThread(socket, printWriterList);
				chatServerThread.start();

				// 9. Ŭ���̾�Ʈ�� ���� �ּ�(IP �ּҿ� ��Ʈ ��ȣ) Ȯ��
				SocketAddress socketAddress = socket.getRemoteSocketAddress();
				InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
				System.out.println("[������ �����մϴ�] " + "Ŭ���̾�Ʈ�� IP: " + inetSocketAddress.getHostString()
						+ ", Ŭ���̾�Ʈ�� ��Ʈ��ȣ: " + inetSocketAddress.getPort());
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			// 10. (�۵� ���̸�) ���� ���� �ݱ�
			try {
				if (serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
