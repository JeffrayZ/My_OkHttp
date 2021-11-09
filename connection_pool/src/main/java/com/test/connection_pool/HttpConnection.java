package com.test.connection_pool;

import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * 连接中持有Socket对象，此HttpConnection是对Socket的封装，OkHttp的底层直接使用Socket，没有使用http协议，灵活性更大，
 * 也可以复用Socket连接。
 */
public class HttpConnection {
    private Socket socket;
    private long hasUseTime;

    public Socket getSocket() {
        return socket;
    }

    public void setHasUseTime(long hasUseTime) {
        this.hasUseTime = hasUseTime;
    }

    public long getHasUseTime() {
        return hasUseTime;
    }

    HttpConnection(final String host, final int port) {
        try {
            socket = new Socket(host, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    HttpConnection(final String host, final int port, final String type) {
        try {
            if ("GET".equalsIgnoreCase(type)) {
                // http
                socket = new Socket(host, port);
            } else {
                // https
                socket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(host, port);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnectionAction(String host, int port) {
        if (socket == null) {
            return false;
        }
        if (socket.getPort() == port && socket.getInetAddress().getHostName().equals(host)) {
            return true;
        }
        return false;
    }

    public void closeSocket() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
