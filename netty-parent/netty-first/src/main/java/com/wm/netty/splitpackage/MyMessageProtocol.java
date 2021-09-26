package com.wm.netty.splitpackage;

/**
 * @author wangm
 * @title: MyMessageProtocol
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2722:32
 */
public class MyMessageProtocol {

    private int len;

    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
