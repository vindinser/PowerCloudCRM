package com.zs.crmserver.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.zs.crmserver.commons.DictConvert;
import com.zs.crmserver.commons.UserConvert;
import com.zs.crmserver.config.converter.*;
import lombok.Data;

/**
 * 客户表
 * t_customer
 */
@Data
public class TCustomer implements Serializable {
    /**
     * 主键，自动增长，客户ID
     */
    private Integer id;

    /**
     * 线索ID
     */
    private Integer clueId;

    /**
     * 选购产品
     */
    @DictConvert(dicType = "product", targetField = "productName", nameField = "name")
    private Integer product;
    private String productName;

    /**
     * 客户描述
     */
    private String description;

    /**
     * 下次联系时间
     */
    private Date nextContactTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    @UserConvert(userNameField = "createName")
    private Integer createBy;
    private String createName;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 编辑人
     */
    @UserConvert(userNameField = "editName")
    private Integer editBy;
    private String editName;

    /**
     * 线索所属人ID
     */
    @UserConvert(userNameField = "ownerName")
    private Integer ownerId;
    private String ownerName;

    /**
     * 活动ID
     */
    @DictConvert(dicType = "appellation", targetField = "activityName", nameField = "name")
    private Integer activityId;
    private String activityName;

    /**
     * 姓名
     */
    private String fullName;

    /**
     * 称呼
     */
    @DictConvert(dicType = "appellation", targetField = "appellationName")
    private Integer appellation;
    private String appellationName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 微信号
     */
    private String weixin;

    /**
     * QQ号
     */
    private String qq;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 职业
     */
    private String job;

    /**
     * 年收入
     */
    private BigDecimal yearIncome;

    /**
     * 地址
     */
    private String address;

    /**
     * 是否需要贷款（0不需要，1需要）
     */
    @DictConvert(dicType = "needLoan", targetField = "needLoanName")
    private Integer needLoan;
    private String needLoanName;

    /**
     * 意向状态
     */
    @DictConvert(dicType = "intentionState", targetField = "intentionStateName")
    private Integer intentionState;
    private String intentionStateName;

    /**
     * 意向产品
     */
    @DictConvert(dicType = "product", targetField = "intentionProductName", nameField = "name")
    private Integer intentionProduct;
    private String intentionProductName;

    /**
     * 线索状态
     */
    @DictConvert(dicType = "clueState", targetField = "stateName")
    private Integer state;
    private String stateName;

    /**
     * 线索来源
     */
    @DictConvert(dicType = "source", targetField = "sourceName")
    private Integer source;
    private String sourceName;

    private static final long serialVersionUID = 1L;
}