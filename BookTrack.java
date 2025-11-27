import java.util.*;

// KRISTEL'S PART
// CUSTOM EXCEPTIONS
class InvalidUserInputException extends Exception {
    public InvalidUserInputException(String msg) {
        super(msg);
    }
}

class BookNotFoundException extends Exception {
    public BookNotFoundException(String msg) {
        super(msg);
    }
}

// ABSTRACT USER CLASS
abstract class User {
    private String name;
    private String userId;
    private int maxBorrowLimit;
    private int currentlyBorrowed;

    public User(String name, String userId, int maxBorrowLimit) {
        this.name = name;
        this.userId = userId;
        this.maxBorrowLimit = maxBorrowLimit;
        this.currentlyBorrowed = 0;
    }

    public String getName() { return name; }
    public String getUserId() { return userId; }
    public int getMaxBorrowLimit() { return maxBorrowLimit; }
    public int getCurrentlyBorrowed() { return currentlyBorrowed; }

    public void incrementBorrowed() { currentlyBorrowed++; }
    public void decrementBorrowed() { if (currentlyBorrowed > 0) currentlyBorrowed--; }

    // Abstract method for polymorphism
    public abstract boolean canBorrow(int currentlyBorrowed);
}

// SUBCLASSES
class Student extends User {
    public Student(String name, String id) {
        super(name, id, 3);
    }

    @Override
    public boolean canBorrow(int borrowed) {
        return borrowed < 3;
    }
}

class Teacher extends User {
    public Teacher(String name, String id) {
        super(name, id, 5);
    }

    @Override
    public boolean canBorrow(int borrowed) {
        return borrowed < 5;
    }
}

class Guest extends User {
    public Guest(String name, String id) {
        super(name, id, 1);
    }

    @Override
    public boolean canBorrow(int borrowed) {
        return borrowed < 1;
    }
}

// BOOK CLASS
class Book {
    private String title;
    private String author;
    private boolean isAvailable;
    private String borrowerId;
    private String[] reservationQueue = new String[10];
    private int queueCount = 0;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isAvailable = true;
        this.borrowerId = null;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }
    public String getBorrowerId() { return borrowerId; }

    public void borrowBook(String userId) {
        isAvailable = false;
        borrowerId = userId;
    }

    public void returnBook() {
        isAvailable = true;
        borrowerId = null;
    }

    public void addReservation(String userId) {
        if (queueCount < 10) {
            reservationQueue[queueCount++] = userId;
        }
    }

    public String popReservation() {
        if (queueCount == 0) return null;
        String next = reservationQueue[0];
        for (int i = 1; i < queueCount; i++) {
            reservationQueue[i - 1] = reservationQueue[i];
        }
        queueCount--;
        return next;
    }

    public int getReservationCount() { return queueCount; }
}

// USER MANAGER
class UserManager {
    User[] users = new User[50];
    int count = 0;

    public void addUser(User u) {
        users[count++] = u;
    }

    public User findUserById(String id) {
        for (int i = 0; i < count; i++) {
            if (users[i].getUserId().equals(id)) return users[i];
        }
        return null;
    }

        public void displayUsers() {
        System.out.println("\n--------------------------------------- USER LIST --------------------------------------");
        
        // Header
        System.out.printf("%-50s %-10s %-10s %-12s%n", "Name", "ID", "Borrowed", "User Type");
        System.out.println("----------------------------------------------------------------------------------------");
        
        for (int i = 0; i < count; i++) {
            User u = users[i];

            // Identify user type
            String type;
            if (u instanceof Student) type = "Student";
            else if (u instanceof Teacher) type = "Teacher";
            else type = "Guest";

            // Print user info in aligned columns
            System.out.printf("%-50s %-10s %-10d %-12s%n", 
                            u.getName(), u.getUserId(), u.getCurrentlyBorrowed(), type);
        }
    }
}

// ELAINE'S PART
// BOOK MANAGER
class BookManager {
    Book[] books = new Book[1000];
    int count = 0;

    public void addBook(Book b) {
        books[count++] = b;
    }

        public Book searchBookByTitle(String title) throws BookNotFoundException {
        String search = title.toLowerCase(); // convert input to lowercase

        for (int i = 0; i < count; i++) {
            // compare lowercase + partial
            if (books[i].getTitle().toLowerCase().contains(search)) {
                return books[i];
            }
        }

        throw new BookNotFoundException("Book titled '" + title + "' not found.");
    }

