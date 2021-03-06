// code by jph
package ch.ethz.idsc.owl.subdiv.curve;

import ch.ethz.idsc.owl.math.GeodesicInterface;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

/** cubic B-spline */
public enum BSpline4CurveSubdivision {
  ;
  private static final Scalar P1_16 = RationalScalar.of(1, 16);
  private static final Scalar P1_11 = RationalScalar.of(1, 11);
  private static final Scalar P2_3 = RationalScalar.of(2, 3);
  private static final Scalar P11_16 = RationalScalar.of(11, 16);
  private static final Scalar P5 = RealScalar.of(5);
  private static final Scalar P16 = RealScalar.of(16);

  /** geodesic split suggested by Dyn/Sharon 2014 p.16 who also show
   * that the scheme with this split has a contractivity factor of mu = 5/6
   * 
   * @param geodesicInterface
   * @return */
  public static CurveSubdivision of(GeodesicInterface geodesicInterface) {
    return new Split2LoDual3PointCurveSubdivision(geodesicInterface, P2_3, P1_16);
  }

  /** @param geodesicInterface
   * @return */
  public static CurveSubdivision split2(GeodesicInterface geodesicInterface) {
    return new Split2HiDual3PointCurveSubdivision(geodesicInterface, P11_16, P1_11);
  }

  /** geodesic split suggested by Hakenberg 2018
   * 
   * @param geodesicInterface
   * @return */
  public static CurveSubdivision split3(GeodesicInterface geodesicInterface) {
    return split3(geodesicInterface, RationalScalar.HALF);
  }

  /** function generalizes all variants above with {1/16, 1/2, 11/16}
   * 
   * @param geodesicInterface
   * @param value in the interval [1/16, 11/16] give the best results
   * @return */
  public static CurveSubdivision split3(GeodesicInterface geodesicInterface, Scalar value) {
    return new Split3Dual3PointCurveSubdivision(geodesicInterface, //
        P5.divide(P16.multiply(value.subtract(RealScalar.ONE))).add(RealScalar.ONE), //
        value.multiply(P16).reciprocal(), value);
  }
}
