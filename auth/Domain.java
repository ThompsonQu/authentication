import java.util.ArrayList;
import java.util.List;

public class Domain {
    private String name;
    private List<String> userNames;

    public Domain(String name) {
        this.name = name;
        this.userNames = new ArrayList<>();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        StringUtil.appendString(stringBuffer, name);
        StringUtil.appendString(stringBuffer, userNames);
        return stringBuffer.toString().trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }
}
