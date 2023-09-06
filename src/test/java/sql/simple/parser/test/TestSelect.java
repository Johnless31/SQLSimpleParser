package sql.simple.parser.test;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLDDLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import sql.simple.parser.digest.SQLSimpleStatement;
import sql.simple.parser.digest.StatementDigest;
import sql.simple.parser.digest.handler.DigestHandler;
import sql.simple.parser.digest.simpleBO.SimpleSelectBO;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestSelect {

    @Test
    public void testSelectSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT Tid,Tname FROM Teachers;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 2);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Teachers"));
            assert(bo.getColumn().equals("Tid") || bo.getColumn().equals("Tname"));
        }

    }

    @Test
    public void testSelectSQL1() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT TC.Tid, TC.Tname FROM db.Teachers AS TC;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 2);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getDatabase().equals("db"));
            assert(bo.getTableView().equals("Teachers"));
            assert(bo.getColumn().equals("Tid") || bo.getColumn().equals("Tname"));
        }
    }

    @Test
    public void testSelectSQL2() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT 100+Tid+100 AS \"新编号\", Tname AS \"身份\" FROM db.Teachers;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 2);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getDatabase().equals("db"));
            assert(bo.getTableView().equals("Teachers"));
            assert(bo.getColumn().equals("Tid") || bo.getColumn().equals("Tname"));
        }
    }

    @Test
    public void testSelectSQL3() {
        //com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT * FROM Teachers;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 1);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Teachers"));
            assert(bo.getColumn().equals("*"));
        }
    }

    @Test
    public void testSelectSQL4() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT 'xxx' AS string, 38 AS number, Tid,Tname FROM Teachers;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 2);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Teachers"));
            assert(bo.getColumn().equals("Tid") || bo.getColumn().equals("Tname"));
        }

    }

    @Test
    public void testSelectSQL5() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT DISTINCT Tid,Tname FROM Teachers;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 2);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Teachers"));
            assert(bo.getColumn().equals("Tid") || bo.getColumn().equals("Tname"));
        }
    }

    @Test
    public void testSelectSQL6() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        // count, sum, avg, max, min
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT MAX(DISTINCT Tid), MIN(Tname) FROM Teachers;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 2);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Teachers"));
            assert(bo.getColumn().equals("Tid") || bo.getColumn().equals("Tname"));
        }
    }

    @Test
    public void testSelectSQL7() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT  product_type, cnt_product FROM (SELECT product_type, COUNT(*) AS cnt_product FROM Product GROUP BY product_type) AS ProductSum;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 2);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Product"));
            assert(bo.getColumn().equals("product_type") || bo.getColumn().equals("*"));
        }
    }

    @Test
    public void testSelectSQL8() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT product_type, cnt_product\n" +
                " FROM (SELECT *\n" +
                " FROM (SELECT product_type, COUNT(*) AS cnt_product\n" +
                " FROM Product\n" +
                " GROUP BY product_type) AS ProductSum\n" +
                " WHERE cnt_product = 4) AS ProductSum2;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 2);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Product"));
            assert(bo.getColumn().equals("product_type") || bo.getColumn().equals("*"));
        }
    }

    @Test
    public void testSelectSQL9() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT product_id,\n" +
                " product_name,\n" +
                " sale_price,\n" +
                " (SELECT AVG(sale_pos)\n" +
                " FROM ProductPos) AS avg_price\n" +
                " FROM Product;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 4);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            if (bo.getTableView().equals("Product")) {
                assert(bo.getColumn().equals("product_id") || bo.getColumn().equals("product_name") || bo.getColumn().equals("sale_price"));
            } else if (bo.getTableView().equals("ProductPos")) {
                assert(bo.getColumn().equals("sale_pos"));
            }
        }
    }

    @Test
    public void testSelectSQL10() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT product_id, product_name, sale_price\n" +
                " FROM Product\n" +
                " WHERE sale_price > (SELECT AVG(sale_pos)\n" +
                " FROM ProductPos);", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 3);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Product"));
            assert(bo.getColumn().equals("product_id") || bo.getColumn().equals("product_name") || bo.getColumn().equals("sale_price"));
        }
    }

    @Test
    public void testSelectSQL11() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT str1, str2,\n" +
                " str1 || str3 AS str_concat\n" +
                " FROM SampleStr;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 4);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("SampleStr"));
            assert(bo.getColumn().equals("str1") || bo.getColumn().equals("str2") || bo.getColumn().equals("str3"));
        }
    }

    @Test
    public void testSelectSQL12() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT product_name, sale_price\n" +
                " FROM Product\n" +
                " WHERE product_id IN (SELECT product_id\n" +
                " FROM ShopProduct\n" +
                " WHERE shop_id = '000C');", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 2);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Product"));
            assert(bo.getColumn().equals("product_name") || bo.getColumn().equals("sale_price"));
        }
    }

    @Test
    public void testSelectUnionSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT product_id, product_name\n" +
                " FROM Product\n" +
                "UNION\n" +
                "SELECT product_id, product_name\n" +
                " FROM Product2;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 4);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Product") || bo.getTableView().equals("Product2"));
            assert(bo.getColumn().equals("product_name") || bo.getColumn().equals("product_id"));
        }
    }
    @Test
    public void testSelectUnionSQL2() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT product_id, product_name\n" +
                " FROM Product\n" +
                "UNION ALL\n" +
                "SELECT product_id, product_name\n" +
                " FROM Product2;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 4);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Product") || bo.getTableView().equals("Product2"));
            assert(bo.getColumn().equals("product_name") || bo.getColumn().equals("product_id"));
        }
    }

    @Test
    public void testSelectUnionSQL3() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT product_id, product_name\n" +
                " FROM Product\n" +
                "INTERSECT\n" +
                "SELECT product_id, product_name\n" +
                " FROM Product2\n" +
                "ORDER BY product_id;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 4);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Product") || bo.getTableView().equals("Product2"));
            assert(bo.getColumn().equals("product_name") || bo.getColumn().equals("product_id"));
        }
    }

    @Test
    public void testSelectUnionSQL4() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT product_id, product_name\n" +
                " FROM Product\n" +
                "EXCEPT\n" +
                "SELECT product_id, product_name\n" +
                " FROM Product2\n" +
                "ORDER BY product_id;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 4);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("Product") || bo.getTableView().equals("Product2"));
            assert(bo.getColumn().equals("product_name") || bo.getColumn().equals("product_id"));
        }
    }

    @Test
    public void testSelectJoinSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT SP.shop_id, SP.shop_name, SP.product_id, P.product_name, " +
                "P.sale_price\n" +
                " FROM ShopProduct AS SP INNER JOIN Product AS P \n" +
                " ON SP.product_id = P.product_id;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 5);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getTableView().equals("ShopProduct") || bo.getTableView().equals("Product"));
            assert(bo.getColumn().equals("shop_id")
                    || bo.getColumn().equals("shop_name")
                    || bo.getColumn().equals("product_id")
                    || bo.getColumn().equals("product_name")
                    || bo.getColumn().equals("sale_price"));
        }
    }

    @Test
    public void testSelectJoinSQL1() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT SP.shop_id, SP.shop_name, SP.product_id, P.product_name,\n" +
                "P.sale_price\n" +
                " FROM ShopProduct AS SP RIGHT OUTER JOIN Product AS P \n" +
                " ON SP.product_id = P.product_id;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 5);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            if (bo.getTableView().equals("ShopProduct") ) {
                assert(bo.getColumn().equals("shop_id")
                        || bo.getColumn().equals("shop_name")
                        || bo.getColumn().equals("product_id"));
            }
            if (bo.getTableView().equals("Product")) {
                assert (bo.getColumn().equals("product_name")
                        || bo.getColumn().equals("sale_price"));
            }
        }
    }

    @Test
    public void testSelectJoinSQL2() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT SP.shop_id, SP.shop_name, SP.product_id, P.product_name, \n" +
                "P.sale_price\n" +
                " FROM Product AS P LEFT OUTER JOIN ShopProduct AS SP \n" +
                " ON SP.product_id = P.product_id;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 5);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            if (bo.getTableView().equals("ShopProduct") ) {
                assert(bo.getColumn().equals("shop_id")
                        || bo.getColumn().equals("shop_name")
                        || bo.getColumn().equals("product_id"));
            }
            if (bo.getTableView().equals("Product")) {
                assert (bo.getColumn().equals("product_name")
                        || bo.getColumn().equals("sale_price"));
            }
        }
    }

    @Test
    public void testSelectJoinSQL3() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT SP.shop_id, SP.shop_name, SP.product_id, P.product_name, \n" +
                "P.sale_price, IP.inventory_quantity\n" +
                " FROM ShopProduct AS SP INNER JOIN Product AS P\n" +
                " ON SP.product_id = P.product_id\n" +
                " INNER JOIN InventoryProduct AS IP\n" +
                " ON SP.product_id = IP.product_id\n" +
                " WHERE IP.inventory_id = 'P001';", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 6);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            if (bo.getTableView().equals("ShopProduct") ) {
                assert(bo.getColumn().equals("shop_id")
                        || bo.getColumn().equals("shop_name")
                        || bo.getColumn().equals("product_id"));
            } else if (bo.getTableView().equals("Product")) {
                assert (bo.getColumn().equals("product_name")
                        || bo.getColumn().equals("sale_price"));
            } else if (bo.getTableView().equals("InventoryProduct")) {
                assert (bo.getColumn().equals("inventory_quantity"));
            }
        }
    }

    @Test
    public void testSelectJoinSQL4() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT SP.shop_id, SP.shop_name, SP.product_id, P.product_name\n" +
                " FROM ShopProduct AS SP CROSS JOIN Product AS P;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 4);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            if (bo.getTableView().equals("ShopProduct") ) {
                assert(bo.getColumn().equals("shop_id")
                        || bo.getColumn().equals("shop_name")
                        || bo.getColumn().equals("product_id"));
            } else if (bo.getTableView().equals("Product")) {
                assert (bo.getColumn().equals("Product")
                        || bo.getColumn().equals("product_name"));
            }
        }
    }

    @Test
    public void testSelectJoinSQL5() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("select id, age, id_card, r.name from tb_role as r inner join (select id,age,id_card from tb_user) as u on r.user_id = u.id;;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        List<SimpleSelectBO> simpleSelectBOList = sqlSimpleStatement.getSimpleSelectBOList();
        assert(simpleSelectBOList.size() == 7);
        for (SimpleSelectBO bo: simpleSelectBOList) {
            assert(bo.getColumn().equals("id") || bo.getColumn().equals("age")
                    || bo.getColumn().equals("id_card") || bo.getColumn().equals("name"));
            if (StringUtils.isEmpty(bo.getTableView())) {
                assert(!bo.getPossibleBelongDbTbl().isEmpty());
            } else {
                assert(bo.getTableView().equals("tb_user") || bo.getTableView().equals("tb_role"));
            }
        }
    }

    @Test
    public void testSelectAtSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("select @@max_allowed_packet;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
    }

    @Test
    public void testSelectVDollarSQL2() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("select b.name,sum(a.bytes/1000000) 总空间 from v$datafile a,v$tablespace b where a.ts#=b.ts# group by b.name;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLSelectStatementHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
    }
}
