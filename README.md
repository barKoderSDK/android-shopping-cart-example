# Barkoder Shopping Cart

## Overview

“Barkoder Shopping Cart” is an Android application that utilizes the SDK barKoder Scanner to enable users to create and manage product lists efficiently. The application allows users to create products, organize them into lists, and use the barcode scanner to quickly check items off their lists.

## Features

* Product Creation: Easily create and add new products to the application.
* List Management: Organize products into lists for better categorization.
* Barcode Scanner: Use the built-in barcode scanner to quickly check items off lists.

## Getting Started

Prerequisites
Android Studio installed

### Installation
1. Clone the repository.
```bash
   git clone https://github.com/barKoderSDK/android-shopping-cart-example.git
   ```

Open the project in Android Studio.
Build and run the application on an Android emulator or physical device.

## Usage

* Home Page:
Upon launching the app, you will be greeted with the home page, providing an overview of the available features.
* Product Creation:
Navigate to the "Create Product" section to add new products to your inventory.
Tap on the "Create Product" option in the navigation menu.
Fill in the product details such as image, name, barcode, unit, quantity, price.
Save the product, and it will be added to your My Products.
* Products Overview:
View all your created products in the "My Products" section.
Tap on the "My Products" option in the navigation menu.
Here, you can see a comprehensive list of all your products with their details.
Edit or delete products as needed.
* Lists Creation:
Move to the "My Lists" section to organize products into lists.
Tap on the button create (+).
Give your list a name and add products from your product inventory.
Save the list, and it will appear in the "Lists" fragment.
* Lists Overview:
Access the "Lists" fragment to view all the lists you have created.
Tap on a specific list to see its contents and make any necessary modifications.
Lists are displayed with the products they contain, providing a clear overview.
* Checking Lists with barKoder Scanner:
In the "Lists" fragment, tap on the list you want to check off.
You will be redirected to the "Checklist" fragment for the selected list.
Tap on the Scan button.
Utilize the built-in barKoder scanner to efficiently mark items as checked.
Scan the barcode of each product in the list, and the app will update the status accordingly.
* Navigation:
* Use the bottom navigation bar to switch between different fragments:
"Home" for the main overview.
"Create Product" to add new products.
"My Products" to manage your products.
"My Lists" to create list or tap on list to checkout it.

