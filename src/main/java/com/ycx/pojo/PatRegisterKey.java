package com.ycx.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PatRegisterKey implements Serializable {
    /**
     * 注册号 6+3+6 <br>
     * 数据库字段是：PAT_REGISTER.REG_ID  <br>
     */
    @JsonProperty("REG_ID")
    private Long regId;

    /**
     * 病人来源(1=门诊/2=住院/3=体检/4=健康档案/5=放射/6=LIS/7=急诊/8=门诊留观/9=预检分诊/10=入院申请/99=其它) <br>
     * 数据库字段是：PAT_REGISTER.SOURCE_TYPE  <br>
     */
    @JsonProperty("SOURCE_TYPE")
    private Short sourceType;

    /**
     * 获取注册号 6+3+6
     * @return PAT_REGISTER.REG_ID
     */
    public Long getRegId() {
        return regId;
    }

    /**
     * 设置注册号 6+3+6
     * @param regId 注册号 6+3+6
     */
    public void setRegId(Long regId) {
        this.regId = regId;
    }

    /**
     * 获取病人来源(1=门诊/2=住院/3=体检/4=健康档案/5=放射/6=LIS/7=急诊/8=门诊留观/9=预检分诊/10=入院申请/99=其它)
     * @return PAT_REGISTER.SOURCE_TYPE
     */
    public Short getSourceType() {
        return sourceType;
    }

    /**
     * 设置病人来源(1=门诊/2=住院/3=体检/4=健康档案/5=放射/6=LIS/7=急诊/8=门诊留观/9=预检分诊/10=入院申请/99=其它)
     * @param sourceType 病人来源(1=门诊/2=住院/3=体检/4=健康档案/5=放射/6=LIS/7=急诊/8=门诊留观/9=预检分诊/10=入院申请/99=其它)
     */
    public void setSourceType(Short sourceType) {
        this.sourceType = sourceType;
    }
}