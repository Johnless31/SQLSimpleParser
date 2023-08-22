package sql.simple.parser.digest.res;

import lombok.Data;

@Data
public class SQLSimpleAttribute {

    private String key;
    private String value;
    private String name;

    public SQLSimpleAttribute(String key, String value, String name) {
        this.setKey(key);
        this.setValue(value);
        this.setName(name);
    }

    public SQLSimpleAttribute(String key, String value) {
        this.setKey(key);
        this.setValue(value);
    }
}
