package sql.simple.parser.digest.common.vlo;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class ColumnVLO {
    private String column;
    private String alias;
    private String belongTableView;
    private String belongDatabase;
    private List<DbTblVLO> possibleBelongDbTbl = new ArrayList<>();

    public void transSQLExprVLO(SQLExprVLO vlo) {
        this.setColumn(vlo.getName());
        this.setBelongTableView(vlo.getOwner());
    }

    public void transTableVLO(TableVLO tableVLO) {
        this.setBelongDatabase(tableVLO.getBelongDatabase());
        this.setBelongTableView(tableVLO.getTableView());
    }
}
