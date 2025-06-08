package main

import (
	"encoding/json"
	"net/http"
)

type Product struct {
	ID    int     `json:"id"`
	Name  string  `json:"name"`
	Price float64 `json:"price"`
}

func enableCORS(w http.ResponseWriter) {
	w.Header().Set("Access-Control-Allow-Origin", "*") // UWAGA: w produkcji lepiej nie używać "*"
	w.Header().Set("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
	w.Header().Set("Access-Control-Allow-Headers", "Content-Type")
}

func getProducts(w http.ResponseWriter, r *http.Request) {
	enableCORS(w)

	if r.Method == "OPTIONS" {
		return
	}

	products := []Product{
		{1, "Kawa", 10.99},
		{2, "Herbata", 7.50},
        {3, "Woda", 1.50},
        {4, "Sok pomaranczowy", 6.50},
        {5, "Woda gazowana", 2.00},
	}
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(products)
}

func handlePayment(w http.ResponseWriter, r *http.Request) {
	enableCORS(w)

	if r.Method == "OPTIONS" {
		return
	}

	var items []Product
	json.NewDecoder(r.Body).Decode(&items)
	// Tu logika płatności lub zapis do bazy
	w.WriteHeader(http.StatusOK)
}

func main() {
	http.HandleFunc("/products", getProducts)
	http.HandleFunc("/payment", handlePayment)
	http.ListenAndServe(":8080", nil)
}
