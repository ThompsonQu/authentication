import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AuthUtil {

    private static Map<String, User> userMap = new LinkedHashMap<>();
    private static Map<String, Domain> domainMap = new LinkedHashMap<>();
    private static Map<String, Type> typeMap = new LinkedHashMap<>();
    private static List<Access> accessList = new ArrayList<>();

    public static void init() {
        userMap = FileUtil.initUser();
        domainMap= FileUtil.initDomain();
        typeMap =FileUtil. initType();
        accessList= FileUtil. initAccess();
    }


    public static User getUser(String username) {
        if (username == null) {
            return null;
        }
        return userMap.get(username);
    }

    public static void addUser(User user) {
        userMap.put(user.getUsername(), user);
        FileUtil.addUser(user);
    }

    public static Domain getDomain(String domainName, boolean init) {
        Domain domain = domainMap.get(domainName);
        if (domain == null && init) {
            domain = new Domain(domainName);
            domainMap.put(domainName, domain);
        }
        return domain;
    }

    public static void setDomain(String domainName, String username) {
        Domain domain = getDomain(domainName, true);
        if (!domain.getUserNames().contains(username)) {
            domain.getUserNames().add(username);
            FileUtil.writeDomain(domainMap.values());
        }
    }

    public static Type getType(String typeName, boolean init) {
        Type type = typeMap.get(typeName);
        if (type == null && init) {
            type = new Type(typeName);
            typeMap.put(typeName, type);
        }
        return type;
    }

    public static void setType(String typeName, String objectName) {
        Type type = getType(typeName, true);
        if (!type.getObjects().contains(objectName)) {
            type.getObjects().add(objectName);
            FileUtil.writeType(typeMap.values());
        }
    }

    public static void setAccess(String operation, Domain domain, Type type) {
        for (Access access : accessList) {
            if (access.getName().equals(operation)) {
                if (access.getDomainName().equals(domain.getName()) && access.getTypeName().equals(type.getName())) {
                    return;
                }
            }
        }
        Access access = new Access(operation, domain.getName(), type.getName());
        accessList.add(access);
        FileUtil.addAccess(access);

    }

    public static List<Access> getAccess(String operation) {
        List<Access> list = new ArrayList<>();
        if (operation == null) {
            return list;
        }
        for (Access access : accessList) {
            if (access.getName().equals(operation)) {
                list.add(access);
            }
        }
        return list;
    }

    public static List<Domain> getUserDomains(String username) {
        List<Domain> list = new ArrayList<>();
        for (Domain domain : domainMap.values()) {
            if (domain.getUserNames().contains(username)) {
                list.add(domain);
            }
        }
        return list;
    }

    public static List<Type> getObjectTypes(String object) {
        List<Type> list = new ArrayList<>();
        for (Type type : typeMap.values()) {
            if (type.getObjects().contains(object)) {
                list.add(type);
            }
        }
        return list;
    }
}
