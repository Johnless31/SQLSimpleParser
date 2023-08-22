package sql.simple.parser.digest.enums;

import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableLifecycle;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerRollbackStatement;

import java.util.HashMap;
import java.util.Map;

public class StatementInsMap {

    public static Map<String, Integer> statementMap = new HashMap<>();

    static {
        // DCL Statement 1~1000
        statementMap.put("SQLCommitStatement", 1);
        statementMap.put("SQLRollbackStatement", 2);
        statementMap.put("SQLServerRollbackStatement", 3);
        statementMap.put("SQLStartTransactionStatement", 4);
        statementMap.put("OscarStartTransactionStatement", 5);
        statementMap.put("PGStartTransactionStatement", 6);
        statementMap.put("MySqlSetTransactionStatement", 7);
        statementMap.put("SQLServerSetTransactionIsolationLevelStatement", 8);
        statementMap.put("OracleSetTransactionStatement", 9);
        statementMap.put("SQLSetStatement", 10);
        statementMap.put("SQLGrantStatement", 11);
        statementMap.put("SQLRevokeStatement",12);
        statementMap.put("SQLCreateDatabaseStatement",13);
        statementMap.put("SQLCreateTableStatement",14);
        statementMap.put("MySqlCreateTableStatement",15);
        statementMap.put("OracleCreateTableStatement",16);
        statementMap.put("SQLCreateIndexStatement",17);
        statementMap.put("OracleCreateIndexStatement",18);
        statementMap.put("SQLCreateViewStatement",19);
        statementMap.put("SQLCreateProcedureStatement", 20);
        statementMap.put("SQLDropDatabaseStatement", 21);
        statementMap.put("SQLDropTableStatement", 22);
        statementMap.put("SQLDropViewStatement", 23);
        statementMap.put("SQLDropIndexStatement", 24);
        statementMap.put("SQLAlterTableStatement",25);


        // DDL Statement 1001~3000

        // DML Statement 30001~Max
    }

