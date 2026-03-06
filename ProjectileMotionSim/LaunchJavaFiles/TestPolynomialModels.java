import java.io.IOException;

public class TestPolynomialModels {
    public static void main(String[] args) throws IOException {
        String shooterPhiModelPath =
                new String("ProjectileMotionSim/ShooterSurfaces/models/phi_shooter_model.json");
        PolynomialModel shooterPhiModel = PolynomialModel.load(shooterPhiModelPath);
        for (int i = 0; i < 22; i++) {
            System.out.println(shooterPhiModel.evaluate((i*0.5-5.5), 0));
        }
    }
}
