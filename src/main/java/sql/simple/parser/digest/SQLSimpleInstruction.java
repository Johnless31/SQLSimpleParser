package sql.simple.parser.digest;

import lombok.Data;
import sql.simple.parser.digest.enums.InstructionType;

@Data
public class SQLSimpleInstruction {

    private InstructionType type = InstructionType.UNKNOWN;
}
