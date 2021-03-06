// code by jph
package ch.ethz.idsc.owl.math.group;

import ch.ethz.idsc.owl.bot.se2.Se2Wrap;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2GeodesicTest extends TestCase {
  public void testSimple() {
    Tensor split = Se2Geodesic.INSTANCE.split(Tensors.vector(0, 0, 0), Tensors.vector(10, 0, 1), RealScalar.of(.7));
    assertTrue(Chop._13.close(split, Tensors.fromString("{7.071951896570488, -1.0688209919859546, 0.7}")));
  }

  public void testEndPoints() {
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int index = 0; index < 100; ++index) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      assertTrue(Chop._14.close(p, Se2Geodesic.INSTANCE.split(p, q, RealScalar.ZERO)));
      Tensor r = Se2Geodesic.INSTANCE.split(p, q, RealScalar.ONE);
      if (!Chop._14.close(q, r))
        assertTrue(Chop._10.allZero(Se2Wrap.INSTANCE.difference(q, r)));
    }
  }

  public void testMod2Pi() {
    Tensor p = Tensors.vector(0, 0, -2 * Math.PI * 3);
    Tensor q = Tensors.vector(0, 0, +2 * Math.PI + 0.1);
    Tensor difference = Se2Wrap.INSTANCE.difference(p, q);
    assertTrue(Chop._13.close(difference, Tensors.vector(0, 0, 0.1)));
  }
}