    public void borrowBook(User u, Book b) {
        if (!b.isAvailable()) {
            System.out.println("Book is currently borrowed.");
            return;
        }
        if (u.canBorrow(u.getCurrentlyBorrowed())) {
            b.borrowBook(u.getUserId());
            u.incrementBorrowed();
            System.out.println("Borrow successful!");
        } else {
            System.out.println("Borrow limit reached!");
        }
    }

    public void returnBook(Book b, User returningUser, UserManager um) {
        // Check if the book is actually borrowed
        if (b.isAvailable()) {
            System.out.println("This book is not currently borrowed!");
            return;
        }

        // Check if the returning user is the one who borrowed it
        if (!b.getBorrowerId().equals(returningUser.getUserId())) {
            System.out.println("This user did not borrow this book. Return denied!");
            return;
        }

        b.returnBook();
        returningUser.decrementBorrowed();
        System.out.println("Book returned successfully by user: " + returningUser.getName() + " (" + returningUser.getUserId() + ")");

        // Assign to next reserved user if any
        while (b.getReservationCount() > 0) {
            String nextUserId = b.popReservation();
            User nextUser = um.findUserById(nextUserId);

            if (nextUser != null) {
                if (nextUser.getCurrentlyBorrowed() >= nextUser.getMaxBorrowLimit()) {
                    System.out.println("Next user " + nextUser.getName() + " has reached borrow limit. Skipping...");
                    continue;
                }

                b.borrowBook(nextUserId);
                nextUser.incrementBorrowed();
                System.out.println("Book automatically assigned to next user in line: " + nextUser.getName() + " (" + nextUser.getUserId() + ")");
                break;
            }
        }
    }



    public void reserveBook(Book b, String userId) {
        b.addReservation(userId);
        System.out.println("Reservation added!");
    }
}

// PATRICK'S PART
// MAIN PROGRAM
public class BookTrack {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserManager um = new UserManager();
        BookManager bm = new BookManager();

