package sql.simple.parser.digest.simpleBO;

import lombok.Data;

@Data
public class SimpleAttributeBO {

    private String key;
    private String value;
    private String name;

    public SimpleAttributeBO(String key, String value, String name) {
        this.setKey(key);
        this.setValue(value);
        this.setName(name);
    }

    public SimpleAttributeBO(String key, String value) {
        this.setKey(key);
        this.setValue(value);
    }
}
