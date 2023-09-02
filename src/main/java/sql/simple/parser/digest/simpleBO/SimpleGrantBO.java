package sql.simple.parser.digest.simpleBO;

import lombok.Data;
import sql.simple.parser.digest.common.vlo.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimpleGrantBO {
    private String database;
    private String tableView;
    private List<PrivilegeVLO> PrivilegeList = new ArrayList<>();
    private List<UserVLO> userList = new ArrayList<>();
    private boolean withGrantOption = false;
    public void transTableVLO(TableVLO vlo) {
        this.setDatabase(vlo.getBelongDatabase());
        this.setTableView(vlo.getTableView());
    }
}
