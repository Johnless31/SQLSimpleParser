package sql.simple.parser.digest.global;

import sql.simple.parser.digest.enums.AlterInstructionType;

import java.util.HashMap;
import java.util.Map;

public class StaticObjMap {

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

        // DDL Statement 1001~3000
        statementMap.put("SQLCreateDatabaseStatement",1001);
        statementMap.put("SQLCreateTableStatement",1002);
        statementMap.put("MySqlCreateTableStatement",1003);
        statementMap.put("OracleCreateTableStatement",1004);
        statementMap.put("SQLCreateIndexStatement",1005);
        statementMap.put("OracleCreateIndexStatement",1006);
        statementMap.put("SQLCreateViewStatement",1007);
        statementMap.put("SQLCreateProcedureStatement", 1008);
        statementMap.put("SQLDropDatabaseStatement", 1009);
        statementMap.put("SQLDropTableStatement", 1010);
        statementMap.put("SQLDropViewStatement", 1011);
        statementMap.put("SQLDropIndexStatement", 1012);
        statementMap.put("SQLAlterTableStatement",1013);
        statementMap.put("SQLAlterDatabaseStatement",1014);

        // DML Statement 3001~Max
        statementMap.put("SQLSelectStatement",3001);
    }

    public static Map<String, AlterInstructionType> alterItemMap = new HashMap<>();
    static {
        alterItemMap.put("SQLAlterTableAddClusteringKey", AlterInstructionType.ADD_CLUSTERING_KEY);
        alterItemMap.put("SQLAlterTableAddColumn", AlterInstructionType.ADD_COLUMN);
        alterItemMap.put("SQLAlterTableAddConstraint", AlterInstructionType.ADD_CONSTRAINT);
        alterItemMap.put("SQLAlterTableAddExtPartition", AlterInstructionType.ADD_EXTPARTITION);
        alterItemMap.put("SQLAlterTableAddIndex", AlterInstructionType.ADD_INDEX);
        alterItemMap.put("SQLAlterTableAddPartition", AlterInstructionType.ADD_PARTITION);
        alterItemMap.put("SQLAlterTableAddSupplemental", AlterInstructionType.ADD_SUPPLEMENTAL);
        alterItemMap.put("SQLAlterTableAlterColumn", AlterInstructionType.ALTER_COLUMN);
        alterItemMap.put("SQLAlterTableAlterIndex", AlterInstructionType.ALTER_INDEX);
        alterItemMap.put("SQLAlterTableAnalyzePartition", AlterInstructionType.ANALYZE_PARTITION);
        alterItemMap.put("SQLAlterTableArchive", AlterInstructionType.ARCHIVE);
        alterItemMap.put("SQLAlterTableArchivePartition", AlterInstructionType.ARCHIVE_PARTITION);
        alterItemMap.put("SQLAlterTableBlockSize", AlterInstructionType.BLOCK_SIZE);
        alterItemMap.put("SQLAlterTableChangeOwner", AlterInstructionType.CHANGE_OWNER);
        alterItemMap.put("SQLAlterTableCheckPartition", AlterInstructionType.CHECK_PARTITION);
        alterItemMap.put("SQLAlterTableCoalescePartition", AlterInstructionType.COALESCE_PARTITION);
        alterItemMap.put("SQLAlterTableCompression", AlterInstructionType.COMPRESSION);
        alterItemMap.put("SQLAlterTableConvertCharSet", AlterInstructionType.CONVERT_CHARSET);
        alterItemMap.put("SQLAlterTableDeleteByCondition", AlterInstructionType.DELETE_BY_CONDITION);
        alterItemMap.put("SQLAlterTableDisableConstraint", AlterInstructionType.DISABLE_CONSTRAINT);
        alterItemMap.put("SQLAlterTableDisableKeys", AlterInstructionType.DISABLE_KEYS);
        alterItemMap.put("SQLAlterTableDisableLifecycle", AlterInstructionType.DISABLE_LIFECYCLE);
        alterItemMap.put("SQLAlterTableDiscardPartition", AlterInstructionType.DISCARD_PARTITION);
        alterItemMap.put("SQLAlterTableDropClusteringKey", AlterInstructionType.DROP_CLUSTERING_KEY);
        alterItemMap.put("SQLAlterTableDropColumnItem", AlterInstructionType.DROP_COLUMN_ITEM);
        alterItemMap.put("SQLAlterTableDropConstraint", AlterInstructionType.DROP_CONSTRAINT);
        alterItemMap.put("SQLAlterTableDropExtPartition", AlterInstructionType.DROP_EXTPARTITION);
        alterItemMap.put("SQLAlterTableDropForeignKey", AlterInstructionType.DROP_FOREIGN_KEY);
        alterItemMap.put("SQLAlterTableDropIndex", AlterInstructionType.DROP_INDEX);
        alterItemMap.put("SQLAlterTableDropKey", AlterInstructionType.DROP_KEY);
        alterItemMap.put("SQLAlterTableDropPartition", AlterInstructionType.DROP_PARTITION);
        alterItemMap.put("SQLAlterTableDropPrimaryKey", AlterInstructionType.DROP_PRIMARY_KEY);
        alterItemMap.put("SQLAlterTableDropSubpartition", AlterInstructionType.DROP_SUBPARTITION);
        alterItemMap.put("SQLAlterTableEnableConstraint", AlterInstructionType.ENABLE_CONSTRAINT);
        alterItemMap.put("SQLAlterTableEnableKeys", AlterInstructionType.ENABLE_KEYS);
        alterItemMap.put("SQLAlterTableEnableLifecycle", AlterInstructionType.ENABLE_LIFECYCLE);
        alterItemMap.put("SQLAlterTableExchangePartition", AlterInstructionType.EXCHANGE_PARTITION);
        alterItemMap.put("SQLAlterTableImportPartition", AlterInstructionType.IMPORT_PARTITION);
        alterItemMap.put("SQLAlterTableMergePartition", AlterInstructionType.MERGE_PARTITION);
        alterItemMap.put("SQLAlterTableModifyClusteredBy", AlterInstructionType.MODIFY_CLUSTERED_BY);
        alterItemMap.put("SQLAlterTableOptimizePartition", AlterInstructionType.OPTIMIZE_PARTITION);
        alterItemMap.put("SQLAlterTablePartition", AlterInstructionType.PARTITION);
        alterItemMap.put("SQLAlterTablePartitionCount", AlterInstructionType.PARTITION_COUNT);
        alterItemMap.put("SQLAlterTablePartitionLifecycle", AlterInstructionType.PARTITION_LIFECYCLE);
        alterItemMap.put("SQLAlterTablePartitionSetProperties", AlterInstructionType.PARTITION_SET_PROPERTIES);
        alterItemMap.put("SQLAlterTableRebuildPartition", AlterInstructionType.REBUILD_PARTITION);
        alterItemMap.put("SQLAlterTableRecoverPartitions", AlterInstructionType.RECOVER_PARTITIONS);
        alterItemMap.put("SQLAlterTableRename", AlterInstructionType.RENAME);
        alterItemMap.put("SQLAlterTableRenameColumn", AlterInstructionType.RENAME_COLUMN);
        alterItemMap.put("SQLAlterTableRenameConstraint", AlterInstructionType.RENAME_CONSTRAINT);
        alterItemMap.put("SQLAlterTableRenameIndex", AlterInstructionType.RENAME_INDEX);
        alterItemMap.put("SQLAlterTableRenamePartition", AlterInstructionType.RENAME_PARTITION);
        alterItemMap.put("SQLAlterTableReOrganizePartition", AlterInstructionType.REORGANIZE_PARTITION);
        alterItemMap.put("SQLAlterTableRepairPartition", AlterInstructionType.REPAIR_PARTITION);
        alterItemMap.put("SQLAlterTableReplaceColumn", AlterInstructionType.REPLACE_COLUMN);
        alterItemMap.put("SQLAlterTableSetComment", AlterInstructionType.SET_COMMENT);
        alterItemMap.put("SQLAlterTableSetLifecycle", AlterInstructionType.SET_LIFECYCLE);
        alterItemMap.put("SQLAlterTableSetLocation", AlterInstructionType.SET_LOCATION);
        alterItemMap.put("SQLAlterTableSetOption", AlterInstructionType.SET_OPTION);
        alterItemMap.put("SQLAlterTableSubpartitionAvailablePartitionNum", AlterInstructionType.SUBPARTITION_AVAILABLE_PARTITION_NUM);
        alterItemMap.put("SQLAlterTableSubpartitionLifecycle", AlterInstructionType.SUBPARTITION_LIFECYCLE);
        alterItemMap.put("SQLAlterTableTouch", AlterInstructionType.TOUCH);
        alterItemMap.put("SQLAlterTableTruncatePartition", AlterInstructionType.TRUNCATE_PARTITION);
        alterItemMap.put("SQLAlterTableUnarchivePartition", AlterInstructionType.UNARCHIVE_PARTITION);
        alterItemMap.put("MysqlAlterTableAlterCheck", AlterInstructionType.ALTER_CHECK);
        alterItemMap.put("MySqlAlterTableAlterColumn", AlterInstructionType.ALTER_COLUMN);
        alterItemMap.put("MySqlAlterTableAlterFullTextIndex", AlterInstructionType.ALTER_FULL_TEXT_INDEX);
        alterItemMap.put("MySqlAlterTableChangeColumn", AlterInstructionType.CHANGE_COLUMN);
        alterItemMap.put("MySqlAlterTableDiscardTablespace", AlterInstructionType.DISCARD_TABLESPACE);
        alterItemMap.put("MySqlAlterTableForce", AlterInstructionType.FORCE);
        alterItemMap.put("MySqlAlterTableImportTablespace", AlterInstructionType.IMPORT_TABLESPACE);
        alterItemMap.put("MySqlAlterTableLock", AlterInstructionType.LOCK);
        alterItemMap.put("MySqlAlterTableModifyColumn", AlterInstructionType.MODIFY_COLUMN);
        alterItemMap.put("MySqlAlterTableOption", AlterInstructionType.OPTION);
        alterItemMap.put("MySqlAlterTableOrderBy", AlterInstructionType.ORDER_BY);
        alterItemMap.put("MySqlAlterTableValidation", AlterInstructionType.VALIDATION);
        alterItemMap.put("OdpsAlterTableSetChangeLogs", AlterInstructionType.SET_CHANGE_LOGS);
        alterItemMap.put("OdpsAlterTableSetFileFormat", AlterInstructionType.SET_FILE_FORMAT);
        alterItemMap.put("OracleAlterTableDropPartition", AlterInstructionType.DROP_PARTITION);
        alterItemMap.put("OracleAlterTableModify", AlterInstructionType.MODIFY);
        alterItemMap.put("OracleAlterTableMoveTablespace", AlterInstructionType.MOVE_TABLESPACE);
        alterItemMap.put("OracleAlterTableRowMovement", AlterInstructionType.ROW_MOVEMENT);
        alterItemMap.put("OracleAlterTableShrinkSpace", AlterInstructionType.SHRINK_SPACE);
        alterItemMap.put("OracleAlterTableSplitPartition", AlterInstructionType.SPLITPAR_TITION);
        alterItemMap.put("OracleAlterTableTruncatePartition", AlterInstructionType.TRUNCATE_PARTITION);

    }
}
