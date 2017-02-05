package com.zhang.keybox;

import java.util.UUID;

/**
 * Created by 张 on 2017/1/27.
 */

public class KeyBox {

    private UUID id ;

    private String name;

    private String count;

    private String password;

    private String remark;

    public KeyBox(){
        this(UUID.randomUUID());
    }

    public KeyBox(UUID id){
        this.id = id;
    }

    public UUID getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
