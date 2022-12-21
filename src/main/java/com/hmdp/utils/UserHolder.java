package com.hmdp.utils;

import com.hmdp.dto.UserDTO;

public class UserHolder {
    // 使用ThreadLocal做到线程隔离，每个线程操作自己的一份数据
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
