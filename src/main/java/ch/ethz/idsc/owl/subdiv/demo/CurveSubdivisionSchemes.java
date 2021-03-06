// code by jph
package ch.ethz.idsc.owl.subdiv.demo;

import java.util.function.Function;

import ch.ethz.idsc.owl.math.GeodesicInterface;
import ch.ethz.idsc.owl.subdiv.curve.BSpline1CurveSubdivision;
import ch.ethz.idsc.owl.subdiv.curve.BSpline2CurveSubdivision;
import ch.ethz.idsc.owl.subdiv.curve.BSpline3CurveSubdivision;
import ch.ethz.idsc.owl.subdiv.curve.BSpline4CurveSubdivision;
import ch.ethz.idsc.owl.subdiv.curve.BSpline5CurveSubdivision;
import ch.ethz.idsc.owl.subdiv.curve.BSpline6CurveSubdivision;
import ch.ethz.idsc.owl.subdiv.curve.CurveSubdivision;
import ch.ethz.idsc.owl.subdiv.curve.DodgsonSabinCurveSubdivision;
import ch.ethz.idsc.owl.subdiv.curve.DualC2FourPointCurveSubdivision;
import ch.ethz.idsc.owl.subdiv.curve.FarSixPointCurveSubdivision;
import ch.ethz.idsc.owl.subdiv.curve.FourPointCurveSubdivision;
import ch.ethz.idsc.owl.subdiv.curve.HormannSabinCurveSubdivision;
import ch.ethz.idsc.owl.subdiv.curve.SixPointCurveSubdivision;

public enum CurveSubdivisionSchemes {
  BSPLINE1(BSpline1CurveSubdivision::new), //
  BSPLINE2(BSpline2CurveSubdivision::new), //
  BSPLINE3(BSpline3CurveSubdivision::new), //
  BSPLINE4(BSpline4CurveSubdivision::of), //
  BSPLINE4S2(BSpline4CurveSubdivision::split2), //
  BSPLINE4S3(StaticHelper::split3), //
  BSPLINE5(BSpline5CurveSubdivision::new), //
  BSPLINE6(BSpline6CurveSubdivision::of), //
  DOBSEB(i -> DodgsonSabinCurveSubdivision.INSTANCE), //
  THREEPOINT(HormannSabinCurveSubdivision::of), //
  FOURPOINT(FourPointCurveSubdivision::new), //
  C2CUBIC(DualC2FourPointCurveSubdivision::cubic), //
  C2TIGHT(DualC2FourPointCurveSubdivision::tightest), //
  SIXPOINT(SixPointCurveSubdivision::new), //
  SIXFAR(FarSixPointCurveSubdivision::new), //
  ;
  public final Function<GeodesicInterface, CurveSubdivision> function;

  private CurveSubdivisionSchemes(Function<GeodesicInterface, CurveSubdivision> function) {
    this.function = function;
  }
}
