package sql.simple.parser.digest.enums;

public enum SelectItemType {
    UNKNOWN,
    COLUMN, // 常规列
    CONSTANT, // 常量
    SUBQUERY // 子查询
}
