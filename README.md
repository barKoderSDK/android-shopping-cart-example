# Barkoder Shopping Cart

## Overview

“Barkoder Shopping Cart” is an Android application that utilizes the [barKoder Barcode Scanner SDK](https://barkoder.com/barcode-scanner-sdk) to enable users to create and manage product lists efficiently. The application allows users to create products, organize them into lists, and use the barcode scanner to quickly check items off their lists. 

**This app is for demonstration and evaluation purposes of the barKoder SDK. It's not inteded as a production app, rather as a quick-start how-to for the android platform.**

### [barKoder Barcode Scanner SDK](https://barkoder.com/barcode-scanner-sdk)

The barKoder Barcode Scanner SDK grants an easy to use solution with a great and completely customizable interface that can be instantly integrated in both iOS and Android apps.

The [barKoder Barcode Scanner SDK](https://barkoder.com/barcode-scanner-sdk) will fully transform the user's smartphones and tablets that deploy your Enterprise and Consumer apps into rugged barcode scanning devices without the need to procure and maintain expensive and sluggish hardware devices that have a very short life span.


#### Symbologies

Even though the barKoder barcode scanner SDK is a relatively new product, it is already more advanced than other competitor API's. Its robust barcode reading engine can be used to read the content of the most widely used barcodes with lightning fast speed and unprecedented recognition rate:

* 1D - [Codabar](https://barkoder.com/barcode-types/codaba), [Code 11](https://barkoder.com/barcode-types/code-11), [Code 25](https://barkoder.com/barcode-types/code-25), [Code 39](https://barkoder.com/barcode-types/code-39), [Code 93](https://barkoder.com/barcode-types/code-93), [Code 128](https://barkoder.com/barcode-types/code-128), [DotCode](https://barkoder.com/barcode-types/dotcode), [EAN-8](https://barkoder.com/barcode-types/ean-upc-code), [EAN-13](https://barkoder.com/barcode-types/ean-upc-code), [Interleaved 2 of 5](https://barkoder.com/barcode-types/code-25), [ITF-14](https://barkoder.com/barcode-types/code-25), [MSI Plessey](https://barkoder.com/barcode-types/msi-plessey), [Pharmacode](https://barkoder.com/barcode-types/code-32), [Telepen](https://barkoder.com/barcode-types/telepen), [UPC-A](https://barkoder.com/barcode-types/ean-upc-code) & [UPC-E](https://barkoder.com/barcode-types/ean-upc-code)

* 2D - [Aztec Code](https://barkoder.com/barcode-types/aztec), [Aztec Compact](https://barkoder.com/barcode-types/aztec), [Data Matrix](https://barkoder.com/barcode-types/data-matrix), [PDF417](https://barkoder.com/barcode-types/pdf417), [Micro PDF417](https://barkoder.com/barcode-types/pdf417), [QR Code](https://barkoder.com/barcode-types/qr-code) & [Micro QR Code](https://barkoder.com/barcode-types/qr-code)

#### Features

The [barKoder SDK](https://barkoder.com/) features multiple algorithms that handle a wide variety of barcode scanning scenarios with unprecedented performance in terms of speed and success rate: 
* [DPM Mode](https://barkoder.com/dpm-barcode-scanner-sdk) - Specially designed scanning template for decoding Data Matrix barcodes engraved using any Direct Part Marking (DPM) technique;
* [MatrixSight](https://barkoder.com/matrixsight) - Proprietary algorithm that can successfully scan QR Codes or Data Matrix barcodes even when they are missing their finder, timing and/or alignment patterns, even part of the data elements;
* [Segment Decoding](https://barkoder.com/segment-decoding) - The advanced barcode localization techniques implemented into the barKoder SDK grants an ability to recognize 1D barcodes that have significant deformations along their Z axis, getting especially handy when trying to recognize barcodes found on test tubes, bottles and other surfaces with rounded, curved, hollowed or otherwise irregular shapes;
* [VIN Barcode Scanning Mode](https://barkoder.com/vin-scanning-mode) - The most advanced VIN barcode scanning mode on the market, utilizing all the special algorithms of the barKoder SDK leading to the ultimate scanning experience of any kind of barcodes used for embedding Vehicle Identification Numbers, including Code 39, Code 128, QR Code and Data Matrix;
* [DeBlur Mode](https://barkoder.com/deblur-mode) - Whether there's lens, motion or focus blur present in EAN or UPC barcodes, the barKoder DeBlur Mode alleviates it fully and doesn't allow the scanning experience to suffer;
* [PDF417-LineSight](https://barkoder.com/pdf417-linesight) - The robust PDF417 barcode scanner SDK that is offered by barKoder can detect even the most severely damaged PDF417 codes, including missing their start and stop patterns, stop row indicators or even entire data columns, making it the sublime choice for apps that need to reliably scan US or Canadian driver's licenses, South African vehicle license discs or driver's licenses, as well as various types of ID's such as Military, Argentinian, Colombian or South African Smart ID Cards.

You can check out our free demo app Barcode Scanner by barKoder available both via [Apple App Store](https://apps.apple.com/us/app/barkoder-scanner/id6443715409?uo=2) & [Google Play Store](https://play.google.com/store/apps/details?id=com.barkoder.demoscanner).

Our SDK is available for varoioius platforms and frameworks. We currently support:

- Android
- iOS
- Flutter
- React Native
- Capacitor
- Xamarin

You can find any of these at one place [here](https://barkoder.com/repository), and by [registering on our portal](https://barkoder.com/register) you can get access to trial licenses, production licenses and 24/7 support for integration.
Alternatively you can download the android-sdk from [barKoder Android Barcode Scanner SDK](https://github.com/barKoderSDK/android-sdk) via Github.
Also you can check other code we have hosted [here](https://barkoder.com/barKoderSDK)

### Free Developer Support

As mentioned, our support is completely free for integration or testing purposes and granted through the [barKoder Portal](https://barkoder.com/login). After registering and logging into your account, you only need to submit a [Support Issue](https://barkoder.com/issues). Alternatively, you can contact us by email via support@barkoder.com.

## App Features

* Product Creation: Easily create and add new products to the application.
* List Management: Organize products into lists for better categorization.
* Barcode Scanner: Use the built-in barcode scanner to quickly check items off lists.

## Getting Started

Prerequisites
Android Studio installed

[Android Studio Download](https://developer.android.com/studio?gclid=Cj0KCQiAtaOtBhCwARIsAN_x-3IkKiAwpKoA9dZYWAjvum_-6tuKIIkKDg5ryev0EMIZZ0cFKr4y_6QaAhYpEALw_wcB&gclsrc=aw.ds)

### Installation
1. Clone the repository.
```bash
   git clone https://github.com/barKoderSDK/android-shopping-cart-example.git
   ```

Open the project in Android Studio.
Build and run the application on an Android emulator or a physical device.

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

