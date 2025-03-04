package com.ycx.net.test;

import com.ycx.pojo.PatRegister;

import java.util.List;

public interface RpcTestService {
    List<PatRegister> getPatRegisterList(Object attachment);
}
