package sql.simple.parser.digest.enums;

public enum SubqueryType {
    UNKNOWN,
    IN_COLUMN, // 字段列表包含子查询
    IN_FROM, // FROM子句中包含子查询
    IN_WHERE, // WHERE子句中包含子查询
}
