package StringToInteger;

public class solution {

    static int myAtoi(String s) {

        // Trim leading and trailing whitespace
        String Str = s.trim();

        // Check if the string is empty after trimming or invalid start
        if (Str.isEmpty()) {
            return 0;
        }

        // Use StringBuffer to construct the result
        StringBuffer result = new StringBuffer();

        // Check the first character
        if (Str.charAt(0) == '+' || Str.charAt(0) == '-') {
            result.append(Str.charAt(0));
            // Start from index 1 to skip the sign
            for (int i = 1; i < Str.length(); i++) {
                char ch = Str.charAt(i);
                if (Character.isDigit(ch)) {
                    result.append(ch);
                } else {
                    break; // Stop at the first non-digit character
                }
            }
        } else {
            // Process if the first character is a digit
            for (int i = 0; i < Str.length(); i++) {
                char ch = Str.charAt(i);
                if (Character.isDigit(ch)) {
                    result.append(ch);
                } else {
                    break; // Stop at the first non-digit character
                }
            }
        }

        // Check if the result is just a sign or empty, which are invalid
        if (result.toString().equals("-") || result.toString().equals("+") || result.length() == 0) {
            return 0;
        }

        try {
            // Safely parse the result as a long to handle larger values
            long value = Long.parseLong(result.toString());

            // Clamp the value within the 32-bit signed integer range
            if (value < Integer.MIN_VALUE) {
                return Integer.MIN_VALUE;
            } else if (value > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }

            return (int) value; // Cast to int if within range
        } catch (NumberFormatException e) {
            // Handle extremely large or malformed numbers
            if (result.charAt(0) == '-') {
                return Integer.MIN_VALUE;
            } else {
                return Integer.MAX_VALUE;
            }
        }
    }


}

