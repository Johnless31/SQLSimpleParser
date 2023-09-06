package sql.simple.parser.digest;

import lombok.Data;
import sql.simple.parser.digest.simpleBO.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class SQLSimpleStatement {

    private SQLSimpleInstruction instruction = new SQLSimpleInstruction();
    //----------------------不同的指令返回不同的对象------------------------//
    // CREATE_PROCEDURE,COMMIT,ROLLBACK,START_TRANSACTION
    // ONLY instruction:
    // SELECT
    private List<SimpleSelectBO> simpleSelectBOList = new ArrayList<>();
    // ROLLBACK, SET_TRANSACTION, SET
    private List<SimpleAttributeBO> attributes = new ArrayList<>();
    // GRANT, REVOKE
    private SimpleGrantBO simpleGrantBO = new SimpleGrantBO();
    // CREATE_DATABASE, CREATE_TABLE, CREATE_INDEX, DROP_DATABASE, DROP_INDEX
    private SimpleResourceBO simpleResourceBO = new SimpleResourceBO();
    // CREATE_VIEW
    private SimpleCreateViewBO simpleCreateViewBO = new SimpleCreateViewBO();
    // DROP_TABLE,DROP_VIEW
    private List<SimpleResourceBO> simpleDropTableViewBO = new ArrayList<>();
    // ALTER_TABLE
    private SimpleAlterBO simpleAlterBO = new SimpleAlterBO();

    private SimpleDeleteBO simpleDeleteBO = new SimpleDeleteBO();



}
