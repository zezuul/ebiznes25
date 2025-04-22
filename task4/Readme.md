Zadanie 4: Wzorce strukturalne - Echo (Go)
Należy stworzyć aplikację w go na frameworku echo. Aplikacja ma mieć minimum jeden endpoint i min jedną funkcję prozy, która pobiera dane o pogodzie z zewnętrznego api (watherstack).

- aplikacja z kontrolerem pogody pozwalającym na pobieranie danych o pogodzie
- model pogoda wykorzystujący gorm, dane mają być załadowane z listy przy uruchomieniu
- klasa proxy, która pobiera dane z serwisu zewnętrznego podczas zapytania do kontrolera i te dane zostaną zwrócone i zapisane w bazie
- endpoint ma zostać rozszerzony o więcej niż jedną lokalizację zwracając jsona