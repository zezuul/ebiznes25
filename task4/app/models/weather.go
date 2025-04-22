package models

import "gorm.io/gorm"

type Weather struct {
	gorm.Model
	Location   string
	Temperature float64
	Description string
}
