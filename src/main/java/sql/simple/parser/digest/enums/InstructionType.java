package sql.simple.parser.digest.enums;

public enum InstructionType {
    UNKNOWN,
    COMMIT,
    ROLLBACK,
    START_TRANSACTION,
    SET_TRANSACTION,
    SET,
    GRANT,
    REVOKE,
    CREATE_DATABASE,
    CREATE_TABLE,
    CREATE_INDEX,
    CREATE_VIEW,
    CREATE_PROCEDURE,
    DROP_DATABASE,
    DROP_TABLE,
    DROP_VIEW,
    DROP_INDEX,
    ALTER_TABLE,
    SELECT,
    SELECT_SUB_QUERY,
    SELECT_UNION,
    SELECT_JOIN
}
