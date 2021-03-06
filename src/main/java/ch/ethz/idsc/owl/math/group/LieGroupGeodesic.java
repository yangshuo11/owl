// code by jph
package ch.ethz.idsc.owl.math.group;

import java.util.function.Function;

import ch.ethz.idsc.owl.math.GeodesicInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

public class LieGroupGeodesic implements GeodesicInterface {
  private final Function<Tensor, LieGroupElement> function;
  private final LieExponential lieExponential;

  public LieGroupGeodesic(Function<Tensor, LieGroupElement> function, LieExponential lieExponential) {
    this.function = function;
    this.lieExponential = lieExponential;
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    LieGroupElement lieGroupAction = function.apply(p);
    Tensor delta = lieGroupAction.inverse().combine(q);
    Tensor x = lieExponential.log(delta).multiply(scalar);
    Tensor m = lieExponential.exp(x);
    return lieGroupAction.combine(m);
  }
}
