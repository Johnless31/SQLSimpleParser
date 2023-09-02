package sql.simple.parser.digest.common.vlo;

import lombok.Data;

@Data
public class DbTblVLO {
    String database;
    String tableView;

    public DbTblVLO(TableVLO tableVLO) {
        this.transTableVLO(tableVLO);
    }
    public void transTableVLO(TableVLO tableVLO) {
        this.setDatabase(tableVLO.getBelongDatabase());
        this.setTableView(tableVLO.getTableView());
    }
}
