package com.thinkdifferent.convertvideo.utils;

public class PrintStream extends Thread {
    java.io.InputStream __is = null;

    public PrintStream(java.io.InputStream is) {
        __is = is;
    }

    public void run() {
        try {
            while (this != null) {
                int _ch = __is.read();
                if (_ch != -1)
                    System.out.print((char) _ch);
                else break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
