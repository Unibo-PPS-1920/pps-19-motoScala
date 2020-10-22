package it.unibo.pps1920.motoscala.view

import it.unibo.pps1920.motoscala.view.screens.{FXMLScreens, ScreenController}
import javafx.scene.Node
import javafx.scene.layout.Pane

/** A Screen loader caches rendered screens and provides method for rendering and displaying nodes */
protected[view] trait ScreenLoader {
  /** Get the screen controller of a particular screen
   *
   * @param screen the Fxml screen
   * @return the screen controller
   */
  def getScreenController(screen: FXMLScreens): ScreenController
  /** Apply the screen to the root pane.
   *
   * @param screen the Fxml screen
   * @param root the root pane
   */
  def applyScreen(screen: FXMLScreens, root: Pane): Unit
  /** Load the node in cache.
   *
   * @param screen the Fxml screen
   * @param controller the screen controller
   */
  def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Unit
}

protected[view] object ScreenLoader {
  class ScreenLoaderImpl extends ScreenLoader {
    private var cache: Map[FXMLScreens, (Node, ScreenController)] = Map()

    override def getScreenController(screen: FXMLScreens): ScreenController = cache(screen)._2
    override def applyScreen(screen: FXMLScreens, root: Pane): Unit = root.getChildren.setAll(cache(screen)._1)
    override def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Unit =
      cache += (screen -> ((loadFxml(screen, controller), controller)))
  }

  /** Factory for [[ScreenLoader]] instances */
  def apply(): ScreenLoader = new ScreenLoaderImpl()
}
