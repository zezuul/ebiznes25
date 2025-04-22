package proxy

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
)

type WeatherResponse struct {
	Current struct {
		Temperature float64 `json:"temperature"`
		WeatherDescriptions []string `json:"weather_descriptions"`
	} `json:"current"`
}

func GetWeatherFromAPI(location string) (float64, string, error) {
	apiKey := "" 
	url := fmt.Sprintf("http://api.weatherstack.com/current?access_key=%s&query=%s", apiKey, location)

	resp, err := http.Get(url)
	if err != nil {
		return 0, "", err
	}
	defer resp.Body.Close()

	body, _ := ioutil.ReadAll(resp.Body)

	var weatherResp WeatherResponse
	if err := json.Unmarshal(body, &weatherResp); err != nil {
		return 0, "", err
	}

	return weatherResp.Current.Temperature, weatherResp.Current.WeatherDescriptions[0], nil
}