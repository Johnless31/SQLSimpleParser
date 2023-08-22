package sql.simple.parser.digest.res;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SQLSimpleResource {

    private SQLSimpleDatabase database = new SQLSimpleDatabase();
    private SQLSimpleTableView tableView = new SQLSimpleTableView();
    private List<SQLSimpleColumn> columns = new ArrayList<>();
    private SQLSimpleIndex index = new SQLSimpleIndex();
    private SQLSimpleProcedure procedure = new SQLSimpleProcedure();
    private SQLSimpleFunction function = new SQLSimpleFunction();

}
