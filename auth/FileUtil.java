import java.io.*;
import java.util.*;


public class FileUtil {
    private static final String PATH = "table";


    public static void addAccess(Access access) {
        appendWriteData("access.txt", access.toString());
    }

    public static void writeType(Collection values) {
        writeData("type.txt", values);
    }

    public static void writeDomain(Collection values) {
        writeData("domain.txt", values);
    }

    public static void addUser(User user) {
        appendWriteData("user.txt", user.toString());
    }

    private static void appendWriteData(String fileName, String message) {
        try {
            File parentFile = new File(PATH);
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            String file = PATH + File.separator + fileName;
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(message + System.lineSeparator());
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void writeData(String fileName, Collection values) {
        try {
            File parentFile = new File(PATH);
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            String file = PATH + File.separator + fileName;

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (Object object : values) {
                bufferedWriter.write(object.toString() + System.lineSeparator());
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<Access> initAccess() {
        List<Access> accessList = new ArrayList<>();
        List<String> lines = FileUtil.readFileString("access.txt");
        if (lines != null) {
            for (String line : lines) {
                List<String> infos = StringUtil.parseList(line);
                Access access = new Access(infos.get(0), infos.get(1), infos.get(2));
                accessList.add( access);
            }
        }

        return accessList;
    }

    public static Map<String, Type> initType() {
        Map<String, Type> typeMap = new LinkedHashMap<>();
        List<String> lines = FileUtil.readFileString("type.txt");
        for (String line : lines) {
            List<String> infos = StringUtil.parseList(line);

            Type type = new Type(infos.remove(0));
            type.setObjects(infos);
            typeMap.put(type.getName(), type);
        }
        return typeMap;
    }

    public static Map<String, Domain> initDomain() {
        Map<String, Domain> domainMap = new LinkedHashMap<>();
        List<String> lines = FileUtil.readFileString("domain.txt");
        for (String line : lines) {
            List<String> infos = StringUtil.parseList(line);

            Domain domain = new Domain(infos.remove(0));
            domain.setUserNames(infos);
            domainMap.put(domain.getName(), domain);
        }
        return domainMap;
    }


    public static Map<String, User> initUser() {
        Map<String, User> userMap = new LinkedHashMap<>();
        List<String> lines = FileUtil.readFileString("user.txt");
        if (lines != null) {
            for (String line : lines) {
                List<String> infos = StringUtil.parseList(line);
                User user = new User(infos.get(0), infos.size() > 1 ? infos.get(1) : "");
                userMap.put(user.getUsername(), user);
            }
        }

        return userMap;
    }

    public static List<String> readFileString(String fileName) {
        List<String> lines = new ArrayList<>();
        try {
            String file = PATH + File.separator + fileName;
            File fileTmp = new File(file);
            if (!fileTmp.exists()) {
                return lines;
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }


}
