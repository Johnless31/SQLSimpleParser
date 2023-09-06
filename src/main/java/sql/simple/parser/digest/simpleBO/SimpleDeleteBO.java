package sql.simple.parser.digest.simpleBO;

import lombok.Data;
import sql.simple.parser.digest.common.vlo.ConditionVLO;
import sql.simple.parser.digest.common.vlo.TableVLO;
import sql.simple.parser.digest.enums.AlterInstructionType;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimpleDeleteBO {

    private String database;
    private String tableView;
    private List<ConditionVLO> conditionVLOS = new ArrayList<>();

    public void transTableVLO(TableVLO vlo) {
        this.setDatabase(vlo.getBelongDatabase());
        this.setTableView(vlo.getTableView());
    }

}
