package com.example.basictest.Class;
import java.io.Serializable;
public class User implements Serializable{
    private String uuid;
    private String username;
    private String password;
    private String code;

    public User(String uuid, String username, String password, String code) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.code = code;
    }

    public User(String uuid, String username, String code) {
        this.uuid = uuid;
        this.username = username;
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
