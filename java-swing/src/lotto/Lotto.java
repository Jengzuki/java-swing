package lotto;

import java.util.Arrays;
import java.util.Vector;

/**
 * @file_name : Lotto.java
 * @author : apfl1@naver.com
 * @date : 2015. 10. 22.
 * @Story : 로또 알고리즘
 */
public class Lotto {
	
	Vector<Integer> vec = new Vector<Integer>();
	int[] lotto = new int[6]; // SBS 에서는 단 하나의 로또 번호만 출력

	public Lotto() {
		this.setLotto();
	}

	public int[] getLotto() {
		for (int i = 0; i < lotto.length; i++) {
			System.out.println("로또"+lotto[i]);
		}
		return lotto;
	}

	public void setLotto() {
		for (int i = 0; i < lotto.length; i++) {
			int randomNum = (int) (Math.random() * 45 + 1);
			boolean exist = false;
			for (int j = 0; j < lotto.length; j++) {
				if (randomNum == lotto[j]) {
					exist = true; // 이미 존재함
					break;
				}
			}
			if (exist) {
				i--; // 중복된 값이 출력되면 카운트 숫자를 줄여준다.
				continue;
			}else {
				//if 문을 타지 않는 경우
				lotto[i] = randomNum;
			}
		} 
		Arrays.sort(lotto);//오름차순 정렬
	}

}
