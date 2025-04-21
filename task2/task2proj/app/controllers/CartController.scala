package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.CartItem
import scala.collection.mutable

@Singleton
class CartController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val cart = mutable.ListBuffer(
    CartItem(1, 1, 2),
    CartItem(2, 2, 1)
  )

  def getAll: Action[AnyContent] = Action {
    Ok(Json.toJson(cart))
  }

  def get(id: Long): Action[AnyContent] = Action {
    cart.find(_.id == id)
      .map(i => Ok(Json.toJson(i)))
      .getOrElse(NotFound(Json.obj("error" -> "Cart item not found")))
  }

  def create: Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[CartItem].fold(
      _ => BadRequest("Invalid format"),
      item => {
        cart += item
        Created(Json.toJson(item))
      }
    )
  }

  def update(id: Long): Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[CartItem].fold(
      _ => BadRequest("Invalid format"),
      updated => {
        cart.indexWhere(_.id == id) match {
          case -1 => NotFound
          case idx =>
            cart.update(idx, updated)
            Ok(Json.toJson(updated))
        }
      }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action {
    val index = cart.indexWhere(_.id == id)
    if (index >= 0) {
      cart.remove(index)
      NoContent
    } else NotFound
  }
}
