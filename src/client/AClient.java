package client;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Agony on 2018/5/23.
 */
public class AClient {

    private AsynchronousSocketChannel aSocketChannel;

    private Charset charset = Charset.forName(Constants.UTF_8);

    private JFrame frame = new JFrame("chat room.");

    private JTextArea textArea = new JTextArea(16, 48);

    private JTextField textField = new JTextField(40);

    private JButton button = new JButton("send");

    public void init() {
        frame.setLayout(new BorderLayout());
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.add(textField);
        panel.add(button);
        Action sendAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = textField.getText();
                try {
                    aSocketChannel.write(ByteBuffer.wrap(content.getBytes(Constants.UTF_8))).get();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                textField.setText("");
            }
        };
        button.addActionListener(sendAction);
        textField.getInputMap().put(KeyStroke.getKeyStroke('\n', InputEvent.CTRL_MASK), "send");
        textField.getActionMap().put("send", sendAction);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    public void connect() throws Exception {
        init();
        final ByteBuffer byteBuffer = ByteBuffer.allocate(Constants.BUFF_SIZE);
        ExecutorService executorService = Executors.newFixedThreadPool(Constants.THREAD_SIZE);
        AsynchronousChannelGroup aChannelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
        aSocketChannel = AsynchronousSocketChannel.open(aChannelGroup);
        aSocketChannel.connect(new InetSocketAddress(Constants.REMOTE_IP, Constants.PORT)).get();
        textArea.append("you connect chat room success.\n");
        byteBuffer.clear();
        aSocketChannel.read(byteBuffer, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                byteBuffer.flip();
                String content = charset.decode(byteBuffer).toString();
                textArea.append("somebody say: " + content + '\n');
                byteBuffer.clear();
                aSocketChannel.read(byteBuffer, null, this);
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new AClient().connect();
    }
}
