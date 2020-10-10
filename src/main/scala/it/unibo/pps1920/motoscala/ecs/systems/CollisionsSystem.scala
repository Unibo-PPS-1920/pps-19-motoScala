package it.unibo.pps1920.motoscala.ecs.systems
import it.unibo.pps1920.motoscala.ecs.Entity
object CollisionsSystem {
  import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}
  import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, ShapeComponent, VelocityComponent}
  import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
  def apply(coordinator: Coordinator, fps: Int) : System = new CollisionsSystemImpl(coordinator,fps)

  private class CollisionsSystemImpl(coordinator: Coordinator, fps: Int) extends AbstractSystem(ECSSignature(classOf[PositionComponent],
                                                                                                             classOf[ShapeComponent],
                                                                                                             classOf[VelocityComponent])) {

    override def update(): Unit = {
      var entitiesToCheck = entitiesRef()
      entitiesRef().foreach(e1=> {
        entitiesToCheck -= e1
        entitiesToCheck.foreach(e2=>{
          if(isTouching(e1, e2)){
              collide(e1,e2)
          }
        })
      })
    }

    private def isTouching(e1 : Entity, e2 : Entity): Boolean ={
      true
    }

    private def collide(e1:Entity, e2:Entity): Unit ={

    }
  }

}
