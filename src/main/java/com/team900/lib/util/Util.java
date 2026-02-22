package com.team900.lib.util;

import com.team900.frc2026.Constants;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/** Contains basic functions that are used often. */
public class Util {
    public static final double kEpsilon = 1e-12;

    /** Prevent this class from being instantiated. */
    private Util() {}

    /** Limits the given input to the given magnitude. */
    public static double limit(double v, double maxMagnitude) {
        return limit(v, -maxMagnitude, maxMagnitude);
    }

    public static double limit(double v, double min, double max) {
        return Math.min(max, Math.max(min, v));
    }

    public static int limit(int v, int min, int max) {
        return Math.min(max, Math.max(min, v));
    }

    public static boolean inRange(double v, double maxMagnitude) {
        return inRange(v, -maxMagnitude, maxMagnitude);
    }

    /** Checks if the given input is within the range (min, max), both exclusive. */
    public static boolean inRange(double v, double min, double max) {
        return v > min && v < max;
    }

    public static double interpolate(double a, double b, double x) {
        x = limit(x, 0.0, 1.0);
        return a + (b - a) * x;
    }

    public static String joinStrings(final String delim, final List<?> strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); ++i) {
            sb.append(strings.get(i).toString());
            if (i < strings.size() - 1) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }

    public static boolean epsilonEquals(double a, double b) {
        return epsilonEquals(a, b, kEpsilon);
    }

    public static boolean epsilonEquals(int a, int b, int epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }

    public static boolean allCloseTo(final List<Double> list, double value, double epsilon) {
        boolean result = true;
        for (Double value_in : list) {
            result &= epsilonEquals(value_in, value, epsilon);
        }
        return result;
    }

    public static double handleDeadband(double value, double deadband) {
        deadband = Math.abs(deadband);
        if (deadband == 1) {
            return 0;
        }
        double scaledValue = (value + (value < 0 ? deadband : -deadband)) / (1 - deadband);
        return (Math.abs(value) > Math.abs(deadband)) ? scaledValue : 0;
    }

     /** Flips an x coordinate to the correct side of the field based on the current alliance color. */
  public static double applyX(double xCoordinate) {
    if (shouldFlip()) {
      return Constants.kFieldLengthMeters - xCoordinate;
    } else {
      return xCoordinate;
    }
  }

  public static double applyY(double yCoordinate) {
    if (shouldFlip()) {
      return Constants.kFieldWidthMeters - yCoordinate;
    } else {
      return yCoordinate;
    }
  }

  /** Flips a translation to the correct side of the field based on the current alliance color. */
  public static Translation2d apply(Translation2d translation) {
    if (shouldFlip()) {
      return new Translation2d(applyX(translation.getX()), applyY(translation.getY()));
    } else {
      return translation;
    }
  }

  /** Flips a rotation based on the current alliance color. */
  public static Rotation2d apply(Rotation2d rotation) {
    if (shouldFlip()) {
      return new Rotation2d(-rotation.getCos(), -rotation.getSin());
    } else {
      return rotation;
    }
  }

  /** Flips a pose to the correct side of the field based on the current alliance color. */
  public static Pose2d apply(Pose2d pose) {
    if (shouldFlip()) {
      return new Pose2d(apply(pose.getTranslation()), apply(pose.getRotation()));
    } else {
      return pose;
    }
  }

  public static Translation3d apply(Translation3d translation3d) {
    if (shouldFlip()) {
      return new Translation3d(
          applyX(translation3d.getX()), applyY(translation3d.getY()), translation3d.getZ());
    } else {
      return translation3d;
    }
  }

  public static boolean shouldFlip() {
    return DriverStation.getAlliance().isPresent()
        && DriverStation.getAlliance().get() == Alliance.Red;
  }

    public static <T> Supplier<T> memoizeByIteration(IntSupplier iteration, Supplier<T> delegate) {
        AtomicReference<T> value = new AtomicReference<>();
        AtomicInteger last_iteration = new AtomicInteger(-1);
        return () -> {
            int last = last_iteration.get();
            int now = iteration.getAsInt();
            if (last != now) {
                value.updateAndGet((cur) -> null);
            }
            T val = value.get();
            if (val == null) {
                val = value.updateAndGet(cur -> cur == null ? delegate.get() : cur);
                last_iteration.set(now);
            }
            return val;
        };
    }
}
