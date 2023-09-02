package sql.simple.parser.digest.simpleBO;

import lombok.Data;
import sql.simple.parser.digest.common.vlo.ColumnVLO;
import sql.simple.parser.digest.common.vlo.DbTblVLO;
import sql.simple.parser.digest.common.vlo.TableVLO;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimpleSelectBO {

    private String database;
    private String tableView;
    private String column;
    private List<DbTblVLO> possibleBelongDbTbl = new ArrayList<>();

    public void transTableVLO(TableVLO dbTblColVLO) {
        this.setDatabase(dbTblColVLO.getBelongDatabase());
        this.setTableView(dbTblColVLO.getTableView());
    }

    public void transColumnVLO(ColumnVLO columnVLO) {
        this.setDatabase(columnVLO.getBelongDatabase());
        this.setTableView(columnVLO.getBelongTableView());
        this.setColumn(columnVLO.getColumn());
        possibleBelongDbTbl = columnVLO.getPossibleBelongDbTbl();
    }

}
