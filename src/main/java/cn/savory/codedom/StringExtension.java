package cn.savory.codedom;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class StringExtension {

    /// <summary>
    /// 小写的名称，由Name计算而来
    /// 示例：Student -> STUDENT
    /// 示例：StudentScore -> STUDENT_SCORE
    /// </summary>
    public static String toUpperCaseUnderLine(String name) {
        return toLowerCaseUnderLine(name).toUpperCase();
    }

    /// <summary>
    /// 小写的名称，由Name计算而来
    /// 示例：Student -> student
    /// 示例：StudentScore -> student_score
    /// </summary>
    public static String toLowerCaseUnderLine(String name) {
        List<String> items = ToItems(name);
        return String.join("_", items);
    }

    /// <summary>
    /// 小写的名称，由Name计算而来
    /// 示例：Student -> student
    /// 示例：StudentScore -> student-score
    /// </summary>
    public static String toLowerCaseBreakLine(String name) {
        List<String> items = ToItems(name);

        return String.join("-", items);
    }

    /// <summary>
    /// 拆分字符串
    /// </summary>
    /// <param name="name"></param>
    /// <returns></returns>
    private static List<String> ToItems(String name) {
        List<String> items = Lists.newArrayList();
        StringBuilder item = new StringBuilder();

        Character last = null;

        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);

            if (ch >= 'A' && ch <= 'Z' && last != null && (last < 'A' || last > 'Z')) {
                items.add(item.toString());
                item = new StringBuilder();

                item.append((char) (ch + 32));
            } else if (ch >= '0' && ch <= '9' && last != null && (last < '0' || last > '9')) {
                items.add(item.toString());
                item = new StringBuilder();

                item.append(ch);
            } else if (ch == '_' || ch == '-') {
                items.add(item.toString());
                item = new StringBuilder();
            } else {
                item.append(((Character) ch).toString().toLowerCase());
            }

            last = ch;
        }

        items.add(item.toString());

        items = items.stream().filter(v -> v.length() > 0).collect(Collectors.toList());

        return items;
    }

    /// <summary>
    /// 将数据库表名转换成实体名称
    /// 示例：student_score -> StudentScore
    /// </summary>
    public static String ToUpperCamelCase(String name) {
        boolean toUpper = true;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char current = name.charAt(i);
            if (current == '_') {
                toUpper = true;
                continue;
            }

            if (toUpper && current >= 'a' && current <= 'z') {
                builder.append((char) (current - 32));
                toUpper = false;
            } else {
                builder.append(current);
                toUpper = false;
            }
        }

        return builder.toString();
    }

    /// <summary>
    /// 由Name计算而来
    /// 示例：name, studentScore
    /// </summary>
    public static String ToLowerCamelCase(String name) {
        return ((Character)name.charAt(0)).toString().toLowerCase() + name.substring(1);
    }
}
