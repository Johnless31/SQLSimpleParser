package sql.simple.parser.digest.handler;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlUserName;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetTransactionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSetTransactionStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerRollbackStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerSetTransactionIsolationLevelStatement;
import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import sql.simple.parser.digest.common.utils.ExtraUtils;
import sql.simple.parser.digest.common.vlo.ColumnVLO;
import sql.simple.parser.digest.common.vlo.DbTblVLO;
import sql.simple.parser.digest.common.vlo.SQLExprVLO;
import sql.simple.parser.digest.enums.StatementInsMap;
import sql.simple.parser.digest.res.*;
import sql.simple.parser.digest.SQLSimpleStatement;
import sql.simple.parser.digest.enums.InstructionType;
import sql.simple.parser.digest.common.vlo.TableVLO;
import sql.simple.parser.digest.simpleBO.SimpleSelectBO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DigestHandler {


    private static void extraDBTBLFromSQLExprTableSource(SQLSimpleStatement sqlSimpleStatement, SQLExprTableSource table) {
        if (table.getExpr() instanceof SQLPropertyExpr tableProper) {
            sqlSimpleStatement.getResource().getDatabase().setName(tableProper.getOwnerName());
            sqlSimpleStatement.getResource().getTableView().setName(tableProper.getName());
        } else if (table.getExpr() instanceof SQLIdentifierExpr identifierExpr) {
            sqlSimpleStatement.getResource().getTableView().setName(identifierExpr.getName());
        }
    }

    private static void extraDBTBLFromSQLExprTableSource(SQLSimpleDatabase sqlSimpleDatabase, SQLSimpleTableView sqlSimpleTableView, SQLExprTableSource table) {
        if (table.getExpr() instanceof SQLPropertyExpr tableProper) {
            sqlSimpleDatabase.setName(tableProper.getOwnerName());
            sqlSimpleTableView.setName(tableProper.getName());
        } else if (table.getExpr() instanceof SQLIdentifierExpr identifierExpr) {
            sqlSimpleTableView.setName(identifierExpr.getName());
        }
    }

    private static void extraPriFromPrivilegeItems (SQLSimpleStatement sqlSimpleStatement, List<SQLPrivilegeItem> privilegeItems, boolean withGrantOption) {
        for (SQLPrivilegeItem item : privilegeItems) {
            SimplePrivilege simplePrivilege = new SimplePrivilege();
            if (item.getAction() instanceof SQLIdentifierExpr action) {
                SQLSimplePrivilegeAction priAction = new SQLSimplePrivilegeAction();
                priAction.setAction(action.getName());
                for (SQLName col: item.getColumns()) {
                    priAction.getColumns().add(col.getSimpleName());
                }
                simplePrivilege.getActions().add(priAction);
            }
            sqlSimpleStatement.getPrivilege().getSimplePrivileges().add(simplePrivilege);
        }
        if (withGrantOption) {
            sqlSimpleStatement.getPrivilege().setWithGrantOption(true);
        }
    }

    private static void extraUserFromSQLUser (SQLSimpleStatement sqlSimpleStatement, SQLExpr sqlUser, SQLExpr identifiedBy) {
        if (sqlUser instanceof SQLIdentifierExpr user) {
            sqlSimpleStatement.getUser().setUsername(user.getName());
        } else if (sqlUser instanceof MySqlUserName user) {
            sqlSimpleStatement.getUser().setUsername(user.getUserName());
            sqlSimpleStatement.getUser().setHost(user.getHost());
            sqlSimpleStatement.getUser().setIdentifyBy(user.getIdentifiedBy());
        }
        if (identifiedBy instanceof SQLCharExpr charIdentified && !charIdentified.getText().isEmpty()) {
            sqlSimpleStatement.getUser().setIdentifyBy(charIdentified.getText());
        }
    }

    private static void extraColumnFromColumnDef(SQLSimpleColumn simpleColumn, SQLColumnDefinition columnDef) {
        simpleColumn.setName(columnDef.getColumnName());
        if (columnDef.getDataType() != null) {
            simpleColumn.setType(columnDef.getDataType().toString());
        }
        for (SQLColumnConstraint constraint: columnDef.getConstraints()) {
            simpleColumn.getConstrains().add(constraint.toString());
        }
        if (columnDef.getDefaultExpr() != null)
            simpleColumn.setDefaultVal(columnDef.getDefaultExpr().toString());
    }

    private static void extraResFromIndexDef(SQLSimpleStatement sqlSimpleStatement, SQLIndexDefinition indexDefinition) {
        sqlSimpleStatement.getResource().getIndex().setName(indexDefinition.getName().getSimpleName());
        sqlSimpleStatement.getResource().getIndex().setType(indexDefinition.getType());
        if (indexDefinition.getTable() instanceof SQLExprTableSource tableSource) {
            extraDBTBLFromSQLExprTableSource(sqlSimpleStatement, tableSource);
        }
        for (SQLSelectOrderByItem item: indexDefinition.getColumns()) {
            SQLSimpleColumn column = new SQLSimpleColumn();
            column.setName(item.toString());
            sqlSimpleStatement.getResource().getColumns().add(column);
        }

    }

    private static void extraTBLIndexFromIndexNameExpr(SQLSimpleResource resource, SQLName sqlName, SQLExprTableSource tableSource) {
        if (sqlName instanceof SQLIdentifierExpr identifierExpr) {
            resource.getIndex().setName(identifierExpr.getName());
        } else if (sqlName instanceof SQLPropertyExpr propertyExpr) {
            resource.getTableView().setName(propertyExpr.getOwnerName());
            resource.getIndex().setName(propertyExpr.getName());
        }
        if (tableSource != null) {
            extraDBTBLFromSQLExprTableSource(resource.getDatabase(), resource.getTableView(), tableSource);
        }
    }
    public static void SQLCommitHandler (SQLSimpleStatement sqlSimpleStatement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.COMMIT);
    }

    public static void SQLRollbackHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.ROLLBACK);
        try {
            SQLRollbackStatement realStatement = (SQLRollbackStatement) statement;
            SQLSimpleAttribute attribute = new SQLSimpleAttribute("SAVEPOINT", realStatement.getTo().getSimpleName());
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
                    SQLSimpleAttribute attribute = new SQLSimpleAttribute("SAVEPOINT", identifierExpr.getName());
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
        SQLSimpleAttribute attribute = new SQLSimpleAttribute("ISOLATION LEVEL", realStatement.getIsolationLevel());
        sqlSimpleStatement.getAttributes().add(attribute);
    }

    public static void SQLServerSetTransactionIsolationLevelHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.SET_TRANSACTION);
        SQLServerSetTransactionIsolationLevelStatement realStatement = (SQLServerSetTransactionIsolationLevelStatement) statement;
        SQLSimpleAttribute attribute = new SQLSimpleAttribute("ISOLATION LEVEL", realStatement.getLevel());
        sqlSimpleStatement.getAttributes().add(attribute);
    }

    public static void OracleSetTransactionlHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.SET_TRANSACTION);
        OracleSetTransactionStatement realStatement = (OracleSetTransactionStatement) statement;
        if (realStatement.isReadOnly()) {
            SQLSimpleAttribute attribute = new SQLSimpleAttribute("READ ONLY", "TRUE");
            sqlSimpleStatement.getAttributes().add(attribute);
        } else if (realStatement.isWrite()) {
            SQLSimpleAttribute attribute = new SQLSimpleAttribute("READ WRITE", "TRUE");
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
                SQLSimpleAttribute attribute = new SQLSimpleAttribute(target.getName(), value.getName());
                sqlSimpleStatement.getAttributes().add(attribute);
            }
        }
    }
    public static void SQLGrantHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.GRANT);
        SQLGrantStatement realStatement = (SQLGrantStatement) statement;
        try {
            SQLExprTableSource table = (SQLExprTableSource) realStatement.getResource();
            extraDBTBLFromSQLExprTableSource(sqlSimpleStatement, table);
            extraPriFromPrivilegeItems (sqlSimpleStatement, realStatement.getPrivileges(), realStatement.getWithGrantOption());
            if (!realStatement.getUsers().isEmpty()) {
                extraUserFromSQLUser(sqlSimpleStatement, realStatement.getUsers().get(0), realStatement.getIdentifiedBy());
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
            extraDBTBLFromSQLExprTableSource(sqlSimpleStatement, table);
            extraPriFromPrivilegeItems (sqlSimpleStatement, realStatement.getPrivileges(), false);
            if (!realStatement.getUsers().isEmpty()) {
                extraUserFromSQLUser(sqlSimpleStatement, realStatement.getUsers().get(0), null);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public static void SQLCreateDatabaseHandler (SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.CREATE_DATABASE);
        SQLCreateDatabaseStatement realStatement = (SQLCreateDatabaseStatement) statement;
        try {
            sqlSimpleStatement.getResource().getDatabase().setName(realStatement.getDatabaseName());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void SQLCreateTableHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.CREATE_TABLE);
        SQLCreateTableStatement realStatement = (SQLCreateTableStatement) statement;
        try {
            SQLExprTableSource table = realStatement.getTableSource();
            extraDBTBLFromSQLExprTableSource(sqlSimpleStatement, table);
            List<String> tbConstrains = new ArrayList<>();
            for (SQLTableElement tableElement: realStatement.getTableElementList()) {
                if (tableElement instanceof SQLColumnDefinition columnDefinition) {
                    SQLSimpleColumn column = new SQLSimpleColumn();
                    extraColumnFromColumnDef(column, columnDefinition);
                    sqlSimpleStatement.getResource().getColumns().add(column);
                } else {
                    tbConstrains.add(tableElement.toString());
                }
            }
            for (String tbCons : tbConstrains) {
                for (SQLSimpleColumn column : sqlSimpleStatement.getResource().getColumns()) {
                    if (tbCons.contains(column.getName())) {
                        column.getConstrains().add(tbCons);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void SQLCreateIndexHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.CREATE_INDEX);
        SQLCreateIndexStatement realStatement = (SQLCreateIndexStatement) statement;
        extraResFromIndexDef(sqlSimpleStatement, realStatement.getIndexDefinition());
    }

    public static void SQLCreateViewHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.CREATE_VIEW);
        SQLCreateViewStatement realStatement = (SQLCreateViewStatement) statement;
        try {
            SQLExprTableSource table = realStatement.getTableSource();
            extraDBTBLFromSQLExprTableSource(sqlSimpleStatement, table);
            for (SQLTableElement tableElement: realStatement.getColumns()) {
                if (tableElement instanceof SQLColumnDefinition columnDefinition) {
                    SQLSimpleColumn column = new SQLSimpleColumn();
                    extraColumnFromColumnDef(column, columnDefinition);
                    sqlSimpleStatement.getResource().getColumns().add(column);
                }
            }
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
        sqlSimpleStatement.getResource().getDatabase().setName(realStatement.getDatabaseName());
    }

    public static void SQLDropTableHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.DROP_TABLE);
        SQLDropTableStatement realStatement = (SQLDropTableStatement) statement;
        for (SQLExprTableSource tableSource: realStatement.getTableSources()) {
            SQLSimpleDBTBLCOL dbtblcol = new SQLSimpleDBTBLCOL();
            extraDBTBLFromSQLExprTableSource(dbtblcol.getDatabase(), dbtblcol.getTableView(), tableSource);
            sqlSimpleStatement.getRefMultiRes().add(dbtblcol);
        }
    }

    public static void SQLDropViewHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.DROP_VIEW);
        SQLDropViewStatement realStatement = (SQLDropViewStatement) statement;
        for (SQLExprTableSource tableSource: realStatement.getTableSources()) {
            SQLSimpleDBTBLCOL dbTblCol = new SQLSimpleDBTBLCOL();
            extraDBTBLFromSQLExprTableSource(dbTblCol.getDatabase(), dbTblCol.getTableView(), tableSource);
            sqlSimpleStatement.getRefMultiRes().add(dbTblCol);
        }
    }

    public static void SQLDropIndexHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.DROP_INDEX);
        SQLDropIndexStatement realStatement = (SQLDropIndexStatement) statement;
        extraTBLIndexFromIndexNameExpr(sqlSimpleStatement.getResource(), realStatement.getIndexName(), realStatement.getTableName());
    }

    public static void SQLAlterTableHandler(SQLSimpleStatement sqlSimpleStatement, SQLStatement statement) {
        sqlSimpleStatement.getInstruction().setType(InstructionType.ALTER_TABLE);
        SQLAlterTableStatement realStatement = (SQLAlterTableStatement) statement;
        extraDBTBLFromSQLExprTableSource(sqlSimpleStatement, realStatement.getTableSource());
        for (SQLAlterTableItem alterTableItem: realStatement.getItems()) {
            String insName = alterTableItem.getClass().getSimpleName();
            if (StatementInsMap.alterItemMap.containsKey(insName)) {
                SQLSimpleDBTBLCOL sqlSimpleDBTBLCOL = new SQLSimpleDBTBLCOL();
                sqlSimpleDBTBLCOL.setSubInstructionType(StatementInsMap.alterItemMap.get(insName));
                sqlSimpleStatement.getRefMultiRes().add(sqlSimpleDBTBLCOL);
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

    private static void SQLSelectQueryHandler(SQLSelectQuery sqlSelectQuery, List<SimpleSelectBO> simpleSelectBOList) {
        if (sqlSelectQuery instanceof SQLSelectQueryBlock selectQueryBlock) {
            if (selectQueryBlock.getFrom() instanceof SQLExprTableSource tableSource) {
                TableVLO tableVLO = ExtraUtils.extraTableVLOFromExprTableSource(tableSource);
                List<ColumnVLO> columnVLOList = new ArrayList<>();
                for (SQLSelectItem item: selectQueryBlock.getSelectList()) {
                    if (item.getExpr() instanceof SQLQueryExpr sqlQueryExpr) {
                        if (sqlQueryExpr.getSubQuery() != null && sqlQueryExpr.getSubQuery().getQuery() != null) {
                            SQLSelectQueryHandler(sqlQueryExpr.getSubQuery().getQuery(), simpleSelectBOList);
                        }
                    } else {
                        List<ColumnVLO> tmpList = ExtraUtils.extraColumnFromSQLSelectItem(item);
                        columnVLOList.addAll(tmpList);
                    }
                }
                refineColumnVLO(columnVLOList, tableVLO);
                for (ColumnVLO col: columnVLOList) {
                    SimpleSelectBO simpleSelectBO = new SimpleSelectBO();
                    simpleSelectBO.transColumnVLO(col);
                    simpleSelectBOList.add(simpleSelectBO);
                }
            } else if (selectQueryBlock.getFrom() instanceof SQLSubqueryTableSource subqueryTableSource) {
                if (subqueryTableSource.getSelect() != null && subqueryTableSource.getSelect().getQuery() != null) {
                    SQLSelectQueryHandler(subqueryTableSource.getSelect().getQuery(), simpleSelectBOList);
                }
            } else if (selectQueryBlock.getFrom() instanceof SQLJoinTableSource joinTableSource) {
                List<SQLExprTableSource> exprTableSourceList = new ArrayList<>();
                List<SQLSubqueryTableSource> subqueryTableSourceList = new ArrayList<>();
                breakSQLJoinTableSource(exprTableSourceList, subqueryTableSourceList, joinTableSource);
                // 处理exprTS
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
                        List<ColumnVLO> tmpList = ExtraUtils.extraColumnFromSQLSelectItem(item);
                        columnVLOList.addAll(tmpList);
                    }
                }
                refineColumnVLO(columnVLOList, tableVLOList);
                for (ColumnVLO col: columnVLOList) {
                    SimpleSelectBO simpleSelectBO = new SimpleSelectBO();
                    simpleSelectBO.transColumnVLO(col);
                    simpleSelectBOList.add(simpleSelectBO);
                }
                // 处理subqueryList
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
}
