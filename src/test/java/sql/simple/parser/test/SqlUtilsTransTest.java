package sql.simple.parser.test;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.support.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import sql.simple.parser.digest.SQLSimpleStatement;
import sql.simple.parser.digest.StatementDigest;
import sql.simple.parser.digest.common.utils.ExtraUtils;
import sql.simple.parser.digest.common.vlo.ColumnDefVLO;
import sql.simple.parser.digest.common.vlo.TableVLO;
import sql.simple.parser.digest.handler.DigestHandler;
import sql.simple.parser.digest.simpleBO.SimpleGrantBO;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SqlUtilsTransTest {

    @Test
    public void testCreateTB0SQL() {
        String sql = "CREATE TABLE `dfs_metrics_work_order_hourly` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `work_order_number` varchar(255) NOT NULL COMMENT '工单号',\n" +
                "  `produce_quantity` double(11,2) DEFAULT NULL COMMENT '产出数量',\n" +
                "  `inspection_quantity` double(11,2) DEFAULT NULL COMMENT '检测数量',\n" +
                "  `unqualified_quantity` double(11,2) DEFAULT NULL COMMENT '不良数量',\n" +
                "  `input_quantity` double(11,2) DEFAULT NULL COMMENT '投入数量',\n" +
                "  `making_quantity` double(11,2) DEFAULT NULL COMMENT '在制数量',\n" +
                "  `qualified_rate` double(11,2) DEFAULT NULL COMMENT '合格率',\n" +
                "  `is_sum` tinyint(4) DEFAULT NULL COMMENT '是否累加之前的',\n" +
                "  `record_time` datetime DEFAULT NULL COMMENT '记录时间(取整点) 统计包含整个小时内的',\n" +
                "  `time` datetime DEFAULT NULL COMMENT '最新统计时间',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `idx_record_time` (`record_time`) USING BTREE,\n" +
                "  KEY `idx_is_sum` (`is_sum`),\n" +
                "  KEY `idx_work_order_number` (`work_order_number`,`record_time`,`is_sum`) USING BTREE\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=447093 DEFAULT CHARSET=utf8mb4 COMMENT='生产工单小时监控';";
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement(sql, DbType.mysql);
        MySqlCreateTableStatement realStatement = (MySqlCreateTableStatement) sqlStatement;
        Map<String, Object> tableInfo = new HashMap<>();
        try {
            SQLExprTableSource table = realStatement.getTableSource();
            if (null != table) {
                tableInfo.put("\"table_name\"", "\"" + table.getName().toString().replace("`", "") +  "\"");
            }
            if (null != realStatement.getComment()) {
                tableInfo.put("\"table_desc\"", "\"" + realStatement.getComment().toString().replace("'", "") + "\"");
            } else {
                tableInfo.put("\"table_desc\"", "\"\"");
            }
            List<Object> cols = new ArrayList<>();
            for (SQLTableElement tableElement: realStatement.getTableElementList()) {
                if (tableElement instanceof SQLColumnDefinition columnDefinition) { // 列的定义表达式
                    Map<String, Object> colInfo = new HashMap<>();
                    colInfo.put("\"name\"", "\"" + columnDefinition.getName().toString().replace("`", "") + "\"");
                    colInfo.put("\"type\"", "\"" + columnDefinition.getDataType().toString() + "\"");
                    if (null != columnDefinition.getComment()) {
                        colInfo.put("\"desc\"", "\"" + columnDefinition.getComment().toString().replace("'", "") + "\"");
                    } else {
                        colInfo.put("\"desc\"", "\"\"");
                    }

                    cols.add(colInfo);
                }
            }
            if (!cols.isEmpty()) {
                tableInfo.put("\"colums\"", cols);
            }
            log.info("msg:\n{}\n", tableInfo.toString().replace("=", ":"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
