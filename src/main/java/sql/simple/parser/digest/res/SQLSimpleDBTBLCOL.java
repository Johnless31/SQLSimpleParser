package sql.simple.parser.digest.res;

import lombok.Data;
import sql.simple.parser.digest.enums.InstructionType;

import java.util.ArrayList;
import java.util.List;
import sql.simple.parser.digest.enums.SubInstructionType;
@Data
public class SQLSimpleDBTBLCOL {
    private SubInstructionType subInstructionType = SubInstructionType.UNKNOWN;
    private SQLSimpleDatabase database = new SQLSimpleDatabase();
    private SQLSimpleTableView tableView = new SQLSimpleTableView();
    private List<SQLSimpleColumn> columns = new ArrayList<>();
}
