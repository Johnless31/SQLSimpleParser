package sql.simple.parser.digest.res;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SQLSimpleColumn {


    private String name;
    private String type;
    private List<String> Constrains = new ArrayList<>();
    private String defaultVal;

}
