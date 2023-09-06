package sql.simple.parser.digest.common.vlo;

import com.alibaba.druid.util.StringUtils;
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

    public boolean isValid() {
        return !(StringUtils.isEmpty(owner) && StringUtils.isEmpty(this.name));
    }
}
