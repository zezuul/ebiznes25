Zadanie 10 Chmura/CI

Należy wykorzystać GitHub Actions (dopuszczalne są inne rozwiązania
CI) oraz chmurę Azure (dopuszczalne inne chmury), aby zbudować oraz zdeployować aplikację kliencką (frontend) oraz serwerową (backend) jako osobne dwie aplikacje. Należy do tego wykorzystać obrazy dockerowe, a aplikacje powinny działać na kontenerach. Dopuszczalne jest zbudowanie wcześniej aplikacji (jar package) oraz budowanie aplikacji via Github Actions. Należy zwrócić uwagę na zasoby dostępne na chmurze.

3.0 Należy stworzyć odpowiednie instancje po stronie chmury na dockerze
3.5 Stworzyć odpowiedni pipeline w Github Actions do budowania aplikacji (np. via fatjar)
4.0 Dodać notyfikację mailową o zbudowaniu aplikacji
4.5 Dodać krok z deploymentem aplikacji serwerowej oraz klienckiej na chmurę
5.0 Dodać uruchomienie regresyjnych testów automatycznych
(funkcjonalnych) jako krok w Actions
