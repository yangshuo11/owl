// code by jph
package ch.ethz.idsc.owl.math.group;

import ch.ethz.idsc.tensor.Tensor;

public enum LinearGroup implements LieGroup {
  INSTANCE;
  // ---
  @Override // from LieGroup
  public LinearGroupElement element(Tensor matrix) {
    return new LinearGroupElement(matrix);
  }
}
