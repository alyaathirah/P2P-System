package de.tum.in.www1.simplechat;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.tum.in.www1.jReto.LocalPeer;
import de.tum.in.www1.jReto.module.wlan.WlanModule;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Label;

public class SimpleChatUI extends javax.swing.JFrame{
	private LocalChatPeer chatPeer;
	List chatPeerList;
	StyledText chatText;
	ProgressBar progressBar;
	private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private java.awt.List list1;
	
	protected Shell shlSimpleChatExample;
	private Text txtDisplayName;
	private ChatRoom selectedPeer;
	private ArrayList<ChatRoom> chatPeers = new ArrayList<>();
	Display display;
	private Executor swtExecutor = new Executor() {
		@Override
		public synchronized void execute(Runnable command) {
			Display.getDefault().syncExec(command);
		}
	};
	private Text txtMessage;
	/**
	 * Launch the application.
	 * @param args
	 */
	
	public SimpleChatUI() {
		initComponents();
		chatPeer = new LocalChatPeer(SimpleChatUI.this, swtExecutor);
	}
	public static void main(String[] args) {

		try {
			SimpleChatUI window = new SimpleChatUI();
			window.open();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new SimpleChatUI().setVisible(true);
//            }
//        });
	}
	
	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		chatPeer = new LocalChatPeer(SimpleChatUI.this, swtExecutor);

		shlSimpleChatExample.open();
		shlSimpleChatExample.layout();
		while (!shlSimpleChatExample.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	
	public void startLocalPeer() {
		chatPeer.start(jTextField1.getText());
		chatPeer.start(txtDisplayName.getText());
		
	}

	public void addChatPeer(ChatRoom chatPeer) {
		this.chatPeers.add(chatPeer);
		this.chatPeerList.add(chatPeer.getDisplayName());
		this.list1.add(chatPeer.getDisplayName());
	}
	public void removeChatPeer(ChatRoom chatPeer) {
		this.chatPeers.remove(chatPeer);
		this.chatPeerList.removeAll();
		this.list1.removeAll();
		
		for (ChatRoom peer : this.chatPeers) {
			this.chatPeerList.add(peer.getDisplayName());
			this.list1.add(peer.getDisplayName());
		}
	}
	
	
	public void selectChatPeer(int index) {
		if (index == -1 || index >= this.chatPeers.size()) {
			this.selectedPeer = null;
		} else {
			this.selectedPeer = this.chatPeers.get(index);
			this.updateChatData();
		}
	}
	
	public void updateChatData() {
		if (this.selectedPeer == null) return;
		this.chatText.setText(this.selectedPeer.getChatText());
		this.jTextArea1.setText(this.selectedPeer.getChatText());
		//this.progressBar.setSelection(this.selectedPeer.getFileProgress());
	}
	
	public FileChannel getSaveFileChannel(String fileName) {		
		FileDialog fd = new FileDialog(shlSimpleChatExample, SWT.SAVE);
        fd.setText("Choose a file for the incoming file transfer");
        fd.setFileName(fileName);
        String selected = fd.open();
        Path path = FileSystems.getDefault().getPath(selected);
        OpenOption[] read = { StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW };
        try {
        	System.out.println("File will be saved to: "+path);
			FileChannel fileChannel = FileChannel.open(path, read);

			return fileChannel;
        } catch (IOException e) {
			e.printStackTrace();
		}
        
        return null;
	}
//	
//	public void sendFile() {
//		if (this.selectedPeer == null) return;
//		
//		FileDialog fd = new FileDialog(shlSimpleChatExample, SWT.OPEN);
//        fd.setText("Choose a file to send");
//        String selected = fd.open();
//
//        if (selected == null) return;
//        
//        Path path = FileSystems.getDefault().getPath(selected);
//        OpenOption[] read = { StandardOpenOption.READ };
//        try {
//			FileChannel fileChannel = FileChannel.open(path, read);
//					
//			this.selectedPeer.sendFile(fileChannel, path.getFileName().toString(), (int)fileChannel.size());
//        } catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlSimpleChatExample = new Shell();
		shlSimpleChatExample.setSize(640, 540);
		shlSimpleChatExample.setText("Simple Chat Example");
		shlSimpleChatExample.setLayout(null);
		shlSimpleChatExample.setBackground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
		shlSimpleChatExample.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		this.chatPeerList = new List(shlSimpleChatExample, SWT.BORDER);
		chatPeerList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectChatPeer(chatPeerList.getSelectionIndex());
				
			}
		});
		chatPeerList.setBounds(10, 66, 243, 420);
		
		txtDisplayName = new Text(shlSimpleChatExample, SWT.BORDER);
		txtDisplayName.setText("Enter a Display Name");
		txtDisplayName.setBounds(10, 10, 243, 30);
		
		Button btnNewButton = new Button(shlSimpleChatExample, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startLocalPeer();
				txtDisplayName.setText("");
			}
		});
		btnNewButton.setBounds(259, 10, 243, 30);
		btnNewButton.setText("Start Advertising and Browsing");
		
		this.chatText = new StyledText(shlSimpleChatExample, SWT.BORDER);
		chatText.setDoubleClickEnabled(false);
		chatText.setEnabled(false);
		chatText.setEditable(false);
		chatText.setBounds(259, 66, 348, 380);
		
		txtMessage = new Text(shlSimpleChatExample, SWT.BORDER);
		txtMessage.setText("Type a message");
		txtMessage.setBounds(259, 456, 270, 30);
		
		Button btnSend = new Button(shlSimpleChatExample, SWT.NONE);
		btnSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selectedPeer != null) {
					selectedPeer.sendMessage(txtMessage.getText());
					txtMessage.setText("");
				}
			}
		});
		btnSend.setBounds(534, 456, 73, 30);
		btnSend.setText("Send");
//		
//		Button btnSendFile = new Button(shlSimpleChatExample, SWT.NONE);
//		btnSendFile.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				sendFile();
//			}
//		});
//		btnSendFile.setBounds(259, 433, 106, 32);
//		btnSendFile.setText("Send a File");
		
//		this.progressBar = new ProgressBar(shlSimpleChatExample, SWT.NONE);
//		progressBar.setBounds(371, 453, 236, 14);
		
		Label lblPeers = new Label(shlSimpleChatExample, SWT.NONE);
		lblPeers.setBounds(10, 46, 243, 20);
		lblPeers.setText("Discovered Peers");
		lblPeers.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		
		Label lblChat = new Label(shlSimpleChatExample, SWT.NONE);
		lblChat.setBounds(259, 46, 348, 20);
		lblChat.setText("Chat with Selected Peer");
		lblChat.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		
//		Label lblFileProgress = new Label(shlSimpleChatExample, SWT.NONE);
//		lblFileProgress.setBounds(368, 433, 148, 20);
//		lblFileProgress.setText("File Transfer Progress");

	}
	
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        startLocalPeer();
    }
	
	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
		if (selectedPeer != null) {
			selectedPeer.sendMessage(txtMessage.getText());
		}
    }
	
	private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        list1 = new java.awt.List();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jTextArea1.setBackground(new java.awt.Color(255, 255, 255));
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("");

        jButton1.setBackground(new java.awt.Color(51, 153, 0));
        jButton1.setText("Advertise");

        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setText("");

        jButton2.setBackground(new java.awt.Color(0, 153, 0));
        jButton2.setText("Send");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(list1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(list1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }
}
