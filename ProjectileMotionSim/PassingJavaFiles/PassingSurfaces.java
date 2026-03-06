public class PassingSurfaces {

    /**
     * Predicts launch angle phi (radians) for FRC 2026 Fuel given distance and forward robot velocity. 
     * Polynomial fitted from the projectile physics simulator.
     * @param r  distance to target (m)
     * @param vf forward robot velocity (m/s)
     * @return phi in radians
     */
    public static double getPhi(double r, double vf) {
        return  1.2700320909990956
              - 2.1950841252e-02 * r
              + 4.1316569403e-02 * vf
              + 2.4456973713e-04 * r*r
              - 1.3195222170e-04 * r*vf
              + 1.1479691370e-04 * vf*vf
              - 6.3647835221e-06 * r*r*r
              + 5.4572784318e-06 * r*r*vf
              - 1.5015247030e-05 * r*vf*vf
              + 9.0038744510e-06 * vf*vf*vf
              + 8.6141147353e-09 * r*r*r*r
              + 2.4224031003e-07 * r*r*r*vf
              - 6.9059379859e-07 * r*r*vf*vf
              + 1.1517661429e-06 * r*vf*vf*vf
              - 5.3902260589e-07 * vf*vf*vf*vf;
    }

    /**
     * Predicts azimuthal angle theta (radians) for FRC 2026 Fuel given distance and lateral robot velocity.
     * Polynomial fitted from the projectile physics simulator.
     * @param r  distance to target (m)
     * @param vl lateral robot velocity (m/s)
     * @return theta in radians
     */
    public static double getTheta(double r, double vl) {
        return  2.2486187127460176e-07
              - 1.9457234863e-06 * r
              - 4.4385355811e-02 * vl
              + 5.0132113624e-07 * r*r
              + 7.8448174121e-07 * r*vl
              + 4.0422027854e-07 * vl*vl
              - 4.0711849850e-08 * r*r*r
              - 2.6022442514e-07 * r*r*vl
              + 7.3919961251e-09 * r*vl*vl
              - 1.4927450829e-05 * vl*vl*vl
              + 1.0405681739e-09 * r*r*r*r
              + 6.9668934267e-09 * r*r*r*vl
              - 1.0742264310e-09 * r*r*vl*vl
              - 2.9635137713e-08 * r*vl*vl*vl
              - 1.5941678796e-08 * vl*vl*vl*vl;
    }
}
