package sql.simple.parser.digest.common.utils;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlUserName;
import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import sql.simple.parser.digest.common.vlo.*;
import sql.simple.parser.digest.common.vlo.ColumnDefVLO;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExtraUtils {

    public static List<PrivilegeVLO> extraPrivilegeVLOFromPrivilegeItemList(List<SQLPrivilegeItem> privilegeItems) {
        List<PrivilegeVLO> privilegeVLOList = new ArrayList<>();
        for (SQLPrivilegeItem item : privilegeItems) {
            if (item.getAction() instanceof SQLIdentifierExpr action) {
                PrivilegeVLO privilegeVLO = new PrivilegeVLO();
                privilegeVLO.setAction(action.getName());
                for (SQLName col: item.getColumns()) {
                    privilegeVLO.getClumns().add(col.getSimpleName());
                }
                privilegeVLOList.add(privilegeVLO);
            }
        }
        return privilegeVLOList;
    }

    public static List<UserVLO> extraUsersFromSQLExprList (List<SQLExpr> sqlExprList) {
        List<UserVLO> UserVLOList = new ArrayList<>();
        for (SQLExpr sqlUser: sqlExprList) {
            UserVLO userVLO = new UserVLO();
            if (sqlUser instanceof SQLIdentifierExpr user) {
                userVLO.setUsername(user.getName());
            } else if (sqlUser instanceof MySqlUserName user) {
                userVLO.setUsername(user.getUserName());
                userVLO.setHost(user.getHost());
            } else if (sqlUser instanceof SQLCharExpr user) {
                userVLO.setUsername(user.getText());
            }
            if (!StringUtils.isEmpty(userVLO.getUsername())) {
                UserVLOList.add(userVLO);
            }
        }
        return UserVLOList;
    }

    public static ColumnDefVLO extraColumnFromColumnDef(SQLColumnDefinition columnDef) {
        ColumnDefVLO simpleColumnBO = new ColumnDefVLO();
        simpleColumnBO.setName(columnDef.getColumnName());
        if (columnDef.getDataType() != null) {
            simpleColumnBO.setType(columnDef.getDataType().toString());
        }
        for (SQLColumnConstraint constraint: columnDef.getConstraints()) {
            simpleColumnBO.getConstrains().add(constraint.toString());
        }
        if (columnDef.getDefaultExpr() != null)
            simpleColumnBO.setDefaultVal(columnDef.getDefaultExpr().toString());
        return simpleColumnBO;
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

    public static List<ColumnDefVLO> extraColumnDefVLOFromSQLTableElementList(List<SQLTableElement> tableElementList) {
        List<ColumnDefVLO> columnVLOList = new ArrayList<>();
        List<String> tbConstrains = new ArrayList<>();
        for (SQLTableElement tableElement: tableElementList) {
            if (tableElement instanceof SQLColumnDefinition columnDefinition) { // 列的定义表达式
                ColumnDefVLO column = ExtraUtils.extraColumnFromColumnDef(columnDefinition);
                columnVLOList.add(column);
            } else { // 约束表达式
                tbConstrains.add(tableElement.toString());
            }
        }
        for (String tbCons : tbConstrains) {
            for (ColumnDefVLO column : columnVLOList) {
                if (tbCons.contains(column.getName())) {
                    column.getConstrains().add(tbCons);
                }
            }
        }
        return columnVLOList;
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
