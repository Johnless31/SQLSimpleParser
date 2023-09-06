package sql.simple.parser.digest.enums;

public enum ConditionType {
    UNKNOWN,
    BINARY_OP, // 具体有哪些二元操作符，参考SQLBinaryOperator
    IN_LIST_OP, // in
    BETWEEN_OP, // between
    EXISTS_OP, // exist
}
