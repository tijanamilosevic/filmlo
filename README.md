# FilmLo- Movies statistics and predictions

This app contains statistics and predicates popularity for movies on Netflix, Hulu, Prime Video and Disney+ platform.
Also, FilmLo include all Netflix  portforilo (movies and TV series) and statistics for them.

FilmLo is written in Clojure programming language. More about Clojure: https://clojure.org/

Application use following libraries:
* [Data.json](https://github.com/clojure/data.json)
* [Data.csv](https://github.com/clojure/data.csv)
* [Compojure](https://github.com/weavejester/compojure)
* [Ring](https://github.com/ring-clojure/ring)
* [Hiccup](https://github.com/weavejester/hiccup)
* [Lib-noir](https://github.com/noir-clojure/lib-noir)
* [Hickory](https://github.com/davidsantiago/hickory)
* [Midje](https://github.com/marick/Midje)

**Home page**- on home page is Top list popular movies from Netflix, Hulu, Prime Video and Disney+ platform. All popular movies are on Popular movies page

**Netflix portfolio page**- on this page is all Netflix portfolio (Movies and TV Shows). You can search portfolio by title, release year, country, duration or type (Movie/TV Show)

**Netflix stats page**- Netflix portfolio statistics (total count, the oldest/newest Movie/Tv Show, max/min duration for Movies/TV Shows. average duration and Mode (most repetitive values): Year, Genre, Country, Rating system)

**Movies page**- all movies from Netflix, Hulu, Prime Video and Disney+ platform. You can search movies by title, release year, country, IMDb rating, language, duration or genre. Also, you can do this search for specific platform by choosing platform name from the list

**Movies stats page**- Movies statistics (total count, the oldest/newest movie/s, average duration for all movies and for specific platform, max/min duration (for all movies and specific platform), duration standard deviation, average IMDb rate (for all movies and specific platform), max/min IMDb (for all movies and specific platform), IMDb standard deviation and Mode (most repetitive values): Duration, Language, Genre, Country, IMDb)

**Popular movies page**- Popular movies list. You can search movies by title, release year, country, IMDb rating, language, duration, genre or platform

**Kids movies page**- Movies for kids. You can search movies by title, release year, country, IMDb rating, language, duration, genre or platform

**IMDb ranking page**- Ranking groups by IMDb rating for movies from Netflix, Hulu, Prime Video and Disney+ platform 
(these groups were obtained using the K-means clustering method). You can choose ranking group and search movies in that group by title, release year, country, IMDb rating, language, duration or genre

# Local run instructions

* Install [Leiningen](https://github.com/technomancy/leiningen)
* Navigate to **filmlo** project and run **lein run [your port]** (for exampe you want to run this app on port 8080: **lein run 8080**)

# References

* Eric Rochester (2014), [Mastering Clojure Data Analysis](https://www.amazon.com/Mastering-Clojure-Data-Analysis-Rochester/dp/1783284137)
* Henry Garner (2015), [Clojure for Data Science_ Statistics, big data, and machine learning for Clojure programmers](https://www.amazon.com/Clojure-Data-Science-Henry-Garner/dp/1784397180)
* Carin Meier (2015), [Living Clojure](https://www.amazon.com/Living-Clojure-Introduction-Training-Developers/dp/1491909048)
* Daniel Higginbotham (2015), [Clojure for the Brave and True](https://www.amazon.com/Clojure-Brave-True-Ultimate-Programmer/dp/1593275919)
* Ryan Baldwin (2015), [Clojure Web Development Essentials](https://www.amazon.com/Clojure-Development-Essentials-Ryan-Baldwin/dp/1784392227)

