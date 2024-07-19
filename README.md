# Finby test task

Catalog API 

## Task Requirements

Develop a Java web application, that allows you to work with product cards in the online shop.

Create a database with information about the products. Fill in the initial test data. The database should contain the following information about the products:

* Product Name
* Brand
* Model
* Quantity
* Weight
* Rating
* Product category
* Description
* Color
* Price
* Name of the image
* Image
* Features (list)

The application has to have the following functionality:
1. CRUD Endpoints for Products. URl: .../api/products (see the example of a Json request)
1.1. When creating or changing the product, the "Description" field must change either. It must contain a brief description of all project's main properties. For example, **Beautiful headphones Acme, color: black. Active noise reduction, Folding design, Built-in microphone.**
2. Image processing functionality - select an image with a background, save the source image in **resources** folder. Then an API request has to be made with the original image to **https://www.remove.bg/api** endpoint. The response from the API is an image without the background. Save this image (it is up to the applicant to choose the directory where to store the processed image), add the processed image to the appropriate field in the product card.

Json body sample:

{
"product": {
"name": "BeauBeautiful headphones",
"description": "Beautiful headphones Acme, color: black. Active noise reduction, Folding design, Built-in microphone.",
"price": 49.99,
"color": "black",
"brand": "Acme",
"category": "Electronics",
"availability": true,
"rating": 4.5,
"image_url": "https://example.com/images/headphones.jpg ",
"weight": "200 g",
"warranty": "2 years",
 "special_features": [
"Active noise reduction",
"Folding design",
"Built-in microphone"
]
}
}

Additional Tasks (optional):
* Endpoint for a product card with the highest rating.
* Endpoint for a product card with the most expensive/cheapest product.		
* Upload and store multiple images for a single product.

## Technology stack

* **Gradle**
* **Spring Boot 3.2.5**
* **Java 17+**
* **Packaging Jar**
* **H2 or SQLite database**
* **logs**

There're the steps to unpack my project: 

* git clone FinbyTestTask

* Open it via your IDE

* Connect to your database via application.yml file

## Usage

Build and run application:

* ./gradlew clean build

* Go to the **build/libs**  directory and run **java -jar FinbyTestTask-0.0.1-SNAPSHOT.jar**

* Open **http://localhost:8080/api/swagger-ui/index.html** to see all the available endpoints and entities

## Peculiarities

The task states, that there have to be no files, that are not related to the project. However, I decided to commit gradle-wrapper files in case someone has no gradle installed. 

Also you'll find text documents in src/main/resources/images/(processed/source)-images. They serve as a placeholder for absent images without which git won't commit previously mentioned directories.
