Dokumentacja Projektu:
,, Personal Trainer Assistant’’

Przygotowali :
Mateusz Majewski 
Kamil Kubas

1. Informacje ogólne
Nazwa projektu: Personal Trainer Assistant
Autor: (Twoje Imię i Nazwisko)
Wersja Javy: 25
Typ aplikacji: Desktopowa (JavaFX)
Baza danych: SQLite

3. Opis systemu
Aplikacja służy do inteligentnego generowania planów treningowych oraz monitorowania postępów zdrowotnych użytkownika.
 Głównym wyróżnikiem systemu jest algorytm dopasowujący ćwiczenia do kontuzji użytkownika (kolana, plecy, barki) oraz dostępnego sprzętu.
Kluczowe funkcjonalności:
Personalizacja planów: Generowanie treningów na podstawie stażu, dostępnego sprzętu oraz celu (masa/redukcja).
Bezpieczeństwo (Filtry medyczne): Wykluczanie ćwiczeń obciążających stawy w przypadku zgłoszonych kontuzji (plecy, kolana, barki).
System Motywacyjny (Streak): Rejestrowanie regularności treningowej i zapisywanie jej w profilu użytkownika.
Monitorowanie Biometryczne: Śledzenie zmian masy ciała i wizualizacja danych na wykresie liniowym.
Archiwizacja: Możliwość trwałego zapisu wygenerowanych planów i ich późniejszego wglądu.

4. Stos Technologiczny i Biblioteki
Java 25: Wykorzystanie najnowszych standardów języka.
JavaFX & FXML: Budowa nowoczesnego, reaktywnego interfejsu użytkownika.
Maven: Zarządzanie zależnościami i cyklem życia projektu.
JDBC SQLite: Obsługa lokalnej bazy danych bez konieczności instalacji serwera zewnętrznego.
CSS:Stylizacja całego projektu, zadbanie o jego przejrzystość i innowacyjny wygląd
5. Architektura i Struktura Projektu
Aplikacja została zbudowana w oparciu o wzorzec MVC (Model-View-Controller), co pozwala na separację logiki
biznesowej od interfejsu użytkownika. Dodatkowo zastosowano wzorzec DAO (Data Access Object) do obsługi warstwy bazodanowej.


Struktura pakietów:
Java-gym-project/
├── 📁 .mvn/                  # Pliki konfiguracyjne wrappera Maven
├── 📄 pom.xml                # Konfiguracja Mavena, zależności (JavaFX, SQLite)
└── 📁 src/main/
    ├── 📁 java/
    │   └── 📁 com.example.menu1/
    │       ├── 📁 controller/        # Warstwa Kontrolerów (Logika UI)
    │       ├── 📁 dao/               # Warstwa Dostępu do Danych (SQL)
    │       ├── 📁 model/             # Warstwa Modeli (Dane i Enumy)
    │       ├── 📁 util/              # Klasy pomocnicze (Narzędziowe)
    │       ├── 📄 DatabaseMenager    # Zarządzanie połączeniem z bazą
    │       ├── 📄 MeinApplication    # Główna klasa JavaFX
    │       ├── 📄 MeinLauncher       # Launcher (obejście problemów z modułami)
    │       └── 📄 PlanSelector       # Logika wyboru treningu
    └── 📁 resources/
        └── 📁 com.example.menu1/
            ├── 📁 db/                # Skrypty SQL i baza danych .db
            ├── 📁 photo/             # Zasoby graficzne (ikony, obrazy)
            ├── 📄 style.css          # Arkusz stylów (Carbon & Ember)
            └── 📄 *.fxml             # Pliki widoków (Layouty)

Opis Warstw Aplikacji

1. Warstwa Kontrolerów (controller)
Obsługuje interakcje użytkownika i zarządza przepływem informacji między widokiem a bazą danych.
LoginController / RegisterController: Zarządzanie dostępem do aplikacji.
UserPanelController: Główny pulpit (Dashboard) z licznikiem streak.
SurveyController: Obsługa kwestionariusza i zbieranie preferencji treningowych.
ResultController: Prezentacja wygenerowanego planu i jego zapis.
ProgressMonitorController: Obsługa wykresów postępu wagi.

2. Warstwa Dostępu do Danych (dao)
Izoluje zapytania SQL od reszty kodu, co ułatwia konserwację bazy.
ConfigUserDatabase: Konfiguracja parametrów bazy SQLite.
ExerciseDao: Pobieranie ćwiczeń z bazy z uwzględnieniem filtrów (kontuzje, sprzęt).
PlanyDao: Zapisywanie i odczytywanie gotowych planów treningowych.
MuscleDao: Zarządzanie danymi o grupach mięśniowych.

3. Warstwa Modeli (model)
Definiuje strukturę danych i reguły biznesowe.
User: Reprezentacja profilu użytkownika (ID, nick, streak).
Exercise: Szczegółowe dane o konkretnym ćwiczeniu.
SurveyResult / PlanResult: Kontenery na dane z ankiety i wyniki generatora.
Enamy (Goal, Level, Equipment, MuscleGroup): Stałe wartości słownikowe, które zapobiegają błędom w logice wyboru.

4. Zasoby (resources)
view-*.fxml: Definicje interfejsu (np. view-user-panel.fxml).
style.css: Stylizacja Twoich przycisków, tabel i wykresów w ciemnym motywie.
user_data.db: Lokalny plik bazy danych przechowujący wszystkie informacje.




 Przepływ Danych w Aplikacji
