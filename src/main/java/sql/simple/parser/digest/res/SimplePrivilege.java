package sql.simple.parser.digest.res;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class SimplePrivilege {

    private List<SQLSimplePrivilegeAction> actions = new ArrayList<>();

}
