public class Palindrome {

    /** return a Deque where the characters appear in the same order as in the String */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> D = new LinkedListDeque<>();
        int length = word.length();
        for (int i = 0; i < length; i++) {
            D.addLast(word.charAt(i));
        }
        return D;
    }

    /** return true if word is a palindrome */
    public boolean isPalindrome(String word) {
        if (word == null) {
            return false;
        } else if (word.length() == 0 || word.length() == 1) {
            return true;
        } else {
            int length = word.length();
            char first = word.charAt(0);
            char last = word.charAt(length - 1);
            if (first == last) {
                String newword = word.substring(1,length - 1);
                return isPalindrome(newword);
            } else {
                return false;
            }
        }
    }

    /*private boolean help(String word) {
        if (word.length() == 0 || word.length() == 1) {
            return true;
        }
    }*/

    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word == null) {
            return false;
        } else if (word.length() == 0 || word.length() == 1) {
            return true;
        } else {
            int length = word.length();
            char first = word.charAt(0);
            char last = word.charAt(length - 1);
            if (cc.equalChars(first, last)) {
                String newword = word.substring(1,length - 1);
                return isPalindrome(newword, cc);
            } else {
                return false;
            }
        }
    }
}
