package sql.simple.parser.digest.common.vlo;

import lombok.Data;

@Data
public class TableVLO {
    private String tableView;
    private String alias;
    private String belongDatabase;
}
