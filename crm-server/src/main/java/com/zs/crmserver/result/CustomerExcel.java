package com.zs.crmserver.result;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CustomerExcel {

    /**
     * Excel表头的字段如下：
     *
     * 所属人	所属活动	客户姓名	客户称呼	客户手机	客户微信	客户QQ
     * 客户邮箱	客户年龄	客户职业	客户年收入
     * 客户住址	是否贷款	客户产品	客户来源	客户描述	下次联系时间
     */

    @ExcelProperty(value = "客户姓名")
    @ColumnWidth(12)
    private String fullName;

    @ExcelProperty(value = "客户称呼")
    @ColumnWidth(12)
    private String appellationName;

    @ExcelProperty(value = "客户手机")
    @ColumnWidth(20)
    private String phone;

    @ExcelProperty(value = "客户微信")
    @ColumnWidth(20)
    private String weixin;

    @ExcelProperty(value = "客户QQ")
    @ColumnWidth(20)
    private String qq;

    @ExcelProperty(value = "客户邮箱")
    @ColumnWidth(20)
    private String email;

    @ExcelProperty(value = "客户年龄")
    @ColumnWidth(12)
    private int age;

    @ExcelProperty(value = "客户职业")
    @ColumnWidth(12)
    private String job;

    @ExcelProperty(value = "客户年收入")
    @ColumnWidth(20)
    private BigDecimal yearIncome;

    @ExcelProperty(value = "客户住址")
    @ColumnWidth(20)
    private String address;

    @ExcelProperty(value = "是否贷款")
    @ColumnWidth(12)
    private String needLoanName;

    @ExcelProperty(value = "客户产品")
    @ColumnWidth(12)
    private String productName;

    @ExcelProperty(value = "客户来源")
    @ColumnWidth(12)
    private String sourceName;

    @ExcelProperty(value = "客户描述")
    @ColumnWidth(20)
    private String description;

    @ExcelProperty(value = "所属人")
    private String ownerName;

    @ExcelProperty(value = "所属活动")
    @ColumnWidth(12)
    private String activityName;

    @ExcelProperty(value = "下次联系时间")
    @ColumnWidth(20)
    private Date nextContactTime;

}
