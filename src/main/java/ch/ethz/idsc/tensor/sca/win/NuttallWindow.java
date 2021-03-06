// code by jph
package ch.ethz.idsc.tensor.sca.win;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/NuttallWindow.html">NuttallWindow</a> */
public class NuttallWindow extends AbstractWindowFunction {
  private static final Scalar A0 = RationalScalar.of(88942, 250000);
  private static final Scalar A1 = RationalScalar.of(121849, 250000);
  private static final Scalar A2 = RationalScalar.of(36058, 250000);
  private static final Scalar A3 = RationalScalar.of(3151, 250000);
  // ---
  private static final WindowFunction FUNCTION = new NuttallWindow();

  public static WindowFunction function() {
    return FUNCTION;
  }

  // ---
  private NuttallWindow() {
  }

  @Override
  public Scalar protected_apply(Scalar x) {
    return StaticHelper.deg3(A0, A1, A2, A3, x);
  }
}
