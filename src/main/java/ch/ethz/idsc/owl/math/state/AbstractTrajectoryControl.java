// code by jph
package ch.ethz.idsc.owl.math.state;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.ethz.idsc.owl.data.GlobalAssert;
import ch.ethz.idsc.owl.math.StateTimeTensorFunction;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.ArgMin;

/**
 * 
 */
public abstract class AbstractTrajectoryControl implements TrajectoryControl {
  // TODO long term, don't require ep int here
  private final EpisodeIntegrator episodeIntegrator;
  private final StateTimeTensorFunction represent_entity;
  private List<TrajectorySample> trajectory = null;
  private int trajectory_skip = 0;

  public AbstractTrajectoryControl( //
      EpisodeIntegrator episodeIntegrator, StateTimeTensorFunction represent_entity) {
    this.episodeIntegrator = episodeIntegrator;
    this.represent_entity = represent_entity;
  }

  @Override
  public synchronized void setTrajectory(List<TrajectorySample> trajectory) {
    this.trajectory = trajectory;
    trajectory_skip = 0;
  }

  public final synchronized Tensor control(Scalar now) {
    // implementation does not require that current position is perfectly located on trajectory
    Tensor u = fallbackControl(); // default control
    if (Objects.nonNull(trajectory)) {
      final int argmin = indexOfPassedTrajectorySample(trajectory.subList(trajectory_skip, trajectory.size()));
      GlobalAssert.that(argmin != ArgMin.NOINDEX);
      int index = trajectory_skip + argmin;
      trajectory_skip = index;
      ++index; // <- next node has flow control
      if (index < trajectory.size()) {
        Optional<Tensor> optional = customControl(trajectory.subList(index, trajectory.size()));
        if (optional.isPresent())
          u = optional.get();
        else {
          GlobalAssert.that(trajectory.get(index).getFlow().isPresent());
          u = trajectory.get(index).getFlow().get().getU();
        }
      } else {
        trajectory = resetAction(trajectory);
      }
    }
    return u;
  }

  // TODO remove synchronized
  @Override
  public final synchronized void integrate(Scalar now) {
    Tensor u = control(now);
    episodeIntegrator.move(u, now);
  }

  protected abstract Tensor fallbackControl();

  /** the return index does not refer to node in the trajectory closest to the entity
   * but rather the index of the node that was already traversed.
   * this ensures that the entity can query the correct flow that leads to the upcoming node
   * 
   * @param trajectory
   * @return index of node that has been traversed most recently by entity */
  public final int indexOfPassedTrajectorySample(List<TrajectorySample> trajectory) {
    final Tensor y = represent_entity.apply(getStateTimeNow());
    Tensor dist = Tensor.of(trajectory.stream() //
        .map(TrajectorySample::stateTime) //
        .map(represent_entity) //
        .map(state -> distance(state, y)));
    int argmin = ArgMin.of(dist);
    // the below 'correction' does not help in tracking
    // instead one could try blending flows depending on distance
    // if (0 < argmin && argmin < dist.length() - 1)
    // if (Scalars.lessThan(dist.Get(argmin - 1), dist.Get(argmin + 1)))
    // --argmin;
    return argmin;
  }

  /** @param delay
   * @return trajectory until delay[s] in the future of entity,
   * or current position if entity does not have a trajectory */
  @Override
  public final synchronized List<TrajectorySample> getFutureTrajectoryUntil(Scalar delay) {
    if (Objects.isNull(trajectory)) // agent does not have a trajectory
      return Collections.singletonList(TrajectorySample.head(getStateTimeNow()));
    int index = trajectory_skip + indexOfPassedTrajectorySample(trajectory.subList(trajectory_skip, trajectory.size()));
    // <- no update of trajectory_skip here
    Scalar threshold = trajectory.get(index).stateTime().time().add(delay);
    return trajectory.stream().skip(index) //
        .filter(trajectorySample -> Scalars.lessEquals(trajectorySample.stateTime().time(), threshold)) //
        .collect(Collectors.toList());
  }

  @Override
  public final StateTime getStateTimeNow() {
    return episodeIntegrator.tail();
  }

  /** @param x from trajectory
   * @param y present state of entity
   * @return */
  protected abstract Scalar distance(Tensor x, Tensor y);

  protected List<TrajectorySample> resetAction(List<TrajectorySample> trajectory) {
    System.err.println("out of trajectory");
    return null;
  }

  /** @param trailAhead
   * @return */
  protected Optional<Tensor> customControl(List<TrajectorySample> trailAhead) {
    return Optional.empty();
  }
}
