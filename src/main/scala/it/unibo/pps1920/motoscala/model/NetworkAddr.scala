package it.unibo.pps1920.motoscala.model

import scala.util.matching.Regex

object NetworkAddr {
  private val IPV4Address: Regex = raw"(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})".r

  def getLocalIPAddress: String = {
    java.net.InetAddress.getLocalHost.getHostAddress
  }
  /** Validates a IPV4 address.
   *
   * @param address The address.
   * @return True, if the address is valid; otherwise false.
   */
  def validateIPV4Address(address: String): Boolean = {
    address match {
      case IPV4Address(first, second, third, fourth) =>
        List(first, second, third, fourth) map validateIPV4Block forall identity
      case _ => false
    }
  }
  /** Validates a IPV4 address block, without broadcast.
   *
   * @param block The block.
   * @return True, if the block is valid; otherwise false.
   */
  private def validateIPV4Block(block: String): Boolean = {
    block.nonEmpty && block.forall(_.isDigit) && (0 to 254 contains block.toInt)
  }
  /** Validates a non well-known network port.
   *
   * @param port The port.
   * @return True, if the port is valid; otherwise false.
   */
  def validatePort(port: Int): Boolean = 1024 to 65535 contains port


}