    public static Map<String, SubInstructionType> alterItemMap = new HashMap<>();
    static {
        alterItemMap.put("SQLAlterTableAddClusteringKey", SubInstructionType.ADD_CLUSTERING_KEY);
        alterItemMap.put("SQLAlterTableAddColumn", SubInstructionType.ADD_COLUMN);
        alterItemMap.put("SQLAlterTableAddConstraint", SubInstructionType.ADD_CONSTRAINT);
        alterItemMap.put("SQLAlterTableAddExtPartition", SubInstructionType.ADD_EXTPARTITION);
        alterItemMap.put("SQLAlterTableAddIndex", SubInstructionType.ADD_INDEX);
        alterItemMap.put("SQLAlterTableAddPartition", SubInstructionType.ADD_PARTITION);
        alterItemMap.put("SQLAlterTableAddSupplemental", SubInstructionType.ADD_SUPPLEMENTAL);
        alterItemMap.put("SQLAlterTableAlterColumn", SubInstructionType.ALTER_COLUMN);
        alterItemMap.put("SQLAlterTableAlterIndex", SubInstructionType.ALTER_INDEX);
        alterItemMap.put("SQLAlterTableAnalyzePartition", SubInstructionType.ANALYZE_PARTITION);
        alterItemMap.put("SQLAlterTableArchive", SubInstructionType.ARCHIVE);
        alterItemMap.put("SQLAlterTableArchivePartition", SubInstructionType.ARCHIVE_PARTITION);
        alterItemMap.put("SQLAlterTableBlockSize", SubInstructionType.BLOCK_SIZE);
        alterItemMap.put("SQLAlterTableChangeOwner", SubInstructionType.CHANGE_OWNER);
        alterItemMap.put("SQLAlterTableCheckPartition", SubInstructionType.CHECK_PARTITION);
        alterItemMap.put("SQLAlterTableCoalescePartition", SubInstructionType.COALESCE_PARTITION);
        alterItemMap.put("SQLAlterTableCompression", SubInstructionType.COMPRESSION);
        alterItemMap.put("SQLAlterTableConvertCharSet", SubInstructionType.CONVERT_CHARSET);
        alterItemMap.put("SQLAlterTableDeleteByCondition", SubInstructionType.DELETE_BY_CONDITION);
        alterItemMap.put("SQLAlterTableDisableConstraint", SubInstructionType.DISABLE_CONSTRAINT);
        alterItemMap.put("SQLAlterTableDisableKeys", SubInstructionType.DISABLE_KEYS);
        alterItemMap.put("SQLAlterTableDisableLifecycle", SubInstructionType.DISABLE_LIFECYCLE);
        alterItemMap.put("SQLAlterTableDiscardPartition", SubInstructionType.DISCARD_PARTITION);
        alterItemMap.put("SQLAlterTableDropClusteringKey", SubInstructionType.DROP_CLUSTERING_KEY);
        alterItemMap.put("SQLAlterTableDropColumnItem", SubInstructionType.DROP_COLUMN_ITEM);
        alterItemMap.put("SQLAlterTableDropConstraint", SubInstructionType.DROP_CONSTRAINT);
        alterItemMap.put("SQLAlterTableDropExtPartition", SubInstructionType.DROP_EXTPARTITION);
        alterItemMap.put("SQLAlterTableDropForeignKey", SubInstructionType.DROP_FOREIGN_KEY);
        alterItemMap.put("SQLAlterTableDropIndex", SubInstructionType.DROP_INDEX);
        alterItemMap.put("SQLAlterTableDropKey", SubInstructionType.DROP_KEY);
        alterItemMap.put("SQLAlterTableDropPartition", SubInstructionType.DROP_PARTITION);
        alterItemMap.put("SQLAlterTableDropPrimaryKey", SubInstructionType.DROP_PRIMARY_KEY);
        alterItemMap.put("SQLAlterTableDropSubpartition", SubInstructionType.DROP_SUBPARTITION);
        alterItemMap.put("SQLAlterTableEnableConstraint", SubInstructionType.ENABLE_CONSTRAINT);
        alterItemMap.put("SQLAlterTableEnableKeys", SubInstructionType.ENABLE_KEYS);
        alterItemMap.put("SQLAlterTableEnableLifecycle", SubInstructionType.ENABLE_LIFECYCLE);
        alterItemMap.put("SQLAlterTableExchangePartition", SubInstructionType.EXCHANGE_PARTITION);
        alterItemMap.put("SQLAlterTableImportPartition", SubInstructionType.IMPORT_PARTITION);
        alterItemMap.put("SQLAlterTableMergePartition", SubInstructionType.MERGE_PARTITION);
        alterItemMap.put("SQLAlterTableModifyClusteredBy", SubInstructionType.MODIFY_CLUSTERED_BY);
        alterItemMap.put("SQLAlterTableOptimizePartition", SubInstructionType.OPTIMIZE_PARTITION);
        alterItemMap.put("SQLAlterTablePartition", SubInstructionType.PARTITION);
        alterItemMap.put("SQLAlterTablePartitionCount", SubInstructionType.PARTITION_COUNT);
        alterItemMap.put("SQLAlterTablePartitionLifecycle", SubInstructionType.PARTITION_LIFECYCLE);
        alterItemMap.put("SQLAlterTablePartitionSetProperties", SubInstructionType.PARTITION_SET_PROPERTIES);
        alterItemMap.put("SQLAlterTableRebuildPartition", SubInstructionType.REBUILD_PARTITION);
        alterItemMap.put("SQLAlterTableRecoverPartitions", SubInstructionType.RECOVER_PARTITIONS);
        alterItemMap.put("SQLAlterTableRename", SubInstructionType.RENAME);
        alterItemMap.put("SQLAlterTableRenameColumn", SubInstructionType.RENAME_COLUMN);
        alterItemMap.put("SQLAlterTableRenameConstraint", SubInstructionType.RENAME_CONSTRAINT);
        alterItemMap.put("SQLAlterTableRenameIndex", SubInstructionType.RENAME_INDEX);
        alterItemMap.put("SQLAlterTableRenamePartition", SubInstructionType.RENAME_PARTITION);
        alterItemMap.put("SQLAlterTableReOrganizePartition", SubInstructionType.REORGANIZE_PARTITION);
        alterItemMap.put("SQLAlterTableRepairPartition", SubInstructionType.REPAIR_PARTITION);
        alterItemMap.put("SQLAlterTableReplaceColumn", SubInstructionType.REPLACE_COLUMN);
        alterItemMap.put("SQLAlterTableSetComment", SubInstructionType.SET_COMMENT);
        alterItemMap.put("SQLAlterTableSetLifecycle", SubInstructionType.SET_LIFECYCLE);
        alterItemMap.put("SQLAlterTableSetLocation", SubInstructionType.SET_LOCATION);
        alterItemMap.put("SQLAlterTableSetOption", SubInstructionType.SET_OPTION);
        alterItemMap.put("SQLAlterTableSubpartitionAvailablePartitionNum", SubInstructionType.SUBPARTITION_AVAILABLE_PARTITION_NUM);
        alterItemMap.put("SQLAlterTableSubpartitionLifecycle", SubInstructionType.SUBPARTITION_LIFECYCLE);
        alterItemMap.put("SQLAlterTableTouch", SubInstructionType.TOUCH);
        alterItemMap.put("SQLAlterTableTruncatePartition", SubInstructionType.TRUNCATE_PARTITION);
        alterItemMap.put("SQLAlterTableUnarchivePartition", SubInstructionType.UNARCHIVE_PARTITION);
        alterItemMap.put("MysqlAlterTableAlterCheck", SubInstructionType.ALTER_CHECK);
        alterItemMap.put("MySqlAlterTableAlterColumn", SubInstructionType.ALTER_COLUMN);
        alterItemMap.put("MySqlAlterTableAlterFullTextIndex", SubInstructionType.ALTER_FULL_TEXT_INDEX);
        alterItemMap.put("MySqlAlterTableChangeColumn", SubInstructionType.CHANGE_COLUMN);
        alterItemMap.put("MySqlAlterTableDiscardTablespace", SubInstructionType.DISCARD_TABLESPACE);
        alterItemMap.put("MySqlAlterTableForce", SubInstructionType.FORCE);
        alterItemMap.put("MySqlAlterTableImportTablespace", SubInstructionType.IMPORT_TABLESPACE);
        alterItemMap.put("MySqlAlterTableLock", SubInstructionType.LOCK);
        alterItemMap.put("MySqlAlterTableModifyColumn", SubInstructionType.MODIFY_COLUMN);
        alterItemMap.put("MySqlAlterTableOption", SubInstructionType.OPTION);
        alterItemMap.put("MySqlAlterTableOrderBy", SubInstructionType.ORDER_BY);
        alterItemMap.put("MySqlAlterTableValidation", SubInstructionType.VALIDATION);
        alterItemMap.put("OdpsAlterTableSetChangeLogs", SubInstructionType.SET_CHANGE_LOGS);
        alterItemMap.put("OdpsAlterTableSetFileFormat", SubInstructionType.SET_FILE_FORMAT);
        alterItemMap.put("OracleAlterTableDropPartition", SubInstructionType.DROP_PARTITION);
        alterItemMap.put("OracleAlterTableModify", SubInstructionType.MODIFY);
        alterItemMap.put("OracleAlterTableMoveTablespace", SubInstructionType.MOVE_TABLESPACE);
        alterItemMap.put("OracleAlterTableRowMovement", SubInstructionType.ROW_MOVEMENT);
        alterItemMap.put("OracleAlterTableShrinkSpace", SubInstructionType.SHRINK_SPACE);
        alterItemMap.put("OracleAlterTableSplitPartition", SubInstructionType.SPLITPAR_TITION);
        alterItemMap.put("OracleAlterTableTruncatePartition", SubInstructionType.TRUNCATE_PARTITION);

    }
}
