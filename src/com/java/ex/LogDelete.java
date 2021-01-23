package com.java.ex;

import java.awt.Container;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class LogDelete extends JFrame {
	Timer timer = new Timer();
	TimerTask timerTask;
	
	JLabel timerLabel;
	JTextField timerText;
	JButton timerStart;
	JButton timerStop;
	static JTextArea logArea;
	JScrollPane scroll;
	
	public LogDelete() {
		Container ct = getContentPane();
		Panel logPanel = new Panel();
		logPanel.setLayout(null);

		// 컴포넌트
		// 라벨
		timerLabel = new JLabel("타이머 인터벌 : ");
		timerLabel.setBounds(20, 10, 100, 20);

		// 텍스트필드
		timerText = new JTextField(20);
		timerText.setBounds(110, 10, 150, 20);

		// 버튼
		timerStart = new JButton("타이머 시작");
		timerStart.setBounds(20, 40, 100, 30);

		timerStop = new JButton("타이머 정지");
		timerStop.setBounds(160, 40, 100, 30);

		// 로그
		logArea = new JTextArea();
		logArea.setCaretPosition(logArea.getDocument().getLength());
		scroll = new JScrollPane(logArea);
		scroll.setBounds(20, 80, 240, 270);

		// 컴포넌트 이벤트
		timerStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					long interval = Long.parseLong(timerText.getText());

					timerTask = new TimerTask() {
						@Override
						public void run() {
							String demos = System.getenv("LocalAppdata");
							deleteFile(demos + "\\TslGame\\Saved\\Demos");
							
							String logs = System.getenv("LocalAppdata");
							deleteFile(logs + "\\TslGame\\Saved\\Logs");
							
							String crashes = System.getenv("LocalAppdata");
							deleteFile(crashes + "\\TslGame\\Saved\\Crashes");
						}
					};
					timer.scheduleAtFixedRate(timerTask, 1000, interval);
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "인터벌을 입력해주세요.", "Interval Input", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		timerStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timerTask.cancel();
			}
		});

		// 컴포넌트 패널 추가
		logPanel.add(timerLabel);
		logPanel.add(timerText);
		logPanel.add(timerStart);
		logPanel.add(timerStop);
		logPanel.add(scroll);

		// 패널 컨테이너 추가
		ct.add(logPanel);

		// 컨테이너 셋팅 정보
		setSize(300, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	//파일 삭제하는 메소드
	//사용중인 파일은 삭제되지 않음(하위파일을 삭제하고 현재 폴더를 삭제)
	public static void deleteFile(String path) {
		try {
			File file = new File(path);
			File[] files = file.listFiles();

			if (files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile()) {
						files[i].delete();
					}
					else {
						deleteFile(files[i].getPath());
					}
					files[i].delete();
					LogDelete.logArea.append(files[i].getPath() + "\n");
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
}