package sql.simple.parser.digest.res;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import sql.simple.parser.digest.enums.AlterInstructionType;
import sql.simple.parser.digest.common.vlo.ColumnDefVLO;
@Data
public class SQLSimpleDBTBLCOL {
    private AlterInstructionType subInstructionType = AlterInstructionType.UNKNOWN;
    private SQLSimpleDatabase database = new SQLSimpleDatabase();
    private SQLSimpleTableView tableView = new SQLSimpleTableView();
    private List<ColumnDefVLO> columns = new ArrayList<>();
}
