# 📚 BookTrack Library Management System
 
# **📌 Description / Overview**
BookTrack is a Java-based console application designed to simulate a full library management system. It provides core library functions such as adding users, adding books, searching, borrowing, returning, and reserving. The system tracks user borrowing limits, manages book availability, and automatically assigns reserved books to the next user in the queue once returned.

This project demonstrates structured Object-Oriented Programming (OOP) in Java using inheritance, polymorphism, and custom exception handling. It also solves typical library workflow challenges—such as preventing over-borrowing, identifying valid borrowers, managing reservation queues, and keeping records organised—making it suitable for academic submissions or small-scale library simulations.

# **🏷 OOP Concepts Applied**
### 1. Encapsulation
Encapsulation is applied by keeping the internal data of classes private and providing public methods to access or modify them.
* In the User class, attributes like name, userId, currentlyBorrowed, and maxBorrowLimit are private.
* Getter and setter methods (getName(), incrementBorrowed(), etc.) are provided to safely access or modify these attributes.
* Similarly, in the Book class, fields like title, author, isAvailable, and reservationQueue are private, ensuring controlled access to the book’s state.
* This prevents direct manipulation of critical data and maintains the integrity of the system.

### **2. Inheritance**
Inheritance is used to create a hierarchical relationship between general and specialized classes:
* The User class is abstract, representing a generic user of the library.
* Specific user types—Student, Teacher, and Guest—inherit from User.
* Each subclass inherits common attributes and methods (name, userId, currentlyBorrowed, canBorrow()) while adding specific rules (e.g., borrowing limits differ among user types).
* This allows code reuse and avoids redundancy.

### **3. Polymorphism**
Polymorphism allows objects to be treated as instances of their parent class while behaving differently according to their actual type:
* The canBorrow(int currentlyBorrowed) method is abstract in User and overridden in each subclass.
* When borrowing a book, the system can call user.canBorrow() without needing to know whether the user is a Student, Teacher, or Guest.
* This enables flexible and dynamic behavior based on user type while keeping code clean and maintainable.

### **4. Abstraction**
Abstraction hides the complex internal logic of classes and exposes only necessary functionality:
* The User class is abstract, providing a template for all user types while hiding the implementation details of borrow limits and tracking.
* Methods like borrowBook(), returnBook(), and reserveBook() in the BookManager encapsulate the complex logic of borrowing, returning, and reserving books from the main program.
* This allows the main program to interact with objects at a high level without worrying about internal details.

### **5. Composition / Association**
Composition and association show how classes interact with each other:
* UserManager contains multiple User objects.
* BookManager contains multiple Book objects.
* Book maintains a reservation queue (array of user IDs) to manage reserved users.
* Methods in BookManager link Book and User objects (e.g., assigning a borrowed book to a user or transferring it to the next reservation in line).
* This model represents real-world relationships between entities in a library system.

# **📚 Program Structure**
The BookTrack program is organized into several main classes, each with specific responsibilities:
#### 1. User (Abstract Class)
* Represents a generic library user.
* Stores common attributes such as name, userId, maxBorrowLimit, and currentlyBorrowed.
* Provides methods to increment or decrement the number of borrowed books.
* Declares an abstract method canBorrow() for checking if a user can borrow more books.

#### 2. Student, Teacher, Guest (Subclasses of User)
* These classes inherit from the User class.
* Each subclass defines the borrowing limit through the overridden canBorrow() method:
   * Student: maximum of 3 books
   * Teacher: maximum of 5 books
   * Guest: maximum of 1 book

#### 3. Book
* Represents a book in the library system.
* Stores attributes such as title, author, isAvailable, borrowerId, and a reservation queue.
* Contains methods to borrow, return, and reserve books, as well as manage the reservation queue.

#### 4. UserManager
* Handles all operations related to users.
* Methods include adding users, finding users by ID, and displaying all users.

#### 5. BookManager
* Handles all operations related to books.
* Methods include adding books, searching books by title, borrowing, returning, and reserving books.
* Also automatically assigns returned books to the next user in the reservation queue, if applicable.

