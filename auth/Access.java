
public class Access {
    private String name;
    private String domainName;
    private String typeName;

    public Access(String name, String domainName, String typeName) {
        this.name = name;
        this.domainName = domainName;
        this.typeName = typeName;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        StringUtil.appendString(stringBuffer, name);
        StringUtil.appendString(stringBuffer, domainName);
        StringUtil.appendString(stringBuffer, typeName);
        return stringBuffer.toString().trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
