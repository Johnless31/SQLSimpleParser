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
import sql.simple.parser.digest.handler.DigestHandler;
import sql.simple.parser.digest.simpleBO.SimpleGrantBO;
import sql.simple.parser.digest.simpleBO.SimpleResourceBO;
import sql.simple.parser.digest.simpleBO.SimpleSelectBO;

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
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLGrantHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        SimpleGrantBO simpleGrantBO = sqlSimpleStatement.getSimpleGrantBO();
        log.info("解析sql:{}", simpleGrantBO);
    }

    @Test
    public void testGrant2SQL() {
        // com.alibaba.druid.sql.ast.statement.SQLGrantStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("grant select(user_id,username), update(username) on smp.users to mo_user@'%' identified by '123345';", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLGrantHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        SimpleGrantBO simpleGrantBO = sqlSimpleStatement.getSimpleGrantBO();
        log.info("解析sql:{}", simpleGrantBO);
    }

    @Test
    public void testGrant3SQL() {
        // com.alibaba.druid.sql.ast.statement.SQLGrantStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("grant create temporary tables on testdb.* to developer@'192.168.0.%';", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLGrantHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        SimpleGrantBO simpleGrantBO = sqlSimpleStatement.getSimpleGrantBO();
        log.info("解析sql:{}", simpleGrantBO);
    }

    @Test
    public void testsqlServerGrantSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLGrantStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("grant select(user_id, username),insert on student to john identified by '123345' with grant option;", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLGrantHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        SimpleGrantBO simpleGrantBO = sqlSimpleStatement.getSimpleGrantBO();
        log.info("解析sql:{}", simpleGrantBO);
    }

    @Test
    public void testRevokeSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLRevokeStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("revoke select(user_id, username), insert on db.tb from 'userA'@'1.1.1.1';", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLRevokeHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        SimpleGrantBO simpleGrantBO = sqlSimpleStatement.getSimpleGrantBO();
        log.info("解析sql:{}", simpleGrantBO);
    }

    @Test
    public void testRevoke2SQL() {
        // com.alibaba.druid.sql.ast.statement.SQLRevokeStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("revoke select(user_id, username), insert on db.tb from 'userA';", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLRevokeHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        SimpleGrantBO simpleGrantBO = sqlSimpleStatement.getSimpleGrantBO();
        log.info("解析sql:{}", simpleGrantBO);
    }

    @Test
    public void testCreateDbSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("create database test;", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLCreateDatabaseHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        SimpleResourceBO simpleGrantBO = sqlSimpleStatement.getSimpleResourceBO();
        log.info("解析sql:{}", simpleGrantBO);
    }

    @Test
    public void testCreateDb2SQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("CREATE DATABASE if not exists test;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLCreateDatabaseHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement);
        SimpleResourceBO simpleGrantBO = sqlSimpleStatement.getSimpleResourceBO();
        log.info("解析sql:{}", simpleGrantBO);
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
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLCreateTableHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getSimpleResourceBO());

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
        DigestHandler.SQLCreateTableHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getSimpleResourceBO());
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
        DigestHandler.SQLCreateTableHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getSimpleResourceBO());
    }

    @Test
    public void testCreateIndexSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement
        // com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateIndexStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("CREATE UNIQUE INDEX idx_age ON person (id,age);", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLCreateIndexHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getClass().getName());
    }

    @Test
    public void testCreateViewSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("create view actor_name_view(fname, lname) as select first_name AS first_name_v, last_name as last_name_v from actor;", DbType.oracle);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLCreateViewHandler(sqlSimpleStatement, sqlStatement);
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
        DigestHandler.SQLDropTableHandler(sqlSimpleStatement, sqlStatement);
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
        DigestHandler.SQLDropIndexHandler(sqlSimpleStatement, sqlStatement);
        log.info("解析sql:{}", sqlSimpleStatement.getClass().getName());
    }

    @Test
    public void testDropIndexSQL2() {
        // com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("drop index test on kk.tt;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
        SQLSimpleStatement sqlSimpleStatement = new SQLSimpleStatement();
        DigestHandler.SQLDropIndexHandler(sqlSimpleStatement, sqlStatement);
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
    public void testArrayListS() {
        List<SQLColumnDefinition> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SQLColumnDefinition ii = new SQLColumnDefinition();
            list.add(ii);
        }
        log.info("size:{}", list.size());
    }

    private String testStr(String cc) {
        String str = cc;
        log.info("str=:{}", str);
        return str;
    }
    @Test
    public void testString() {
        String cc = "kkk";
        String db = testStr(cc);
        log.info("str=:{}", db);
    }
}
