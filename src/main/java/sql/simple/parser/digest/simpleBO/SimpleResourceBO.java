package sql.simple.parser.digest.simpleBO;

import lombok.Data;
import sql.simple.parser.digest.common.vlo.ColumnDefVLO;
import sql.simple.parser.digest.common.vlo.TableVLO;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimpleResourceBO {

    private String database;
    private String tableView;
    private List<ColumnDefVLO> columns = new ArrayList<>();
    private SimpleIndexBO index = new SimpleIndexBO();
    private String procedure;

    public void transTableVLO(TableVLO vlo) {
        this.setDatabase(vlo.getBelongDatabase());
        this.setTableView(vlo.getTableView());
    }

}
