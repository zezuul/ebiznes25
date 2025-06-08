from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.alert import Alert
import time, os

# Ustawienia
BASE_URL = "http://localhost:5173"  # Adres frontendu React

def setup():
    driver_path = r"C:\Users\julia\Desktop\sem02mgr\ebiznes25\task6\googletest\chromedriver-win64\chromedriver.exe"
    print("Szukam w: ", driver_path)
    service = Service(driver_path)
    driver = webdriver.Chrome(service=service)
    driver.get(BASE_URL)
    driver.maximize_window()
    time.sleep(5)
    return driver

def teardown(driver):
    driver.quit()

# 1. Czy produkty się wyświetlają?
def test_products_visible():
    driver = setup()
    products = driver.find_elements(By.TAG_NAME, "h4")
    assert len(products) > 0, "Brak produktów"
    teardown(driver)

# 2. czy wyswietla sie 5 produktow?
def test_products_number():
    driver = setup()
    products = driver.find_elements(By.TAG_NAME, "h4")
    assert len(products) == 5, f"mam {products} produktów, zamiast 5."

# 3. dodaj produkt do koszyka
def test_add_to_cart():
    driver = setup()
    try:
        WebDriverWait(driver, 5).until(
            EC.presence_of_element_located((By.TAG_NAME, "button"))
        )
        # Kliknij pierwszy przycisk "Dodaj do koszyka"
        buttons = driver.find_elements(By.TAG_NAME, "button")
        buttons[0].click()

        # Przejdź do koszyka
        driver.find_element(By.LINK_TEXT, "Koszyk").click()
        time.sleep(4)

        items = driver.find_elements(By.TAG_NAME, "div")
        assert len(items) > 0, "Koszyk jest pusty mimo dodania produktu"
    finally:
        teardown(driver)


# 4. Koszyk początkowo pusty
def test_cart_initially_empty():
    driver = setup()
    cart_link = driver.find_element(By.LINK_TEXT, "Koszyk")
    cart_link.click()
    time.sleep(1)

    # Check if the "Pusty koszyk" message is visible
    msg = driver.find_element(By.TAG_NAME, "p").text
    assert "pusty" in msg.lower(), f"Oczekiwano pustego koszyka, znaleziono: {msg}"
    teardown(driver)

# 5. Przejście do widoku płatności
def test_navigate_to_payment():
    driver = setup()
    driver.find_element(By.LINK_TEXT, "Płatność").click()
    assert "payment" in driver.current_url
    teardown(driver)

# 6. Płatność z pustym koszykiem (oczekiwana obsługa)
def test_payment_with_empty_cart():
    driver = setup()
    driver.find_element(By.LINK_TEXT, "Płatność").click()
    msg = driver.find_element(By.TAG_NAME, "div").text
    assert "brak produktów" in msg.lower() or "koszyk" in msg.lower()
    teardown(driver)

# 7. Pełny flow: dodanie i płatność
def test_full_flow():
    driver = setup()

    # 1. Dodaj pierwszy produkt
    buttons = driver.find_elements(By.TAG_NAME, "button")
    assert buttons, "Nie znaleziono żadnych przycisków produktów"
    buttons[0].click()

    # 2. Przejdź do płatności
    driver.find_element(By.LINK_TEXT, "Płatność").click()
    time.sleep(1)

    # 3. Kliknij przycisk "Zapłać"
    pay_buttons = driver.find_elements(By.TAG_NAME, "button")
    pay_clicked = False
    for btn in pay_buttons:
        if "Zapłać" in btn.text:
            btn.click()
            pay_clicked = True
            break
    assert pay_clicked, "Nie znaleziono przycisku 'Zapłać'"
    time.sleep(1)

    # 4. Obsłuż alert
    try:
        alert = Alert(driver)
        alert_text = alert.text
        assert "płatność przetworzona" in alert_text.lower(), f"Alert nie zawiera oczekiwanego tekstu: {alert_text}"
        alert.accept()
        
    except Exception as e:
        raise

    teardown(driver)

# 8. Czy suma kwoty się aktualizuje
def test_payment_sum_updates():
    driver = setup()
    driver.find_elements(By.TAG_NAME, "button")[0].click()
    driver.find_element(By.LINK_TEXT, "Płatność").click()
    pay_button = driver.find_element(By.TAG_NAME, "button")
    assert "zł" in pay_button.text
    teardown(driver)

# 9. Czy przycisk zapłać jest zablokowany gdy koszyk pusty
def test_disabled_pay_button_when_cart_empty():
    driver = setup()
    driver.find_element(By.LINK_TEXT, "Płatność").click()
    pay_button = driver.find_element(By.TAG_NAME, "button")
    assert not pay_button.is_enabled()
    teardown(driver)

# 10. Przycisk „Dodaj do koszyka” działa
def test_add_button_clickable():
    driver = setup()
    btn = driver.find_elements(By.TAG_NAME, "button")[0]
    assert btn.is_enabled()
    btn.click()
    teardown(driver)

