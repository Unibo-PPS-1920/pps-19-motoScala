package it.unibo.pps1920.motoscala

import com.jfoenix.controls.{JFXDialog, JFXDialogLayout}
import eu.hansolo.enzo.notification.{Notification, NotificationEvent}
import it.unibo.pps1920.motoscala.view.JavafxEnums.{BigDialog, InfoNotification, MediumDuration}
import it.unibo.pps1920.motoscala.view.screens.{FXMLScreens, ScreenController}
import javafx.application.Platform
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
  def initializeJavaFXThread(): Unit = Platform.startup(() => {})

  /** Load the fxml and create the render node
   *
   * @param fxml the fxml
   * @param controller the screen controller
   * @return the loaded node
   */
  def loadFxml(fxml: FXMLScreens, controller: ScreenController): Node = {
    val loader = new FXMLLoader(View.getClass.getResource(fxml.resourcePath))
    loader.setController(controller)
    loader.load()
  }

  /** Charge css into the main scene
   *
   * @param scene the Scene
   * @param screen the FXMLScreen
   */
  def chargeSceneSheets(scene: Scene, screen: FXMLScreens): Unit = scene.getStylesheets
    .add(View.getClass.getResource(screen.cssPath).toString)

  /** A simple method for charging icon in node.
   *
   * @param icon the icon
   * @param fontSize the font size
   * @return the icon
   */
  def iconSetter(icon: Ikon, fontSize: JavafxEnums.IconDimension): FontIcon = {
    val tempIcon = new FontIcon(icon)
    tempIcon.setIconSize(fontSize.dim)
    tempIcon
  }

  /** Show a simple dialog
   *
   * @param title the string title of the notification
   * @param message the String text of the notification
   * @param ev the event to call on close
   */
  def showSimpleDialog(mainPane: StackPane,
                       title: String,
                       message: String,
                       ev: EventHandler[MouseEvent]): Unit =
    Platform.runLater(() => showDialog(mainPane, title, message, BigDialog, ev))

  /** Show a dialog into the main pane.
   *
   * @param mainPane the main [[StackPane]]
   * @param title the String title dialog
   * @param description the String description
   * @param size the  size
   * @param ev the [[MouseEvent]]
   */
  private[view] def showDialog(mainPane: StackPane,
                               title: String,
                               description: String,
                               size: JavafxEnums.DimDialog,
                               ev: EventHandler[MouseEvent]): Unit = {
    var css = ""
    val content = new JFXDialogLayout
    val _title = new Text(title)
    val _description = new Text(description)
    size match {
      case JavafxEnums.SmallDialog =>
        css = "dialogTextSmall"
      case JavafxEnums.MediumDialog =>
        css = "dialogTextMedium"
      case JavafxEnums.BigDialog =>
        css = "dialogTextBig"
      case _ =>
    }
    _title.getStyleClass.add(css)
    _description.getStyleClass.add(css)
    content.setHeading(new StackPane(_title))
    content.setBody(new StackPane(_description))
    val dialog = new JFXDialog(mainPane, content, JFXDialog.DialogTransition.CENTER)
    content.getStyleClass.add("dialogContentBackground")
    dialog.getStyleClass.add("dialogBackground")
    dialog.show()
    content.setCache(true)
    content.setCacheHint(CacheHint.SPEED)
    dialog.setOnMouseClicked(ev)
  }

  /** Show a simple popup
   *
   * @param title the string title of the notification
   * @param message the String text of the notification
   */
  def showSimplePopup(title: String, message: String): Unit =
    Platform.runLater(() => showNotificationPopup(title, message, MediumDuration, InfoNotification, () => _))

  /** Show a notification popup into the main windows of the operating system.
   *
   * @param title the String title of the notification
   * @param message the String text of the notification
   * @param secondsDuration the number of  of the notification
   * @param notificationType the type of the notification
   * @param ev the [[EventHandler]] ev, lambda
   */
  private[view] def showNotificationPopup(
    title: String, message: String,
    secondsDuration: JavafxEnums.Notification_Duration,
    notificationType: JavafxEnums.NotificationType,
    ev: EventHandler[NotificationEvent]): Unit = {
    val no = Notification.Notifier.INSTANCE
    val notification = new Notification(title, message)
    no.setPopupLifetime(Duration.seconds(secondsDuration.time))
    notificationType match {
      case JavafxEnums.ErrorNotification => no.notifyError(title, message)
      case JavafxEnums.WarningNotification => no.notifyWarning(title, message)
      case JavafxEnums.SuccessNotification => no.notifySuccess(title, message)
      case JavafxEnums.InfoNotification => no.notifyInfo(title, message)
      case _ => no.notify(notification)
    }
    no.setOnNotificationPressed(ev)
  }

  /** Constants for notification, popups, dialogs.
   */
  object JavafxEnums {
    sealed abstract class DimDialog(val dim: Int) {}
    case object BigDialog extends DimDialog(3)
    case object MediumDialog extends DimDialog(2)
    case object SmallDialog extends DimDialog(1)

    sealed abstract class IconDimension(val dim: Int) {}
    case object BiggestIcon extends IconDimension(80)
    case object BiggerIcon extends IconDimension(60)
    case object BigIcon extends IconDimension(40)
    case object MediumIcon extends IconDimension(30)
    case object SmallIcon extends IconDimension(20)
    case object SmallerIcon extends IconDimension(15)
    case object SmallestIcon extends IconDimension(10)

    sealed abstract class NotificationType(val code: Int) {}
    case object ErrorNotification extends NotificationType(1)
    case object WarningNotification extends NotificationType(2)
    case object SuccessNotification extends NotificationType(3)
    case object InfoNotification extends NotificationType(4)

    sealed abstract class Notification_Duration(val time: Int) {}
    case object LongDuration extends Notification_Duration(7)
    case object MediumDuration extends Notification_Duration(5)
    case object ShortDuration extends Notification_Duration(3)
  }
}