Start: MeinLauncher uruchamia aplikację.
Sesja: UserSession (w pakiecie util) zapamiętuje, kto jest zalogowany.
Logika: Kontroler odbiera akcję (np. kliknięcie "Done Training"), aktualizuje model User, a DatabaseMenager przesyła zmianę do pliku .db.
Widok: JavaFX odświeża etykiety i wykresy na podstawie zaktualizowanych modeli.


5. Model Bazy Danych
System przechowuje dane w pliku user_data.db. Inicjalizacja odbywa się poprzez skrypty schema.sql (struktura) oraz seed.sql (dane początkowe ćwiczeń).
Główne tabele:
users: Dane kont, hasła oraz pole workout_streak (licznik dni).
exercises: Katalog ćwiczeń z parametrami trudności i wymaganego sprzętu.
training_plans: Zapisane, wygenerowane plany treningowe użytkowników.

6. Kluczowe Algorytmy i Funkcjonalności
   
6.1. Algorytm Generowania Planu (ResultController + ExerciseDao)
Algorytm tworzy harmonogram tygodniowy w formie List<List<Exercise>>. Proces przebiega następująco:
Filtrowanie kontuzji: Jeśli użytkownik zgłosi ból pleców czy kolan, ćwiczenia oznaczone jako obciążające te stawy są odrzucane.
Dopasowanie sprzętu: Algorytm wybiera tylko te ćwiczenia, które odpowiadają poziomowi wyposażenia użytkownika (np. brak sprzętu vs pełna siłownia).
Formatowanie widoku: Wynik jest prezentowany w TableView, gdzie nazwy ćwiczeń są dynamicznie prefiksowane numerem dnia treningowego.

6.2. Zarządzanie Sesją (UserSession)
Aplikacja śledzi zalogowanego użytkownika w całej sesji, co pozwala na:
Prawidłowe przypisywanie zapisanego planu do profilu.
Aktualizację liczników aktywności (Streak).
8. Instrukcja Obsługi


Po włączeniu aplikacji ukazuje się nam panel główny, w którym zalogujemy się już do istniejącego konta, lub jeśli jestesmy tu pierwszy raz założymy nowe konto użytkownika.

Aplikacja została zabezpieczona przed utworzeniem użytkownika o takim samym loginie ponieważ nie pozwoli na utworzenie drugiego użytkownika z tym samym nickiem i wyświetli 
nam o tym komunikat. To samo wydarzy się gdy podamy dwa różne hasła. Program poinformuje nas o tym że musimy podać poprawne hasło ponieważ te podane różnią się od siebie.
Istnieje też możliwość podglądu hasła wpisywanego zarówno w pierwszym jak i drugim polu. Po odpowiednim wypełnieniu pól rejestracyjnych możemy użyć przycisku Create Account, 
jeżeli wypełniliśmy je poprawnie otrzymamy komunikat o 
tym że konto zostało utworzone pomyślnie, tak jak zostało to przedstawione poniżej.
 
następnie należy wyjść przyciskiem Go Back do głównego menu i wybrać pole ZALOGUJ SIĘ.
Po poprawnym zalogowaniu się otrzymamy widok menu głównego panelu użytkownika 
Mamy tutaj dostęp do 3 zakładek. 
Create a new plan ( zakłada pozwala na utworzenie nowego planu poprzez wypełnienie kwestionariusza, aby uzyskać plan należy wypełnić wszystkie znajdujące się w nim pola. 
Mamy 5 głównych kategorii z czego każda z nich rozwija się i posiada checkboxy do wybrania. Sam proces podawania informacji do wygenerowania planu jest bardzo
intuicyjny i prostyw obsłudze. Po wypełnieniu kwestionariusza klikamy przycisk Generuj plan treningowy)


Zamieszczamy poniżej przykładowo utworzony plan przez nasz algorytm. Dostajemy informacje w postaci komunikatu o tym że plan został pomyślnie wygenerowany. Jeżeli wygenerowany plan odpowiada naszym oczekiwaniom możemy przypisać go do swojego konta użytkownika przez co po zalogowaniu będzie się znajdować w zakładce saved plans.

Saved plans ( po wejściu w tą zakładkę znajdziemy wszystkie wygenerowane plany przez nasz algorytm, które wybraliśmy do zapisania ponieważ nie zapisane plany nie zostaną tu przeniesione)

Monitor Progress ( w zakładce monitor progress znajdziemy wykres przedstawiający naszą wagę. Wpisana waga zapisuje się z datą, w którym został podany wynik. Dane przesyłane są i przechowywane w naszej bazie danych ma to na celu ułatwienie kontrolowania naszej wagi) 



Do każdego profilu możemy kontrolować swoje postępy w treningu i po każdym wykonanym dniu ćwiczeń odhaczyć sobie wykonanie danego treningu. Postępy pokazywane są w postaci liczbowej ile dni z rzędu zakończyliśmy swój trening.

Wszystkie postępu projektu były prowadzone poprzez Githuba dlatego załączamy link do niego. Znajdziemy w nim poszczególne commity, które wprowadzały kolejne zmiany podczas procesu powstawania naszej aplikacji.
Link do projektu na git hub:
Chinczykxk/Java-gym-project

