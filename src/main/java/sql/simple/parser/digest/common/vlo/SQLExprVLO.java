package sql.simple.parser.digest.common.vlo;

import lombok.Data;

import java.util.Map;

@Data
public class SQLExprVLO {
    private String owner;
    private String name;

    public void transOwner(Map<String, String> alias2Tbl) {
        if (alias2Tbl.containsKey(owner)) {
            this.setOwner(alias2Tbl.get(owner));
        }
    }
}
