package it.unibo.pps1920.motoscala.view

import it.unibo.pps1920.motoscala.view.screens.{FXMLScreens, ScreenController}
import javafx.scene.Node
import javafx.scene.layout.Pane

private[view] trait ScreenLoader {
  def getScreenController(screen: FXMLScreens): ScreenController
  def applyScreen(screen: FXMLScreens, root: Pane): Unit
  def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Unit
}
private[view] object ScreenLoader {
  class ScreenLoaderImpl extends ScreenLoader {
    private var cache: Map[FXMLScreens, (Node, ScreenController)] = Map()

    override def getScreenController(screen: FXMLScreens): ScreenController = cache(screen)._2
    def applyScreen(screen: FXMLScreens, root: Pane): Unit = root.getChildren.setAll(cache(screen)._1)
    def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Unit = {
      println(screen, controller.getClass)
      cache += (screen -> ((loadFxml(screen, controller), controller)))
    }
  }
  def apply(): ScreenLoader = new ScreenLoaderImpl()
}
