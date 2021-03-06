package com.eigengo.lift.exercise

import akka.actor.{ActorRef, ActorSystem}
import akka.contrib.pattern.ClusterSharding
import com.eigengo.lift.exercise.ExerciseBoot._
import spray.routing.Route

import scala.concurrent.ExecutionContext

case class ExerciseBoot(userExercises: ActorRef, userExercisesView: ActorRef, exerciseClassifiers: ActorRef) {
  /**
   * Starts the route given the exercise boot
   * @param ec the execution context
   * @return the route
   */
  def route(implicit ec: ExecutionContext): Route = exerciseRoute(userExercises, userExercisesView)

}

/**
 * Starts the actors in this microservice
 */
object ExerciseBoot extends ExerciseService {

  /**
   * Boot the exercise microservice
   * @param system the AS to boot the microservice in
   */
  def boot(notification: ActorRef)(implicit system: ActorSystem): ExerciseBoot = {
    val exerciseClassifiers = system.actorOf(ExerciseClassifiers.props, ExerciseClassifiers.name)
    val userExercise = ClusterSharding(system).start(
      typeName = UserExercises.shardName,
      entryProps = Some(UserExercises.props(notification, exerciseClassifiers)),
      idExtractor = UserExercises.idExtractor,
      shardResolver = UserExercises.shardResolver)
    val userExerciseView = ClusterSharding(system).start(
      typeName = UserExercisesView.shardName,
      entryProps = Some(UserExercisesView.props),
      idExtractor = UserExercisesView.idExtractor,
      shardResolver = UserExercisesView.shardResolver)

    ExerciseBoot(userExercise, userExerciseView, exerciseClassifiers)
  }

}
