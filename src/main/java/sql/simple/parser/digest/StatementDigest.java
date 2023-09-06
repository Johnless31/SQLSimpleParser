package sql.simple.parser.digest;

import com.alibaba.druid.sql.ast.SQLStatement;

import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlFlushStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetTransactionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSetTransactionStatement;
import com.alibaba.druid.sql.dialect.oscar.ast.stmt.OscarStartTransactionStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGStartTransactionStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerRollbackStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerSetTransactionIsolationLevelStatement;
import lombok.extern.slf4j.Slf4j;
import sql.simple.parser.digest.handler.DigestHandler;

@Slf4j
public class StatementDigest {

    public static SQLSimpleStatement sqlSimpleStatementParse(SQLStatement statement) {
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        if (statement instanceof SQLSelectStatement) {
            DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLCreateTableStatement) {
            DigestHandler.SQLCreateTableHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLCreateIndexStatement) {
            DigestHandler.SQLCreateIndexHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLCreateViewStatement) {
            DigestHandler.SQLCreateViewHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLCreateDatabaseStatement) {
            DigestHandler.SQLCreateDatabaseHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLCreateProcedureStatement) {
            DigestHandler.SQLCreateProcedureHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLDropDatabaseStatement) {
            DigestHandler.SQLDropDatabaseHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLDropTableStatement) {
            DigestHandler.SQLDropTableHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLDropViewStatement) {
            DigestHandler.SQLDropViewHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLDropIndexStatement) {
            DigestHandler.SQLDropIndexHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLAlterTableStatement) {
            DigestHandler.SQLAlterTableHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLAlterDatabaseStatement) {
            DigestHandler.SQLAlterDatabaseHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLCommitStatement) {
            DigestHandler.SQLCommitHandler(sqlSimpleStatement);
        } else if (statement instanceof SQLRollbackStatement) {
            if (statement instanceof SQLServerRollbackStatement) {
                DigestHandler.SQLServerRollbackHandler (sqlSimpleStatement, statement);
            } else {
                DigestHandler.SQLRollbackHandler (sqlSimpleStatement, statement);

            }
        } else if (statement instanceof SQLStartTransactionStatement
                || statement instanceof OscarStartTransactionStatement
                || statement instanceof PGStartTransactionStatement) {
            DigestHandler.SQLStarTransactionHandler (sqlSimpleStatement);
        } else if (statement instanceof MySqlSetTransactionStatement) {
            DigestHandler.MySqlSetTransactionHandler (sqlSimpleStatement, statement);
        } else if (statement instanceof SQLServerSetTransactionIsolationLevelStatement) {
            DigestHandler.SQLServerSetTransactionIsolationLevelHandler (sqlSimpleStatement, statement);
        } else if (statement instanceof OracleSetTransactionStatement) {
            DigestHandler.OracleSetTransactionlHandler (sqlSimpleStatement, statement);
        } else if (statement instanceof SQLSetStatement) {
            DigestHandler.SQLSetHandler (sqlSimpleStatement, statement);
        } else if (statement instanceof SQLGrantStatement) {
            DigestHandler.SQLGrantHandler (sqlSimpleStatement, statement);
        } else if (statement instanceof SQLRevokeStatement) {
            DigestHandler.SQLRevokeHandler (sqlSimpleStatement, statement);
        } else if (statement instanceof  SQLDeleteStatement) {
            DigestHandler.SQLDeleteStatementHandler (sqlSimpleStatement, statement);
        } else if (statement instanceof SQLExplainStatement) {
            DigestHandler.SQLExplainStatementHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLDescribeStatement) {
            DigestHandler.SQLDescribeStatementHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLExplainAnalyzeStatement) {
            DigestHandler.SQLExplainAnalyzeStatementHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLShowStatement) {
            DigestHandler.SQLShowStatementHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof SQLPurgeLogsStatement) {
            DigestHandler.SQLPurgeLogStatementHandler(sqlSimpleStatement, statement);
        } else if (statement instanceof MySqlFlushStatement) {
            DigestHandler.MySqlFlushStatementHandler(sqlSimpleStatement, statement);
        }

        return sqlSimpleStatement;
    }
    /*
    public static SQLSimpleStatement SQLSimpleStatementParse(SQLStatement statement) {
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        if (statement != null) {
            String insName = statement.getClass().getSimpleName();
            Integer insCode = StaticObjMap.statementMap.get(insName);
            switch (insCode) {
                case 1:{
                    DigestHandler.SQLCommitHandler(sqlSimpleStatement);
                    break;
                }
                case 2: {
                    DigestHandler.SQLRollbackHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 3: {
                    DigestHandler.SQLServerRollbackHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 4:
                case 5:
                case 6: {
                    DigestHandler.SQLStarTransactionHandler (sqlSimpleStatement);
                    break;
                }
                case 7: {
                    DigestHandler.MySqlSetTransactionHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 8: {
                    DigestHandler.SQLServerSetTransactionIsolationLevelHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 9: {
                    DigestHandler.OracleSetTransactionlHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 10: {
                    DigestHandler.SQLSetHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 11:
                case 12: {
                    DigestHandler.SQLGrantHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 13: {
                    DigestHandler.SQLRevokeHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 1001: {
                    DigestHandler.SQLCreateDatabaseHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 1002:
                case 1003:
                case 1004: {
                    DigestHandler.SQLCreateTableHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 1005:
                case 1006: {
                    DigestHandler.SQLCreateIndexHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 1007: {
                    DigestHandler.SQLCreateViewHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 1008: {
                    DigestHandler.SQLCreateProcedureHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 1009: {
                    DigestHandler.SQLDropDatabaseHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 1010: {
                    DigestHandler.SQLDropTableHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 1011: {
                    DigestHandler.SQLDropViewHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 1012: {
                    DigestHandler.SQLDropIndexHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 1013: {
                    DigestHandler.SQLAlterTableHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 1014: {
                    DigestHandler.SQLAlterDatabaseHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 3001: {
                    DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, statement);
                    break;
                }
                default: break;
            }
        }
        return sqlSimpleStatement;
    }
     */

}
