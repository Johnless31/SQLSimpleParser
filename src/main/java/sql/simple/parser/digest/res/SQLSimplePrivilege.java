package sql.simple.parser.digest.res;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SQLSimplePrivilege {

    private List<SimplePrivilege> simplePrivileges = new ArrayList<>();
    private boolean withGrantOption = false;
}
