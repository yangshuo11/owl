// code by jph
package ch.ethz.idsc.owl.bot.rn.rrts;

import ch.ethz.idsc.owl.bot.rn.RnRrtsNodeCollection;
import ch.ethz.idsc.owl.bot.rn.RnTransitionSpace;
import ch.ethz.idsc.owl.bot.util.UserHome;
import ch.ethz.idsc.owl.gui.win.OwlyFrame;
import ch.ethz.idsc.owl.gui.win.OwlyGui;
import ch.ethz.idsc.owl.math.sample.BoxRandomSample;
import ch.ethz.idsc.owl.rrts.adapter.LengthCostFunction;
import ch.ethz.idsc.owl.rrts.adapter.RrtsNodes;
import ch.ethz.idsc.owl.rrts.core.DefaultRrts;
import ch.ethz.idsc.owl.rrts.core.Rrts;
import ch.ethz.idsc.owl.rrts.core.RrtsNode;
import ch.ethz.idsc.owl.rrts.core.RrtsNodeCollection;
import ch.ethz.idsc.owl.rrts.core.TransitionRegionQuery;
import ch.ethz.idsc.owl.rrts.core.TransitionSpace;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.AnimationWriter;

/* package */ enum R2ExpandDemo {
  ;
  private static final TransitionSpace TRANSITION_SPACE = RnTransitionSpace.INSTANCE;

  public static void main(String[] args) throws Exception {
    int wid = 7;
    Tensor min = Tensors.vector(0, 0);
    Tensor max = Tensors.vector(wid, wid);
    RrtsNodeCollection nc = new RnRrtsNodeCollection(min, max);
    TransitionRegionQuery trq = StaticHelper.polygon1();
    // ---
    Rrts rrts = new DefaultRrts(TRANSITION_SPACE, nc, trq, LengthCostFunction.IDENTITY);
    RrtsNode root = rrts.insertAsNode(Tensors.vector(0, 0), 5).get();
    BoxRandomSample rnUniformSampler = new BoxRandomSample(min, max);
    try (AnimationWriter gsw = AnimationWriter.of(UserHome.Pictures("r2rrts.gif"), 250)) {
      OwlyFrame owlyFrame = OwlyGui.start();
      owlyFrame.configCoordinateOffset(42, 456);
      owlyFrame.jFrame.setBounds(100, 100, 500, 500);
      int frame = 0;
      while (frame++ < 40 && owlyFrame.jFrame.isVisible()) {
        for (int c = 0; c < 10; ++c)
          rrts.insertAsNode(rnUniformSampler.randomSample(), 20);
        owlyFrame.setRrts(root, trq);
        gsw.append(owlyFrame.offscreen());
        Thread.sleep(100);
      }
      int repeatLast = 3;
      while (0 < repeatLast--)
        gsw.append(owlyFrame.offscreen());
    }
    System.out.println(rrts.rewireCount());
    RrtsNodes.costConsistency(root, TRANSITION_SPACE, LengthCostFunction.IDENTITY);
  }
}
