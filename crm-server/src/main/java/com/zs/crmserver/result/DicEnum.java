package com.zs.crmserver.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public enum DicEnum {

    APPELLATION("appellation"),

    NEEDLOAN("needLoan"),

    INTENTIONSTATE("intentionState"),

    PRODUCT("product"),

    SOURCE("source"),

    STATE("clueState"),

    ;

    @Setter
    @Getter
    private String code;
}
