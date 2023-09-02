package sql.simple.parser.digest;

import lombok.Data;
import sql.simple.parser.digest.res.*;
import sql.simple.parser.digest.simpleBO.SimpleAttributeBO;
import sql.simple.parser.digest.simpleBO.SimpleGrantBO;
import sql.simple.parser.digest.simpleBO.SimpleResourceBO;
import sql.simple.parser.digest.simpleBO.SimpleSelectBO;

import java.util.ArrayList;
import java.util.List;
import sql.simple.parser.digest.simpleBO.SimpleCreateViewBO;

@Data
public class SQLSimpleStatement {

    private SQLSimpleInstruction instruction = new SQLSimpleInstruction();



    private SQLSimpleResource resource = new SQLSimpleResource();

    private SQLSimplePrivilege privilege = new SQLSimplePrivilege();

    private SQLSimpleUser user = new SQLSimpleUser();

    private List<SQLSimpleDBTBLCOL> RefMultiRes = new ArrayList<>();

    //----------------------不同的指令返回不同的对象------------------------//
    // SELECT
    private List<SimpleSelectBO> simpleSelectBOList = new ArrayList<>();
    // ROLLBACK, SET_TRANSACTION, SET
    private List<SimpleAttributeBO> attributes = new ArrayList<>();
    // GRANT, REVOKE
    private SimpleGrantBO simpleGrantBO = new SimpleGrantBO();
    // CREATE_DATABASE, CREATE_TABLE, CREATE_INDEX
    private SimpleResourceBO simpleResourceBO = new SimpleResourceBO();
    // CREATE_VIEW
    private SimpleCreateViewBO simpleCreateViewBO = new SimpleCreateViewBO();

}
