package tune_test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.*;


public class SoundPlayerGUI extends JFrame {
    private String[] notes = {"도", "레", "미", "파", "솔", "라", "시"};
    private String correctNote;
    private String[] correctAnswers = new String[10]; // 배열로 10개의 문제의 정답 보관
    private int currentQuestion = 0; // 현재 문제 번호
    private int score = 0; // 점수
    ButtonGroup group = new ButtonGroup(); //라디오 버튼 저장 그룹
    int g_empty = 1;	//group 비었는지 확인 위함

    BufferedWriter out = null;
    BufferedReader in = null;
    Socket socket = null;
    
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
               	if(g_empty == 1) {	//제출 전이라면 같은 음 반복 출력 위함	난이도 높으려면 음 출력까지 묶어서 반복 재생 금지 가능
               		g_empty = 0;
                		
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
               	}
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
                checkAnswer(group);
                Enumeration<AbstractButton> buttons = group.getElements();
                while (buttons.hasMoreElements()) {
                    AbstractButton button = buttons.nextElement();
                    if (button instanceof JRadioButton) {
                        JRadioButton radioButton = (JRadioButton) button;
                        panel.remove(radioButton);
                    }
                }
                group.clearSelection();

                g_empty = 1;
                panel.revalidate();
                panel.repaint();

                currentQuestion++; // 다음 문제로 이동
                if (currentQuestion >= 2) {
                    // 모든 문제를 푼 후 점수 표시
                    JOptionPane.showMessageDialog(null, "점수: " + score + "/10");
                        
                    try {
                    	socket = new Socket("localhost", 9999);
                       	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                       	out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        
                        out.write("begin\n");	//서버에 점수 전송
    					out.flush();
    					out.write(score + "\n");
    					out.flush();
    				        					

    					String mes = null;	
    					while(mes == null) {
    						mes = in.readLine(); // 서버로부터 결과 수신
    					}	
    					if(mes.equals("first")) {
    						JOptionPane.showMessageDialog(null, "감격스러운 첫 기록!");
    					}
    					else if(mes.equals("high")) {
    						JOptionPane.showMessageDialog(null, "최고 기록 달성!!!");
    					}
                    } catch (IOException err) {
                    	System.out.println(err.getMessage());
                    } finally {
                    	try {
                    		out.write("fin\n");
        					out.flush();
                    		if(socket != null) socket.close(); // 클라이언트 소켓 닫기
                    		setVisible(false); // 현재 창을 숨김
                            dispose(); // 현재 창을 메모리에서 제거
                    	} catch (IOException err) {
                    		System.out.println("서버와 채팅 중 오류가 발생했습니다.");
                    	}
                    }    
    				
                    currentQuestion = 0; // 문제 초기화
                    score = 0; // 점수 초기화
                    setVisible(false); // 현재 창을 숨김
                    dispose(); // 현재 창을 메모리에서 제거
                    new DifficultySelector(); // 이렇게 하면 DifficultySelector를 다시 실행함
                }
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
                    score++;
                } else {
                    JOptionPane.showMessageDialog(null, "틀렸습니다.");
                }
                
                return; // 정답을 찾았으니 반복문 종료
            }
        }
        JOptionPane.showMessageDialog(null, "선택된 답이 없습니다.");
    }
    


}