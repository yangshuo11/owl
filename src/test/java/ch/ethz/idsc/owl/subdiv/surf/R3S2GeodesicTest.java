// code by jph
package ch.ethz.idsc.owl.subdiv.surf;

import ch.ethz.idsc.owl.math.sample.RandomSampleInterface;
import ch.ethz.idsc.owl.math.sample.SphereRandomSample;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class R3S2GeodesicTest extends TestCase {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  public void testZero() {
    Tensor split = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{1,2,3},{1,0,0}}"), //
        Tensors.fromString("{{8,8,8},{0,1,0}}"), //
        RealScalar.ZERO);
    assertEquals(split, Tensors.fromString("{{1,2,3},{1,0,0}}"));
  }

  public void testOne() {
    Tensor split = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{1,2,3},{1,0,0}}"), //
        Tensors.fromString("{{8,8,8},{0,1,0}}"), //
        RealScalar.ONE);
    assertTrue(Chop._10.close(split, Tensors.fromString("{{8,8,8},{0,1,0}}")));
  }

  public void testHalfShift() {
    Tensor split = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{1,2,3},{1,0,0}}"), //
        Tensors.fromString("{{8,8,8},{1,0,0}}"), //
        RationalScalar.HALF);
    assertTrue(Chop._10.close(split, Tensors.fromString("{{4.5,5,5.5}, {1,0,0}}")));
  }

  public void testHalfRotate() {
    Tensor split = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{1,2,3},{1,0,0}}"), //
        Tensors.fromString("{{1,2,3},{0,1,0}}"), //
        RationalScalar.HALF);
    assertTrue(Chop._10.close(split, Tensors.fromString( //
        "{{1,2,3},{0.7071067811865476, 0.7071067811865475, 0}}")));
  }

  public void testHalfSome() {
    Tensor split = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{1,2,3},{1,0,0}}"), //
        Tensors.fromString("{{8,8,8},{0,1,0}}"), //
        RationalScalar.HALF);
    assertTrue(Chop._10.close(split.get(0), Tensors.fromString( //
        "{5.742640687119285, 3.5502525316941673, 5.5}")));
    assertTrue(Chop._10.close(split.get(1), Tensors.fromString( //
        "{0.7071067811865476, 0.7071067811865475, 0}")));
  }

  public void testSphereRepro() {
    Tensor n0 = UnitVector.of(3, 0);
    Tensor n1 = UnitVector.of(3, 1);
    Tensor p = Tensors.of(n0, n0);
    Tensor q = Tensors.of(n1, n1);
    Tensor split = R3S2Geodesic.INSTANCE.split(p, q, RationalScalar.of(1, 2));
    assertTrue(Chop._10.close(split.get(0), split.get(1)));
  }

  public void testSphereRepro2() {
    for (int index = 0; index < 50; ++index) {
      RandomSampleInterface randomSampleInterface = //
          SphereRandomSample.of(Tensors.vector(0, 0, 0), RealScalar.ONE);
      Tensor pn = NORMALIZE.apply(randomSampleInterface.randomSample());
      Tensor qn = NORMALIZE.apply(randomSampleInterface.randomSample());
      Tensor p = Tensors.of(pn, pn);
      Tensor q = Tensors.of(qn, qn);
      Tensor split = R3S2Geodesic.INSTANCE.split(p, q, RationalScalar.of(1, 2));
      assertTrue(Chop._10.close(split.get(0), split.get(1)));
    }
  }
}
