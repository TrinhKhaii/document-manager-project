# Document Manager API
***
### User API:
1. **Find all and search multi users:** 
   * **URL**: `http://localhost:8080/api/users/list`
   * **Method:** `POST`
   * **Value:**
   ```php
   {
     "name": "",
     "pageNumber": 0,
     "elementNumber": 4
   }
   ```
2. **Create new users:** 
   * **URL**: `http://localhost:8080/api/users/save`
   * **Method:** `POST`
   * **Value:** 
    ```php
   {
      "userName": ""
   }
    ```
3. **Update users:** 
    * **URL**: `http://localhost:8080/api/users/update`
    * **Method:** `POST`
    * **Value:**
    ```php
   {
      "id": ,
      "userName": ""
   }
    ```
4. **Delete users:** 
    * **URL**: `http://localhost:8080/api/users/delete`
    * **Method:** `POST`
    * **Value:**
    ```php
   {
      "id": 
   }
    ```
5. **Find user by id:**
   * **URL**: `http://localhost:8080/api/users/find-by-id`
   * **Method:** `POST`
   * **Value:**
    ```php
   {
      "id": 
   }
    ```
6. **Find all and full text search users:**
   * **URL**: `http://localhost:8080/api/users/fts-list`
   * **Method:** `POST`
   * **Value:**
   ```php
   {
      "name": "",
      "pageNumber": 0,
      "elementNumber": 4
   }
   ```
***
### Incoming Document API:
1. **Find all and search multi incoming document:**
    * **URL**: `http://localhost:8080/api/incoming-document/list`
    * **Method:** `POST`
    * **Value:**
    ```php
   {
     "excerpt": "",
     "serialNumber": "",
     "fromDate": "",
     "toDate": "",
     "signerName": "",
     "page": 0,
     "elementNum": 4
    }
    ```
2. **Find all and search multi incoming document DTO:**
   * **URL**: `http://localhost:8080/api/incoming-document/list-dto`
   * **Method:** `POST`
   * **Value:**
    ```php
   {
     "excerpt": "",
     "serialNumber": "",
     "fromDate": "",
     "toDate": "",
     "signerName": "",
     "page": 0,
     "elementNum": 4
    }
    ```
3. **Create new incoming document:**
    * **URL**: `http://localhost:8080/api/incoming-document/save`
    * **Method:** `POST`
    * **Value:**
    ```php
   {
       "excerpt": "Hợp đồng D",
       "serialNumber": "05/2022/Unitech",
       "signerId": 4
    }
    ```
4. **Update incoming document:**
    * **URL**: `http://localhost:8080/api/incoming-document/update`
    * **Method:** `POST`
    * **Value:**
    ```php
   {
      "id": 7,
      "excerpt": "Hợp đồng D",
      "serialNumber": "05/2022/Unitech",
      "signerId": 4
   }
    ```
5. **Delete incoming document:**
    * **URL**: `http://localhost:8080/api/incoming-document/delete`
    * **Method:** `POST`
    * **Value:**
    ```php
   {
      "id": 7
   }
    ```
6. **Find incoming document by id:**
   * **URL**: `http://localhost:8080/api/incoming-document/find-by-id`
   * **Method:** `POST`
   * **Value:**
    ```php
   {
      "id": 7
   }
    ```
7. **Find all and search multi incoming document DTO with full text search:**
   * **URL**: `http://localhost:8080/api/incoming-document/list-dto-fts`
   * **Method:** `POST`
   * **Value:**
   ```php
   {
      "searchParam": "Khai",
      "pageNumber": 0,
      "elementNumber": 4
   }
   ```
***
### Rotation API:
1. **Find all and search multi rotation:**
    * **URL**: `http://localhost:8080/api/rotation/list`
    * **Method:** `POST`
    * **Value:**
    ```php
   {
       "excerpt": "",
       "fromDate": "2000-01-01 00:00:00",
       "toDate": "3000-01-01 23:59:59",
       "senderName": "",
       "receiverName": "",
       "pageNumber": 0,
       "elementNumber": 4
   }
    ```
2. **Find all and search multi rotation DTO:**
   * **URL**: `http://localhost:8080/api/rotation/list-dto`
   * **Method:** `POST`
   * **Value:**
    ```php
   {
       "excerpt": "",
       "fromDate": "2000-01-01 00:00:00",
       "toDate": "3000-01-01 23:59:59",
       "senderName": "",
       "receiverName": "",
       "pageNumber": 0,
       "elementNumber": 4
   }
    ```   
3. **Create new rotation:**
    * **URL**: `http://localhost:8080/api/rotation/save`
    * **Method:** `POST`
    * **Value:**
    ```php
   {
       "incomingDocumentId": 1,
       "senderId": 4,
       "receiverId": 1
   }
    ```
4. **Update rotation:**
    * **URL**: `http://localhost:8080/api/rotation/update`
    * **Method:** `POST`
    * **Value:**
    ```php
   {
      "id": 11,
      "incomingDocumentId": 3,
      "senderId": 3,
      "receiverId": 1
   }
    ```
5. **Delete rotation:**
    * **URL**: `http://localhost:8080/api/rotation/delete`
    * **Method:** `POST`
    * **Value:**
    ```php
   {
      "id": 7
   }
    ```
6. **Get the rotation list of any document:**
   * **URL**: `http://localhost:8080/api/rotation/rotation-list-of-document`
   * **Method:** `POST`
   * **Value:**
    ```php
   {
      "id": 1
   }
    ```
7. **Statistics on the number of document received by person:**
   * **URL**: `http://localhost:8080/api/rotation/document-amount-received-list`
   * **Method:** `GET`

8. **Create new rotation to multi receiver:**
   * **URL**: `http://localhost:8080/api/rotation/save-multi`
   * **Method:** `POST`
   * **Value:**
    ```php
   {
       "incomingDocumentId": 6,
       "senderId": 4,
       "receiverId": [1, 2, 3]
   }
    ```
9. **Get the statistics on the number of document received by person report:**
   * **URL**: `http://localhost:8080/api/rotation/document_amount_receiver_list-report`
   * **Method:** `GET`

10. **Get the rotation list of any document report:**
    * **URL**: `http://localhost:8080/api/rotation/rotation-list-of-document-report`
    * **Method:** `POST`
    * **Value:**
        ```php
       {
         "id": "1",
         "format": "html"
       }
        ```
11. **Find all and search multi rotation DTO with full text search:**
    * **URL**: `http://localhost:8080/api/rotation/list-dto-fts`
    * **Method:** `POST`
    * **Value:**
    ```php
    {
       "searchParam": "Khai",
       "pageNumber": 0,
       "elementNumber": 4
    }
    ```

***
### Send notification API:
* **URL**: `http://localhost:8080/notification`
* **Method:** `POST`
* **Value:**
  ```php
   {
     "title": "KhaiTT",
     "message": "Hậu chú bé đần",
     "to": "eTh89_PDnEyZQFKIuTh4tH:APA91bEUoWPz2QQW0lCFeuKLy4_JLnG7EpdSp6QpWXP6hHdb93ao0X8HxNoxd2pPH8SjbcpN1866v3RM6VW2m6ZTXgfUh6xQ_iVZQESRsf6g2ViEM7DMe8a9Sv_bGqXPzZ8n_qvDHL86"
   }
  ```

***
_Author:_ **KhaiTT**

_Time:_ **08/07/2022**
***
