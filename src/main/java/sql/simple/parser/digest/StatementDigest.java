package sql.simple.parser.digest;

import com.alibaba.druid.sql.ast.SQLStatement;

import lombok.extern.slf4j.Slf4j;
import sql.simple.parser.digest.handler.DigestHandler;
import sql.simple.parser.digest.global.StaticObjMap;

@Slf4j
public class StatementDigest {

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
                case 11: {
                    DigestHandler.SQLGrantHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 12: {
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

}
