package sql.simple.parser.digest;

import lombok.Data;
import sql.simple.parser.digest.enums.SubInstructionType;
import sql.simple.parser.digest.res.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class SQLSimpleStatement {

    private SQLSimpleInstruction instruction = new SQLSimpleInstruction();


    private List<SQLSimpleAttribute> attributes = new ArrayList<>();

    private SQLSimpleResource resource = new SQLSimpleResource();

    private SQLSimplePrivilege privilege = new SQLSimplePrivilege();

    private SQLSimpleUser user = new SQLSimpleUser();

    private List<SQLSimpleDBTBLCOL> RefMultiRes = new ArrayList<>();


}
