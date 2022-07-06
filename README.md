# Genealogy
The prevalence of digital pictures, especially from smart phones, has increased the number of images and videos of people. At the same time, the abundance of storage space tends to have people not delete pictures or videos that they take. Consequently, finding images and videos increases in difficulty. Genealogy (the study and tracing of lines of descendants) is well-established for tracing and tracking family relations, but doesn’t have good ties into the richer sets of pictures that exist today or to archived pictures that are now being digitized. Ideally, when a genealogist asks about the great grandmother of person X, they would like to get the set of pictures for the individual as well as their name. This project will create a system that links family tree information with an archive of pictures and the metadata of the pictures

- The program store and manipulate data of person and files of them. We have stored the
personal details and file information in database to be persistent forever.
Code main divide into 3 parts:
- Person’s information
- File’s information
- Reporting function of person and file

We have stored the link between 2 nodes as parent-child relationship, partner1-partner2
relationship and if want to be separated then we also have dissolution features in
program.

User can store as many as and any kind of attributes for person and for the file detail.
There is no restriction over storing the attributes to person and file.
However, code has some fixed attributes for more depth manipulation and more
analytics.

We also can find the relationship between 2 nodes in database anytime user want in
minimum time condition.

We also can connect files and person detail with each other and get some useful
information from that.

There are many details available for file details which can be gotten with respect of date
and chronological order.

Data structure used in program:
-----------------------------------------------------------------------------------------------------
-Arraylist
-Hashset
-Array
-Tree based structure
-Basic OOPs concepts


Total functionality of program:
-----------------------------------------------------------------------------------------------------
-Find ancestor of node(person)
-Find descendants of node(person)
-Find the relationship between 2 nodes
-Add the (node)person in database
-Add details of person in database
-Add relationship as parent-child, partner1-partner2 and dissolution between persons
-Add file detail in database
-Add file attributes (date/location/quality etc.) in database
-Add people in image/video file
-Add tag in image/video file
-And many more.


Limitation:
-----------------------------------------------------------------------------------------------------
-Date should be in YYYY-MM-DD, YYYY-MM, YYYY format only.
-Can not remove parent child relationship once it will be established
-Cannot store the blob content of image/video in database
-Only store the file name of images/videos
-Cannot store some portion of images and video
-Cannot throw an option for selecting the from multiple options (persons and files are
 which has same name)
-With GUI, we can provide the more efficient way to operate the functionalities
-Prefix attributes are not available for person and file so that no extra and accurate
 manipulation can be done
