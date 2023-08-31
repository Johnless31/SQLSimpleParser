package sql.simple.parser.test;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLDDLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import sql.simple.parser.digest.SQLSimpleStatement;
import sql.simple.parser.digest.StatementDigest;
import sql.simple.parser.digest.handler.DCLDigestHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SqlUtilsTestDSID {

    @Test
    public void test() {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements("select 1 from test;", DbType.mysql);
        log.info("解析sql:{}", sqlStatements);
        sqlStatements = SQLUtils.parseStatements("use xxx;select 1 from test;explain select 1 from test", DbType.mysql);
        log.info("解析sql:{}", sqlStatements);
        sqlStatements = SQLUtils.parseStatements("select 1 from1 test", DbType.mysql);
        log.info("解析sql:{}", sqlStatements);
    }

    @Test
    public void testParseSQL() {
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("create database test;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        log.info("解析sql:{}", sqlStatement instanceof SQLDDLStatement);
        log.info("解析sql:{}", sqlStatement instanceof SQLSelectStatement);

    }

    @Test
    public void testParseStartTransactionSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLStartTransactionStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("start transaction;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testParseBeginTransactionSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLStartTransactionStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("begin transaction;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testParseCommitSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCommitStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("commit work;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sstatement = StatementDigest.SQLSimpleStatementParse(sqlStatement);
        log.info("解析sql:{}", sstatement.getInstruction().getType().name());
    }

    @Test
    public void testParseRollbackSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLRollbackStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("rollback TRANSACTION xxx;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());

        SQLStatement sqlStatement2 = SQLUtils.parseSingleStatement("rollback to  xxx;", DbType.oracle);
        log.info("解析sql:{}", sqlStatement2.getClass().getName());

        SQLStatement sqlStatement3 = SQLUtils.parseSingleStatement("rollback to savepoint xxx;", DbType.oracle);
        log.info("解析sql:{}", sqlStatement3.getClass().getName());
    }

    @Test
    public void testSetTransactionIsolationLevelSQL() {
        // com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerSetTransactionIsolationLevelStatement
        // com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetTransactionStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testOracleSetTransactionSQL() {
        // com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSetTransactionStatement
        // SET TRANSACTION READ ONLY NAME 'XXX';
        // SET TRANSACTION READ WRITE NAME 'XXX';
        // SET TRANSACTION ISOLATION LEVEL  READ COMMITTED NAME 'XXX'; 不支持
        // SET TRANSACTION USE ROLLBACK SEGMENT 'ABC' NAME 'XXX'; 不支持
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SET TRANSACTION READ WRITE NAME 'XXX';", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testSetAutoCommitSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLSetStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("set ABC=on;", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testImplicitTransactionsSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLSetStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("set implicit_transactions off;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testSqlserverSetSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLSetStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SET XXX ON;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testMysqlSetSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLSetStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SET SESSION query_cache_type = OFF;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testGrantSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLGrantStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("grant alter,select(user_id, username),insert on testdb.* to 'john'@'%' with grant option;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testGrant2SQL() {
        // com.alibaba.druid.sql.ast.statement.SQLGrantStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("grant select(user_id,username), update(username) on smp.users to mo_user@'%' identified by '123345';", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testGrant3SQL() {
        // com.alibaba.druid.sql.ast.statement.SQLGrantStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("grant create temporary tables on testdb.* to developer@'192.168.0.%';", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testsqlServerGrantSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLGrantStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("grant select(user_id, username),insert on student to john identified by '123345' with grant option;", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testRevokeSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLRevokeStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("revoke select(user_id, username), insert on db.tb from 'userA'@'1.1.1.1';", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testRevoke2SQL() {
        // com.alibaba.druid.sql.ast.statement.SQLRevokeStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("revoke select(user_id, username), insert on db.tb from 'userA';", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testCreateDbSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("create database test;", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testCreateDb2SQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("CREATE DATABASE if not exists test;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testCreateTBSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement
        // com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement
        // com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("CREATE TABLE Students" +
                "(" +
                "ROLL_NO int(3) PRIMARY KEY," +
                "ID_NO varcha(30) UNIQUE," +
                "NAME varchar(20) NOT NULL," +
                "SUBJECT varchar(20) NOT NULL DEFAULT ''," +
                "U_ID int(3) REFERENCES tb_user(id)" +
                ");", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());

        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testCreateTB2SQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement
        // com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement
        // com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("CREATE TABLE Students" +
                "(" +
                "ROLL_NO int(3)," +
                "ID_NO varcha(30) UNIQUE," +
                "NAME varchar(20) NOT NULL," +
                "SUBJECT varchar(20) NOT NULL DEFAULT 'cs'," +
                "U_ID int(3) NOT NULL," +
                "PRIMARY KEY(ROLL_NO)," +
                "FOREIGN KEY (U_ID) REFERENCES tb_user(id)" +
                ");", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DCLDigestHandler.SQLCreateTableHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getClass().getName());
    }

    @Test
    public void testCreateTB3SQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement
        // com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement
        // com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("CREATE TABLE Students" +
                "(" +
                "ROLL_NO int(3)," +
                "ID_NO varcha(30) UNIQUE," +
                "NAME varchar(20) NOT NULL," +
                "SUBJECT varchar(20) NOT NULL DEFAULT ''," +
                "U_ID int(3) NOT NULL," +
                "PRIMARY KEY(ROLL_NO)," +
                "UNIQUE (name)," +
                "CONSTRAINT `FK_ID_ST` FOREIGN KEY (U_ID) REFERENCES tb_user(id)" +
                ");", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DCLDigestHandler.SQLCreateTableHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getClass().getName());
    }

    @Test
    public void testCreateIndexSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement
        // com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateIndexStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("CREATE UNIQUE INDEX idx_age ON person (id,age);", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DCLDigestHandler.SQLCreateIndexHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getClass().getName());
    }

    @Test
    public void testCreateViewSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("create view actor_name_view(fname, lname) as select first_name AS first_name_v, last_name as last_name_v from actor;", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DCLDigestHandler.SQLCreateViewHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getClass().getName());
    }

    @Test
    public void testCreateProcedureSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCreateProcedureStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("CREATE PROCEDURE GetScoreByStu (IN name VARCHAR(30)) BEGIN SELECT student_score FROM tb_students_score WHERE student_name=name; END;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testCreateProcedure2SQL() {
        // 未支持
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("CREATE PROCEDURE SP_STUDENT " +
                "(" +
                "SID IN VARCHAR2," +
                "SNAME IN VARCHAR2 " +
                ")" +
                "AS " + "BEGIN INSERT INTO STUDENT VALUES(SID, SNAME); " +
                "END SP_STUDENT;", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        log.info("解析sql:{}", sqlStatement.getClass().getSimpleName());
    }

    @Test
    public void testDropDBSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("drop database if exists test;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testDropTableSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLDropTableStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("Drop table if exists db.u_tb, db2.stu;", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DCLDigestHandler.SQLDropTableHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getClass().getName());
    }

    @Test
    public void testDropViewSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLDropViewStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("drop view if exists db.test, tt.test;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());

    }

    @Test
    public void testDropIndexSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("drop index if exists tt.test;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DCLDigestHandler.SQLDropIndexHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getClass().getName());
    }

    @Test
    public void testDropIndexSQL2() {
        // com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("drop index test on kk.tt;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DCLDigestHandler.SQLDropIndexHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getClass().getName());
    }

    @Test
    public void testAlterDatabaseSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("alter database `testdb` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testAlterTableSQL2() {
        // com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("alter table db.tb drop column col, " +
                "add col2 int(3), " +
                "change col3 col4 int(3), " +
                "drop primary key," +
                "add CONSTRAINT `FK_ID_ST` FOREIGN KEY (U_ID) REFERENCES tb_user (id)," +
                "add UNIQUE id_idx (id), " +
                "modify col5 int(6)," +
                "ORDER BY col7 ASC, " +
                "DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        /*SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DCLDigestHandler.SQLDropIndexHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getClass().getName());*/
    }

    @Test
    public void testAlterTableSQL3() {
        // com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("ALTER TABLE db.tb DROP COLUMN col1,col2;", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testSelectSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT Tid,Tname FROM Teachers;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectSQL1() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT TC.Tid, TC.Tname FROM Teachers AS TC;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectSQL2() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT Tid+100 AS \"新编号\", Tname AS \"身份\" FROM db.Teachers;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectSQL3() {
        //com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT * FROM Teachers;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectSQL4() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT 'xxx' AS string, 38 AS number, Tid,Tname FROM Teachers;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectSQL5() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT DISTINCT Tid,Tname FROM Teachers;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectSQL6() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        // count, sum, avg, max, min
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT MAX(DISTINCT sale_price), MIN(purchase_price) FROM Teachers;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectSQL7() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT  product_type, cnt_product FROM (SELECT product_type, COUNT(*) AS cnt_product FROM Product GROUP BY product_type) AS ProductSum;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
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
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectSQL9() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT product_id,\n" +
                " product_name,\n" +
                " sale_price,\n" +
                " (SELECT AVG(sale_price)\n" +
                " FROM Product) AS avg_price\n" +
                " FROM Product;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectSQL10() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT product_id, product_name, sale_price\n" +
                " FROM Product\n" +
                " WHERE sale_price > (SELECT AVG(sale_price)\n" +
                " FROM Product);", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectSQL11() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT str1, str2,\n" +
                " str1 || str2 AS str_concat\n" +
                " FROM SampleStr;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
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
        SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        log.info("解析select list:{}", selectQueryBlock.getSelectList());
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
        //SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        //log.info("解析select list:{}", selectQueryBlock.getSelectList());
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
        //SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        //log.info("解析select list:{}", selectQueryBlock.getSelectList());
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
        //SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        //log.info("解析select list:{}", selectQueryBlock.getSelectList());
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
        //SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        //log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectJoinSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT SP.shop_id, SP.shop_name, SP.product_id, P.product_name, " +
                "P.sale_price\n" +
                " FROM ShopProduct AS SP INNER JOIN Product AS P \n" +
                " ON SP.product_id = P.product_id;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        //SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        //log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectJoinSQL1() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT SP.shop_id, SP.shop_name, SP.product_id, P.product_name,\n" +
                "P.sale_price\n" +
                " FROM ShopProduct AS SP RIGHT OUTER JOIN Product AS P \n" +
                " ON SP.product_id = P.product_id;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        //SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        //log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectJoinSQL2() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT SP.shop_id, SP.shop_name, SP.product_id, P.product_name, \n" +
                "P.sale_price\n" +
                " FROM Product AS P LEFT OUTER JOIN ShopProduct AS SP \n" +
                " ON SP.product_id = P.product_id;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        //SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        //log.info("解析select list:{}", selectQueryBlock.getSelectList());
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
        //SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        //log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testSelectJoinSQL4() {
        // com.alibaba.druid.sql.ast.statement.SQLSelectStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("SELECT SP.shop_id, SP.shop_name, SP.product_id, P.product_name\n" +
                " FROM ShopProduct AS SP CROSS JOIN Product AS P;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        //SQLSelectQueryBlock selectQueryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();
        //log.info("解析select list:{}", selectQueryBlock.getSelectList());
    }

    @Test
    public void testArrayListS() {
        List<SQLColumnDefinition> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SQLColumnDefinition ii = new SQLColumnDefinition();
            list.add(ii);
        }
        log.info("size:{}", list.size());
    }
}
