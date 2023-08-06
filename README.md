### Приложение для скачивания новостных статей
Приложение скачивает записи о новостных статьях, скачивает сами новостные статья по записям и выдает информацию о скачанных новостных статья, а также сами новостные статьи.

**1. Скачивание новостных статей**

При старте приложение создает настраиваемый пул потоков. 
Потоки начинаю скачивать записи о новостных статьях согласно спецификации
https://api.spaceflightnewsapi.net/documentation#/Article/get_v3_articles

Количество скачиваемых записей одним потоком за 1 цикл настраивается. 

Общее количество скачиваемых записей всеми потоками также настраивается.

Когда поток скачал свою партию записей о новостных статьях происходит фильтрация новостей по полю title в соответствии с настраиваемым черным списком слов.

Потом происходит сортировка записей по дате публикации и их группировка по новостному сайту.

Потоки складывают записи о новостных статьях в буфер. Каждый раз при записи данных в буфер происходит проверка о количестве уже существующих записях для каждого новостного сайта.
Если количество записей для конкретного сайта больше или равно настраиваемому лимиту, 
то поток берет записи для этого новостного сайта, скачивает статьи новостей используя поле url, сохраняет эти статьи в базе данных и очищает буфер от записей для этого новостного сайта.

Когда достигнут общий лимит скачанных записей, происходит скачивание оставшихся в буфере статей, сохранение их в базе данных и полная очистка буфера.

**2. Отображение информации о новостных статьях**

Приложение отображает информацию о новостных статьях по следующим адресам:
- `http://localhost:9000/articles` - получить все записи о новостных статьях
- `http://localhost:9000/articles/newsSite/{newsSite}` - получить все записи о новостных статьях для конкретного новостного сайта
- `http://localhost:9000/articles/id/{id}` - получить конкретную новостную статью по id записи о ней.
