public class OffByN implements CharacterComparator{

    int N;
    public OffByN(int number) {
        N = number;
    }

    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == N;
    }
}
