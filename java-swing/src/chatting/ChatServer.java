package chatting;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.security.Provider.Service;
import java.util.*;
import javax.swing.*;

public class ChatServer extends JFrame implements ActionListener, Runnable {
	public static void main(String[] args) {
		ChatServer cs = new ChatServer();
		new Thread(cs).start();
	}
	
	/**
	 * 직렬화 : 데이터들을 한줄로 정렬시키는 것.
	 */
	private static final long serialVersionUID = 1L; // 직렬화
	
	Vector<Service> vec; // 접속자를 담아두는 자료구조
	JTextArea txt = new JTextArea();
	JButton btn;

	public ChatServer() {
		super("서버"); // 프레임 이름주고~
		// 부품
		vec = new Vector<Service>();
		txt = new JTextArea();
		btn = new JButton("서버종료");
		btn.addActionListener(this);
		// 조립
		this.add(txt, "Center"); // BorderLayout.CENTER 텍스트는 중간에 놓고
		this.add(btn, "South"); // BorderLayout.SOUTH 버튼은 아래에 놓고
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 프레임에 닫기 버튼 장착
		this.pack(); // 본드로 붙여라. 컴포넌트끼리 떨어지지 않게.
		this.setBounds(50, 100, 300, 600); // 50 = x 좌표, 100 = y좌표 , 300[가로] *
		// 600[세로] 픽셀.
		this.setVisible(true); // 완성했으면 보여줘라
	}
	// "서버종료" 라는 버튼을 클릭했을 경우 이벤트 발생(리스너의 기능)

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "서버종료":
			System.exit(0); // 이프로그램을 종료시켜라!
			break;

		default:
			break;
		}
	}

	@Override
	public void run() {
		ServerSocket ss = null; // 지역변수는 초기화 해야함
		try {
			ss = new ServerSocket(5555);
			// 5555는 서버 접속포트 번호 : 포트 = 1가 3번지.
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			txt.append("클라이언트 접속 대기중\n");
			try {
				Socket s = ss.accept();
				//클라이언트의 소켓은 서버소켓이 이용해야 생성된다.
				txt.append("클라이언트 접속처리");
				Service cs = new Service(s);
				cs.start(); //클라이언트의 스레드가 작동하는 것
				cs.nickName= cs.in.readLine();
				cs.sendMessageAll("/c"+cs.nickName); // 이미 접속되어있는 다른 클라이언트에 나를 알리고
				vec.add(cs);  //접속된 클라이언트를 벡터에 추가함
				for (int i = 0; i < vec.size(); i++) {
					Service service = vec.elementAt(i);
					service.sendMessage("/c"+service.nickName);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	// 이너클레스
	class Service extends Thread{ // 접속자 객체를 생성하는 기본
		String nickName = "guest"; // 대화명
		BufferedReader in; 
		// *Reader, *writer 는 2바이트씩 처리함으로 문자에 최적화(빠르다
		
		OutputStream out;
		// *Stream 는 1 바이트씩 처리하므로 이미지, 음악, 파일에 최적화 되었다.
		Socket s; // 클라이언트 쪽 통신
		Calendar now = Calendar.getInstance(); //싱글톤
		// 디폴트 생성자를 만들지 않고, 파라미터가 존재하는 생성자를 만든다면
		// 반드시 객체를 생성할 때 소켓을 연결해야한다는 의마가 된다.
		public Service(Socket s) {
			this.s = s;
			try {
				in =new BufferedReader(new InputStreamReader(s.getInputStream()));
				out = s.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			while (true) {
				try {
					String msg = in.readLine();
					txt.append("전송메세지 : " + msg + "\n");
					if (msg == null) {
						return; // 더이상 메세지가 없으면 종료
					}
					if (msg.charAt(0) == '/') { 
						char temp = msg.charAt(1);
						switch (temp) {
						case 'n': // n : 이름바꾸기
							if (msg.charAt(2)==' ') {
								sendMessageAll("\n"+nickName+"-"+msg.substring(3).trim());
								// "위의 문장중에서 - 는 닉네임과 새이름을 분리하기 위해 임의로 설정한 기호"
								this.nickName = msg.substring(3).trim();
							}
							break;
						case 'q': // 클라이언트가 퇴장
							for (int i = 0; i < vec.size(); i++) {
								Service svc = (Service) vec.get(i);
								if (nickName.equals(svc.nickName)) {
									vec.remove(i);
									break;
								}
							}
							sendMessageAll(">" + nickName + "님이 퇴장하셨습니다.");
							in.close(); // 인풋 네트워크해제
							out.close(); // 아웃풋 네트워크 해제
							s.close(); // 소켓 해제
							return;
						case 's' : //귓속말
							String name = msg.substring(2, msg.indexOf('-')).trim();
							for (int i = 0; i < vec.size(); i++) {
								Service cs3 = (Service) vec.elementAt(i);
								if (name.equals(cs3.nickName)) {
									cs3.sendMessage((nickName+" >>(귓속말)"+msg.substring(msg.indexOf('-')+1)));
									break;
								}
							}
							break;
						default:
							sendMessageAll(nickName+" >> "+ msg);
							break;
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		public void sendMessageAll(String msg){
			// 벡터의 데이터를 꺼내어 클라이언트에게 보내기
			for (int i = 0; i < vec.size(); i++) {
				Service cs = (Service) vec.elementAt(i);
				cs.sendMessage(msg);
			}
		}
		public void sendMessage(String msg){
			try {
				out.write((msg+"\n").getBytes());
				txt.append("보냄 : " + msg +"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
