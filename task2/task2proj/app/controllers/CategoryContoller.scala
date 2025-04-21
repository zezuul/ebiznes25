package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.Category
import scala.collection.mutable

@Singleton
class CategoryController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val categories = mutable.ListBuffer(
    Category(1, "Electronics"),
    Category(2, "Books")
  )

  def getAll: Action[AnyContent] = Action {
    Ok(Json.toJson(categories))
  }

  def get(id: Long): Action[AnyContent] = Action {
    categories.find(_.id == id)
      .map(c => Ok(Json.toJson(c)))
      .getOrElse(NotFound(Json.obj("error" -> "Category not found")))
  }

  def create: Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[Category].fold(
      _ => BadRequest("Invalid category format"),
      category => {
        categories += category
        Created(Json.toJson(category))
      }
    )
  }

  def update(id: Long): Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[Category].fold(
      _ => BadRequest("Invalid format"),
      updated => {
        categories.indexWhere(_.id == id) match {
          case -1 => NotFound
          case idx =>
            categories.update(idx, updated)
            Ok(Json.toJson(updated))
        }
      }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action {
    val index = categories.indexWhere(_.id == id)
    if (index >= 0) {
      categories.remove(index)
      NoContent
    } else NotFound
  }
}
