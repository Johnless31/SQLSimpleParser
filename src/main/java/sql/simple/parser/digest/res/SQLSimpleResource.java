package sql.simple.parser.digest.res;

import lombok.Data;
import sql.simple.parser.digest.common.vlo.ColumnDefVLO;

import java.util.ArrayList;
import java.util.List;

@Data
public class SQLSimpleResource {

    private SQLSimpleDatabase database = new SQLSimpleDatabase();
    private SQLSimpleTableView tableView = new SQLSimpleTableView();
    private List<ColumnDefVLO> columns = new ArrayList<>();
    private SQLSimpleIndex index = new SQLSimpleIndex();
    private SQLSimpleProcedure procedure = new SQLSimpleProcedure();

}
