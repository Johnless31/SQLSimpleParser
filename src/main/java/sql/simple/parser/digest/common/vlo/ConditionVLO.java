package sql.simple.parser.digest.common.vlo;

import lombok.Data;
import sql.simple.parser.digest.enums.ConditionType;

@Data
public class ConditionVLO {
    private ConditionType type = ConditionType.UNKNOWN;
    private String leftStr; // 条件左值字符串
    private String rightStr; // 条件右值字符串
    private String operateStr; // 操作符
    private String column; // 条件涉及的列
}
