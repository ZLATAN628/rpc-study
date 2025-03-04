package com.ycx.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Timestamp;
import com.ycx.net.rpc.serializer.HessianSerializer;
import com.ycx.net.rpc.serializer.JdkSerializer;
import com.ycx.net.rpc.serializer.ProtobufSerializer;
import com.ycx.net.rpc.serializer.Serializer;
import com.ycx.pojo.PatRegister;
import com.ycx.test.pojo.PatRegisterOuterClass;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public class SerializerTest {

    static List<PatRegister> BigContent;

    static PatRegisterOuterClass.PatRegisterList BigContent2;


    public void handle(Serializer serializer, int a) {
        long start = System.currentTimeMillis();
        int length = 0;
        for (int i = 0; i < 100000; i++) {
            byte[] serialize;
            if (a == 1) {
                serialize = serializer.serialize(BigContent);
                serializer.deserialize(serialize, List.class);
            } else if (a == 2) {
                serialize = serializer.serialize(BigContent2);
                serializer.deserialize(serialize, PatRegisterOuterClass.PatRegisterList.class);
            } else {
                throw new RuntimeException("error");
            }
            if (i == 0) {
                length = serialize.length;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("cost time: " + (end - start) + " ms, length: " + length);
    }

    @Test
    public void test() {
        HessianSerializer hessianSerializer = new HessianSerializer();
        ProtobufSerializer protobufSerializer = new ProtobufSerializer();
        JdkSerializer jdkSerializer = new JdkSerializer();
        System.out.println("--------------  test jdk serializer ----------------------");
        handle(jdkSerializer, 1);
        System.out.println("--------------  test hessian serializer ----------------------");
        handle(hessianSerializer, 1);
        System.out.println("--------------  test protobuf serializer ----------------------");
        handle(protobufSerializer, 2);
    }


    static {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String BigContentStr = new String(Files.readAllBytes(Paths.get("/Users/zlatan/code/IdeaProjects/solon-study/src/main/resources/test/BigContent.json")));
            BigContent = objectMapper.readValue(BigContentStr, new TypeReference<List<PatRegister>>() {
            });

            PatRegisterOuterClass.PatRegisterList.Builder ppListBuild = PatRegisterOuterClass.PatRegisterList.newBuilder();

            BigContent.forEach(p -> {
                PatRegisterOuterClass.PatRegisterList.PatRegister.Builder ppBuild = PatRegisterOuterClass.PatRegisterList.PatRegister.newBuilder();
                ppBuild.setRegId(p.getRegId());
                ppBuild.setSourceType(p.getSourceType());
                {
                    if (p.getPatId() != null) {
                        ppBuild.setPatId(p.getPatId());
                    }

                    if (p.getBranchCode() != null) {
                        ppBuild.setBranchCode(p.getBranchCode());
                    }

                    if (p.getCardType() != null) {
                        ppBuild.setCardType(p.getCardType());
                    }

                    if (p.getCardData() != null) {
                        ppBuild.setCardData(p.getCardData());
                    }

                    if (p.getName() != null) {
                        ppBuild.setName(p.getName());
                    }

                    if (p.getSex() != null) {
                        ppBuild.setSex(p.getSex());
                    }

                    if (p.getDateOfBirth() != null) {
                        ppBuild.setDateOfBirth(convertDateToTimestamp(p.getDateOfBirth()));
                    }

                    if (p.getTimeOfBirth() != null) {
                        ppBuild.setTimeOfBirth(p.getTimeOfBirth());
                    }

                    if (p.getNationality() != null) {
                        ppBuild.setNationality(p.getNationality());
                    }

                    if (p.getProvince() != null) {
                        ppBuild.setProvince(p.getProvince());
                    }

                    if (p.getCity() != null) {
                        ppBuild.setCity(p.getCity());
                    }

                    if (p.getCounty() != null) {
                        ppBuild.setCounty(p.getCounty());
                    }

                    if (p.getTown() != null) {
                        ppBuild.setTown(p.getTown());
                    }

                    if (p.getCommunity() != null) {
                        ppBuild.setCommunity(p.getCommunity());
                    }

                    if (p.getTownName() != null) {
                        ppBuild.setTownName(p.getTownName());
                    }

                    if (p.getCommunityName() != null) {
                        ppBuild.setCommunityName(p.getCommunityName());
                    }

                    if (p.getRoad() != null) {
                        ppBuild.setRoad(p.getRoad());
                    }

                    if (p.getLane() != null) {
                        ppBuild.setLane(p.getLane());
                    }

                    if (p.getNo() != null) {
                        ppBuild.setNo(p.getNo());
                    }

                    if (p.getRoom() != null) {
                        ppBuild.setRoom(p.getRoom());
                    }

                    if (p.getTelePhone() != null) {
                        ppBuild.setTelePhone(p.getTelePhone());
                    }

                    if (p.getIdCardType() != null) {
                        ppBuild.setIdCardType(p.getIdCardType());
                    }

                    if (p.getIdCard() != null) {
                        ppBuild.setIdCard(p.getIdCard());
                    }

                    if (p.getProfession() != null) {
                        ppBuild.setProfession(p.getProfession());
                    }

                    if (p.getMarried() != null) {
                        ppBuild.setMarried(p.getMarried());
                    }

                    if (p.getBloodAbo() != null) {
                        ppBuild.setBloodAbo(p.getBloodAbo());
                    }

                    if (p.getBloodRh() != null) {
                        ppBuild.setBloodRh(p.getBloodRh());
                    }

                    if (p.getEthnic() != null) {
                        ppBuild.setEthnic(p.getEthnic());
                    }

                    if (p.getEmail() != null) {
                        ppBuild.setEmail(p.getEmail());
                    }

                    if (p.getMobilePhone() != null) {
                        ppBuild.setMobilePhone(p.getMobilePhone());
                    }

                    if (p.getPost() != null) {
                        ppBuild.setPost(p.getPost());
                    }

                    if (p.getFamilyPhone() != null) {
                        ppBuild.setFamilyPhone(p.getFamilyPhone());
                    }

                    if (p.getAddr() != null) {
                        ppBuild.setAddr(p.getAddr());
                    }

                    if (p.getContactsType() != null) {
                        ppBuild.setContactsType(p.getContactsType());
                    }

                    if (p.getContactsName() != null) {
                        ppBuild.setContactsName(p.getContactsName());
                    }

                    if (p.getContactsPhone() != null) {
                        ppBuild.setContactsPhone(p.getContactsPhone());
                    }

                    if (p.getContactsAddr() != null) {
                        ppBuild.setContactsAddr(p.getContactsAddr());
                    }

                    if (p.getCultureLevel() != null) {
                        ppBuild.setCultureLevel(p.getCultureLevel());
                    }

                    if (p.getWorkUnit() != null) {
                        ppBuild.setWorkUnit(p.getWorkUnit());
                    }

                    if (p.getWorkAddr() != null) {
                        ppBuild.setWorkAddr(p.getWorkAddr());
                    }

                    if (p.getWorkPhone() != null) {
                        ppBuild.setWorkPhone(p.getWorkPhone());
                    }

                    if (p.getPrivacyLevel() != null) {
                        ppBuild.setPrivacyLevel(p.getPrivacyLevel());
                    }

                    if (p.getIsTemp() != null) {
                        ppBuild.setIsTemp(p.getIsTemp());
                    }

                    if (p.getIsInvalid() != null) {
                        ppBuild.setIsInvalid(p.getIsInvalid());
                    }

                    if (p.getDcd() != null) {
                        ppBuild.setDcd(p.getDcd());
                    }

                    if (p.getRegTime() != null) {
                        ppBuild.setRegTime(convertDateToTimestamp(p.getRegTime()));
                    }

                    if (p.getPriceType() != null) {
                        ppBuild.setPriceType(p.getPriceType());
                    }

                    if (p.getPatNo() != null) {
                        ppBuild.setPatNo(p.getPatNo());
                    }

                    if (p.getRegBranch() != null) {
                        ppBuild.setRegBranch(p.getRegBranch());
                    }

                    if (p.getRemark() != null) {
                        ppBuild.setRemark(p.getRemark());
                    }

                    if (p.getPatType() != null) {
                        ppBuild.setPatType(p.getPatType());
                    }

                    if (p.getEngName() != null) {
                        ppBuild.setEngName(p.getEngName());
                    }

                    if (p.getPassport() != null) {
                        ppBuild.setPassport(p.getPassport());
                    }

                    if (p.getPlatPatId() != null) {
                        ppBuild.setPlatPatId(p.getPlatPatId());
                    }

                    if (p.getDetailAddr() != null) {
                        ppBuild.setDetailAddr(p.getDetailAddr());
                    }

                    if (p.getDisabledNo() != null) {
                        ppBuild.setDisabledNo(p.getDisabledNo());
                    }
                }
                ppListBuild.addPp(ppBuild.build());
            });

            BigContent2 = ppListBuild.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static Timestamp convertDateToTimestamp(Date date) {
        long seconds = date.getTime() / 1000;  // 获取秒数
        int nanos = (int) ((date.getTime() % 1000) * 1000000);  // 获取纳秒数

        // 创建一个 Timestamp 对象
        return Timestamp.newBuilder()
                .setSeconds(seconds)
                .setNanos(nanos)
                .build();
    }

}
