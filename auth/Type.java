import java.util.ArrayList;
import java.util.List;

public class Type {
    private String name;
    private List<String> objects;

    public Type(String name) {
        this.name = name;
        this.objects = new ArrayList<>();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        StringUtil.appendString(stringBuffer, name);
        StringUtil.appendString(stringBuffer, objects);
        return stringBuffer.toString().trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getObjects() {
        return objects;
    }

    public void setObjects(List<String> objects) {
        this.objects = objects;
    }
}
