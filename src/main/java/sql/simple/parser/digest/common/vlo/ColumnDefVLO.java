package sql.simple.parser.digest.common.vlo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ColumnDefVLO {
    private String name;
    private String type;
    private List<String> Constrains = new ArrayList<>();
    private String defaultVal;

}
