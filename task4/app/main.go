package main

import (
	"app/controller"
	"app/config"
	"app/models"

	"github.com/labstack/echo/v4"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

func main() {
	// Init DB
	db, err := gorm.Open(sqlite.Open("weather.db"), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}

	db.AutoMigrate(&models.Weather{})
	data.SeedWeather(db)

	// Start Echo
	e := echo.New()
	e.GET("/weather", controllers.GetWeather(db))
	e.Logger.Fatal(e.Start(":8080"))
}
