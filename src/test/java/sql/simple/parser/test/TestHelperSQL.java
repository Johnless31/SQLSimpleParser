package sql.simple.parser.test;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import sql.simple.parser.digest.SQLSimpleStatement;
import sql.simple.parser.digest.handler.DigestHandler;


@Slf4j
public class TestHelperSQL {

    @Test
    public void testDescSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLDescribeStatement
        // com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("desc stu id;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());

    }

    @Test
    public void testDescSQL1() {
        // com.alibaba.druid.sql.ast.statement.SQLDescribeStatement
        // com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("desc select * from stu;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());

    }

    @Test
    public void testDescSQL2() {
        // com.alibaba.druid.sql.ast.statement.SQLExplainStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("explain select * from stu;", DbType.sqlserver);
        log.info("解析sql:{}", sqlStatement.getClass().getName());

    }

    @Test
    public void testDescSQL3() {
        // com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLExplainStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("explain stu;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());

    }

    @Test
    public void testShowSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("show tables;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testPurgeSQL() {
        // com.alibaba.druid.sql.ast.statement.SQLPurgeLogsStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("purge binary logs to 'mysql-bin3306.000003';", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }

    @Test
    public void testFlushSQL() {
        // com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlFlushStatement
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement("flush binary logs;", DbType.mysql);
        log.info("解析sql:{}", sqlStatement.getClass().getName());
    }


}
