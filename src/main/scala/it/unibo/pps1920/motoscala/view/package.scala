package it.unibo.pps1920.motoscala

import com.jfoenix.controls.{JFXDialog, JFXDialogLayout}
import eu.hansolo.enzo.notification.{Notification, NotificationEvent}
import it.unibo.pps1920.motoscala.view.FXMLScreens.FXMLScreens
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import javafx.scene.{CacheHint, Node, Scene}
import javafx.util.Duration
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.javafx.FontIcon

package object view {
  def loadFxml(controller: Any, fxml: FXMLScreens): Node = {
    val loader = new FXMLLoader(Main.getClass.getResource(fxml.resourcePath))
    loader.setController(controller)
    loader.load()
  }

  def chargeSceneSheets(scene: Scene): Unit = {
    scene.getStylesheets.add(Main.getClass.getResource(FXMLScreens.HOME.cssPath).toString)
  }

  /**
   * A simple method for charging icon in node.
   *
   * @param icon the icon
   * @param fontSize the font size
   * @return [[FontIcon]]
   */
  def iconSetter(icon: Ikon, fontSize: JavafxEnums.IconDimension): FontIcon = {
    val tempIcon = new FontIcon(icon)
    tempIcon.setIconSize(fontSize.dim)
    tempIcon
  }

  /**
   * Show a dialog into the main pane.
   *
   * @param mainPane the main [[StackPane]]
   * @param title the String title dialog
   * @param description the String description
   * @param size the  size
   * @param ev the [[MouseEvent]]
   */
  def showDialog(mainPane: StackPane, title: String, description: String, size: JavafxEnums.DimDialog,
                 ev: EventHandler[MouseEvent]): Unit = {
    var css = ""
    val content = new JFXDialogLayout
    val _title = new Text(title)
    val _description = new Text(description)
    size match {
      case JavafxEnums.SMALL_DIALOG =>
        css = "dialogTextSmall"
      case JavafxEnums.MEDIUM_DIALOG =>
        css = "dialogTextMedium"
      case JavafxEnums.BIG_DIALOG =>
        css = "dialogTextBig"
      case _ =>
    }
    _description.getStyleClass.add(css)
    _title.getStyleClass.add(css)
    content.setHeading(_title)
    content.setBody(_description)
    content.getStyleClass.add("dialogContentBackground")
    val dialog = new JFXDialog(mainPane, content, JFXDialog.DialogTransition.CENTER)
    dialog.getStyleClass.add("dialogBackground")
    dialog.show()
    content.setCache(true)
    content.setCacheHint(CacheHint.SPEED)
    dialog.setOnMouseClicked(ev)
  }

  /**
   * Show a notification popup into the main windows of the operating system.
   *
   * @param title the String title of the notification
   * @param message the String text of the notification
   * @param secondsDuration the number of  of the notification
   * @param notificationType the type of the notification
   * @param ev the [[EventHandler]] ev, lambda
   */
  def showNotificationPopup(title: String, message: String, secondsDuration: JavafxEnums.Notification_Duration,
                            notificationType: JavafxEnums.NotificationType,
                            ev: EventHandler[NotificationEvent]): Unit = { // _____________________________PATTERN STRATEGY
    val no = Notification.Notifier.INSTANCE
    val notification = new Notification(title, message)
    no.setPopupLifetime(Duration.seconds(secondsDuration.time))
    notificationType match {
      case JavafxEnums.ERROR_NOTIFICATION =>
        no.notifyError(title, message)
      case JavafxEnums.WARNING_NOTIFICATION =>
        no.notifyWarning(title, message)
      case JavafxEnums.SUCCESS_NOTIFICATION =>
        no.notifySuccess(title, message)
      case JavafxEnums.INFO_NOTIFICATION =>
        no.notifyInfo(title, message)
      case _ =>
        no.notify(notification)
    }
    no.setOnNotificationPressed(ev)
  }

  object JavafxEnums {
    sealed abstract class DimDialog(val dim: Int) {}
    case object BIG_DIALOG extends DimDialog(3)
    case object MEDIUM_DIALOG extends DimDialog(2)
    case object SMALL_DIALOG extends DimDialog(1)

    sealed abstract class IconDimension(val dim: Int) {}
    case object BIGGEST_ICON extends IconDimension(80)
    case object BIGGER_ICON extends IconDimension(60)
    case object BIG_ICON extends IconDimension(40)
    case object MEDIUM_ICON extends IconDimension(30)
    case object SMALL_ICON extends IconDimension(20)
    case object SMALLER_ICON extends IconDimension(15)
    case object SMALLEST_ICON extends IconDimension(10)

    sealed abstract class NotificationType(val code: Int) {}
    case object ERROR_NOTIFICATION extends NotificationType(1)
    case object WARNING_NOTIFICATION extends NotificationType(2)
    case object SUCCESS_NOTIFICATION extends NotificationType(3)
    case object INFO_NOTIFICATION extends NotificationType(4)

    sealed abstract class Notification_Duration(val time: Int) {}
    case object LONG_DURATION extends Notification_Duration(7)
    case object MEDIUM_DURATION extends Notification_Duration(5)
    case object SHORT_DURATION extends Notification_Duration(3)
  }
}
