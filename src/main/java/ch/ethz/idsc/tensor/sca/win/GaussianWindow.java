// code by jph
package ch.ethz.idsc.tensor.sca.win;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.red.Times;
import ch.ethz.idsc.tensor.sca.Exp;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/GaussianWindow.html">GaussianWindow</a> */
public class GaussianWindow extends AbstractWindowFunction {
  private static final Scalar _50_9 = RationalScalar.of(-50, 9);
  // ---
  private static final WindowFunction FUNCTION = new GaussianWindow();

  public static WindowFunction function() {
    return FUNCTION;
  }

  // ---
  private GaussianWindow() {
  }

  @Override
  public Scalar protected_apply(Scalar x) {
    return Exp.FUNCTION.apply(Times.of(_50_9, x, x));
  }
}
