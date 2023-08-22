package sql.simple.parser.digest.res;

import lombok.Data;
import sql.simple.parser.digest.enums.InstructionType;
import sql.simple.parser.digest.enums.SubInstructionType;

@Data
public class SQLSimpleInstruction {

    private InstructionType type = InstructionType.UNKNOWN;
}
