package controllers

import (
	"net/http"
	"strings"
	"app/models"
	"app/proxy"

	"github.com/labstack/echo/v4"
	"gorm.io/gorm"
)

type WeatherResponse struct {
	Location    string  `json:"location"`
	Temperature float64 `json:"temperature"`
	Description string  `json:"description"`
}

func GetWeather(db *gorm.DB) echo.HandlerFunc {
	return func(c echo.Context) error {
		locationsParam := c.QueryParam("locations")
		if locationsParam == "" {
			return c.JSON(http.StatusBadRequest, echo.Map{"error": "Missing locations param"})
		}

		locations := strings.Split(locationsParam, ",")

		var response []WeatherResponse

		for _, loc := range locations {
			temp, desc, err := proxy.GetWeatherFromAPI(loc)
			if err != nil {
				continue
			}

			weather := models.Weather{
				Location:    loc,
				Temperature: temp,
				Description: desc,
			}
			db.Create(&weather)
			
			response = append(response, WeatherResponse{
				Location:    loc,
				Temperature: temp,
				Description: desc,
			})
		}

		return c.JSON(http.StatusOK, response)
	}
}
