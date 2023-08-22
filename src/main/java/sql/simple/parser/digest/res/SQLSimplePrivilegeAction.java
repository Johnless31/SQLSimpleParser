package sql.simple.parser.digest.res;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class SQLSimplePrivilegeAction {

    private String action;
    private List<String> columns = new ArrayList<>();
    
}
