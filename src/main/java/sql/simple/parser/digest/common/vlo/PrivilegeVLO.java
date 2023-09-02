package sql.simple.parser.digest.common.vlo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PrivilegeVLO {
    String action;
    List<String> clumns = new ArrayList<>();
}
