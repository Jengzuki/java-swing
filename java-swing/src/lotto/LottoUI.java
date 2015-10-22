package lotto;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class LottoUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	Lotto lotto;
	// 컴포넌트(스윙에서 화면을 나타내는 클래스)는 연관관계로 부모=자식 관계를 맺는다.
	JButton btn;
	JPanel panelNorth, panelSouth; //보더레이아웃 개념으로 볼때 동서남북으로 위치값을 줌.
	ImageIcon icon;
	List<JButton> btns;
		
	public LottoUI() {
		init();
	}
	public void init(){ // initialize의 약자로 초기화 메소드 이름으로 많이 사용함.
		/* 부품 준비단계 -> 큰것에서 작은것 순으로*/
		this.setTitle("SBS로또 추첨");
		lotto = new Lotto(); 
		btns = new ArrayList<JButton>();
		panelNorth = new JPanel();
		panelSouth = new JPanel();
		btn = new JButton("로또 번호추첨");
		/* 조합단계 -> 작은것부터 큰것 순으로 */
		btn.addActionListener(this);// 이클레스에서 구현한 관련 메소드를 할당한다.
		panelNorth.add(btn);//북쪽 패널에 버튼을 장착
		this.add(panelNorth, BorderLayout.NORTH);
		this.add(panelSouth, BorderLayout.SOUTH);
		this.setBounds(300, 400, 1200, 300);
		//300,400은 x,y로 좌표값
		//1200, 300은 픽셀로 크기
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (btns.size() == 0) {
			JButton tmp = null;
			for (int i = 0; i < 6; i++) {
				tmp = new JButton(); // 번호가 붙지 않은 로또볼객체를 만들어라.
				btns.add(tmp); // 6회전이므로 비어있는 로또볼 6새를 만들어 달아라.
				panelSouth.add(tmp); // 리스트에 담고, 또 그 로또볼을 패널에 붙여라
			}
		}
		lotto.setLotto(); // 로또볼 추첨에 들어갑니다.
		int[] temp = lotto.getLotto();
		for (int i = 0; i < temp.length; i++) {
			btns.get(i).setIcon(new ImageIcon("src/image/"+temp[i]+".gif"));
		}
	}

}
