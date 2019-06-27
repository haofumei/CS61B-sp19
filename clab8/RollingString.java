import java.util.LinkedList;

/**
 * A String-like class that allows users to add and remove characters in the String
 * in constant time and have a constant-time hash function. Used for the Rabin-Karp
 * string-matching algorithm.
 */
class RollingString {

    /**
     * Number of total possible int values a character can take on.
     * DO NOT CHANGE THIS.
     */
    static final int UNIQUECHARS = 128;

    /**
     * The prime base that we are using as our mod space. Happens to be 61B. :)
     * DO NOT CHANGE THIS.
     */
    static final int PRIMEBASE = 6113;

    /**
     * Initializes a RollingString with a current value of String s.
     * s must be the same length as the maximum length.
     */

    private LinkedList<Character> rollingString = new LinkedList();
    private int hashCode = 0;
    private int mul = 1;
    private int size;

    public RollingString(String s, int length) {
        assert(s.length() == length);
        size = length;
        helpMul();
        for (int i = 0; i < length; i ++) {
            rollingString.addLast(s.charAt(i));
            hashCode = hashCode * UNIQUECHARS % PRIMEBASE;
            hashCode = (hashCode + (int)s.charAt(i)) % PRIMEBASE;
        }
    }

    /**
     * Adds a character to the back of the stored "string" and 
     * removes the first character of the "string". 
     * Should be a constant-time operation.
     */
    public void addChar(char c) {
        rollingString.addLast(c);
        char r = rollingString.remove();
        hashCode = (UNIQUECHARS * (hashCode - (int)r * mul) + (int)c);
        hashCode = Math.floorMod(hashCode, PRIMEBASE);
    }

    private void helpMul() {
        for (int i = 1; i < this.size; i ++) {
            mul = (mul * UNIQUECHARS) % PRIMEBASE;
        }
    }


    /**
     * Returns the "string" stored in this RollingString, i.e. materializes
     * the String. Should take linear time in the number of characters in
     * the string.
     */
    public String toString() {
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < length(); i ++) {
            strb.append(rollingString.get(i));
        }
        return strb.toString();
    }

    /**
     * Returns the fixed length of the stored "string".
     * Should be a constant-time operation.
     */
    public int length() {
        return this.size;
    }


    /**
     * Checks if two RollingStrings are equal.
     * Two RollingStrings are equal if they have the same characters in the same
     * order, i.e. their materialized strings are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        RollingString temp = (RollingString)o;
        if (this.length() != temp.length()) {
            return false;
        }
        for (int i = 0; i < this.length(); i ++) {
            if (this.rollingString.get(i) != temp.rollingString.get(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the hashcode of the stored "string".
     * Should take constant time.
     */
    @Override
    public int hashCode() {
        return hashCode;
    }
}