        // === DEFAULT SAMPLE BOOKS ADDED (50 BOOKS) ===
        String[][] defaultBooks = {
            // ---------------- Fiction ----------------
            {"To Kill a Mockingbird", "Harper Lee"}, {"Pride and Prejudice", "Jane Austen"},
            {"The Great Gatsby", "F. Scott Fitzgerald"}, {"War and Peace", "Leo Tolstoy"},
            {"The Book Thief", "Markus Zusak"}, {"All the Light We Cannot See", "Anthony Doerr"},
            {"The Nightingale", "Kristin Hannah"}, {"The Pillars of the Earth", "Ken Follett"},
            {"Gone Girl", "Gillian Flynn"}, {"The Girl with the Dragon Tattoo", "Stieg Larsson"},
            {"Murder on the Orient Express", "Agatha Christie"}, {"Big Little Lies", "Liane Moriarty"},
            {"The Da Vinci Code", "Dan Brown"}, {"Angels and Demons", "Dan Brown"},
            {"The Silent Patient", "Alex Michaelides"}, {"The Shining", "Stephen King"},
            {"It", "Stephen King"}, {"Dracula", "Bram Stoker"}, {"Frankenstein", "Mary Shelley"},
            {"The Hunger Games", "Suzanne Collins"}, {"Catching Fire", "Suzanne Collins"},
            {"Mockingjay", "Suzanne Collins"}, {"Divergent", "Veronica Roth"}, {"Insurgent", "Veronica Roth"},
            {"Allegiant", "Veronica Roth"}, {"Harry Potter and the Sorcerer's Stone", "J.K. Rowling"},
            {"Harry Potter and the Chamber of Secrets", "J.K. Rowling"}, {"Harry Potter and the Prisoner of Azkaban", "J.K. Rowling"},
            {"Harry Potter and the Goblet of Fire", "J.K. Rowling"}, {"The Hobbit", "J.R.R. Tolkien"}, {"The Lord of the Rings", "J.R.R. Tolkien"},
            {"Eragon", "Christopher Paolini"}, {"The Name of the Wind", "Patrick Rothfuss"}, {"The Wise Man's Fear", "Patrick Rothfuss"},
            {"Twilight", "Stephenie Meyer"}, {"New Moon", "Stephenie Meyer"}, {"Eclipse", "Stephenie Meyer"},
            {"Breaking Dawn", "Stephenie Meyer"}, {"City of Bones", "Cassandra Clare"}, {"City of Ashes", "Cassandra Clare"},
            {"City of Glass", "Cassandra Clare"}, {"Percy Jackson: The Lightning Thief", "Rick Riordan"}, {"Percy Jackson: Sea of Monsters", "Rick Riordan"},
            {"Percy Jackson: Titan's Curse", "Rick Riordan"}, {"Percy Jackson: Battle of the Labyrinth", "Rick Riordan"}, {"Percy Jackson: Last Olympian", "Rick Riordan"},
            {"Twilight", "Stephenie Meyer"}, {"Red Queen", "Victoria Aveyard"}, {"Glass Sword", "Victoria Aveyard"},

            // ---------------- Non-Fiction ----------------
            {"Sapiens: A Brief History of Humankind", "Yuval Noah Harari"}, {"Homo Deus: A Brief History of Tomorrow", "Yuval Noah Harari"},
            {"Educated", "Tara Westover"}, {"Becoming", "Michelle Obama"}, {"The Immortal Life of Henrietta Lacks", "Rebecca Skloot"},
            {"Thinking, Fast and Slow", "Daniel Kahneman"}, {"The Power of Habit", "Charles Duhigg"}, {"Atomic Habits", "James Clear"},
            {"Born a Crime", "Trevor Noah"}, {"Quiet: The Power of Introverts", "Susan Cain"}, {"Unbroken", "Laura Hillenbrand"},
            {"The Diary of a Young Girl", "Anne Frank"}, {"The Wright Brothers", "David McCullough"}, {"Steve Jobs", "Walter Isaacson"},
            {"Alexander Hamilton", "Ron Chernow"}, {"The Glass Castle", "Jeannette Walls"}, {"Into the Wild", "Jon Krakauer"},
            {"Into Thin Air", "Jon Krakauer"}, {"The Art of War", "Sun Tzu"}, {"Meditations", "Marcus Aurelius"}, {"Man's Search for Meaning", "Viktor E. Frankl"},
            {"Guns, Germs, and Steel", "Jared Diamond"}, {"Freakonomics", "Steven D. Levitt"}, {"Outliers", "Malcolm Gladwell"}, {"The Tipping Point", "Malcolm Gladwell"},
            {"David and Goliath", "Malcolm Gladwell"}, {"Blink", "Malcolm Gladwell"}, {"The Subtle Art of Not Giving a F*ck", "Mark Manson"}, {"12 Rules for Life", "Jordan B. Peterson"},

            // ---------------- Graphic / Visual ----------------
            {"Maus", "Art Spiegelman"}, {"Persepolis", "Marjane Satrapi"}, {"Watchmen", "Alan Moore"}, {"V for Vendetta", "Alan Moore"},
            {"Saga", "Brian K. Vaughan"}, {"Y: The Last Man", "Brian K. Vaughan"}, {"Sandman", "Neil Gaiman"}, {"Bone", "Jeff Smith"},
            {"Naruto", "Masashi Kishimoto"}, {"One Piece", "Eiichiro Oda"}, {"Attack on Titan", "Hajime Isayama"}, {"Death Note", "Tsugumi Ohba"},
            {"Fullmetal Alchemist", "Hiromu Arakawa"}, {"My Hero Academia", "Kohei Horikoshi"}, {"Tokyo Ghoul", "Sui Ishida"}, {"Fruits Basket", "Natsuki Takaya"},

            // ---------------- Poetry / Drama ----------------
            {"The Sun and Her Flowers", "Rupi Kaur"}, {"Milk and Honey", "Rupi Kaur"}, {"Leaves of Grass", "Walt Whitman"},
            {"The Raven", "Edgar Allan Poe"}, {"Othello", "William Shakespeare"}, {"Hamlet", "William Shakespeare"},
            {"Macbeth", "William Shakespeare"}, {"The Odyssey", "Homer"}, {"The Iliad", "Homer"}, {"Beowulf", "Anonymous"},

            // ---------------- Educational / Reference ----------------
            {"Oxford English Dictionary", "Oxford University Press"}, {"Gray's Anatomy", "Henry Gray"}, {"Encyclopedia Britannica", "Various"},
            {"How to Win Friends and Influence People", "Dale Carnegie"}, {"The Elements of Style", "William Strunk Jr."}, {"A Brief History of Time", "Stephen Hawking"},
            {"Cosmos", "Carl Sagan"}, {"The Selfish Gene", "Richard Dawkins"}, {"Guns, Germs, and Steel", "Jared Diamond"}, {"The Art of Happiness", "Dalai Lama"},

            // ---------------- Language / Learning ----------------
            {"Fluent English: Perfect Natural Speech", "Barbara Raifsnider"}, 
            {"English Grammar in Use", "Raymond Murphy"}, 
            {"Word Power Made Easy", "Norman Lewis"}, 
            {"Practice Makes Perfect: Spanish Verb Tenses", "Dorothy Richmond"}, 
            {"French for Dummies", "Dodi-Katrin Schmidt"}, 
            {"Japanese from Zero!", "George Trombley"}, 
            {"Mandarin Chinese for Beginners", "Yi Ren"}, 
            {"Korean Made Simple", "Billy Go"}, 
            {"Teach Yourself Filipino", "F. S. Llamzon"}, 
            {"Basic Tagalog for Foreigners", "Corazon P. Paras"},

            // ---------------- Philippine Local Literature ----------------
            {"Noli Me Tangere", "José Rizal"}, 
            {"El Filibusterismo", "José Rizal"}, 
            {"Florante at Laura", "Francisco Balagtas"}, 
            {"Mga Ibong Mandaragit", "Amado V. Hernandez"}, 
            {"Dekada '70", "Lualhati Bautista"}, 
            {"Bata, Bata... Pa'no Ka Ginawa?", "Lualhati Bautista"}, 
            {"Gapô", "Lualhati Bautista"}, 
            {"May Day Eve", "Nick Joaquin"}, 
            {"The Woman Who Had Two Navels", "Nick Joaquin"}, 
            {"A Portrait of the Artist as Filipino", "Nick Joaquin"},
            {"Smaller and Smaller Circles", "F.H. Batacan"}, 
            {"Ilustrado", "Miguel Syjuco"}, 
            {"In the Country", "Cecilia Manguerra Brainard"}, 
            {"Dogeaters", "Jessica Hagedorn"}, 
            {"America Is in the Heart", "Carlos Bulosan"}, 
            {"The Pretenders", "F. Sionil José"}, 
            {"Tree", "F. Sionil José"}, {"Mass", "F. Sionil José"}, 
            {"The Rosales Saga", "F. Sionil José"}, {"Po-on", "F. Sionil José"},

            // ---------------- Programming / Technology ----------------
            {"Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin"}, 
            {"The Pragmatic Programmer", "Andrew Hunt & David Thomas"}, 
            {"Introduction to Algorithms", "Thomas H. Cormen"}, 
            {"Design Patterns: Elements of Reusable Object-Oriented Software", "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides"}, 
            {"Head First Java", "Kathy Sierra & Bert Bates"}, 
            {"Effective Java", "Joshua Bloch"}, 
            {"Python Crash Course", "Eric Matthes"}, 
            {"Automate the Boring Stuff with Python", "Al Sweigart"}, 
            {"JavaScript: The Good Parts", "Douglas Crockford"}, 
            {"Eloquent JavaScript", "Marijn Haverbeke"}, 
            {"Learning PHP, MySQL & JavaScript", "Robin Nixon"}, 
            {"C Programming Language", "Brian W. Kernighan & Dennis M. Ritchie"}, 
            {"C++ Primer", "Stanley B. Lippman"}, 
            {"Programming Pearls", "Jon Bentley"}, 
            {"Structure and Interpretation of Computer Programs", "Harold Abelson & Gerald Jay Sussman"}, 
            {"Code Complete", "Steve McConnell"}, 
            {"Refactoring: Improving the Design of Existing Code", "Martin Fowler"}, 
            {"The Art of Computer Programming", "Donald E. Knuth"}, 
            {"You Don't Know JS", "Kyle Simpson"}, 
            {"Cracking the Coding Interview", "Gayle Laakmann McDowell"}
        };

