package it.unibo.pps1920.motoscala.engine

import java.util.UUID

import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.core.Coordinator
import it.unibo.pps1920.motoscala.ecs.entities._
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.engine.Constants.PlayerLife
import it.unibo.pps1920.motoscala.engine.Life._
import it.unibo.pps1920.motoscala.engine.Masses._
import it.unibo.pps1920.motoscala.engine.Scores._
import it.unibo.pps1920.motoscala.model.Level._

import scala.collection.mutable
import scala.util.Random


protected[engine] object EngineUtils {
  /**
   * Sets up the game
   *
   * @param coordinator coordinator
   * @param level level entities
   * @param iterablePlayers user controlled players
   * @param difficult level difficulty
   */
  def addEntities(coordinator: Coordinator, level: LevelData, iterablePlayers: Iterator[BumperCarEntity],
                  difficult: Int): Unit = {
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
                               CollisionComponent(BlackPupaLife * difficult, mass = BaseEnemyMass),
                               AIComponent(Foolishness.BlackPupa / difficult, Random.shuffle(playerStack)),
                               ScoreComponent(BlackPupaLife * difficult))
      case RedPupa(position, shape, velocity) =>
        val red = RedPupaEntity(UUID.randomUUID())
        coordinator.addEntity(red)
          .addEntityComponents(red, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               VelocityComponent(defVel = (velocity.x, velocity.y)),
                               CollisionComponent(RedPupaLife * difficult, mass = BaseEnemyMass),
                               AIComponent(Foolishness.RedPupa / difficult, Random.shuffle(playerStack)),
                               ScoreComponent(RedPupaScore * difficult))
      case BluePupa(position, shape, velocity) =>
        val blue = BluePupaEntity(UUID.randomUUID())
        coordinator.addEntity(blue)
          .addEntityComponents(blue, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               VelocityComponent(defVel = (velocity.x, velocity.y)),
                               CollisionComponent(BluePupaLife * difficult, mass = BaseEnemyMass),
                               AIComponent(Foolishness.BluePupa / difficult, Random.shuffle(playerStack)),
                               ScoreComponent(BluePupaLife * difficult))
      case Polar(position, shape, velocity) =>
        val polar = PolarEntity(UUID.randomUUID())
        coordinator.addEntity(polar)
          .addEntityComponents(polar, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               VelocityComponent(defVel = (velocity.x, velocity.y)),
                               CollisionComponent(PolaLifer * difficult, mass = BaseEnemyMass),
                               AIComponent(Foolishness.Polar / difficult, mutable.Stack(playerStack.head)),
                               ScoreComponent(PolaScore * difficult))
      case BigBoss(position, shape, velocity) =>
        val bigBoss = BigBossEntity(UUID.randomUUID())
        coordinator.addEntity(bigBoss)
          .addEntityComponents(bigBoss, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               VelocityComponent(defVel = (velocity.x, velocity.y)),
                               CollisionComponent(BigBossLife * difficult, mass = BigBossMass),
                               AIComponent(Foolishness.BigBoss / difficult, mutable.Stack(playerStack.head)),
                               ScoreComponent(BigBosScore * difficult))
      case Nabicon(position, shape, velocity) =>
        val nabi = NabiconEntity(UUID.randomUUID())
        coordinator.addEntity(nabi)
          .addEntityComponents(nabi, ShapeComponent(shape),
                               PositionComponent((position.x, position.y)),
                               VelocityComponent(defVel = (velocity.x, velocity.y)),
                               CollisionComponent(NabiconLife, damage = 0, mass = NabiconMass),
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
/**
 * Possible powerup durations
 */
private object Duration {
  val Short: Int = 5 * 60
  val Medium: Int = 10 * 60
  val Long: Int = 15 * 60
}
/**
 * Possible entity bounties
 */
private object Scores {
  val PlayerScore: Int = 0
  val NabiconScore: Int = 700
  val BeeconScore: Int = 500
  val BaseEnemyScore: Int = 300
  val PolaScore: Int = 100
  val BlackPupaScore: Int = 400
  val RedPupaScore: Int = 200
  val BluePupaScore: Int = 500
  val BigBosScore: Int = 3000
}
/**
 * Possible entity masses
 */
private object Masses {
  val PowerUpMass: Int = 0
  val PlayerMass: Int = 100
  val BaseEnemyMass: Int = 30
  val BigBossMass: Int = 250
  val BeeconMass: Int = Int.MaxValue
  val NabiconMass: Int = Int.MaxValue
}
/**
 * Possible entity life values
 */
private object Life {
  val NabiconLife: Int = 100
  val BeeconLife: Int = 100
  val PolaLifer: Int = 100
  val BlackPupaLife: Int = 120
  val RedPupaLife: Int = 100
  val BluePupaLife: Int = 150
  val PowerUpLife: Int = 100
  val BigBossLife: Int = 700
}
/**
 * Possible enemy foolishness
 */
private object Foolishness {
  val Polar: Int = 30
  val BlackPupa: Int = 50
  val RedPupa: Int = 75
  val BluePupa: Int = 25
  val BigBoss: Int = 10
}
