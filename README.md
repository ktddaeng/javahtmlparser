#Java HTML Parser
This application parses the HTML of order forms from the carts of wholesale merchandise sites and exports them into a CSV file for easy reading and record-keeping.

## Motivation
This was made for a local gift shop who wanted something easy and simple to extract information about their orders from various vendors.

## What's New
* Currently can parse [JC Wholesales](https://www.jcsalesweb.com/) and [Bargain Wholesales](https://www.bargainw.com) cart list.

## Prerequisites
* [JSoup 1.10.3](https://jsoup.org/)
* HTML pages from vendor's cart

## Getting Started
1. A standalone executable file is located in the "dist" folder. Please remember to include the libs foldier. Running the jar file should open the main window of the application.
<img src="/screenshots/Screenshot_20170818-173006.png" width="250px" height="auto">
2. If you have not yet downloaded the HTML pages of your shopping cart, save a copy of the webpage containing your list of orders.
|Example (JC Sales)|Example (Bargain Wholesale)|
|---|---|
|<img src="/screenshots/Screenshot_20170818-173006.png" width="250px" height="auto">|<img src="/screenshots/Screenshot_20170818-173418.png" width="250px" height="auto">|
3. Select from the dropdown menu which site you are extracting information from.
4. Select "Load File" to determine the path of your HTML page corresponding to the vendor you selected.
5. Select "Extract Data" to parse the HTML. If the application is working correctly, the dialog should display every item that has been extracted from the HTML document.
6. After making sure you extracted the right information, select "Export Data" to save the extraced information into a CSV file. You will be asked to choose the location you want to save the file to.
