package sql.simple.parser.digest.common.utils;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlUserName;
import lombok.extern.slf4j.Slf4j;
import sql.simple.parser.digest.SQLSimpleStatement;
import sql.simple.parser.digest.common.vlo.ColumnVLO;
import sql.simple.parser.digest.common.vlo.SQLExprVLO;
import sql.simple.parser.digest.res.*;
import sql.simple.parser.digest.common.vlo.TableVLO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExtraUtils {


    public static void extraDBTBLFromSQLExprTableSource(SQLSimpleStatement sqlSimpleStatement, SQLExprTableSource table) {
        if (table.getExpr() instanceof SQLPropertyExpr tableProper) {
            sqlSimpleStatement.getResource().getDatabase().setName(tableProper.getOwnerName());
            sqlSimpleStatement.getResource().getTableView().setName(tableProper.getName());
        } else if (table.getExpr() instanceof SQLIdentifierExpr identifierExpr) {
            sqlSimpleStatement.getResource().getTableView().setName(identifierExpr.getName());
        }
    }

    public static void extraDBTBLFromSQLExprTableSource(SQLSimpleDatabase sqlSimpleDatabase, SQLSimpleTableView sqlSimpleTableView, SQLExprTableSource table) {
        if (table.getExpr() instanceof SQLPropertyExpr tableProper) {
            sqlSimpleDatabase.setName(tableProper.getOwnerName());
            sqlSimpleTableView.setName(tableProper.getName());
        } else if (table.getExpr() instanceof SQLIdentifierExpr identifierExpr) {
            sqlSimpleTableView.setName(identifierExpr.getName());
        }
    }

    public static void extraPriFromPrivilegeItems (SQLSimpleStatement sqlSimpleStatement, List<SQLPrivilegeItem> privilegeItems, boolean withGrantOption) {
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

    public static void extraUserFromSQLUser (SQLSimpleStatement sqlSimpleStatement, SQLExpr sqlUser, SQLExpr identifiedBy) {
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

    public static void extraColumnFromColumnDef(SQLSimpleColumn simpleColumn, SQLColumnDefinition columnDef) {
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

    public static void extraResFromIndexDef(SQLSimpleStatement sqlSimpleStatement, SQLIndexDefinition indexDefinition) {
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

    public static void extraTBLIndexFromIndexNameExpr(SQLSimpleResource resource, SQLName sqlName, SQLExprTableSource tableSource) {
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

    public static TableVLO extraTableVLOFromExprTableSource(SQLExprTableSource table) {
        TableVLO dbTblVLO = new TableVLO();
        if (table.getExpr() instanceof SQLPropertyExpr tableProper) {
            dbTblVLO.setBelongDatabase(tableProper.getOwnerName());
            dbTblVLO.setTableView(tableProper.getName());
        } else if (table.getExpr() instanceof SQLIdentifierExpr identifierExpr) {
            dbTblVLO.setTableView(identifierExpr.getName());
        }
        dbTblVLO.setAlias(table.getAlias());
        return dbTblVLO;
    }

    /**
     * 从ArrayList<SQLSelectItem> 解析出ColumnVLO
     * 支持的类型包括xxx:SQLIdentifierExpr, xxx.xxx:SQLPropertyExpr, xxxOPxxx:SQLBinaryOpExpr, fun(xxx):SQLAggregateExpr
     * @param itemList
     * @return
     */
    public static List<ColumnVLO> extraColumnFromSQLSelectItem(List<SQLSelectItem> itemList) {
        List<ColumnVLO> colList = new ArrayList<>();
        for (SQLSelectItem item: itemList) {
            List<SQLExprVLO> exprVLOList = new ArrayList<>();
            extraExprVLOFromSQLExpr(exprVLOList, item.getExpr());
            for (SQLExprVLO exprVLO: exprVLOList) {
                ColumnVLO columnVLO = new ColumnVLO();
                columnVLO.transSQLExprVLO(exprVLO);
                columnVLO.setAlias(item.getAlias());
                colList.add(columnVLO);
            }
        }
        return colList;
    }

    public static List<ColumnVLO> extraColumnFromSQLSelectItem(SQLSelectItem item) {
        List<ColumnVLO> colList = new ArrayList<>();
        List<SQLExprVLO> exprVLOList = new ArrayList<>();
        extraExprVLOFromSQLExpr(exprVLOList, item.getExpr());
        for (SQLExprVLO exprVLO: exprVLOList) {
            ColumnVLO columnVLO = new ColumnVLO();
            columnVLO.transSQLExprVLO(exprVLO);
            columnVLO.setAlias(item.getAlias());
            colList.add(columnVLO);
        }
        return colList;
    }

    public static void extraExprVLOFromSQLExpr(List<SQLExprVLO> exprVLOList, SQLExpr sqlExpr) {
        SQLExprVLO sqlExprVLO = new SQLExprVLO();
        if (sqlExpr instanceof SQLAllColumnExpr allColumnExpr) {
            sqlExprVLO.setName("*");
            exprVLOList.add(sqlExprVLO);
        } else if (sqlExpr instanceof SQLPropertyExpr propertyExpr) {
            sqlExprVLO.setOwner(propertyExpr.getOwnerName());
            sqlExprVLO.setName(propertyExpr.getName());
            exprVLOList.add(sqlExprVLO);
        } else if (sqlExpr instanceof SQLIdentifierExpr identifierExpr) {
            sqlExprVLO.setName(identifierExpr.getName());
            exprVLOList.add(sqlExprVLO);
        } else if (sqlExpr instanceof SQLBinaryOpExpr binaryOpExpr) {
            extraExprVLOFromSQLExpr(exprVLOList, binaryOpExpr.getLeft());
            extraExprVLOFromSQLExpr(exprVLOList, binaryOpExpr.getRight());
        } else if (sqlExpr instanceof SQLAggregateExpr aggregateExpr) {
            for (SQLExpr arg: aggregateExpr.getArguments()) {
                extraExprVLOFromSQLExpr(exprVLOList, arg);
            }
        }/* else if (sqlExpr instanceof SQLQueryExpr sqlQueryExpr) {
            if (sqlQueryExpr.getSubQuery()!= null &&
                    sqlQueryExpr.getSubQuery().getQuery() != null &&
                    sqlQueryExpr.getSubQuery().getQuery() instanceof SQLSelectQueryBlock selectQueryBlock) {
                for (SQLSelectItem item: selectQueryBlock.getSelectList()) {
                    extraExprVLOFromSQLExpr(exprVLOList, item.getExpr());
                }
            }
        }*/
    }

    public static List<TableVLO> extraTableVloListFromSQLJoinTableSource(SQLJoinTableSource joinTableSource) {
        List<TableVLO> tableVLOList = new ArrayList<>();
        getTableVloListFromSQLJoinTableSource(tableVLOList, joinTableSource);
        return tableVLOList;
    }
    private static void getTableVloListFromSQLJoinTableSource(List<TableVLO> tableVLOList, SQLJoinTableSource joinTableSource) {
        if (joinTableSource.getLeft() instanceof SQLExprTableSource tableSource) {
            TableVLO tableVlo = extraTableVLOFromExprTableSource(tableSource);
            tableVLOList.add(tableVlo);
        } else if (joinTableSource.getLeft() instanceof SQLJoinTableSource joinSource) {
            getTableVloListFromSQLJoinTableSource(tableVLOList, joinSource);
        }

        if (joinTableSource.getRight() instanceof SQLExprTableSource tableSource) {
            TableVLO tableVlo = extraTableVLOFromExprTableSource(tableSource);
            tableVLOList.add(tableVlo);
        } else if (joinTableSource.getRight() instanceof SQLJoinTableSource joinSource) {
            getTableVloListFromSQLJoinTableSource(tableVLOList, joinSource);
        }

    }
}
