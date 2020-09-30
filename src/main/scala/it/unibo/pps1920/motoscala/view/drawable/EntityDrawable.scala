package it.unibo.pps1920.motoscala.view.drawable

import it.unibo.pps1920.motoscala.controller.mediation.EventData.DrawEntityData
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image

class EntityDrawable(override val image: Image,
                     val graphicsContext: GraphicsContext) extends ImageDrawable {
  def draw(data: DrawEntityData): Unit = {
    graphicsContext.drawImage(image, data.pos.x, data.pos.y, 10, 10)
  }
}
