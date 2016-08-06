package none.test;

/**
 * Created by ekin4 on 06/08/2016.
 */
public class Color {

    int A = 0, totRGBA = 0;
    float H = 0, S = 0, V = 0;

    public Color(int R_, int G_, int B_) {
        A = 255;
        H = R_;
        S = G_;
        V = B_;
        H /= 255;
        S /= 255;
        V /= 255;
        float varMin = ((H < V ? H : V) < S ? (H < V ? H : V) : S),
              varMax = ((H > V ? H : V) > S ? (H > V ? H : V) : S),
              delMax = varMax - varMin;
        if (delMax == 0) {
            H = 0;
            S = 0;
        } else {
            float delR = (((varMax - H) / 6)) / delMax + (float) 0.5,
                  delG = (((varMax - S) / 6)) / delMax + (float) 0.5,
                  delB = (((varMax - V) / 6)) / delMax + (float) 0.5,
                  varR = H;
            if (varR == varMax) {
                H = delB - delG;
            } else if (S == varMax) {
                H = ((float) 1.0 / 3) + delR - delB;
            } else if (V == varMax) {
                H = ((float) 2.0 / 3) + delG - delR;
            }
        }
        V = varMax;
        S = delMax / varMax;
        totRGBA = ((((255 & 255) << 24) | (R_ & 255) << 16) | (G_ & 255) << 8) | (B_ & 255);
    }
    public Color(int R_, int G_, int B_, int A_) {
        A = A_;
        H = R_;
        S = G_;
        V = B_;
        H /= 255;
        S /= 255;
        V /= 255;
        float varMin = ((H < V ? H : V) < S ? (H < V ? H : V) : S),
                varMax = ((H > V ? H : V) > S ? (H > V ? H : V) : S),
                delMax = varMax - varMin;
        if (delMax == 0) {
            H = 0;
            S = 0;
        } else {
            float delR = (((varMax - H) / 6)) / delMax + (float) 0.5,
                    delG = (((varMax - S) / 6)) / delMax + (float) 0.5,
                    delB = (((varMax - V) / 6)) / delMax + (float) 0.5,
                    varR = H;
            if (varR == varMax) {
                H = delB - delG;
            } else if (S == varMax) {
                H = ((float) 1.0 / 3) + delR - delB;
            } else if (V == varMax) {
                H = ((float) 2.0 / 3) + delG - delR;
            }
        }
        V = varMax;
        S = delMax / varMax;
        totRGBA = ((((A_ & 255) << 24) | (R_ & 255) << 16) | (G_ & 255) << 8) | (B_ & 255);
    }

    public int getRGB() {
        return totRGBA;
    }
}