        for (String[] b : defaultBooks) {
            bm.addBook(new Book(b[0], b[1]));
        }

        while (true) {
            System.out.println("\n=============================");
            System.out.println("      BOOKTRACK SYSTEM");
            System.out.println("=============================");
            System.out.println("1. Add User");
            System.out.println("2. Add Book");
            System.out.println("3. Search Book");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. Reserve Book");
            System.out.println("7. View Users");
            System.out.println("8. View Books");
            System.out.println("9. Exit");

            System.out.print("\nEnter choice: ");
            String input = sc.nextLine();

            int choice = 0;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("\nEnter User Type (1-Student, 2-Teacher, 3-Guest): ");
                        int type = Integer.parseInt(sc.nextLine());
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter User ID: ");
                        String id = sc.nextLine();

                        if (type == 1) um.addUser(new Student(name, id));
                        else if (type == 2) um.addUser(new Teacher(name, id));
                        else if (type == 3) um.addUser(new Guest(name, id));
                        else throw new InvalidUserInputException("Invalid user type.");

                        System.out.println("User added successfully!");
                        break;

                    case 2:
                        System.out.print("\nEnter Book Title: ");
                        String title = sc.nextLine();
                        System.out.print("Enter Author: ");
                        String author = sc.nextLine();
                        bm.addBook(new Book(title, author));
                        System.out.println("Book added successfully!");
                        break;

                    case 3:
                        System.out.print("\nEnter title to search: ");
                        String st = sc.nextLine();
                        Book found = bm.searchBookByTitle(st);
                        System.out.println("Book Found!");
                        System.out.println("Title: " + found.getTitle());
                        System.out.println("Author: " + found.getAuthor());
                        System.out.println("Available: " + found.isAvailable());
                        break;

                    case 4:
                        System.out.print("Enter User ID: ");
                        String uid = sc.nextLine();
                        User user = um.findUserById(uid);

                        if (user == null) {
                            System.out.println("User ID not found. Borrowing denied.");
                            break;
                        }

                        System.out.print("\nEnter Book Title: ");
                        String bt = sc.nextLine();
                        Book bk = bm.searchBookByTitle(bt);
                        System.out.println("Borrowing: " + bk.getTitle());
                        bm.borrowBook(user, bk);
                        break;

                    case 5:
                        System.out.print("\nEnter User ID returning the book: ");
                        String returningUserId = sc.nextLine();
                        User returningUser = um.findUserById(returningUserId);

                        if (returningUser == null) {
                            System.out.println("User ID not found. Cannot return book.");
                            break;
                        }

                        System.out.print("Enter Book Title: ");
                        String rbt = sc.nextLine();

                        try {
                            Book rb = bm.searchBookByTitle(rbt);
                            bm.returnBook(rb, returningUser, um);
                        } catch (BookNotFoundException bnfe) {
                            System.out.println("Book not found. Cannot return.");
                        }
                        break;


                    case 6:
                        System.out.print("\nEnter User ID: ");
                        String rid = sc.nextLine();
                        // check user exists first
                        User reservingUser = um.findUserById(rid);
                        if (reservingUser == null) {
                            System.out.println("User ID not found. Please add the user before reserving a book.");
                            break;
                        }

                        System.out.print("Enter Book Title: ");
                        String rbt2 = sc.nextLine();
                        try {
                            Book rb2 = bm.searchBookByTitle(rbt2);
                            bm.reserveBook(rb2, rid);
                        } catch (BookNotFoundException bnfe) {
                            System.out.println("Book not found. Cannot add reservation.");
                        }
                        break;

                    case 7:
                        um.displayUsers();
                        break;

                    case 8:
                        System.out.println("\n-------------------------------------------------------- BOOK LIST -----------------------------------------------------");
                        // Header row
                        System.out.printf("%-70s %-12s %-12s %-12s%n", "Title", "Available", "Borrower", "Reservations");
                        System.out.println("------------------------------------------------------------------------------------------------------------------------");
                        
                        for (int i = 0; i < bm.count; i++) {
                            Book book = bm.books[i];
                            System.out.printf("%-70s %-12s %-12s %-12d%n",
                                            book.getTitle(),
                                            book.isAvailable(),
                                            book.getBorrowerId() != null ? book.getBorrowerId() : "None",
                                            book.getReservationCount());
                        }
                        break;


                    case 9:
                        System.out.println("Thank you for using BookTrack!!!");
                        return;

                    default:
                        System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