# 11. Koszyk aktualizuje się po dodaniu produktu
def test_cart_updates_after_adding():
    driver = setup()
    driver.find_elements(By.TAG_NAME, "button")[0].click()
    driver.find_element(By.LINK_TEXT, "Koszyk").click()
    items = driver.find_elements(By.TAG_NAME, "div")
    assert any("zł" in el.text for el in items)
    teardown(driver)

# 12. Przycisk płatności znika po płatności
def test_pay_button_disappears_after_payment():
    driver = setup()
    driver.find_elements(By.TAG_NAME, "button")[0].click()
    driver.find_element(By.LINK_TEXT, "Płatność").click()
    time.sleep(1)
    driver.find_element(By.TAG_NAME, "button").click()
    try:
        Alert(driver).accept()
    except:
        pass
    driver.refresh()
    btn = driver.find_element(By.TAG_NAME, "button")
    assert not btn.is_enabled()
    teardown(driver)

# 13. Powrót do strony płatności po płatności
def test_redirect_after_payment():
    driver = setup()
    driver.find_elements(By.TAG_NAME, "button")[0].click()
    driver.find_element(By.LINK_TEXT, "Płatność").click()
    driver.find_element(By.TAG_NAME, "button").click()
    try:
        Alert(driver).accept()
    except:
        pass
    time.sleep(1)
    assert "payment" in driver.current_url
    teardown(driver)

# 14. Produkty zawierają nazwę i cenę
def test_product_contains_name_and_price():
    driver = setup()
    items = driver.find_elements(By.TAG_NAME, "div")
    found = False
    for el in items:
        if "zł" in el.text and any(c.isalpha() for c in el.text):
            found = True
            break
    assert found
    teardown(driver)

# 15. Pusta strona koszyka nie pokazuje produktów
def test_empty_cart_has_no_items():
    driver = setup()
    driver.find_element(By.LINK_TEXT, "Koszyk").click()
    items = driver.find_elements(By.TAG_NAME, "h4")
    assert len(items) == 0
    teardown(driver)

# 16. Strona płatności zawiera nagłówek
def test_payment_page_has_header():
    driver = setup()
    driver.find_element(By.LINK_TEXT, "Płatność").click()
    header = driver.find_element(By.TAG_NAME, "h2").text
    assert "płatność" in header.lower()
    teardown(driver)

# 17. Strona koszyka zawiera nagłówek
def test_cart_page_has_header():
    driver = setup()
    driver.find_element(By.LINK_TEXT, "Koszyk").click()
    header = driver.find_element(By.TAG_NAME, "h2").text
    assert "koszyk" in header.lower()
    teardown(driver)

# 18. Po płatności koszyk jest pusty
def test_cart_is_cleared_after_payment():
    driver = setup()
    driver.find_elements(By.TAG_NAME, "button")[0].click()
    driver.find_element(By.LINK_TEXT, "Płatność").click()
    driver.find_element(By.TAG_NAME, "button").click()
    try:
        Alert(driver).accept()
    except:
        pass
    driver.find_element(By.LINK_TEXT, "Koszyk").click()
    msg = driver.find_element(By.TAG_NAME, "p").text
    assert "pusty" in msg.lower()
    teardown(driver)

# 19. Widoczność przycisków „Dodaj do koszyka”
def test_add_to_cart_buttons_visible():
    driver = setup()
    buttons = driver.find_elements(By.TAG_NAME, "button")
    assert any("dodaj" in btn.text.lower() for btn in buttons)
    teardown(driver)

# 20. Sprawdzenie działania przycisku „Koszyk”
def test_cart_navigation_works():
    driver = setup()
    driver.find_element(By.LINK_TEXT, "Koszyk").click()
    assert "koszyk" in driver.page_source.lower()
    teardown(driver)

if __name__ == "__main__":
    import inspect

    test_functions = [
        obj for name, obj in globals().items()
        if inspect.isfunction(obj) and name.startswith("test_")
    ]

    passed = []
    failed = []

    print("\nRozpoczynam testowanie...\n")

    for test_func in test_functions:
        test_name = test_func.__name__
        try:
            test_func()
            print(f"{test_name} — OK")
            passed.append(test_name)
        except AssertionError as e:
            print(f"{test_name} — FAIL")
            print(f"Błąd: {e}")
            failed.append(test_name)
        except Exception as e:
            print(f"{test_name} — ERROR")
            print(f"Wyjątek: {e}")
            failed.append(test_name)

    print("\nPodsumowanie:")
    print(f"Udane: {len(passed)}")
    print(f"Nieudane: {len(failed)}")

    if failed:
        print("Nieudane testy:")
        for name in failed:
            print(f"     - {name}")
    else:
        print("Wszystkie testy zakończone sukcesem!")

    print("\nTesty zakończone.\n")

