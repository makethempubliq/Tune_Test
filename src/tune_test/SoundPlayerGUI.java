package tune_test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SoundPlayerGUI extends JFrame {
    public SoundPlayerGUI() {
        setTitle("청음 훈련");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton playButton = new JButton("재생");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("C:\\Users\\Shin\\eclipse-workspace\\sound_test\\resources\\sounds\\01 BASS.wav"); // 여기에 사운드 파일 경로 입력
            }
        });

        // 음정 선택을 위한 라디오 버튼 추가
        JRadioButton option1 = new JRadioButton("음정 1");
        JRadioButton option2 = new JRadioButton("음정 2");
        JRadioButton option3 = new JRadioButton("음정 3");
        JRadioButton option4 = new JRadioButton("음정 4");
        
        // ... 필요한 만큼 라디오 버튼 추가

        ButtonGroup group = new ButtonGroup();
        group.add(option1);
        group.add(option2);
        group.add(option3);
        group.add(option4);
        // ... 다른 라디오 버튼 추가

        add(playButton);
        add(option1);
        add(option2);
        add(option3);
        add(option4);
        // ... 다른 라디오 버튼 추가
        setSize(500, 900);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SoundPlayerGUI().setVisible(true);
            }
        });
    }
}