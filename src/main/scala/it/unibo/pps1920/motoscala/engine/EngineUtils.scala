package it.unibo.pps1920.motoscala.engine

import java.util.UUID

import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.core.Coordinator
import it.unibo.pps1920.motoscala.ecs.entities._
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.engine.Constants.PlayerLife
import it.unibo.pps1920.motoscala.engine.Life.{BaseEnemyLife, BeeconLife, NabiconLife, PowerUpLife}
import it.unibo.pps1920.motoscala.engine.Masses._
import it.unibo.pps1920.motoscala.engine.Scores._
import it.unibo.pps1920.motoscala.model.Level._

import scala.collection.mutable
import scala.util.Random


object EngineUtils {

  def addEntities(coordinator: Coordinator, level: LevelData, iterablePlayers: Iterator[BumperCarEntity],
                 ): Unit = {
    val playerStack = mutable.Stack[BumperCarEntity]()
    level.entities.foreach {
      case Player(position, shape, velocity) =>
        val player = iterablePlayers.next()
        playerStack.push(player)
        coordinator.addEntity(player)
          .addEntityComponents(player,
                               ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               VelocityComponent(defVel = (velocity.x, velocity.y)),
                               JumpComponent(),
                               CollisionComponent(PlayerLife, mass = PlayerMass),
                               ScoreComponent(PlayerScore))
      case BlackPupa(position, shape, velocity) =>
        val black = BlackPupaEntity(UUID.randomUUID())
        coordinator.addEntity(black)
          .addEntityComponents(black, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               VelocityComponent(defVel = (velocity.x, velocity.y)),
                               CollisionComponent(BaseEnemyLife, mass = BaseEnemyMass),
                               AIComponent(10, Random.shuffle(playerStack)),
                               ScoreComponent(BaseEnemyScore))
      case RedPupa(position, shape, velocity) =>
        val red = RedPupaEntity(UUID.randomUUID())
        coordinator.addEntity(red)
          .addEntityComponents(red, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               VelocityComponent(defVel = (velocity.x, velocity.y)),
                               CollisionComponent(BaseEnemyLife, mass = BaseEnemyMass),
                               AIComponent(20, Random.shuffle(playerStack)),
                               ScoreComponent(BaseEnemyScore))
      case BluePupa(position, shape, velocity) =>
        val blue = BluePupaEntity(UUID.randomUUID())
        coordinator.addEntity(blue)
          .addEntityComponents(blue, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               VelocityComponent(defVel = (velocity.x, velocity.y)),
                               CollisionComponent(BaseEnemyLife, mass = BaseEnemyMass),
                               AIComponent(25, Random.shuffle(playerStack)),
                               ScoreComponent(BaseEnemyScore))
      case Polar(position, shape, velocity) =>
        val polar = PolarEntity(UUID.randomUUID())
        coordinator.addEntity(polar)
          .addEntityComponents(polar, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               VelocityComponent(defVel = (velocity.x, velocity.y)),
                               CollisionComponent(BaseEnemyLife, mass = BaseEnemyMass),
                               AIComponent(5, mutable.Stack(playerStack.head)),
                               ScoreComponent(BaseEnemyScore))
      case Nabicon(position, shape, velocity) =>
        val nabi = NabiconEntity(UUID.randomUUID())
        coordinator.addEntity(nabi)
          .addEntityComponents(nabi, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               VelocityComponent(defVel = (velocity.x, velocity.y)),
                               CollisionComponent(NabiconLife, mass = NabiconMass),
                               ScoreComponent(NabiconScore))
      case Beecon(position, shape, velocity) =>
        val bee = BeeconEntity(UUID.randomUUID())
        coordinator.addEntity(bee)
          .addEntityComponents(bee, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               VelocityComponent(defVel = (velocity.x, velocity.y)),
                               CollisionComponent(BeeconLife, mass = BeeconMass),
                               ScoreComponent(BeeconScore))
      case JumpPowerUp(position, shape) =>
        val jmp = JumpPowerUpEntity(UUID.randomUUID())
        coordinator.addEntity(jmp)
          .addEntityComponents(jmp, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               CollisionComponent(PowerUpLife, mass = PowerUpMass),
                               VelocityComponent(),
                               PowerUpComponent(effect = PowerUpEffect.JumpPowerUp(duration = Duration.Short)))
      case WeightBoostPowerUp(position, shape) =>
        val w = WeightPowerUpEntity(UUID.randomUUID())
        coordinator.addEntity(w)
          .addEntityComponents(w, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               CollisionComponent(PowerUpLife, mass = PowerUpMass),
                               VelocityComponent(),
                               PowerUpComponent(effect = PowerUpEffect
                                 .WeightBoostPowerUp(duration = Duration.Long, isActive = false, _ + 50)))
      case SpeedBoostPowerUp(position, shape) =>
        val s = SpeedPowerUpEntity(UUID.randomUUID())
        coordinator.addEntity(s)
          .addEntityComponents(s, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               CollisionComponent(PowerUpLife, mass = PowerUpMass),
                               VelocityComponent(),
                               PowerUpComponent(effect = PowerUpEffect
                                 .SpeedBoostPowerUp(duration = Duration.Medium, isActive = false,
                                                    e => Vector2(e.x * 0.5, e.y * 0.5))))
    }
  }
}
private object Duration {
  val Short: Int = 5 * 60
  val Medium: Int = 10 * 60
  val Long: Int = 15 * 60
}
private object Scores {
  val PlayerScore: Int = 0
  val NabiconScore: Int = 50000
  val BeeconScore: Int = 1000
  val BaseEnemyScore: Int = 300
}
private object Masses {
  val PowerUpMass: Int = 0
  val PlayerMass: Int = 100
  val BaseEnemyMass: Int = 30
  val BeeconMass: Int = 1000
  val NabiconMass: Int = Int.MaxValue
}

private object Life {
  val NabiconLife: Int = 20
  val BeeconLife: Int = 20
  val BaseEnemyLife: Int = 20
  val PowerUpLife: Int = 20
}
