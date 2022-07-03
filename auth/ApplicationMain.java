import java.util.List;
import java.util.Scanner;

public class ApplicationMain {
    public static void main(String[] args) {
        AuthUtil.init();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
//                System.out.print("> ");
                String input = scanner.nextLine().trim();
                if (input.startsWith("auth ")) {
                    List<String> params = StringUtil.parseList(input.substring(5));
                    if (params.size() == 0) {
                        throw new IllegalArgumentException();
                    }

                    String command = params.remove(0);
                    switch (command) {
                        case "AddUser":
                            addUser(params);
                            break;
                        case "Authenticate":
                            authenticate(params);
                            break;
                        case "SetDomain":
                            setDomain(params);
                            break;
                        case "DomainInfo":
                            domainInfo(params);
                            break;
                        case "SetType":
                            setType(params);
                            break;
                        case "TypeInfo":
                            typeInfo(params);
                            break;
                        case "AddAccess":
                            addAccess(params);
                            break;
                        case "CanAccess":
                            canAccess(params);
                            break;
                        default:
                            System.out.println("Error: invalid command " + command);
                            break;
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                System.out.println("Error: invalid command input");
            }

//            System.out.println();

        }
    }

    private static void canAccess(List<String> params) {
        if (!checkParams("CanAccess", params, 3)) {
            return;
        }
        String operation = params.size() > 0 ? params.get(0) : null;
        String username = params.size() > 1 ? params.get(1) : "";
        String object = params.size() > 2 ? params.get(2) : "";
        List<Access> accessList = AuthUtil.getAccess(operation);
        List<Domain> userDomains = AuthUtil.getUserDomains(username);
        List<Type> objectTypes = AuthUtil.getObjectTypes(object);
        for (Domain domain : userDomains) {
            for (Type type : objectTypes) {
                for (Access access : accessList) {
                    if (access.getDomainName().equals(domain.getName()) && access.getTypeName().equals(type.getName())) {
                        System.out.println("Success");
                        return;
                    }
                }
            }
        }

        System.out.println("Error: access denied");
    }

    private static void addAccess(List<String> params) {
        if (!checkParams("AddAccess", params, 3)) {
            return;
        }
        String operation = params.size() > 0 ? params.get(0) : null;
        String domainName = params.size() > 1 ? params.get(1) : "";
        String typeName = params.size() > 2 ? params.get(2) : "";
        if (operation == null) {
            System.out.println("Error: missing operation");
            return;
        } else if (domainName.trim().length() == 0) {
            System.out.println("Error: missing domain");
            return;
        } else if (typeName.trim().length() == 0) {
            System.out.println("Error: missing type");
            return;
        }

        AuthUtil.setAccess(operation, AuthUtil.getDomain(domainName, true), AuthUtil.getType(typeName, true));
        System.out.println("Success");
    }

    private static void typeInfo(List<String> params) {
        if (!checkParams("TypeInfo", params, 1)) {
            return;
        }
        String typeName = params.size() == 1 ? params.get(0) : "";
        if (typeName == null || typeName.trim().equals("")) {
            System.out.println("Error: missing type");
            return;
        }
        Type type = AuthUtil.getType(typeName, false);
        if (type != null) {
            for (String object : type.getObjects()) {
                System.out.println(object);
            }
        }
    }

    private static void setType(List<String> params) {
        if (!checkParams("SetType", params, 2)) {
            return;
        }

        String objectName = params.size() > 0 ? params.get(0) : null;
        String typeName = params.size() == 2 ? params.get(1) : "";
        if (objectName == null) {
            System.out.println("Error: missing objectName");
            return;
        }
        if (typeName == null || typeName.trim().length() == 0) {
            System.out.println("Error: missing type");
            return;
        }

        AuthUtil.setType(typeName, objectName);
    }

    private static void domainInfo(List<String> params) {
        if (!checkParams("DomainInfo", params, 1)) {
            return;
        }
        String domainName = params.size() == 1 ? params.get(0) : "";
        if (domainName == null || domainName.trim().equals("")) {
            System.out.println("Error: missing domain");
            return;
        }
        Domain domain = AuthUtil.getDomain(domainName, false);
        if (domain != null) {
            for (String username : domain.getUserNames()) {
                System.out.println(username);
            }
        }
    }

    private static void setDomain(List<String> params) {
        if (!checkParams("SetDomain", params, 2)) {
            return;
        }

        String username = params.size() > 0 ? params.get(0) : null;
        String domainName = params.size() == 2 ? params.get(1) : "";
        if (domainName == null || domainName.trim().equals("")) {
            System.out.println("Error: missing domain");
            return;
        }

        User user = AuthUtil.getUser(username);
        if (user == null) {
            System.out.println("Error: no such user");
            return;
        }

        AuthUtil.setDomain(domainName, username);
        System.out.println("Success");
    }

    private static void authenticate(List<String> params) {
        if (!checkParams("Authenticate", params, 2)) {
            return;
        }
        String username = params.size() > 0 ? params.get(0) : null;
        String password = params.size() == 2 ? params.get(1) : "";
        User user = AuthUtil.getUser(username);
        if (user == null) {
            System.out.println("Error: no such user");
            return;
        } else if (!user.getPassword().equals(password)) {
            System.out.println("Error: bad password");
            return;
        }

        System.out.println("Success");
    }


    private static void addUser(List<String> params) {
        if (!checkParams("AddUser", params, 2)) {
            return;
        }
        if (params.size() == 0) {
            System.out.println("Error: username missing");
            return;
        }

        String username = params.get(0);
        String password = params.size() == 2 ? params.get(1) : "";
        User user = AuthUtil.getUser(username);
        if (user != null) {
            System.out.println("Error: user exists");
            return;
        }

        AuthUtil.addUser(new User(username, password));
        System.out.println("Success");
    }

    private static boolean checkParams(String command, List<String> params, int maxParamCount) {
//        if (params.length < paramCount) {
//            System.out.println("Error: too less arguments for " + command);
//            return false;
//        } else

        if (params.size() > maxParamCount) {
            System.out.println("Error: too many arguments for " + command);
            return false;
        }
        return true;
    }



}
