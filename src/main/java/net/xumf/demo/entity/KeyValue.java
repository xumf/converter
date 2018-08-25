package main.java.net.xumf.demo.entity;

public class KeyValue {

    // 键
    private Long key;

    // 值
    private String value;

    @Override
    public String toString() {
        return "KeyValue{" +
                "key=" + key +
                ", value='" + value + '\'' +
                '}';
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}