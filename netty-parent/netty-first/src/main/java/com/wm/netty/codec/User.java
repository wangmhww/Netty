package com.wm.netty.codec;

import java.io.Serializable;

/**
 * @author wangm
 * @title: User
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/240:14
 */
public class User implements Serializable {

    private int id;

    private String name;

    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
