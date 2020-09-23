package it.unibo.pps1920.motoscala.view

import it.unibo.pps1920.motoscala.view.screens.{FXMLScreens, ScreenController}
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.layout.Pane

private[view] trait ScreenLoader {
  def getScreenController(screen: FXMLScreens): ScreenController
  def applyScreen(screen: FXMLScreens, root: Pane): Unit
  def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Node
}
private[view] object ScreenLoader {
  class ScreenLoaderImpl extends ScreenLoader {
    private var cache: Map[FXMLScreens, (Node, ScreenController)] = Map()

    override def getScreenController(screen: FXMLScreens): ScreenController = cache(screen)._2
    def applyScreen(screen: FXMLScreens, root: Pane): Unit = root.getChildren.setAll(cache(screen)._1)
    def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Node =
      this.cache.get(screen) match {
        case Some((node, _)) => node
        case None =>
          val loadedNode = loadNode(screen, controller)
          cache += (screen -> ((loadedNode, controller)))
          loadedNode
      }
    private def loadNode(screen: FXMLScreens, controller: ScreenController): Node = {
      val loader = new FXMLLoader()
      loader.setController(controller)
      loader.setLocation(ScreenLoader.getClass.getResource(screen.resourcePath))
      loader.load
    }
  }
  def apply(): ScreenLoader = new ScreenLoaderImpl()
}
