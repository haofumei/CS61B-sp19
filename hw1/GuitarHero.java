import es.datastructur.synthesizer.GuitarString;
import java.lang.Math;
public class GuitarHero {

    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] Strings = new GuitarString[37];
        for (int i = 0; i < 37; i++) {
            Strings[i] = new GuitarString(440 * Math.pow(2, (i - 24) / 12));
        }
        GuitarString s = Strings[0];
        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int i = keyboard.indexOf(key);
                if (i != -1) {
                    s = Strings[i];
                    s.pluck();
                }
            }
            //the programs must outside the if loop
            double sample = s.sample();
            StdAudio.play(sample);
            s.tic();
        }
    }
}
