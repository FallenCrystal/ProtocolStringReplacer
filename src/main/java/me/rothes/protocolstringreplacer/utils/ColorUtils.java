package me.rothes.protocolstringreplacer.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    private static String RGBRegex = "&#([0-9a-fA-F]{6})";
    private static Pattern pattern = Pattern.compile(RGBRegex);

    @Nonnull
    public static String getColored(@Nonnull String textToTranslate) {
        Validate.notNull(textToTranslate, "Text cannot be null");
        return translateRGB(ChatColor.translateAlternateColorCodes('&', textToTranslate));
    }

    @Nonnull
    public static String showColorCodes(@Nonnull String string) {
        Validate.notNull(string, "String cannot be null");
        return showColorCodes(string, false);
    }

    @Nonnull
    public static String showColorCodes(@Nonnull String string, boolean correctHex) {
        Validate.notNull(string, "String cannot be null");

        StringBuilder stringBuilder = new StringBuilder(string);
        for(int i = string.length() - 2; i >= 0; i--) {
            char Char = string.charAt(i);
            char nextChar = string.charAt(i + 1);
            if (Char == '§') {
                int indexOf = "x0123456789abcdefklmnor".indexOf(nextChar);
                if (indexOf != -1) {
                    stringBuilder.insert(i + 2, nextChar).insert(i + 2, '&');
                    if (correctHex && indexOf == 0 && stringBuilder.length() >= i + 28) {
                        String part = stringBuilder.substring(i, i + 28);
                        StringBuilder correct = new StringBuilder(28);
                        for (int j = 1, k = 0; j < 28; j += 4) {
                            char c = part.charAt(j);
                            correct.insert(k++, '§').insert(k++, c).append('&').append(c);
                        }
                        stringBuilder.replace(i, i + 28, correct.toString());
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    @Nonnull
    public static String restoreColored(@Nonnull String string) {
        Validate.notNull(string, "String cannot be null");

        char[] chars = string.toCharArray();
        for(int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == '§') {
                chars[i] = '&';
            }
        }
        return new String(chars);
    }

    @Nonnull
    public static String getTextColor(@Nonnull BaseComponent component) {
        Validate.notNull(component, "TextComponent cannot be null");

        StringBuilder colorBuilder = new StringBuilder();
        if (component.getColorRaw() != null) {
            colorBuilder.append(component.getColorRaw());
        }
        if (component.isBoldRaw() != null && component.isBoldRaw()) {
            colorBuilder.append("§l");
        }
        if (component.isItalicRaw() != null && component.isItalicRaw()) {
            colorBuilder.append("§o");
        }
        if (component.isObfuscatedRaw() != null && component.isObfuscatedRaw()) {
            colorBuilder.append("§m");
        }
        if (component.isUnderlinedRaw() != null && component.isUnderlinedRaw()) {
            colorBuilder.append("§n");
        }
        return colorBuilder.toString();

    }

    @Nonnull
    private static String translateRGB(@Nonnull String string) {
        Validate.notNull(string, "String cannot be null");

        String result = string;
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            StringBuilder stringBuilder = new StringBuilder(matcher.group(1));
            int i = stringBuilder.length();
            while (i > 0) {
                stringBuilder.insert(--i, '§');
            }
            stringBuilder.insert(0, "§x");
            result = result.replace(matcher.group(0), stringBuilder.toString());
        }
        return result;
    }

}
