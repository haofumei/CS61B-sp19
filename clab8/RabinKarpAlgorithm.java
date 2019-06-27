public class RabinKarpAlgorithm {


    /**
     * This algorithm returns the starting index of the matching substring.
     * This method will return -1 if no matching substring is found, or if the input is invalid.
     */
    public static int rabinKarp(String input, String pattern) {
        if (input.length() < pattern.length()) {
            return -1;
        }

        int length = pattern.length();
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            strb.append(input.charAt(i));
        }
        String subInput = strb.toString();

        RollingString ptRoll = new RollingString(pattern, length);
        RollingString inRoll = new RollingString(subInput, length);

        if (ptRoll.hashCode() == inRoll.hashCode() && ptRoll.equals(inRoll)) {
            return 0;
        }
        for (int i = length; i < input.length(); i++) {
            inRoll.addChar(input.charAt(i));
            if (ptRoll.hashCode() == inRoll.hashCode() && ptRoll.equals(inRoll)) {
                return i - length + 1;
            }
        }
        return -1;
    }
}
