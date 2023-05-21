package ca.pandaaa.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    // Applies the format used threw the whole plugin (returns a formatted string) //
    public static String applyFormat(String message) {
        // Replaces the double arrows automatically //
        message = message.replace(">>", "»").replace("<<", "«");

        // Changes the hex color code to set the colors //
        Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]){6}");
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            ChatColor hexColor = ChatColor.of(matcher.group().substring(1));
            String before = message.substring(0, matcher.start());
            String after = message.substring(matcher.end());
            message = before + hexColor + after;
            matcher = hexPattern.matcher(message);
        }

        // Returns the message with the correct colors and formats //
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // Checks if a string is an integer //
    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }
}
