package sql.simple.parser.digest.handler;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetTransactionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSetTransactionStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerRollbackStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerSetTransactionIsolationLevelStatement;
import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import sql.simple.parser.digest.common.utils.ExtraUtils;
import sql.simple.parser.digest.common.vlo.*;
import sql.simple.parser.digest.global.StaticObjMap;
import sql.simple.parser.digest.SQLSimpleStatement;
import sql.simple.parser.digest.enums.InstructionType;
import sql.simple.parser.digest.simpleBO.*;
import sql.simple.parser.digest.common.vlo.ColumnDefVLO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DigestHandler {

    public static void SQLCommitHandler (SQLSimpleStatement sqlSimpleStatement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.COMMIT);
    }

    public static void SQLRollbackHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.ROLLBACK);
        try {
            SQLRollbackStatement realStatement = (SQLRollbackStatement) statement;
            SimpleAttributeBO attribute = new SimpleAttributeBO("SAVEPOINT", realStatement.getTo().getSimpleName());
            sqlSimpleStatement.getAttributes().add(attribute);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void SQLServerRollbackHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.ROLLBACK);
        SQLServerRollbackStatement realStatement = (SQLServerRollbackStatement) statement;
        if (realStatement.getName() != null) {
            if (realStatement.getName() instanceof SQLIdentifierExpr identifierExpr) {
                try {
                    SimpleAttributeBO attribute = new SimpleAttributeBO("SAVEPOINT", identifierExpr.getName());
                    sqlSimpleStatement.getAttributes().add(attribute);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    public static void SQLStarTransactionHandler (SQLSimpleStatement sqlSimpleStatement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.START_TRANSACTION);
    }

    public static void MySqlSetTransactionHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.SET_TRANSACTION);
        MySqlSetTransactionStatement realStatement = (MySqlSetTransactionStatement) statement;
        SimpleAttributeBO attribute = new SimpleAttributeBO("ISOLATION LEVEL", realStatement.getIsolationLevel());
        sqlSimpleStatement.getAttributes().add(attribute);
    }

    public static void SQLServerSetTransactionIsolationLevelHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.SET_TRANSACTION);
        SQLServerSetTransactionIsolationLevelStatement realStatement = (SQLServerSetTransactionIsolationLevelStatement) statement;
        SimpleAttributeBO attribute = new SimpleAttributeBO("ISOLATION LEVEL", realStatement.getLevel());
        sqlSimpleStatement.getAttributes().add(attribute);
    }

    public static void OracleSetTransactionlHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.SET_TRANSACTION);
        OracleSetTransactionStatement realStatement = (OracleSetTransactionStatement) statement;
        if (realStatement.isReadOnly()) {
            SimpleAttributeBO attribute = new SimpleAttributeBO("READ ONLY", "TRUE");
            sqlSimpleStatement.getAttributes().add(attribute);
        } else if (realStatement.isWrite()) {
            SimpleAttributeBO attribute = new SimpleAttributeBO("READ WRITE", "TRUE");
            sqlSimpleStatement.getAttributes().add(attribute);
        }
    }

    public static void SQLSetHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.SET);
        SQLSetStatement realStatement = (SQLSetStatement) statement;
        List<SQLAssignItem> items = realStatement.getItems();
        for (SQLAssignItem item : items) {
            if (item.getTarget() instanceof SQLIdentifierExpr target
                    && item.getValue() instanceof SQLIdentifierExpr value) {
                SimpleAttributeBO attribute = new SimpleAttributeBO(target.getName(), value.getName());
                sqlSimpleStatement.getAttributes().add(attribute);
            }
        }
    }
    public static void SQLGrantHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.GRANT);
        SQLGrantStatement realStatement = (SQLGrantStatement) statement;
        try {
            SQLExprTableSource table = (SQLExprTableSource) realStatement.getResource();
            TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(table);
            sqlSimpleStatement.getSimpleGrantBO().transTableVLO(tableVLO);
            List<PrivilegeVLO> privilegeVLOList = ExtraUtils.extraPrivilegeVLOFromPrivilegeItemList(realStatement.getPrivileges());
            sqlSimpleStatement.getSimpleGrantBO().setPrivilegeList(privilegeVLOList);
            sqlSimpleStatement.getSimpleGrantBO().setWithGrantOption(realStatement.getWithGrantOption());
            if (!realStatement.getUsers().isEmpty()) {
                List<UserVLO> userVLOList = ExtraUtils.extraUsersFromSQLExprList(realStatement.getUsers());
                sqlSimpleStatement.getSimpleGrantBO().setUserList(userVLOList);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void SQLRevokeHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.REVOKE);
        SQLRevokeStatement realStatement = (SQLRevokeStatement) statement;
        try {
            SQLExprTableSource table = (SQLExprTableSource) realStatement.getResource();
            TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(table);
            sqlSimpleStatement.getSimpleGrantBO().transTableVLO(tableVLO);
            List<PrivilegeVLO> privilegeVLOList = ExtraUtils.extraPrivilegeVLOFromPrivilegeItemList(realStatement.getPrivileges());
            sqlSimpleStatement.getSimpleGrantBO().setPrivilegeList(privilegeVLOList);
            if (!realStatement.getUsers().isEmpty()) {
                List<UserVLO> userVLOList = ExtraUtils.extraUsersFromSQLExprList(realStatement.getUsers());
                sqlSimpleStatement.getSimpleGrantBO().setUserList(userVLOList);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public static void SQLCreateDatabaseHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.CREATE_DATABASE);
        SQLCreateDatabaseStatement realStatement = (SQLCreateDatabaseStatement) statement;
        try {
            sqlSimpleStatement.getSimpleResourceBO().setDatabase(realStatement.getDatabaseName());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void SQLCreateTableHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.CREATE_TABLE);
        SQLCreateTableStatement realStatement = (SQLCreateTableStatement) statement;
        try {
            SQLExprTableSource table = realStatement.getTableSource();
            TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(table);
            sqlSimpleStatement.getSimpleResourceBO().transTableVLO(tableVLO);
            List<ColumnDefVLO> columnDefVLOList = ExtraUtils.extraColumnDefVLOFromSQLTableElementList(realStatement.getTableElementList());
            sqlSimpleStatement.getSimpleResourceBO().setColumns(columnDefVLOList);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void SQLCreateIndexHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.CREATE_INDEX);
        SQLCreateIndexStatement realStatement = (SQLCreateIndexStatement) statement;
        SQLIndexDefinition indexDefinition = realStatement.getIndexDefinition();
        SimpleResourceBO simpleResourceBO = sqlSimpleStatement.getSimpleResourceBO();
        simpleResourceBO.getIndex().setName(indexDefinition.getName().getSimpleName());
        simpleResourceBO.getIndex().setType(indexDefinition.getType());
        if (indexDefinition.getTable() instanceof SQLExprTableSource tableSource) {
            TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(tableSource);
            simpleResourceBO.transTableVLO(tableVLO);
        }
        for (SQLSelectOrderByItem item: indexDefinition.getColumns()) {
            ColumnDefVLO column = new ColumnDefVLO();
            column.setName(item.toString());
            simpleResourceBO.getColumns().add(column);
        }
    }

    public static void SQLCreateViewHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.CREATE_VIEW);
        SQLCreateViewStatement realStatement = (SQLCreateViewStatement) statement;
        try {
            SimpleCreateViewBO simpleCreateViewBO = sqlSimpleStatement.getSimpleCreateViewBO();
            SQLExprTableSource table = realStatement.getTableSource();
            TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(table);
            simpleCreateViewBO.transTableVLO(tableVLO);
            for (SQLTableElement tableElement: realStatement.getColumns()) {
                if (tableElement instanceof SQLColumnDefinition columnDefinition) {
                    ColumnDefVLO column = ExtraUtils.extraColumnFromColumnDef(columnDefinition);
                    simpleCreateViewBO.getColumns().add(column);
                }
            }
            SQLSelectQueryHandler(realStatement.getSubQuery().getQuery(), simpleCreateViewBO.getRelatedQuery());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void SQLCreateProcedureHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.CREATE_PROCEDURE);
    }

    public static void SQLDropDatabaseHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.DROP_DATABASE);
        SQLDropDatabaseStatement realStatement = (SQLDropDatabaseStatement) statement;
        sqlSimpleStatement.getSimpleResourceBO().setDatabase(realStatement.getDatabaseName());
    }

    public static void SQLDropTableHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.DROP_TABLE);
        SQLDropTableStatement realStatement = (SQLDropTableStatement) statement;
        for (SQLExprTableSource tableSource: realStatement.getTableSources()) {
            SimpleResourceBO resourceBO = new SimpleResourceBO();
            TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(tableSource);
            resourceBO.transTableVLO(tableVLO);
            sqlSimpleStatement.getSimpleDropTableViewBO().add(resourceBO);
        }
    }

    public static void SQLDropViewHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.DROP_VIEW);
        SQLDropViewStatement realStatement = (SQLDropViewStatement) statement;
        for (SQLExprTableSource tableSource: realStatement.getTableSources()) {
            SimpleResourceBO resourceBO = new SimpleResourceBO();
            TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(tableSource);
            resourceBO.transTableVLO(tableVLO);
            sqlSimpleStatement.getSimpleDropTableViewBO().add(resourceBO);
        }
    }

    public static void SQLDropIndexHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.DROP_INDEX);
        SQLDropIndexStatement realStatement = (SQLDropIndexStatement) statement;
        List<SQLExprVLO> exprVLOList = new ArrayList<>();
        ExtraUtils.extraExprVLOFromSQLExpr(exprVLOList, realStatement.getIndexName());
        if (!exprVLOList.isEmpty()) {
            SQLExprVLO exprVLO = exprVLOList.get(0);
            sqlSimpleStatement.getSimpleResourceBO().getIndex().setName(exprVLO.getName());
            sqlSimpleStatement.getSimpleResourceBO().setTableView(exprVLO.getOwner());
        }
        if (realStatement.getTableName() != null) {
            TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(realStatement.getTableName());
            sqlSimpleStatement.getSimpleResourceBO().transTableVLO(tableVLO);
        }
    }

    public static void SQLAlterDatabaseHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.ALTER_DATABASE);
        SQLAlterDatabaseStatement realStatement = (SQLAlterDatabaseStatement) statement;
        sqlSimpleStatement.getSimpleResourceBO().setDatabase(realStatement.getName().getSimpleName());
    }

    public static void SQLAlterTableHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.ALTER_TABLE);
        SQLAlterTableStatement realStatement = (SQLAlterTableStatement) statement;
        SimpleAlterBO simpleAlterBO = sqlSimpleStatement.getSimpleAlterBO();
        TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(realStatement.getTableSource());
        simpleAlterBO.transTableVLO(tableVLO);
        for (SQLAlterTableItem alterTableItem: realStatement.getItems()) {
            String insName = alterTableItem.getClass().getSimpleName();
            if (StaticObjMap.alterItemMap.containsKey(insName)) {
                simpleAlterBO.getAlterInstructionList().add(StaticObjMap.alterItemMap.get(insName));
            }
        }
    }

    private static void refineColumnVLO(List<ColumnVLO> columnVLOS, TableVLO tableVLO) {
        for (ColumnVLO col: columnVLOS) {
            col.transTableVLO(tableVLO);
        }
    }
    private static void refineColumnVLO(List<ColumnVLO> columnVLOS, List<TableVLO> tableVLOList) {
        Map<String, TableVLO> alias2Tbl = new HashMap<>();
        Map<String, TableVLO> tbl2Tbl = new HashMap<>();
        for (TableVLO tbl: tableVLOList) {
            if (!StringUtils.isEmpty(tbl.getAlias())) {
                alias2Tbl.put(tbl.getAlias(), tbl);
            }
            if (!StringUtils.isEmpty(tbl.getTableView())) {
                tbl2Tbl.put(tbl.getTableView(), tbl);
            }
        }
        for (ColumnVLO col: columnVLOS) {
            if (!StringUtils.isEmpty(col.getBelongTableView())) {
                if (alias2Tbl.containsKey(col.getBelongTableView())) {
                    col.transTableVLO(alias2Tbl.get(col.getBelongTableView()));
                } else if (tbl2Tbl.containsKey(col.getBelongTableView())) {
                    col.transTableVLO(tbl2Tbl.get(col.getBelongTableView()));
                } else {
                    col.setBelongDatabase(null);
                    col.setBelongTableView(null);
                    for (TableVLO tbl: tableVLOList) {
                        col.getPossibleBelongDbTbl().add(new DbTblVLO(tbl));
                    }
                }
            } else {
                col.setBelongDatabase(null);
                col.setBelongTableView(null);
                for (TableVLO tbl: tableVLOList) {
                    col.getPossibleBelongDbTbl().add(new DbTblVLO(tbl));
                }
            }
        }

    }

    private static void breakSQLJoinTableSource(List<SQLExprTableSource> exprTableSourceList, List<SQLSubqueryTableSource> subqueryTableSourceList, SQLJoinTableSource joinTableSource) {
        if (joinTableSource.getLeft() instanceof SQLExprTableSource exprTableSource) {
            exprTableSourceList.add(exprTableSource);
        } else if (joinTableSource.getLeft() instanceof SQLSubqueryTableSource subqueryTableSource) {
            subqueryTableSourceList.add(subqueryTableSource);
        } else if (joinTableSource.getLeft() instanceof SQLJoinTableSource leftJoinTableSource) {
            breakSQLJoinTableSource(exprTableSourceList, subqueryTableSourceList, leftJoinTableSource);
        }

        if (joinTableSource.getRight() instanceof SQLExprTableSource exprTableSource) {
            exprTableSourceList.add(exprTableSource);
        } else if (joinTableSource.getRight() instanceof SQLSubqueryTableSource subqueryTableSource) {
            subqueryTableSourceList.add(subqueryTableSource);
        } else if (joinTableSource.getRight() instanceof SQLJoinTableSource leftJoinTableSource) {
            breakSQLJoinTableSource(exprTableSourceList, subqueryTableSourceList, leftJoinTableSource);
        }
    }

    /**
     * 先处理from子句，再处理select columns子句
     * @param sqlSelectQuery
     * @param simpleSelectBOList
     */
    private static void SQLSelectQueryHandler(SQLSelectQuery sqlSelectQuery, List<SimpleSelectBO> simpleSelectBOList) {
        if (sqlSelectQuery instanceof SQLSelectQueryBlock selectQueryBlock) {
            // 1、正常查表的情况
            if (selectQueryBlock.getFrom() instanceof SQLExprTableSource tableSource) {
                TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(tableSource);
                List<ColumnVLO> columnVLOList = new ArrayList<>();
                for (SQLSelectItem item: selectQueryBlock.getSelectList()) {
                    if (item.getExpr() instanceof SQLQueryExpr sqlQueryExpr) {
                        if (sqlQueryExpr.getSubQuery() != null && sqlQueryExpr.getSubQuery().getQuery() != null) {
                            SQLSelectQueryHandler(sqlQueryExpr.getSubQuery().getQuery(), simpleSelectBOList);
                        }
                    } else {
                        List<ColumnVLO> tmpList = ExtraUtils.extraColumnVLOFromSQLSelectItem(item);
                        columnVLOList.addAll(tmpList);
                    }
                }
                refineColumnVLO(columnVLOList, tableVLO);
                for (ColumnVLO col: columnVLOList) {
                    SimpleSelectBO simpleSelectBO = new SimpleSelectBO();
                    simpleSelectBO.transColumnVLO(col);
                    simpleSelectBOList.add(simpleSelectBO);
                }
            // 2、子查询的情况
            } else if (selectQueryBlock.getFrom() instanceof SQLSubqueryTableSource subqueryTableSource) {
                if (subqueryTableSource.getSelect() != null && subqueryTableSource.getSelect().getQuery() != null) {
                    SQLSelectQueryHandler(subqueryTableSource.getSelect().getQuery(), simpleSelectBOList);
                }
            // 3、join表查询的情况
            } else if (selectQueryBlock.getFrom() instanceof SQLJoinTableSource joinTableSource) {
                List<SQLExprTableSource> exprTableSourceList = new ArrayList<>();
                List<SQLSubqueryTableSource> subqueryTableSourceList = new ArrayList<>();
                // 将"from xx表"和"from子查询" 分开处理
                breakSQLJoinTableSource(exprTableSourceList, subqueryTableSourceList, joinTableSource);
                // 处理"from xx表"，即对exprTableSourceList进行处理
                {
                    List<TableVLO> tableVLOList = new ArrayList<>();
                    for (SQLExprTableSource exprTS: exprTableSourceList ) {
                        TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(exprTS);
                        tableVLOList.add(tableVLO);
                    }
                    List<ColumnVLO> columnVLOList = new ArrayList<>();
                    for (SQLSelectItem item: selectQueryBlock.getSelectList()) {
                        if (item.getExpr() instanceof SQLQueryExpr sqlQueryExpr) {
                            if (sqlQueryExpr.getSubQuery() != null && sqlQueryExpr.getSubQuery().getQuery() != null) {
                                SQLSelectQueryHandler(sqlQueryExpr.getSubQuery().getQuery(), simpleSelectBOList);
                            }
                        } else {
                            List<ColumnVLO> tmpList = ExtraUtils.extraColumnVLOFromSQLSelectItem(item);
                            columnVLOList.addAll(tmpList);
                        }
                    }
                    refineColumnVLO(columnVLOList, tableVLOList);
                    for (ColumnVLO col: columnVLOList) {
                        SimpleSelectBO simpleSelectBO = new SimpleSelectBO();
                        simpleSelectBO.transColumnVLO(col);
                        simpleSelectBOList.add(simpleSelectBO);
                    }
                }
                // 处理"from子查询" ，subqueryTableSourceList
                for (SQLSubqueryTableSource subqueryTS: subqueryTableSourceList) {
                    SQLSelectQueryHandler(subqueryTS.getSelect().getQuery(), simpleSelectBOList);
                }
            }
        } else if (sqlSelectQuery instanceof SQLUnionQuery unionQuery) {
            for (SQLSelectQuery query: unionQuery.getRelations()) {
                SQLSelectQueryHandler(query, simpleSelectBOList);
            }
        }
    }

    public static void SQLSelectStatementHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.SELECT);
        SQLSelectStatement realStatement = (SQLSelectStatement) statement;
        if (realStatement.getSelect() != null && realStatement.getSelect().getQuery() != null) {
            SQLSelectQueryHandler(realStatement.getSelect().getQuery(), sqlSimpleStatement.getSimpleSelectBOList());
        }
    }

    private static void getSQLExprListFromWhereExpr(List<SQLExpr> sqlExprList,  SQLExpr whereExpr) {
        if (whereExpr != null) {
            if (whereExpr instanceof SQLBinaryOpExpr binaryOpExpr && ("AND".equals(binaryOpExpr.getOperator().getName()) || "OR".equals(binaryOpExpr.getOperator().getName()))) {
                getSQLExprListFromWhereExpr(sqlExprList, binaryOpExpr.getLeft());
                getSQLExprListFromWhereExpr(sqlExprList, binaryOpExpr.getRight());
            } else {
                sqlExprList.add(whereExpr);
            }
        }
    }

    public static List<SQLExpr> getSQLExprListFromWhereExpr(SQLExpr whereExpr) {
        List<SQLExpr> sqlExprList = new ArrayList<>();
        if (whereExpr != null) {
            getSQLExprListFromWhereExpr(sqlExprList, whereExpr);
        }
        return sqlExprList;
    }
    public static void SQLDeleteStatementHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.DELETE);
        SQLDeleteStatement realStatement = (SQLDeleteStatement) statement;
        SimpleDeleteBO deleteBO = sqlSimpleStatement.getSimpleDeleteBO();
        if (realStatement.getTableSource() instanceof SQLExprTableSource tableSource) {
            TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(tableSource);
            deleteBO.transTableVLO(tableVLO);
        }
        if (realStatement.getWhere() != null) {
            List<SQLExpr> sqlExprList = getSQLExprListFromWhereExpr(realStatement.getWhere());
            for (SQLExpr expr : sqlExprList) {
                ConditionVLO conditionVLO = ExtraUtils.extraConditionVLOFromSQLExpr(expr);
                deleteBO.getConditionVLOS().add(conditionVLO);
            }

        }
    }
}
