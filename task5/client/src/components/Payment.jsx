import { useCart } from '../context/CartContext';
import axios from 'axios';

const Payment = () => {
  const { cartItems, clearCart } = useCart();

  const handlePayment = () => {
    axios.post('http://localhost:8080/payment', { items: cartItems })
      .then(() => {
        alert("Płatność przetworzona!");
        clearCart();
      })
      .catch((err) => console.error(err));
  };

  return (
    <div>
      <h2>Płatność</h2>
      <button onClick={handlePayment} disabled={cartItems.length === 0}>
        Zapłać {cartItems.reduce((sum, i) => sum + i.price, 0)} zł
      </button>
    </div>
  );
};

export default Payment;
