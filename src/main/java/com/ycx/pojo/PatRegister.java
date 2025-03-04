package com.ycx.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class PatRegister extends PatRegisterKey implements Serializable {
    /**
     * 档案号 <br>
     * 数据库字段是：PAT_REGISTER.PAT_ID  <br>
     */
    @JsonProperty("PAT_ID")
    private Long patId;

    /**
     * 创建机构号 <br>
     * 数据库字段是：PAT_REGISTER.BRANCH_CODE  <br>
     */
    @JsonProperty("BRANCH_CODE")
    private String branchCode;

    /**
     * 卡类型 <br>
     * 数据库字段是：PAT_REGISTER.CARD_TYPE  <br>
     */
    @JsonProperty("CARD_TYPE")

    private Short cardType;

    /**
     * 就诊卡号 <br>
     * 数据库字段是：PAT_REGISTER.CARD_DATA  <br>
     */
    @JsonProperty("CARD_DATA")

    private String cardData;

    /**
     * 姓名 <br>
     * 数据库字段是：PAT_REGISTER.NAME  <br>
     */
    @JsonProperty("NAME")

    private String name;

    /**
     * 性别(1=男/2=女/3=不详/9=其它) <br>
     * 数据库字段是：PAT_REGISTER.SEX  <br>
     */
    @JsonProperty("SEX")


    private Short sex;

    /**
     * 出生日期 <br>
     * 数据库字段是：PAT_REGISTER.DATE_OF_BIRTH  <br>
     */
    @JsonProperty("DATE_OF_BIRTH")

    private Date dateOfBirth;

    /**
     * 出生时间<br>
     * 数据库字段是：PAT_REGISTER.TIME_OF_BIRTH  <br>
     */
    @JsonProperty("TIME_OF_BIRTH")

    private String timeOfBirth;

    /**
     * 国籍(HR02.04.001,值GB/T 2659-2000,默认值156,表示中国) <br>
     * 数据库字段是：PAT_REGISTER.NATIONALITY  <br>
     */
    @JsonProperty("NATIONALITY")

    private String nationality;

    /**
     * 省 <br>
     * 数据库字段是：PAT_REGISTER.PROVINCE  <br>
     */
    @JsonProperty("PROVINCE")

    private String province;

    /**
     * 市 <br>
     * 数据库字段是：PAT_REGISTER.CITY  <br>
     */
    @JsonProperty("CITY")

    private String city;

    /**
     * 区（县） <br>
     * 数据库字段是：PAT_REGISTER.COUNTY  <br>
     */
    @JsonProperty("COUNTY")

    private String county;

    /**
     * 街道（镇） <br>
     * 数据库字段是：PAT_REGISTER.TOWN  <br>
     */
    @JsonProperty("TOWN")

    private String town;

    /**
     * 村 <br>
     * 数据库字段是：PAT_REGISTER.COMMUNITY  <br>
     */
    @JsonProperty("COMMUNITY")

    private String community;

    /**
     * 街道名称 <br>
     * 数据库字段是：PAT_REGISTER.TOWN_NAME  <br>
     */
    @JsonProperty("TOWN_NAME")

    private String townName;

    /**
     * 村名称 <br>
     * 数据库字段是：PAT_REGISTER.COMMUNITY_NAME  <br>
     */
    @JsonProperty("COMMUNITY_NAME")

    private String communityName;

    /**
     * 路(街) <br>
     * 数据库字段是：PAT_REGISTER.ROAD  <br>
     */
    @JsonProperty("ROAD")

    private String road;

    /**
     * 弄 <br>
     * 数据库字段是：PAT_REGISTER.LANE  <br>
     */
    @JsonProperty("LANE")

    private String lane;

    /**
     * 号 <br>
     * 数据库字段是：PAT_REGISTER.NO  <br>
     */
    @JsonProperty("NO")

    private String no;

    /**
     * 室 <br>
     * 数据库字段是：PAT_REGISTER.ROOM  <br>
     */
    @JsonProperty("ROOM")

    private String room;

    /**
     * 电话号码 <br>
     * 数据库字段是：PAT_REGISTER.TELE_PHONE  <br>
     */
    @JsonProperty("TELE_PHONE")

    private String telePhone;

    /**
     * 1=身份证/2=护照/3=港澳通行证/4=台湾通行证/5=军官证 <br>
     * 数据库字段是：PAT_REGISTER.ID_CARD_TYPE  <br>
     */
    @JsonProperty("ID_CARD_TYPE")

    private Short idCardType;

    /**
     * 证件号码 <br>
     * 数据库字段是：PAT_REGISTER.ID_CARD  <br>
     */
    @JsonProperty("ID_CARD")

    private String idCard;

    /**
     * 职业ID,取通用字典 <br>
     * 数据库字段是：PAT_REGISTER.PROFESSION  <br>
     */
    @JsonProperty("PROFESSION")

    private String profession;

    /**
     * 婚姻状况,取通用字典 <br>
     * 数据库字段是：PAT_REGISTER.MARRIED  <br>
     */
    @JsonProperty("MARRIED")

    private String married;

    /**
     * ABO血型(A,B,AB,O),取通用字典 <br>
     * 数据库字段是：PAT_REGISTER.BLOOD_ABO  <br>
     */
    @JsonProperty("BLOOD_ABO")

    private String bloodAbo;

    /**
     * RH血型(+阳性,-阴性),取通用字典 <br>
     * 数据库字段是：PAT_REGISTER.BLOOD_RH  <br>
     */
    @JsonProperty("BLOOD_RH")

    private String bloodRh;

    /**
     * 民族，取通用字典 <br>
     * 数据库字段是：PAT_REGISTER.ETHNIC  <br>
     */
    @JsonProperty("ETHNIC")

    private String ethnic;

    /**
     * 电子邮箱 <br>
     * 数据库字段是：PAT_REGISTER.EMAIL  <br>
     */
    @JsonProperty("EMAIL")

    private String email;

    /**
     * 移动电话 <br>
     * 数据库字段是：PAT_REGISTER.MOBILE_PHONE  <br>
     */
    @JsonProperty("MOBILE_PHONE")

    private String mobilePhone;

    /**
     * 邮编 <br>
     * 数据库字段是：PAT_REGISTER.POST  <br>
     */
    @JsonProperty("POST")

    private String post;

    /**
     * 家庭电话 <br>
     * 数据库字段是：PAT_REGISTER.FAMILY_PHONE  <br>
     */
    @JsonProperty("FAMILY_PHONE")

    private String familyPhone;

    /**
     * 居住地址 <br>
     * 数据库字段是：PAT_REGISTER.ADDR  <br>
     */
    @JsonProperty("ADDR")

    private String addr;

    /**
     * 联系人关系,取通用字典 <br>
     * 数据库字段是：PAT_REGISTER.CONTACTS_TYPE  <br>
     */
    @JsonProperty("CONTACTS_TYPE")

    private String contactsType;

    /**
     * 联系人姓名 <br>
     * 数据库字段是：PAT_REGISTER.CONTACTS_NAME  <br>
     */
    @JsonProperty("CONTACTS_NAME")

    private String contactsName;

    /**
     * 联系人电话 <br>
     * 数据库字段是：PAT_REGISTER.CONTACTS_PHONE  <br>
     */
    @JsonProperty("CONTACTS_PHONE")

    private String contactsPhone;

    /**
     * 联系人地址 <br>
     * 数据库字段是：PAT_REGISTER.CONTACTS_ADDR  <br>
     */
    @JsonProperty("CONTACTS_ADDR")

    private String contactsAddr;

    /**
     * 文化程度，取通用字典 <br>
     * 数据库字段是：PAT_REGISTER.CULTURE_LEVEL  <br>
     */
    @JsonProperty("CULTURE_LEVEL")

    private String cultureLevel;

    /**
     * 工作单位 <br>
     * 数据库字段是：PAT_REGISTER.WORK_UNIT  <br>
     */
    @JsonProperty("WORK_UNIT")

    private String workUnit;

    /**
     * 工作地址 <br>
     * 数据库字段是：PAT_REGISTER.WORK_ADDR  <br>
     */
    @JsonProperty("WORK_ADDR")

    private String workAddr;

    /**
     * 工作联系电话 <br>
     * 数据库字段是：PAT_REGISTER.WORK_PHONE  <br>
     */
    @JsonProperty("WORK_PHONE")

    private String workPhone;

    /**
     * 稳私级别(0=普通/1=秘密/2=机密/3=绝密) <br>
     * 数据库字段是：PAT_REGISTER.PRIVACY_LEVEL  <br>
     */
    @JsonProperty("PRIVACY_LEVEL")

    private Short privacyLevel;

    /**
     * 是否临时人员(新生儿、无名氏等为临时人员) <br>
     * 数据库字段是：PAT_REGISTER.IS_TEMP  <br>
     */
    @JsonProperty("IS_TEMP")

    private Short isTemp;

    /**
     * 是否注销(0=否,1=是) <br>
     * 数据库字段是：PAT_REGISTER.IS_INVALID  <br>
     */
    @JsonProperty("IS_INVALID")

    private Short isInvalid;

    /**
     * 数据检验位 <br>
     * 数据库字段是：PAT_REGISTER.DCD  <br>
     */
    @JsonProperty("DCD")

    private String dcd;

    /**
     * 注册时间 <br>
     * 数据库字段是：PAT_REGISTER.REG_TIME  <br>
     */
    @JsonProperty("REG_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regTime;

    /**
     * 价格分类 <br>
     * 数据库字段是：PAT_REGISTER.PRICE_TYPE  <br>
     */
    @JsonProperty("PRICE_TYPE")

    private Short priceType;

    /**
     * 病历号  长期复用  （索引） <br>
     * 数据库字段是：PAT_REGISTER.PAT_NO  <br>
     */
    @JsonProperty("PAT_NO")

    private String patNo;

    /**
     * 登记机构 <br>
     * 数据库字段是：PAT_REGISTER.REG_BRANCH  <br>
     */
    @JsonProperty("REG_BRANCH")

    private String regBranch;

    /**
     * 备注 <br>
     * 数据库字段是：PAT_REGISTER.REMARK  <br>
     */
    @JsonProperty("REMARK")

    private String remark;

    /**
     * 0或空=个人,1=单位 数据库字段是：PAT_TYPE  <br>
     */
    @JsonProperty("PAT_TYPE")

    private Short patType;

    /**
     * 英文名 <br>
     * 数据库字段是：PAT_REGISTER.ENG_NAME  <br>
     */
    @JsonProperty("ENG_NAME")

    private String engName;

    /**
     * 护照号 <br>
     * 数据库字段是：PAT_REGISTER.PASSPORT  <br>
     */
    @JsonProperty("PASSPORT")

    private String passport;

    /**
     * 平台主索引号(系统显示为患者ID)
     */
    @JsonProperty("PLAT_PAT_ID")
    private String platPatId;

    @JsonProperty("DETAIL_ADDR")
    private String detailAddr;

    @JsonProperty("DISABLED_NO")
    private String disabledNo;

    public Long getPatId() {
        return patId;
    }

    public void setPatId(Long patId) {
        this.patId = patId;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public Short getCardType() {
        return cardType;
    }

    public void setCardType(Short cardType) {
        this.cardType = cardType;
    }

    public String getCardData() {
        return cardData;
    }

    public void setCardData(String cardData) {
        this.cardData = cardData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getTimeOfBirth() {
        return timeOfBirth;
    }

    public void setTimeOfBirth(String timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public Short getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(Short idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = married;
    }

    public String getBloodAbo() {
        return bloodAbo;
    }

    public void setBloodAbo(String bloodAbo) {
        this.bloodAbo = bloodAbo;
    }

    public String getBloodRh() {
        return bloodRh;
    }

    public void setBloodRh(String bloodRh) {
        this.bloodRh = bloodRh;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getFamilyPhone() {
        return familyPhone;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getContactsType() {
        return contactsType;
    }

    public void setContactsType(String contactsType) {
        this.contactsType = contactsType;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsAddr() {
        return contactsAddr;
    }

    public void setContactsAddr(String contactsAddr) {
        this.contactsAddr = contactsAddr;
    }

    public String getCultureLevel() {
        return cultureLevel;
    }

    public void setCultureLevel(String cultureLevel) {
        this.cultureLevel = cultureLevel;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getWorkAddr() {
        return workAddr;
    }

    public void setWorkAddr(String workAddr) {
        this.workAddr = workAddr;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public Short getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(Short privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public Short getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(Short isTemp) {
        this.isTemp = isTemp;
    }

    public Short getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(Short isInvalid) {
        this.isInvalid = isInvalid;
    }

    public String getDcd() {
        return dcd;
    }

    public void setDcd(String dcd) {
        this.dcd = dcd;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Short getPriceType() {
        return priceType;
    }

    public void setPriceType(Short priceType) {
        this.priceType = priceType;
    }

    public String getPatNo() {
        return patNo;
    }

    public void setPatNo(String patNo) {
        this.patNo = patNo;
    }

    public String getRegBranch() {
        return regBranch;
    }

    public void setRegBranch(String regBranch) {
        this.regBranch = regBranch;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Short getPatType() {
        return patType;
    }

    public void setPatType(Short patType) {
        this.patType = patType;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPlatPatId() {
        return platPatId;
    }

    public void setPlatPatId(String platPatId) {
        this.platPatId = platPatId;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public String getDisabledNo() {
        return disabledNo;
    }

    public void setDisabledNo(String disabledNo) {
        this.disabledNo = disabledNo;
    }

    @Override
    public String toString() {
        return "PatRegister{" +
                "patId=" + patId +
                ", branchCode='" + branchCode + '\'' +
                ", cardType=" + cardType +
                ", cardData='" + cardData + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}