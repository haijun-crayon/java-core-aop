package com.ccy.IO.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;


public class MultTimeServer implements Runnable {
    private Selector selector;
    private ServerSocketChannel channel;
    private volatile boolean stop;


    public MultTimeServer(int port) {
        try {
            selector = Selector.open();
            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(port), 1024);
            channel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("TIME SERVER IS LISTENING!!!");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public void stop() {
        this.stop = true;
    }

    public void run() {
        while (!stop) {
            try {
                //selector每一秒被唤醒一次,等待一秒
                System.out.println("server等待读");

                int i = selector.select(1000);
                System.out.println("server select 成功");

                System.out.println("select result-" + i);
                //还回就绪状态的chanel的selectedKeys
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null)
                                key.channel().close();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                //通过ServerSocketChannel的accept()操作接收客户端的请求并创立SocketChannel连接，相当于完成TCP三次握手操作
                ServerSocketChannel schannel = (ServerSocketChannel) key.channel();
                SocketChannel accept = schannel.accept();
                accept.configureBlocking(false);
                accept.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                //开辟缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                //非阻塞读
                int size = sc.read(buffer);
                //根据还回结果做判断
                if (size > 0) {
                    //设置当前读取位置
                    buffer.flip();
                    byte[] arr = new byte[buffer.remaining()];
                    buffer.get(arr);
                    String body = new String(arr, "UTF-8");
                    System.out.println(body);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    doWriter(sc, format.format(new java.util.Date()));
                } else if (size < 0) {
                    key.cancel();
                    sc.close();
                }
            }

        }
    }


    private void doWriter(SocketChannel sc, String res) throws IOException {
        if (res != null && res.trim().length() > 0) {
            byte[] bytes = res.getBytes();
            ByteBuffer buffe = ByteBuffer.allocate(bytes.length);
            buffe.put(bytes);
            buffe.flip();
            sc.write(buffe);
        }
    }
}
