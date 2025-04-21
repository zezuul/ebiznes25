package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import scala.collection.mutable

case class Product(id: Long, name: String, price: Double)
object Product {
  implicit val format: OFormat[Product] = Json.format[Product]
}

@Singleton
class ProductController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val products = mutable.ListBuffer(
    Product(1, "Laptop", 2999.99),
    Product(2, "Phone", 1499.50)
  )

  def getAll: Action[AnyContent] = Action {
    Ok(Json.toJson(products))
  }

  def get(id: Long): Action[AnyContent] = Action {
    products.find(_.id == id)
      .map(p => Ok(Json.toJson(p)))
      .getOrElse(NotFound(Json.obj("error" -> "Not found")))
  }

  def create: Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[Product].fold(
      _ => BadRequest("Invalid product format"),
      product => {
        products += product
        Created(Json.toJson(product))
      }
    )
  }

  def update(id: Long): Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[Product].fold(
      _ => BadRequest("Invalid format"),
      updated => {
        products.indexWhere(_.id == id) match {
          case -1 => NotFound
          case idx =>
            products.update(idx, updated)
            Ok(Json.toJson(updated))
        }
      }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action {
    val removed = products.indexWhere(_.id == id)
    if (removed >= 0) {
      products.remove(removed)
      NoContent
    } else NotFound
  }
}
