package it.unibo.pps1920.motoscala.view.drawable

import it.unibo.pps1920.motoscala.controller.mediation.EventData.EntityData
import it.unibo.pps1920.motoscala.ecs.components.Shape
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image

/** A class representing an entity that can be drawn on screen.
 *
 * @param image the image to draw
 */
protected[view] class EntityDrawable(override val image: Image) extends ImageDrawable {
  def getSize(data: EntityData): (Int, Int) = data.shape match {
    case Shape.Rectangle(dimX, dimY) => ((dimX / 2).toInt, (dimY / 2).toInt)
    case Shape.Circle(radius) => (radius.toInt, radius.toInt)
  }
  def draw(data: EntityData, graphicsContext: GraphicsContext): Unit = {
    val size = getSize(data)
    graphicsContext.drawImage(image, data.pos.x - size._1, data.pos.y - size._2, size._1 * 2, size._2 * 2)
  }
}
