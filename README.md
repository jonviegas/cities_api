# :earth_americas: Brazilian Cities API
- - -
## :book: Introduction
This **REST API** provides a search and navigation system for information related to countries and locations in Brazil. In addition, it also provides the functionality of calculating the distance between two locations by their geographic points.
- - -
## :cloud: Cloud

The project is also hosted on *Heroku*, [click here](https://cities-system-api.herokuapp.com/cities) to access it.
- - -
## :green_book: Documentation

[Click here](https://cities-system-api.herokuapp.com/swagger-ui.html) to access the complete documentation made with *swagger*.

---
## :paperclip: Data

Records added to the database can be found [here](https://github.com/chinnonsantos/sql-paises-estados-cidades/tree/master/PostgreSQL).
- - -
## :computer: Usage

- ### **Overview**
  This **API** has three instances:
  - ```/countries```
  - ```/states```
  - ```/cities```

- ### **Find All**
    ```/{instance}```
    Returns all records.

- ### **Find By Id**
    ```/{instance}/{id}```
    Returns a record, if exists, by its id.

- ### **Find By Name**
  ```/{instance}/search/{name}```
    Search and returns all records that match the typed entry.

- ### **Find Cities By State**
  ```/cities/search/{stateUf}```
    Search and returns all cities belonging to the current state

- ### **Pagination**
  By default, queries related to **"search"** and **"find all"** return up to 20 records found.
  
  - **To page forward:**
  ```/{instance}?page={pageNumber}```
  
  - **To change the total of returned records**:
  ```/{instance}?size={totalRecords}```

- ### **Sorting**
  To perform an ordered query, choose an attribute of the current instance that you want to use as a parameter.
  
  ```/{instance}?sort={attribute}```

- ### **Distance Calculation**

  - **By Points:**
  ```cities/distance?by=points&from={city1Id}&to={city2Id}```
  
  - **By Cube**: 
  ```cities/distance?by=cube&from={city1Id}&to={city2Id}```
