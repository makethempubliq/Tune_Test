package tune_test;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class DifficultySelector {
    public DifficultySelector() {
        Object[] options = {"초급", "중급", "고급"};
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
                // 중급 난이도에 해당하는 코드 추가
                break;
            case 2:
                // 고급 난이도의 실행 코드
                // 고급 난이도에 해당하는 코드 추가
                break;
            default:
                break;
        }
    }

    
    public static void main(String[] args) {
    	
    	new DifficultySelector();
    	
    }
}