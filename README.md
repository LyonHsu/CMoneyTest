# CMoneyTest

實作題\n\n
目標 base API: https://jsonplaceholder.typicode.com, end point: /photos
做出兩個頁面,第一個頁面只需要有一個 Button 換場到下一個頁面。\n\n

第二個頁面要把 API 的內容呈現，需要呈現的項目有三個,分別是 “id”, “title”, “thumbnailUrl”，Id 和 title 是 String，thumbnailUrl 是圖案的網址,要在 cell 內的 imageView 中呈現每一行放四個格子
\n\n
第三個頁面是點擊了第二個頁面後的任一格，推入下一頁，要呈現當格的背景圖、id、和 title

UI 要能自動適應各尺寸大小的螢幕
\n\n
實作請使用 kotlin
\n
禁止使用第三方套件
除了上述指定 UI 條件之外,你也可以自行優化沒指定的 UI

JSON 內容

     {
      "albumId": 1,
      "id": 1,
      "title": "accusamus beatae ad facilis cum similique qui sunt",
      "url": "https://via.placeholder.com/600/92c952",
      "thumbnailUrl": "https://via.placeholder.com/150/92c952"
    }
    
使用 BufferedReader 去獲取 資料

Image 部分使用 BitmapFactory.decodeStream 獲取

