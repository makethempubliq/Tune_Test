package tune_test;

import javax.swing.*;
import java.net.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class DifficultySelector {
    public DifficultySelector() {
        Object[] options = {"초급", "중급", "고급", "종료"};
        int choice = JOptionPane.showOptionDialog(null, "난이도를 선택하세요.", "난이도 선택",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        switch (choice) {
            case 0:
                // 초급 난이도의 실행 코드
                SoundPlayerGUI beginner = new SoundPlayerGUI();
                beginner.setVisible(true);
                break;
            case 1:
                // 중급 난이도의 실행 코드
            	SoundPlayerGUI_Interm interm = new SoundPlayerGUI_Interm();
            	interm.setVisible(true);
                break;
            case 2:
                // 고급 난이도의 실행 코드
            	SoundPlayerGUI_adv adv = new SoundPlayerGUI_adv();
            	adv.setVisible(true);
                break;
            case 3:
            	Socket socket = null;
            	try {
            	socket = new Socket("localhost", 9999);
            	BufferedWriter	out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                
                out.write("end\n");	//서버에 종료 신호 전송
				out.flush();
            	} catch (IOException err) {
                	System.out.println(err.getMessage());
                } finally {
                	try {
                		if(socket != null) socket.close(); // 클라이언트 소켓 닫기
                	} catch (IOException err) {
                		System.out.println("서버와 채팅 중 오류가 발생했습니다.");
                	}
                }    
            	
            default:
                break;
        }
    }

    
    public static void main(String[] args) {
    	
    	new DifficultySelector();
    	
    }
}