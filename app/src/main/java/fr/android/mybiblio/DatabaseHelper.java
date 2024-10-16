package fr.android.mybiblio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.mindrot.jbcrypt.BCrypt;


public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "library.db";
    private static final int DATABASE_VERSION = 2;

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "user_id";
    //public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";

    // Books table
    public static final String TABLE_BOOKS = "books";
    public static final String COLUMN_BOOK_ID = "book_id";
    public static final String COLUMN_ISBN = "isbn";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                //+ COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_EMAIL + " TEXT" + ")";
        db.execSQL(createUsersTable);

        String createBooksTable = "CREATE TABLE " + TABLE_BOOKS + "("
                + COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ISBN + " TEXT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_AUTHOR + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_DESCRIPTION+ " TEXT" +")";

        db.execSQL(createBooksTable);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
            onCreate(db);
        }

    public boolean addUser(String email, String password) { //String username,
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Hacher le mot de passe avant de le stocker
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        //values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, hashedPassword);
        values.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1; // return true if inserted successfully
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            String storedHashedPassword = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            cursor.close();

            // Vérifier si le mot de passe fourni correspond au mot de passe haché stocké
            return BCrypt.checkpw(password, storedHashedPassword);
        }

        cursor.close();
        return false;
    }

    public boolean checkUser1(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE "
                + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }
    // Method to check if a user already exists by email
    public boolean doesUserExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


    // CRUD operations go here


    // Method to add a new book
    public boolean addBook(String isbn, String title, String author, String category, String description) {
        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || category.isEmpty()) {
            // Ne pas insérer si des champs obligatoires sont vides
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISBN, isbn);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DESCRIPTION, description);

        // Populate with sample data
        populateWithSampleBooks();

        long result = db.insert(TABLE_BOOKS, null, values);
        return result != -1; // return true if inserted successfully
    }

    // Method to get a book by its ID
    public Cursor getBookById(int bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOOKS, null, COLUMN_BOOK_ID + "=?",
                new String[]{String.valueOf(bookId)}, null, null, null);
    }

    // Method to get all books
    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOOKS, null, null, null, null, null, COLUMN_TITLE + " ASC");
    }

    // Method to update a book's details
    public boolean updateBook(int bookId, String isbn, String title, String author, String category, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISBN, isbn);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DESCRIPTION, description);

        int rowsAffected = db.update(TABLE_BOOKS, values, COLUMN_BOOK_ID + "=?",
                new String[]{String.valueOf(bookId)});
        return rowsAffected > 0; // return true if update was successful
    }

    // Method to delete a book
    public boolean deleteBook(int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_BOOKS, COLUMN_BOOK_ID + "=?",
                new String[]{String.valueOf(bookId)});
        return rowsDeleted > 0; // return true if delete was successful
    }

    // Method to search books by title or author
    public Cursor searchBooks(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + TABLE_BOOKS + " WHERE "
                + COLUMN_TITLE + " LIKE ? OR "
                + COLUMN_AUTHOR + " LIKE ? OR "
                + COLUMN_CATEGORY + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%"};
        return db.rawQuery(searchQuery, selectionArgs);
    }

    public Cursor searchBooks1(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + TABLE_BOOKS + " WHERE "
                + COLUMN_TITLE + " LIKE ? OR "
                + COLUMN_AUTHOR + " LIKE ? OR "
                + COLUMN_CATEGORY + " LIKE ?";
        return db.rawQuery(searchQuery, new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%"});
    }

    // Method to get books by category
    public Cursor getBooksByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_CATEGORY + " = ?";
        return db.rawQuery(query, new String[]{category});
    }


    public void populateWithSampleBooks() {
        // Sample book data
        String[][] books = {
                {"978-1234567890", "The Silent Echo", "Elena Frost", "Mystery", "A detective with synesthesia investigates a series of crimes where the only clue is a mysterious sound no one else can hear."},
                {"978-2345678901", "Circuits of the Heart", "Dr. Aiden Chen", "Romance/Sci-Fi", "In a world where emotions are regulated by implants, two people risk everything to experience true love."},
                {"978-3456789012", "Whispers of the Ancients", "Maya Stoneheart", "Historical Fantasy", "An archaeologist uncovers an artifact that allows her to communicate with civilizations long past, altering our understanding of history."},
                {"978-4567890123", "The Algorithm of Life", "Prof. Samuel Turing", "Popular Science", "A comprehensive exploration of how computational thinking can be applied to solve real-world biological problems."},
                {"978-5678901234", "Culinary Chromosomes", "Chef Lydia Umami", "Cookbook/Science", "A unique cookbook that explores the genetic basis of flavor, with recipes tailored to individual DNA profiles."},
                {"978-6789012345","The Paradox Equation","Theo Schrödinger","Thriller","A mathematician receives an equation that predicts future events with uncanny accuracy, leading to a cat-and-mouse game with shadowy organizations."},
                {"978-7890123456","Nebula's Embrace","Capt. Aria Stardust","Space Opera","The crew of a living spaceship navigate interstellar politics and their own tangled relationships as they search for a new home for humanity."},
                {"978-8901234567","The Symbiont Solution","Dr. Fern Gaia","Environmental Sci-Fi","In a polluted future, a scientist develops a symbiotic organism to clean the environment, but it begins to evolve in unexpected ways. "},
                {"978-9012345678","Rhythms of Revolution","DJ Rebel Beat","Music Biography","The story of how underground music movements shaped political revolutions across the globe in the 21st century."},
                {"978-0123456789","The Quantum Butterfly Effect","Zara Nightingale","Science Fiction","A physicist discovers that manipulating quantum states can alter past events, leading to ethical dilemmas and unforeseen consequences."},
                {"978-1234567891","Fractured Reflections","Miriam Glass", "Psychological Thriller","A therapist specializing in mirror therapy for amputees begins to question her own sanity when her patients report seeing impossible things in their reflections. "},
                {"978-2345678902","The Neurodiversity Advantage","Dr. Oliver Spectrum","Business/Psychology","How embracing cognitive differences in the workplace leads to innovation and success, with real-world case studies and strategies."},
                {"978-3456789013","Chrono-Archaeology","Prof. Evelyn Timesift","Speculative Science","A futuristic approach to archaeology using time-viewing technology to settle historical debates and uncover lost knowledge."},
                {"978-4567890124","The Meme Merchants","Viral V. Vector","Media Studies","An examination of how internet memes shape modern culture, influence politics, and drive economic trends."},
                {"978-5678901235","Photosynthetic Fantasies","Botanist Bob Green", "Eco-Fiction","Short stories exploring a world where humans have evolved to photosynthesize, dramatically altering society and the environment."},
                // Programming
                {"978-0134685991", "Effective Java", "Joshua Bloch", "Programming", "A comprehensive guide to best practices in Java programming."},
                {"978-0262033844", "Introduction to Algorithms", "Thomas H. Cormen et al.", "Programming", "A comprehensive guide to algorithms in computer science."},
                {"978-0201633610", "Design Patterns", "Erich Gamma et al.", "Programming", "A guide to software design patterns."},
                {"978-0135957059", "The Pragmatic Programmer", "Andrew Hunt & David Thomas", "Programming", "A guide to becoming a better programmer."},
                {"978-0262062794", "Structure and Interpretation of Computer Programs", "Harold Abelson & Gerald Jay Sussman", "Programming", "A classic text on computer programming."},
                {"978-0131103627", "Programming Pearls", "Jon Bentley", "Programming", "A collection of programming problems and their solutions."},
                {"978-0132350884", "Clean Code", "Robert C. Martin", "Programming", "A handbook of agile software craftsmanship."},
                {"978-1491950357", "Programming Rust", "Jim Blandy & Jason Orendorff", "Programming", "A comprehensive guide to Rust programming."},
                {"978-1449355730", "Learning Python", "Mark Lutz", "Programming", "A comprehensive introduction to Python programming."},
                {"978-0596517748", "JavaScript: The Good Parts", "Douglas Crockford", "Programming", "A guide to the best parts of JavaScript."},

                // Electronique
                {"978-0131471224", "The Art of Electronics", "Paul Horowitz & Winfield Hill", "Electronique", "A comprehensive reference book for learning and mastering electronics."},
                {"978-0071771337", "Practical Electronics for Inventors", "Paul Scherz & Simon Monk", "Electronique", "A hands-on guide to electronics for inventors and hobbyists."},
                {"978-1260239126", "Electronic Devices and Circuit Theory", "Robert Boylestad & Louis Nashelsky", "Electronique", "A comprehensive textbook on electronic devices and circuits."},
                {"978-0071816243", "Microelectronic Circuits", "Adel S. Sedra & Kenneth C. Smith", "Electronique", "A thorough introduction to analog and digital electronic circuits."},
                {"978-0132129374", "Digital Design", "M. Morris Mano & Michael D. Ciletti", "Electronique", "A textbook on digital logic design."},
                {"978-1259642586", "Fundamentals of Electric Circuits", "Charles Alexander & Matthew Sadiku", "Electronique", "A comprehensive guide to electric circuit analysis."},
                {"978-1119038917", "Schaum's Outline of Basic Electricity", "Milton Gussow", "Electronique", "A problem-solving approach to understanding basic electricity."},
                {"978-0471476885", "The ARRL Handbook for Radio Communications", "ARRL Inc.", "Electronique", "A comprehensive resource for radio and electronics enthusiasts."},
                {"978-1118875865", "Analog Integrated Circuit Design", "Tony Chan Carusone et al.", "Electronique", "A guide to analog integrated circuit design."},
                {"978-0071823975", "RF Circuit Design", "Christopher Bowick", "Electronique", "A practical guide to RF circuit design."},

                // Management
                {"978-0060885366", "The Lean Startup", "Eric Ries", "Management", "A method for developing businesses and products by shortening the product development cycles."},
                {"978-0062316110", "Good to Great", "Jim Collins", "Management", "An analysis of how good companies become great companies."},
                {"978-0062273208", "The Hard Thing About Hard Things", "Ben Horowitz", "Management", "Practical advice on building and running a startup."},
                {"978-1591847328", "Zero to One", "Peter Thiel", "Management", "Notes on startups and how to build the future."},
                {"978-0307465351", "Drive", "Daniel H. Pink", "Management", "An exploration of human motivation in business."},
                {"978-1591846406", "Start with Why", "Simon Sinek", "Management", "How great leaders inspire everyone to take action."},
                {"978-0062291578", "The Innovator's Dilemma", "Clayton M. Christensen", "Management", "When new technologies cause great firms to fail."},
                {"978-0062457714", "Principles", "Ray Dalio", "Management", "Life and work principles from a successful entrepreneur."},
                {"978-1591847489", "Leaders Eat Last", "Simon Sinek", "Management", "Why some teams pull together and others don't."},
                {"978-0060833459", "The 7 Habits of Highly Effective People", "Stephen R. Covey", "Management", "Powerful lessons in personal change."},

                // Litterature
                {"978-0451524935", "1984", "George Orwell", "Litterature", "A dystopian novel about totalitarianism and surveillance."},
                {"978-0061120084", "To Kill a Mockingbird", "Harper Lee", "Litterature", "A classic novel about racial injustice in the American South."},
                {"978-0743273565", "The Great Gatsby", "F. Scott Fitzgerald", "Litterature", "A novel about the American Dream in the Roaring Twenties."},
                {"978-0316769174", "The Catcher in the Rye", "J.D. Salinger", "Litterature", "A coming-of-age novel about teenage angst and alienation."},
                {"978-0679783268", "Pride and Prejudice", "Jane Austen", "Litterature", "A classic romance novel about manners and misunderstandings."},
                {"978-0060929879", "Brave New World", "Aldous Huxley", "Litterature", "A dystopian novel about a genetically engineered future society."},
                {"978-0143105428", "The Picture of Dorian Gray", "Oscar Wilde", "Litterature", "A philosophical novel about the pursuit of pleasure and beauty."},
                {"978-0141439693", "Jane Eyre", "Charlotte Brontë", "Litterature", "A Gothic romance about a governess and her mysterious employer."},
                {"978-0143039433", "One Hundred Years of Solitude", "Gabriel García Márquez", "Litterature", "A multi-generational saga with elements of magical realism."},
                {"978-0307474278", "The Kite Runner", "Khaled Hosseini", "Litterature", "A novel about friendship, betrayal, and redemption in Afghanistan."},

                // Chimie
                {"978-0321972012", "Chemistry: The Central Science", "Theodore E. Brown et al.", "Chimie", "A foundational textbook on chemistry."},
                {"978-1319153830", "Organic Chemistry", "Paula Yurkanis Bruice", "Chimie", "A comprehensive guide to organic chemistry."},
                {"978-1429218122", "Physical Chemistry", "Peter Atkins & Julio de Paula", "Chimie", "A thorough exploration of physical chemistry concepts."},
                {"978-1429219556", "Inorganic Chemistry", "Catherine Housecroft & Alan G. Sharpe", "Chimie", "A comprehensive textbook on inorganic chemistry."},
                {"978-1319154110", "Analytical Chemistry", "Daniel C. Harris & Charles A. Lucy", "Chimie", "A guide to analytical methods in chemistry."},
                {"978-1429275002", "Biochemistry", "Jeremy M. Berg et al.", "Chimie", "A comprehensive textbook on biochemistry."},
                {"978-0321768148", "Environmental Chemistry", "Colin Baird & Michael Cann", "Chimie", "An exploration of chemistry in environmental contexts."},
                {"978-1429288974", "Chemical Principles", "Peter Atkins et al.", "Chimie", "A foundational text on chemical principles."},
                {"978-1118501207", "The Periodic Table: A Very Short Introduction", "Eric R. Scerri", "Chimie", "A concise guide to the periodic table and its history."},
                {"978-0199608942", "Why Chemical Reactions Happen", "James Keeler & Peter Wothers", "Chimie", "An accessible explanation of chemical reactions."},

                // Cyber Security
                {"978-1118026472", "The Web Application Hacker's Handbook", "Dafydd Stuttard & Marcus Pinto", "Cyber Security", "A guide to web application security and hacking techniques."},
                {"978-1119085294", "The Hacker Playbook 3", "Peter Kim", "Cyber Security", "Practical guide to penetration testing."},
                {"978-1491902337", "Threat Modeling", "Adam Shostack", "Cyber Security", "Designing for security in software development."},
                {"978-1593277505", "Practical Malware Analysis", "Michael Sikorski & Andrew Honig", "Cyber Security", "The hands-on guide to dissecting malicious software."},
                {"978-1593272906", "The Tangled Web", "Michal Zalewski", "Cyber Security", "A guide to securing modern web applications."},
                {"978-1491971017", "Network Security Assessment", "Chris McNab", "Cyber Security", "Know your network."},
                {"978-1593278656", "Attacking Network Protocols", "James Forshaw", "Cyber Security", "A hacker's guide to capture, analysis, and exploitation."},
                {"978-1119085294", "Applied Cryptography", "Bruce Schneier", "Cyber Security", "Protocols, algorithms, and source code in C."},
                {"978-1118987055", "The Art of Memory Forensics", "Michael Hale Ligh et al.", "Cyber Security", "Detecting malware and threats in Windows, Linux, and Mac memory."},
                {"978-1593277581", "Black Hat Python", "Justin Seitz", "Cyber Security", "Python programming for hackers and pentesters."},

                // Mathematiques
                {"978-0262033848", "Introduction to Algorithms", "Thomas H. Cormen et al.", "Mathematiques", "A comprehensive guide to algorithms and data structures."},
                {"978-0321982384", "Linear Algebra and Its Applications", "Gilbert Strang", "Mathematiques", "A thorough exploration of linear algebra."},
                {"978-0521675994", "A Course in Analytic Number Theory", "Marius Overholt", "Mathematiques", "An introduction to analytic number theory."},
                {"978-0821847305", "Real and Complex Analysis", "Walter Rudin", "Mathematiques", "A classic text on real and complex analysis."},
                {"978-0821826942", "Topology", "James Munkres", "Mathematiques", "A comprehensive introduction to topology."},
                {"978-0486457796", "Differential Equations and Their Applications", "Martin Braun", "Mathematiques", "An introduction to applied mathematics."},
                {"978-0131816299", "Abstract Algebra", "David S. Dummit & Richard M. Foote", "Mathematiques", "A comprehensive text on abstract algebra."},
                {"978-0521675956", "A Course in Combinatorics", "J.H. van Lint & R.M. Wilson", "Mathematiques", "An exploration of combinatorial mathematics."},
                {"978-0486404530", "An Introduction to Probability Theory and Its Applications", "William Feller", "Mathematiques", "A classic text on probability theory."},
                {"978-0521675995", "A Course in Mathematical Logic for Mathematicians", "Yu. I. Manin", "Mathematiques", "An introduction to mathematical logic."},

                // Physique
                {"978-0465025275", "The Elegant Universe", "Brian Greene", "Physique", "An exploration of string theory and the nature of the universe."},
                {"978-1108409063", "A Student's Guide to the Schrödinger Equation", "Daniel A. Fleisch", "Physique", "An accessible introduction to quantum mechanics."},
                {"978-0691158716", "Einstein Gravity in a Nutshell", "A. Zee", "Physique", "A comprehensive introduction to general relativity."},
                {"978-0691175607", "Quantum Field Theory in a Nutshell", "A. Zee", "Physique", "An introduction to quantum field theory."},
                {"978-0199595068", "Statistical Physics of Particles", "Mehran Kardar", "Physique", "A comprehensive guide to statistical physics."},
                {"978-0691135144", "The Road to Reality", "Roger Penrose", "Physique", "A complete guide to the laws of the universe."},
                {"978-0691158716", "The Feynman Lectures on Physics", "Richard P. Feynman et al.", "Physique", "A classic collection of physics lectures."},
                {"978-0691175607", "Introduction to Electrodynamics", "David J. Griffiths", "Physique", "A comprehensive guide to electrodynamics."},
                {"978-0199595068", "Modern Classical Physics", "Kip S. Thorne & Roger D. Blandford", "Physique", "A comprehensive guide to classical physics."},
                {"978-0691135144", "The Theoretical Minimum", "Leonard Susskind & George Hrabovsky", "Physique", "What you need to know to start doing physics."}
        };

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (String[] book : books) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_ISBN, book[0]);
                values.put(COLUMN_TITLE, book[1]);
                values.put(COLUMN_AUTHOR, book[2]);
                values.put(COLUMN_CATEGORY, book[3]);
                values.put(COLUMN_DESCRIPTION, book[4]);

                db.insert(TABLE_BOOKS, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

}
