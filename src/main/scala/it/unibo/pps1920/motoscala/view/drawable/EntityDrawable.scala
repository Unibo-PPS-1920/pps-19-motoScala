package it.unibo.pps1920.motoscala.view.drawable

import it.unibo.pps1920.motoscala.controller.mediation.EventData.DrawEntityData
import it.unibo.pps1920.motoscala.ecs.components.Shape
import it.unibo.pps1920.motoscala.ecs.util.Direction
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.{Image, ImageView}
import javafx.scene.paint.Color
import scalafx.scene.SnapshotParameters

class EntityDrawable(override val image: Image,
                     val graphicsContext: GraphicsContext) extends ImageDrawable {
  private val RotationDegree: Int = 45
  def rotation(direction: Direction): Int = RotationDegree * (direction match {
    case Direction.North => 0
    case Direction.NorthEast => 1
    case Direction.East => 2
    case Direction.SouthEast => 3
    case Direction.South => 4
    case Direction.SouthWest => 5
    case Direction.West => 6
    case Direction.NorthWest => 7
    case Direction.Center => 0
    case _ => 0
  })
  def size(data: DrawEntityData): (Int, Int) = data.shape match {
    case Shape.Rectangle(dimX, dimY) => (dimX.toInt, dimY.toInt)
    case Shape.Circle(radius) => ((radius * 2).toInt, (radius * 2).toInt)
  }
  def draw(data: DrawEntityData): Unit = {
    val s = size(data)
    val iv = new ImageView(image)
    iv.setRotate(40)
    val params = new SnapshotParameters()
    params.setFill(Color.TRANSPARENT)
    val rotatedImage = iv.snapshot(params, null)
    graphicsContext.drawImage(rotatedImage, data.pos.x, data.pos.y, s._1, s._2)
  }
}