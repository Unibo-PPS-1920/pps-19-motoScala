package it.unibo.pps1920.motoscala.view

import it.unibo.pps1920.motoscala.view.FXMLScreens.FXMLScreens
import it.unibo.pps1920.motoscala.view.errors.ViewError.ScreenNotfound
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.layout.Pane

trait ScreenLoader {
  def applyScreen(screen: FXMLScreens, root: Pane): Either[ScreenNotfound, Boolean]
  def loadFXMLNode(screen: FXMLScreens, controller: Object): Node
}
object ScreenLoader {
  class ScreenLoaderImpl extends ScreenLoader {
    private var cache: Map[FXMLScreens, Node] = Map()

    def applyScreen(screen: FXMLScreens, root: Pane): Either[ScreenNotfound, Boolean] = {
      cache.get(screen) match {
        case Some(node) => Right(root.getChildren.setAll(node))
        case None => Left(ScreenNotfound())
      }
    }
    def loadFXMLNode(screen: FXMLScreens, controller: Object): Node =
      this.cache.get(screen) match {
        case Some(node) => node
        case None =>
          val loadedNode = loadNode(screen, controller)
          cache += (screen -> loadedNode)
          loadedNode
      }
    private def loadNode(screen: FXMLScreens, controller: Object): Node = {
      val loader = new FXMLLoader()
      loader.setController(controller)
      loader.setLocation(ScreenLoader.getClass.getResource(screen.resourcePath))
      loader.load
    }
  }
  def apply(): ScreenLoader = new ScreenLoaderImpl()
}
