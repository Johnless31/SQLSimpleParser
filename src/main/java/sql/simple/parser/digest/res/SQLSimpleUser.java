package sql.simple.parser.digest.res;

import lombok.Data;

@Data
public class SQLSimpleUser {

    private String username;
    private String host;
    private String identifyBy;
}
