public class ShooterSurfacesPolynomial {

    /**
     * Predicts launch angle phi (radians) for FRC 2026 Fuel given distance and forward robot velocity.
     * Polynomial fitted from the projectile physics simulator.
     * @param r  distance to target (m)
     * @param vf forward robot velocity (m/s)
     * @return phi in radians
     */
    public static double getPhi(double r, double vf) {
        return  1.3319077394113694
              - 3.7917476957e-02 * r
              + 6.5559211155e-02 * vf
              + 9.1578107241e-04 * r*r
              - 3.3448392435e-04 * r*vf
              + 4.0252360850e-04 * vf*vf
              - 5.5561511125e-05 * r*r*r
              + 2.8140998785e-05 * r*r*vf
              - 8.0052327056e-05 * r*vf*vf
              + 3.8622151398e-05 * vf*vf*vf
              - 1.3708875933e-06 * r*r*r*r
              + 1.4983176335e-05 * r*r*r*vf
              - 2.3428958040e-05 * r*r*vf*vf
              + 1.8910264589e-05 * r*vf*vf*vf
              - 4.4641776123e-06 * vf*vf*vf*vf;
    }

    /**
     * Predicts azimuthal angle theta (radians) for FRC 2026 Fuel given distance and lateral robot velocity.
     * Polynomial fitted from the projectile physics simulator.
     * @param r  distance to target (m)
     * @param vl lateral robot velocity (m/s)
     * @return theta in radians
     */
    public static double getTheta(double r, double vl) {
        return -1.8722509645831825e-06
              - 7.3510622274e-06 * r
              - 6.7834581637e-02 * vl
              + 8.9871349979e-06 * r*r
              - 2.2214896752e-05 * r*vl
              + 4.8712473326e-07 * vl*vl
              - 2.6831898409e-06 * r*r*r
              + 1.4942965759e-05 * r*r*vl
              - 2.8428821571e-07 * r*vl*vl
              - 5.5109921945e-05 * vl*vl*vl
              + 2.3075944928e-07 * r*r*r*r
              - 2.4046620763e-06 * r*r*r*vl
              + 3.7418622874e-08 * r*r*vl*vl
              - 5.3380041757e-07 * r*vl*vl*vl
              - 2.2116720819e-09 * vl*vl*vl*vl;
    }
}
