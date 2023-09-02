package sql.simple.parser.digest;

import com.alibaba.druid.sql.ast.SQLStatement;

import lombok.extern.slf4j.Slf4j;
import sql.simple.parser.digest.handler.DigestHandler;
import sql.simple.parser.digest.global.StatementInsMap;

@Slf4j
public class StatementDigest {

    public static SQLSimpleStatement SQLSimpleStatementParse(SQLStatement statement) {

        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        if (statement != null) {
            String insName = statement.getClass().getSimpleName();
            Integer insCode = StatementInsMap.statementMap.get(insName);
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
                case 13: {
                    DigestHandler.SQLCreateDatabaseHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 14:
                case 15:
                case 16: {
                    DigestHandler.SQLCreateTableHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 17:
                case 18: {
                    DigestHandler.SQLCreateIndexHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 19: {
                    DigestHandler.SQLCreateViewHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 20: {
                    DigestHandler.SQLCreateProcedureHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 21: {
                    DigestHandler.SQLDropDatabaseHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 22: {
                    DigestHandler.SQLDropTableHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 23: {
                    DigestHandler.SQLDropViewHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 24: {
                    DigestHandler.SQLDropIndexHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 25: {
                    DigestHandler.SQLAlterTableHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 26: {
                    DigestHandler.SQLAlterDatabaseHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 27: {
                    DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, statement);
                    break;
                }
                default: break;
            }
        }
        return sqlSimpleStatement;
    }

}
