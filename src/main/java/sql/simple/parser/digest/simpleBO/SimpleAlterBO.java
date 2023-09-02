package sql.simple.parser.digest.simpleBO;

import lombok.Data;
import sql.simple.parser.digest.common.vlo.TableVLO;
import sql.simple.parser.digest.enums.AlterInstructionType;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimpleAlterBO {

    private String database;
    private String tableView;
    private List<AlterInstructionType> alterInstructionList = new ArrayList<>();

    public void transTableVLO(TableVLO vlo) {
        this.setDatabase(vlo.getBelongDatabase());
        this.setTableView(vlo.getTableView());
    }

}
