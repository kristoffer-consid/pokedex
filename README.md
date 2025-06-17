# 🎒 Mini Pokédex

Welcome to the **Mini Pokédex** – a kid-friendly and parent-approved Android app designed to showcase Pokémon in a fun and informative way. Built as part of a 2-day (max 8 hours) coding assignment using **Jetpack Compose** and **Kotlin**, this app leverages the [PokeAPI](https://pokeapi.co/docs/v2) to provide delightful Pokémon experiences for families.

---

## ✨ Features

* 🔀 **Random Pokémon Generator**
* 🔎 **Smart Search** with fuzzy search
* 📚 **Pokedex View** to see all Pokémon
* 🌱 **Evolution Chains and Requirements**
* ⭐ **Favorite System** using local Room database
* 🎨 **Kid-friendly Design** using Material 3

---

## ⚙️ How to Run

1. Clone the repository:

   ```bash
   git clone https://github.com/kristoffer-consid/pokedex.git
   ```
2. Open in **Android Studio**
3. Let Gradle sync and index the project
4. Click **Run** to launch on your emulator or device

---

There is also a debug built .apk file in the releases tab which can be installed on an Android device if you download the file

---

## 📚 Tech Stack
* **Hilt (Dependency Injection)**: Built on top of dagger to and makes it easier to handle dependency injection
* **Compose Destinations**: A type-safe and simple way to handle screen navigation
* **Accompanist System UI Controller**: Used to dynamically style status and nav bars for immersive UIs
* **PokeKotlin**: A simple wrapper around PokeAPI v2
* **Room Database**: Used to store and manage bookmarked Pokémon 
* **Coil**: Fast image loading
* **Apache Commons Text**: Includes a Jaro Winkler implementation I utilized for fuzzy searching pokemon names

---

## 💡 Future Improvements

* Use the GraphQL support from PokeAPI to let users search pokemon by color and shape to help a parent
* Add animations and pokemon sound effects
* Add support for all evolution types
* Replace coil with glide for a supposed performance boost
