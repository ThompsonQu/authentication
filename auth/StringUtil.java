import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public static List<String> parseList(String input) {
        List<String> list = new ArrayList<>();

        String doubleQuotationParam = "";
        boolean startWithDoubleQuotations = false;
        while (input.length() > 0) {
            int index = input.indexOf(" ");
            String next = "";
            if (index == -1) {
                next = input;
                input = "";
            } else {
                next = input.substring(0, index);
                input = input.substring(index + 1);
            }

            if (!startWithDoubleQuotations) {
                if (next.startsWith("\"")) {
                    if (next.endsWith("\"")) {
                        list.add(next.substring(1, next.length() - 1));
                    } else {
                        doubleQuotationParam = next.substring(1);
                        startWithDoubleQuotations = true;
                    }
                } else {
                    if (next.length() > 0) {
                        list.add(next);
                    }
                }
            } else {
                doubleQuotationParam += " " + next;
                if (doubleQuotationParam.endsWith("\"")) {
                    list.add(doubleQuotationParam.substring(0, doubleQuotationParam.length() - 1));
                    doubleQuotationParam = "";
                    startWithDoubleQuotations = false;
                }
            }
        }
        return list;
    }

    public static void appendString(StringBuffer stringBuffer, String string) {
        if (string.contains(" ")) {
            stringBuffer.append("\"").append(string).append("\"").append(" ");
        } else {
            stringBuffer.append(string).append(" ");
        }
    }

    public static void appendString(StringBuffer stringBuffer, List<String> strings) {
        for (String string : strings) {
            appendString(stringBuffer, string);
        }
    }

    public static String trimToNull(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.length() == 0) {
            return null;
        }
        return str;
    }
}
