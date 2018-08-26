package com.nowcoder.model;

/**
 * @Author: LiuHao
 * @Descirption:
 * @Date: 2018/8/26_18:46
 */
public class User {

    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return "This is " + name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
