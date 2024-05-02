Опис основних сутностей

У програмі існують такі сутності як Product і Manufacturer. Product основна сутність, відноситься до другорядної як багато-до-одного, тобто у одного виробника може бути багато продуктів. Основна сутність має кілька атрибутів: назва, рік випуску, ціна, виробник та категорії, до яких цей товар відносится. Виробник має унікальне ім'я, дату початку співпраці, унікальний контактний номер та унікальний емаіл .

Приклади вхідних і вихідних файлів

Файл вхідний містится в папці jsonFiles проекту, приклад products.json: https://github.com/SemeikinaKateryna/SimpleProductRestApp/blob/master/jsonFiles/products.json

Вихідний файл, приклад uploadStatistics.json: https://github.com/SemeikinaKateryna/SimpleProductRestApp/blob/master/jsonFiles/uploadStatistics.json

Інструкція по запуску програми

Оскільки БД в Postgres була створена локально, для запуску необхідно створити базу даних store, а в liquibase скрипті вже наявні створення таблиць, індексів та заповнення таблиці виробників даними.

