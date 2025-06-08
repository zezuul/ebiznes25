import { useEffect, useState } from 'react';
import { useCart } from '../context/CartContext';
import axios from 'axios';

const Products = () => {
  const [products, setProducts] = useState([]);
  const { addToCart } = useCart();

  useEffect(() => {
    axios.get('http://localhost:8080/products')
      .then((res) => {
        console.log("Produkty z backendu:", res.data);
        setProducts(res.data);
      })
      .catch((err) => console.error("Błąd axios:", err));
  }, []);
  

  return (
    <div style={{ display: 'flex', justifyContent: 'center' }}>
      <div style={{ maxWidth: '600px', width: '100%' }}>
        <h2>Produkty</h2>
        {products.length === 0 ? (
          <p>Brak produktów do wyświetlenia</p>
        ) : (
          products.map((p) => (
            <div key={p.id} style={{ border: '1px solid #ccc', marginBottom: '10px', padding: '10px', borderRadius: '8px' }}>
              <h4>{p.name}</h4>
              <p>{p.price} zł</p>
              <button onClick={() => addToCart(p)}>Dodaj do koszyka</button>
            </div>
          ))
        )}
      </div>
    </div>
  );
  
  
};

export default Products;
