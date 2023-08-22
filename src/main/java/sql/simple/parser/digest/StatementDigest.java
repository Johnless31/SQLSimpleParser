package sql.simple.parser.digest;

import com.alibaba.druid.sql.ast.SQLStatement;

import lombok.extern.slf4j.Slf4j;
import sql.simple.parser.digest.handler.DCLDigestHandler;
import sql.simple.parser.digest.enums.StatementInsMap;

@Slf4j
public class StatementDigest {

    public static SQLSimpleStatement SQLSimpleStatementParse(SQLStatement statement) {

        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        if (statement != null) {
            String insName = statement.getClass().getSimpleName();
            Integer insCode = StatementInsMap.statementMap.get(insName);
            switch (insCode) {
                case 1:{
                    DCLDigestHandler.SQLCommitHandler(sqlSimpleStatement);
                    break;
                }
                case 2: {
                    DCLDigestHandler.SQLRollbackHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 3: {
                    DCLDigestHandler.SQLServerRollbackHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 4:
                case 5:
                case 6: {
                    DCLDigestHandler.SQLStarTransactionHandler (sqlSimpleStatement);
                    break;
                }
                case 7: {
                    DCLDigestHandler.MySqlSetTransactionHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 8: {
                    DCLDigestHandler.SQLServerSetTransactionIsolationLevelHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 9: {
                    DCLDigestHandler.OracleSetTransactionlHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 10: {
                    DCLDigestHandler.SQLSetHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 11: {
                    DCLDigestHandler.SQLGrantHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 12: {
                    DCLDigestHandler.SQLRevokeHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 13: {
                    DCLDigestHandler.SQLCreateDatabaseHandler (sqlSimpleStatement, statement);
                    break;
                }
                case 14:
                case 15:
                case 16: {
                    DCLDigestHandler.SQLCreateTableHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 17:
                case 18: {
                    DCLDigestHandler.SQLCreateIndexHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 19: {
                    DCLDigestHandler.SQLCreateViewHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 20: {
                    DCLDigestHandler.SQLCreateProcedureHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 21: {
                    DCLDigestHandler.SQLDropDatabaseHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 22: {
                    DCLDigestHandler.SQLDropTableHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 23: {
                    DCLDigestHandler.SQLDropViewHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 24: {
                    DCLDigestHandler.SQLDropIndexHandler(sqlSimpleStatement, statement);
                    break;
                }
                case 25: {
                    DCLDigestHandler.SQLAlterTableHandler(sqlSimpleStatement, statement);
                    break;
                }
                default: break;
            }
        }
        return sqlSimpleStatement;
    }

}
