package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class server {

	public static void main(String [] args) {
		BufferedReader in = null;
		BufferedWriter out = null;
		ServerSocket listener = null;
		Socket socket = null;
		
		ArrayList<Integer> beginner = new ArrayList<Integer>();
		ArrayList<Integer> intermed = new ArrayList<Integer>();
		ArrayList<Integer> advanced = new ArrayList<Integer>();
		String inputMessage = null;
		String temp = null;
		
		try {
			listener = new ServerSocket(9999);  // 서버 소켓 생성
			System.out.println("연결을 기다리고 있습니다.....");
			socket = listener.accept(); // 클라이언트로부터 연결 요청 대기
			socket.setSoTimeout(60 * 1000);
			System.out.println("연결되었습니다.");
			
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while (true) {
				inputMessage = null;
				temp = null;
				
				while(inputMessage == null) {
					inputMessage = in.readLine();
				}
				
				if (inputMessage.equalsIgnoreCase("end")) {
					System.out.println("클라이언트에서 연결을 종료하였음");
					break; // "end"를 받으면 연결 종료
				}
			
				while(temp == null) {
					temp = in.readLine();
				}
				int score = Integer.parseInt(temp);
				
				
				if(inputMessage.equalsIgnoreCase("begin")) {
					if(beginner.isEmpty()) {
						System.out.println("초보");
						out.write("first\n"); // 첫 기록임을 알리는 문자열 전송
					}
					else if(beginner.get(0) < score){
						out.write("high\n");// 최고 기록임을 알리는 문자열 전송
					}
					else {			
						out.write("not\n");	//아무것도 아님 알리는 문자열 전송
					}
					
					beginner.add(score);
					Collections.sort(beginner);
					Collections.reverse(beginner);	//배열에 점수 추가 후 내림차순으로 정렬
					
				}
				else if(inputMessage.equalsIgnoreCase("Interm")) {
					if(intermed.isEmpty()) {
						out.write("first\n"); // 첫 기록임을 알리는 문자열 전송
					}
					else if(intermed.get(0) < score){
						out.write("high\n");// 최고 기록임을 알리는 문자열 전송
					}
					else {			
						out.write("not\n");	//아무것도 아님 알리는 문자열 전송
					}
					
					intermed.add(score);
					Collections.sort(intermed);
					Collections.reverse(intermed);	//배열에 점수 추가 후 내림차순으로 정렬
				}
				else if(inputMessage.equalsIgnoreCase("Adv")) {
					if(advanced.isEmpty()) {
						out.write("first\n"); // 첫 기록임을 알리는 문자열 전송
					}
					else if(advanced.get(0) < score){
						out.write("high\n");// 최고 기록임을 알리는 문자열 전송
					}
					else {			
						out.write("not\n");	//아무것도 아님 알리는 문자열 전송
					}
					
					advanced.add(score);
					Collections.sort(advanced);
					Collections.reverse(advanced);	//배열에 점수 추가 후 내림차순으로 정렬
				}
				out.flush();
						
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(socket != null) socket.close(); // 통신용 소켓 닫기
				if(listener != null) listener.close(); // 서버 소켓 닫기
			} catch (IOException e) {
				System.out.println("클라이언트와 연결 중 오류가 발생했습니다.");
			}
		}
	}
}
