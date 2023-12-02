package tune_test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class SoundPlayerGUI extends JFrame {
    private String[] notes = {"도", "레", "미", "파", "솔", "라", "시"};
    private String correctNote;
    ButtonGroup group = new ButtonGroup(); //라디오 버튼 저장 그룹

    public SoundPlayerGUI() {
        setTitle("청음 훈련");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new GridBagLayout());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // 음정 버튼 간격 조정

        JButton playButton = new JButton("재생");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 도레미파솔라시 중에서 네 개의 랜덤한 음정 선택
                ArrayList<String> notesList = new ArrayList<>(Arrays.asList(notes));
                Collections.shuffle(notesList);
                ArrayList<String> selectedNotes = new ArrayList<>(notesList.subList(0, 4));
                
                correctNote = selectedNotes.get(0); // 정답 노트 설정
                Collections.shuffle(selectedNotes); // 섞음
                

                // 라디오 버튼 생성
                for (String note : selectedNotes) {
                    JRadioButton radioButton = new JRadioButton(note);

                    group.add(radioButton);
                    panel.add(radioButton);
                }
                //변경된 점 적용
                panel.revalidate();
                panel.repaint();
                
                //정답의 파일 재생
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        SoundPlayer.playSound("/sounds/" + correctNote +".wav");
                        return null;
                    }
                };
                worker.execute();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(50, 50, 50, 50); // 간격 조정

        // 재생 버튼 추가
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        c.add(playButton, gbc);

        
        gbc.gridy = 1;
        c.add(panel, gbc);

        JButton submitButton = new JButton("제출");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 정답 체크 및 점수 증가
                checkAnswer(group);
                
                //제출 완료시 라디오버튼 제거
                Enumeration<AbstractButton> buttons = group.getElements();
                while (buttons.hasMoreElements()) {
                    AbstractButton button = buttons.nextElement();
                    if (button instanceof JRadioButton) {
                        JRadioButton radioButton = (JRadioButton) button;
                        panel.remove(radioButton); // 패널에서 라디오 버튼 제거
                    }
                }
                group.clearSelection();
                
                panel.revalidate(); // 패널을 다시 그립니다.
                panel.repaint();
            }
        });

        gbc.gridy = 2;
        c.add(submitButton, gbc);
        
        setSize(500, 400);
        setLocationRelativeTo(null);
    }
    private void checkAnswer(ButtonGroup group) {
        // 선택된 라디오 버튼 가져오기
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                // 선택된 버튼이 정답인지 확인
                if (button.getText().equals(correctNote)) {
                    JOptionPane.showMessageDialog(null, "정답입니다!");
                    // 정답인 경우 점수 증가
                    // score++;
                } else {
                    JOptionPane.showMessageDialog(null, "틀렸습니다.");
                }
                
                return; // 정답을 찾았으니 반복문 종료
            }
        }
        JOptionPane.showMessageDialog(null, "선택된 답이 없습니다.");
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SoundPlayerGUI soundPlayerGUI = new SoundPlayerGUI();
                soundPlayerGUI.setVisible(true);
            }
        });
    }
}
