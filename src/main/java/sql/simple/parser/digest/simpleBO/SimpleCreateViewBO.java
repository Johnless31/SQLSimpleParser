package sql.simple.parser.digest.simpleBO;

import lombok.Data;
import sql.simple.parser.digest.common.vlo.ColumnDefVLO;
import sql.simple.parser.digest.common.vlo.TableVLO;
import sql.simple.parser.digest.res.SQLSimpleIndex;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimpleCreateViewBO {

    private String database;
    private String tableView;
    private List<ColumnDefVLO> columns = new ArrayList<>();
    private List<SimpleSelectBO> relatedQuery = new ArrayList<>();
    public void transTableVLO(TableVLO vlo) {
        this.setDatabase(vlo.getBelongDatabase());
        this.setTableView(vlo.getTableView());
    }

}
