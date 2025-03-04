package com.ycx.net.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycx.pojo.PatRegister;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RpcTestImpl implements RpcTestService {

    static final List<PatRegister> BigContent;

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String BigContentStr = new String(Files.readAllBytes(Paths.get("/Users/zlatan/code/IdeaProjects/solon-study/src/main/resources/test/BigContent.json")));
            BigContent = objectMapper.readValue(BigContentStr, new TypeReference<List<PatRegister>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PatRegister> getPatRegisterList(Object attachment) {
        return BigContent;
    }
}
