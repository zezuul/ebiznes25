import { useCart } from '../context/CartContext';

const Cart = () => {
  const { cartItems } = useCart();

  return (
    <div>
      <h2>Koszyk</h2>
      {cartItems.length === 0 ? <p>Pusty koszyk</p> : (
        cartItems.map((item, i) => (
          <div key={i}>
            {item.name} - {item.price} zł
          </div>
        ))
      )}
    </div>
  );
};

export default Cart;
