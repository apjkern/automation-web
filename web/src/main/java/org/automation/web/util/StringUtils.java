//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.automation.web.util;

import java.util.Arrays;

public final class StringUtils {
    private StringUtils() {
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean containsWhitespace(String str) {
        return str != null && str.codePoints().anyMatch(Character::isWhitespace);
    }

    public static boolean doesNotContainWhitespace(String str) {
        return !containsWhitespace(str);
    }

    public static boolean containsIsoControlCharacter(String str) {
        return str != null && str.codePoints().anyMatch(Character::isISOControl);
    }

    public static boolean doesNotContainIsoControlCharacter(String str) {
        return !containsIsoControlCharacter(str);
    }

    public static String nullSafeToString(Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj.getClass().isArray()) {
            if (obj.getClass().getComponentType().isPrimitive()) {
                if (obj instanceof boolean[]) {
                    return Arrays.toString((boolean[]) obj);
                }

                if (obj instanceof char[]) {
                    return Arrays.toString((char[]) obj);
                }

                if (obj instanceof short[]) {
                    return Arrays.toString((short[]) obj);
                }

                if (obj instanceof byte[]) {
                    return Arrays.toString((byte[]) obj);
                }

                if (obj instanceof int[]) {
                    return Arrays.toString((int[]) obj);
                }

                if (obj instanceof long[]) {
                    return Arrays.toString((long[]) obj);
                }

                if (obj instanceof float[]) {
                    return Arrays.toString((float[]) obj);
                }

                if (obj instanceof double[]) {
                    return Arrays.toString((double[]) obj);
                }
            }

            return Arrays.deepToString((Object[]) obj);
        } else {
            return obj.toString();
        }
    }
}
