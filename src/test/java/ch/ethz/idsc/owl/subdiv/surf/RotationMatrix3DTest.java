// code by jph
package ch.ethz.idsc.owl.subdiv.surf;

import ch.ethz.idsc.owl.math.sample.RandomSampleInterface;
import ch.ethz.idsc.owl.math.sample.SphereRandomSample;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class RotationMatrix3DTest extends TestCase {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  public void testSimple() {
    for (int index = 0; index < 50; ++index) {
      RandomSampleInterface randomSampleInterface = //
          SphereRandomSample.of(Tensors.vector(0, 0, 0), RealScalar.ONE);
      Tensor p = NORMALIZE.apply(randomSampleInterface.randomSample());
      Tensor q = NORMALIZE.apply(randomSampleInterface.randomSample());
      Tensor tensor = RotationMatrix3D.of(p, q);
      assertTrue(Chop._10.close(tensor.dot(p), q));
      assertTrue(OrthogonalMatrixQ.of(tensor));
    }
  }
}