#### 6. BookTrack (Main Class)
* Contains the main() method that runs the program.
* Provides a menu-driven interface for the user to interact with the system.
* Allows users to add books/users, borrow and return books, reserve books, and view all users and books.
* Integrates UserManager and BookManager to coordinate the functionality.

### Class Relationships:
* Student, Teacher, and Guest inherit from User.
* UserManager manages multiple User objects.
* BookManager manages multiple Book objects.
* BookTrack coordinates both UserManager and BookManager to provide the complete library system functionality.
* Book can have a borrowerId which links to a User and a reservation queue linking to multiple User IDs.

# **🖥️ How to Run the Program**
Follow these steps to compile and run the **BookTrack** library management system in Java:

#### 1. Requirements
* Java Development Kit (JDK) installed (version 8 or higher recommended)
* A command-line terminal (Command Prompt, Terminal, or an IDE with terminal support)
* Optional: IDE like IntelliJ IDEA, Eclipse, or VS Code for easier editing

#### 2. Save the Source Code
* Create a folder for your project, e.g., BookTrackProject.
* Inside the folder, create a file named: BookTrack.java
* Copy the full source code into BookTrack.java.

#### 3. Compile the Program
* Open a terminal in the project folder.
* Run the following command to compile the Java program: javac BookTrack.java
* This will generate a BookTrack.class file if there are no syntax errors.
* Make sure the terminal is in the same directory as BookTrack.java.


#### 4. Run the Program
* After successful compilation, run the program with: java BookTrack
* The program will display a text-based menu:
##### ==============
##### BOOKTRACK SYSTEM
##### ==============
1. Add User
2. Add Book
3. Search Book
4. Borrow Book
5. Return Book
6. Reserve Book
7. View Users
8. View Books
9. Exit
* Enter the corresponding number to perform operations like adding users, borrowing books, returning books, and more.

#### 5. Notes
* The program comes with default sample books already loaded.
* Ensure unique User IDs for each user when adding them.
* Book searches are case-insensitive and allow partial title matches.
* Reservations are automatically assigned when a returned book has pending reservations.

# Sample output

# Author and Acknowledgement
## Ma. Kristel C. Estavillo
BS Information Technology
estakristel12@gmail.com

I would like to express my sincere gratitude to the following individuals and resources for their invaluable contributions and support during the development of the BookTrack system:

## Patrick Gabriel C. Boongaling
BS Information Technology
patrickgabrielboongaling@gmail.com

I want to thank my dear groupmates for their time and effort in doing this project. For the contributions of us as individuals is very much appreciated to make this project successful. Also to our professor for guiding us in this project.

## Elaine Grace F. Fanoga 
BS Information Technology
elainegracefanoga123@gmail.com

I am thankful to my groupmates for their time and effort in completing the BookTrack System. I also appreciate the guidance of our professor, which made this project possible. Thank you to everyone who contributed.

# 🌱 Future Enhancements
This section outlines possible improvements or features that can be added to the BookTrack system in the future:
* Implement a graphical user interface (GUI) for easier interaction instead of a console-based system.
* Add a database (e.g., MySQL, SQLite) to store users and books persistently.
* Introduce user authentication and password management for enhanced security.
* Allow filtering and sorting of books by genre, author, or availability.
* Implement overdue tracking and automated notifications for borrowed books.
* Expand the reservation system to notify users when a reserved book becomes available.

# 🌐 References
* This section lists all the resources and materials used to develop the project:
* TutorialsPoint – Java Programming Tutorial – https://www.tutorialspoint.com/java/index.htm
* W3Schools – Java references and tutorials – https://www.w3schools.com/java/
* GeeksforGeeks – Java programming examples and OOP concepts – https://www.geeksforgeeks.org/java/
* Lecture Notes and Slides from our Information Technology course.
* Visual Studio Code (VSCode) – IDE used for coding and testing the program.
* YouTube Tutorials – Java programming tutorials for beginners and advanced users (various channels).
