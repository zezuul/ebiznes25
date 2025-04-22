package data

import (
	"app/models"

	"gorm.io/gorm"
)

func SeedWeather(db *gorm.DB) {
	initialData := []models.Weather{
		{Location: "London", Temperature: 0.0, Description: "Init"},
		{Location: "New York", Temperature: 0.0, Description: "Init"},
	}
	for _, w := range initialData {
		db.Create(&w)
	}
}
