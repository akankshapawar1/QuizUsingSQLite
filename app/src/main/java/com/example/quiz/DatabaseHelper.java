package com.example.quiz;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.ListView;

//import com.example.quiz.QuizContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    //Questions qq = new Questions();
    //DisplayScore displayScore = new DisplayScore();
    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME="QuizJava.db";

    private static DatabaseHelper instance;

    //creates an object for creating, opening and managing the database.
    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if ( instance == null ){
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private static final String TABLE0_NAME = "quizCategories";
    private static final String COLUMNCAT_NAME = "name";

    private static final String TABLE_NAME="infoUser";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_EMAIL="email";
    private static final String COLUMN_PASSWORD="password";
    private static final String COLUMN_USERNAME="username";
    private static final String COLUMN_SCORE="score";


    private static final String TABLE2_NAME = "quizQuestions";
    private static final String COLUMN_QUESTION = "questions";
    private static final String COLUMN_OPTION1 ="option1";
    private static final String COLUMN_OPTION2 ="option2";
    private static final String COLUMN_OPTION3 ="option3";
    private static final String COLUMN_OPTION4 ="option4";
    private static final String COLUMN_ANSWERS_NR ="answerNr";
    private static final String COLUMN_DFFICULTY ="difficulty";
    private static final String COLUMN_CATEGORY_ID = "categoryID";

    /*private static final String TABLE3_NAME = "scoreBoard";
    private static final String COLUMN_USERNAME_SCORE="username";
    private static final String COLUMN_SCORE = "score";*/

    public static final String CATEGORY_TABLE_CREATE = "create table "+ TABLE0_NAME + "( "+
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMNCAT_NAME +" text "+
            ")";

    //public static final String get_username = "select username from infoUser";

    private static final String TABLE_CREATE = "create table infoUser (id integer primary key autoincrement," +
             "name text not null , email text not null , password text not null, username text not null, score integer);";

    private static final String TABLE2_CREATE = "CREATE TABLE " +
            TABLE2_NAME + " ("+
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_QUESTION + " TEXT, " +
            COLUMN_OPTION1 + " TEXT, " +
            COLUMN_OPTION2 + " TEXT, " +
            COLUMN_OPTION3 + " TEXT, " +
            COLUMN_OPTION4 + " TEXT, " +
            COLUMN_ANSWERS_NR + " INTEGER ," +
            COLUMN_DFFICULTY + " TEXT ,"+
            COLUMN_CATEGORY_ID + " INTEGER, "+
            "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES "+
            TABLE0_NAME + "(" + _ID + ")" + " ON DELETE CASCADE "+
            ")";

    /*public static final String TABLE3_CREATE = "CREATE TABLE "+ TABLE3_NAME + "("+
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COLUMN_USERNAME_SCORE + " TEXT, "+
            COLUMN_SCORE +" INTEGER "+ ")";*/

    //private final String UPDATE_SCORE = "UPDATE infoUser SET score ="+ cc.getScore() +" WHERE username ="+
           // cc.getUsername() ;



    SQLiteDatabase db;

    //called only once when database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        //executes the sql query not select query.
        db.execSQL(CATEGORY_TABLE_CREATE);
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE2_CREATE);
        //db.execSQL(TABLE3_CREATE);
        fillCategoriesTable();
        fillQuestionsTable();
    }


    //checking if username exists
    public Boolean checkUsername(String username){
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from infoUser where username=?",new String[]{username});
        if(cursor.getCount()>0) return false;
        else return true;
        //cursor.close();
    }

    //called when database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query0 = "DROP TABLE IF EXISTS "+ TABLE0_NAME;
        db.execSQL(query0);
        String query = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        db.execSQL(query);
        String query2 = "DROP TABLE IF EXISTS "+ TABLE2_NAME;
        db.execSQL(query2);
        //String query3 = "DROP TABLE IF EXISTS "+ TABLE3_NAME;
        //db.execSQL(query3);
        this.onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public void fillCategoriesTable(){
        Category c1 = new Category("JAVA");
        addCategory(c1);
        Category c2 = new Category("Database");
        addCategory(c2);
        Category c3 = new Category("Android");
        addCategory(c3);
    }

    private void addCategory(Category category){
        ContentValues cv = new ContentValues();
        cv.put(COLUMNCAT_NAME, category.getName());
        db.insert(TABLE0_NAME, null ,cv);
    }



    private void addQuestion(QuestionAnswers question) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_QUESTION, question.getQuestions());
        cv.put(COLUMN_OPTION1, question.getOption1());
        cv.put(COLUMN_OPTION2, question.getOption2());
        cv.put(COLUMN_OPTION3,question.getOption3());
        cv.put(COLUMN_OPTION4, question.getOption4());
        cv.put(COLUMN_ANSWERS_NR, question.getAnswerNr());
        cv.put(COLUMN_DFFICULTY, question.getDifficulty());
        cv.put(COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(TABLE2_NAME, null, cv);
    }

    public void insertContact(contact c){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,c.getName());
        values.put(COLUMN_EMAIL,c.getEmail());
        values.put(COLUMN_USERNAME,c.getUsername());
        values.put(COLUMN_PASSWORD,c.getPass());
        values.put(COLUMN_SCORE,c.getScore());

        db.insert(TABLE_NAME,null,values);
       db.close();
    }

    /*public boolean addScore(contact cc){
        db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_NAME,cc.getName());
        v.put(COLUMN_EMAIL,cc.getEmail());
        v.put(COLUMN_USERNAME,cc.getUsername());
        v.put(COLUMN_PASSWORD,cc.getPass());
        v.put(COLUMN_SCORE,cc.getScore());
        db.update(TABLE_NAME, v, " username = "+ cc.getUsername(),null);
        db.close();
        return true;

        //db.execSQL(UPDATE_SCORE);
    }*/

    public void addScore(String username,int score){
        db = this.getWritableDatabase();
        db= this.getReadableDatabase();
        /*ContentValues values = new ContentValues();
        v.put(COLUMN_USERNAME_SCORE,username);
        v.put(COLUMN_SCORE,score);
        db.insert(TABLE3_NAME,null,values);*/
        final String UPDATE_SCORE = "UPDATE infoUser SET score ="+ score +" WHERE username ="+username;
        db.execSQL(UPDATE_SCORE);
        db.close();
    }


    public String searchPass(String username){
        db = this.getReadableDatabase();
        String query = "select username , password from infoUser";
        Cursor cursor = db.rawQuery(query,null);
        String a,b;
        b="not found!";
        if(cursor.moveToFirst()){
            do {
                a= cursor.getString(0);
                if (a.equals(username)){
                    b= cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return b;
    }

    public List<Category> getAllCategories(){
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TABLE0_NAME,null);
        if (c.moveToFirst()){
            do{
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(_ID)));
                category.setName(c.getString(c.getColumnIndex(COLUMNCAT_NAME)));
                categoryList.add(category);
            }while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }

    public List<QuestionAnswers> getAllQuestions(){
        List<QuestionAnswers> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TABLE2_NAME,null);
        if (c.moveToFirst()) {
            do {
                QuestionAnswers question = new QuestionAnswers();
                question.setId(c.getInt(c.getColumnIndex(_ID)));
                question.setQuestions(c.getString(c.getColumnIndex(COLUMN_QUESTION)));//getColumnIndex tells cursor which column to select from
                question.setOption1(c.getString(c.getColumnIndex(COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(COLUMN_ANSWERS_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(COLUMN_DFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    public List<QuestionAnswers> getQuestions(int categoryID, String difficulty){
        List<QuestionAnswers> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = COLUMN_CATEGORY_ID + " = ? "+
                " AND " + COLUMN_DFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID),difficulty};
        Cursor c = db.query(
                TABLE2_NAME,
                null,
                selection,
                selectionArgs,
                null,null,null
        );

        if (c.moveToFirst()) {
            do {
                QuestionAnswers question = new QuestionAnswers();
                question.setId(c.getInt(c.getColumnIndex(_ID)));
                question.setQuestions(c.getString(c.getColumnIndex(COLUMN_QUESTION)));//getColumnIndex tells cursor which column to select from
                question.setOption1(c.getString(c.getColumnIndex(COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(COLUMN_ANSWERS_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(COLUMN_DFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    public void fillQuestionsTable(){
        QuestionAnswers q1 = new QuestionAnswers("What is the output of this program? \nclass mainclass { \npublic static void main(String args[]) {\n char a = 'A'; " +
                "\na++;\n System.out.print((int)a); } }",
                "66","67","65","64",1
                ,QuestionAnswers.DIFFICULTY_EASY,Category.JAVA);
        addQuestion(q1);
        QuestionAnswers q2 = new QuestionAnswers("What is the range of data type short in Java?",
                "-128 to 127", "-32768 to 32767", "-2147483648 to 2147483647", "None of the mentioned",  2
                ,QuestionAnswers.DIFFICULTY_EASY,Category.JAVA);
        addQuestion(q2);
        QuestionAnswers q3 = new QuestionAnswers("An expression involving byte, int, and literal numbers is promoted to which of these?",
                "int", "long", "byte", "float", 1
                ,QuestionAnswers.DIFFICULTY_EASY,Category.JAVA);
        addQuestion(q3);
        QuestionAnswers q4 = new QuestionAnswers("Which data type value is returned by all transcendental math functions?",
                "int", "float", "double", "long", 3
                ,QuestionAnswers.DIFFICULTY_EASY,Category.JAVA);
        addQuestion(q4);
        QuestionAnswers q5 = new QuestionAnswers("Which of these coding types is used for data type characters in Java?",
                "ASCII", "ISO-LATIN-1", "UNICODE", "None of the mentioned",1
                ,QuestionAnswers.DIFFICULTY_EASY,Category.JAVA);
        addQuestion(q5);
        QuestionAnswers q6 = new QuestionAnswers("Which of these values can a boolean variable contain?",
                "True & False", "0 & 1", "Any integer value", "Both a & b",1
                ,QuestionAnswers.DIFFICULTY_EASY,Category.JAVA);
        addQuestion(q6);
        QuestionAnswers q7 = new QuestionAnswers("What is the output of this program?\nclass mainclass {\npublic static void main(String args[]) {\nboolean var1 = true; boolean var2 = false;" +
                "\nif (var1)\nSystem.out.println(var1);\nelse System.out.println(var2);} }",
                "0", "1", "true", "false",3
                ,QuestionAnswers.DIFFICULTY_EASY,Category.JAVA);
        addQuestion(q7);
        QuestionAnswers q8 = new QuestionAnswers("Which of these class is superclass of String and StringBuffer class?",
                "java.util", "java.lang", "ArrayList","None of the mentioned",2
                ,QuestionAnswers.DIFFICULTY_EASY,Category.JAVA);
        addQuestion(q8);
        QuestionAnswers q9 = new QuestionAnswers("Which of these operators can be used to concatenate two or more String objects?",
                "+", "+=", "&", "||",1
                ,QuestionAnswers.DIFFICULTY_EASY,Category.JAVA);
        addQuestion(q9);
        QuestionAnswers q10 = new QuestionAnswers("Which of these method of class String is used to obtain length of String object?",
                "get()","Sizeof()", "lengthOf()","length()",4
                ,QuestionAnswers.DIFFICULTY_EASY,Category.JAVA);
        addQuestion(q10);
        QuestionAnswers q11 = new QuestionAnswers("Which of these methods deletes all the elements from invoking collection?",
                "clear()", "reset()", "delete()", "refresh()", 1
                ,QuestionAnswers.DIFFICULTY_MEDIUM,Category.JAVA);
        addQuestion(q11);
        QuestionAnswers q12 = new QuestionAnswers("Which of these interface is not a part of Java’s collection framework","List","Set","SortedMap","SortedList",4
                ,QuestionAnswers.DIFFICULTY_MEDIUM,Category.JAVA);
        addQuestion(q12);
        QuestionAnswers q13 = new QuestionAnswers("Which of these is an incorrect form of using method max() to obtain maximum element?",
                " max(Collection c)", "max(Collection c, Comparator comp)","max(Comparator comp)", " max(List c)", 3
                ,QuestionAnswers.DIFFICULTY_MEDIUM,Category.JAVA);
        addQuestion(q13);
        QuestionAnswers q14 = new QuestionAnswers("Which of these methods can convert an object into a List?",
                " SetList()"," ConvertList()"," singletonList()", "CopyList()", 3
                ,QuestionAnswers.DIFFICULTY_MEDIUM,Category.JAVA);
        addQuestion(q14);
        QuestionAnswers q15 = new QuestionAnswers("Which of these is static variable defined in Collections?",
                "EMPTY_SET", "EMPTY_LIST","EMPTY_MAP","All of the mentioned", 4
                ,QuestionAnswers.DIFFICULTY_MEDIUM,Category.JAVA);
        addQuestion(q15);
        QuestionAnswers q16 = new QuestionAnswers("Which of this access specifies can be used for a class so that its members can be accessed by a different class in the same package?",
                "Public","Protected","No Modifier","All of the mentioned", 4
                ,QuestionAnswers.DIFFICULTY_MEDIUM,Category.JAVA);
        addQuestion(q16);
        QuestionAnswers q17 = new QuestionAnswers("Which of the following is correct way of importing an entire package ‘pkg’?",
                "import pkg."," Import pkg. "," import pkg.* "," Import pkg.* ", 3
                ,QuestionAnswers.DIFFICULTY_MEDIUM,Category.JAVA);
        addQuestion(q17);
        QuestionAnswers q18 = new QuestionAnswers("What is the output of this program?\npackage pkg;\nclass output {\npublic static void main(String args[])\n{ " +
                "\nStringBuffer s1 = new StringBuffer('Hello');\ns1.setCharAt(1, x);\nSystem.out.println(s1);}}",
                " Xello","Xxxxx","Hxllo","Hexlo",3
                ,QuestionAnswers.DIFFICULTY_MEDIUM,Category.JAVA);
        addQuestion(q18);
        QuestionAnswers q19 = new QuestionAnswers(" Which of the following is incorrect statement about packages?",
                "Package defines a namespace in which classes are stored."," A package can contain other package within it.",
                " Java uses file system directories to store packages."," A package can be renamed without renaming the directory in which the classes are stored", 4
                ,QuestionAnswers.DIFFICULTY_MEDIUM,Category.JAVA);
        addQuestion(q19);
        QuestionAnswers q20 = new QuestionAnswers("Which of the following package stores all the standard java classes?",
                " Lang", "Java","Util","java.packages", 2
                ,QuestionAnswers.DIFFICULTY_MEDIUM,Category.JAVA);
        addQuestion(q20);
        QuestionAnswers q21 = new QuestionAnswers("Which of these keywords must be used to handle the exception thrown by try block in some rational manner?",
                "try", "finally", "throw", "catch", 4
                ,QuestionAnswers.DIFFICULTY_HARD,Category.JAVA);
        addQuestion(q21);
        QuestionAnswers q22 = new QuestionAnswers("Which of these class is related to all the exceptions that can be caught by using catch?",
                " Error", "Exception","RuntimeExecption","All of the mentioned", 2
                ,QuestionAnswers.DIFFICULTY_HARD,Category.JAVA);
        addQuestion(q22);
        QuestionAnswers q23 = new QuestionAnswers("Which of these handles the exception when no catch is used?",
                " Default handler","finally","throw handler", "Java run time system", 1
                ,QuestionAnswers.DIFFICULTY_HARD,Category.JAVA);
        addQuestion(q23);
        QuestionAnswers q24 = new QuestionAnswers("Which of these operator is used to generate an instance of an exception than can be thrown by using throw?",
                "new","malloc", "alloc", "thrown", 1
                ,QuestionAnswers.DIFFICULTY_HARD,Category.JAVA);
        addQuestion(q24);
        QuestionAnswers q25 = new QuestionAnswers("Which of these interface is not a member of java.io package?",
                " DataInput","ObjectInput","ObjectFilter","FileFilter", 3
                ,QuestionAnswers.DIFFICULTY_HARD,Category.JAVA);
        addQuestion(q25);
        QuestionAnswers q26 = new QuestionAnswers("Which of these is a process of converting a simple data type into a class?",
                "type wrapping","type conversion","type casting", "None of the Mentioned", 2
                ,QuestionAnswers.DIFFICULTY_HARD,Category.JAVA);
        addQuestion(q26);
        QuestionAnswers q27 = new QuestionAnswers("Which of these methods is used to check for infinitely large and small values?",
                " isInfinite()"," isNaN()"," Isinfinite()"," IsNaN()", 1
                ,QuestionAnswers.DIFFICULTY_HARD,Category.JAVA);
        addQuestion(q27);
        QuestionAnswers q28 = new QuestionAnswers("Which of these methods is used to obtain value of invoking object as a long?",
                " long value()"," long longValue()"," Long longvalue()"," Long Longvalue()", 2
                ,QuestionAnswers.DIFFICULTY_HARD,Category.JAVA);
        addQuestion(q28);
        QuestionAnswers q29 = new QuestionAnswers("Which of these class can encapsulate an entire executing program?",
                " Void","Process","Runtime", "System", 2
                ,QuestionAnswers.DIFFICULTY_HARD,Category.JAVA);
        addQuestion(q29);
        QuestionAnswers q30 = new QuestionAnswers("Which of the following is method of System class is used to find how long a program takes to execute?",
                " currenttime()"," currentTime()"," currentTimeMillis()"," currenttimeMillis()", 3
                ,QuestionAnswers.DIFFICULTY_HARD,Category.JAVA);
        addQuestion(q30);
        QuestionAnswers q31 = new QuestionAnswers("How to kill an activity in Android?","finish()","finishActivity(int requestCode)",
                "A & B","kill()",3,QuestionAnswers.DIFFICULTY_EASY,Category.ANDROID);
        addQuestion(q31);
        QuestionAnswers q32 = new QuestionAnswers("On which thread broadcast receivers will work in android?",
                "Activity Thread","”,” Main Thread","Worker Thread","None of the Above",2,QuestionAnswers.DIFFICULTY_EASY,Category.ANDROID);
        addQuestion(q32);
        QuestionAnswers q33 = new QuestionAnswers( "Which features are considered while creating android application?",
                "Screen SizeInput configuration","Platform Version","None of the above","All of above",4,
                QuestionAnswers.DIFFICULTY_EASY,Category.ANDROID);
        addQuestion(q33);
        QuestionAnswers q34 = new QuestionAnswers("What is breakpoint in android?","Breaks the application","Breaks the development code",
                " Breaks the execution",
                " None of the above",3,QuestionAnswers.DIFFICULTY_EASY,Category.ANDROID);
        addQuestion(q34);
        QuestionAnswers q35 = new QuestionAnswers("What is the package name of HTTP client in android?"," com.json"," org.apache.http.client"
                ," com.android.JSON"," org.json",2,QuestionAnswers.DIFFICULTY_EASY,Category.ANDROID
        );
        addQuestion(q35);
        QuestionAnswers q36 = new QuestionAnswers("How to find the JSON element length in android JSON?","count()","sum()",
                " add()","length()",4,QuestionAnswers.DIFFICULTY_EASY,Category.ANDROID
        );
        addQuestion(q36);
        QuestionAnswers q37 = new QuestionAnswers("What is the purpose of super.onCreate() in android?", "To create an activity",
                "To create a graphical window for subclass"," It allows the developers to write the program"," None of the above",
                2,QuestionAnswers.DIFFICULTY_EASY,Category.ANDROID
        );
        addQuestion(q37);
        QuestionAnswers q38 = new QuestionAnswers("What is off-line synchronization in android?","Synchronization with internet",
                "Background synchronization","Synchronization without internet"," None of the above",3,QuestionAnswers.DIFFICULTY_EASY,Category.ANDROID
        );
        addQuestion(q38);
        QuestionAnswers q39 = new QuestionAnswers("What is LastKnownLocation in android?","To find the last location of a phone",
                "To find known location of a phone"," To find the last known location of a phone","None of the above", 3,QuestionAnswers.DIFFICULTY_EASY,Category.ANDROID

        );
        addQuestion(q39);
        QuestionAnswers q40 = new QuestionAnswers("What is a GCM in android?","Goggle Could Messaging for chrome","cGoggle Count Messaging",
                "Goggle Message pack","None of the above", 4,QuestionAnswers.DIFFICULTY_EASY,Category.ANDROID);
        addQuestion(q40);
        QuestionAnswers q41 = new QuestionAnswers("What is the JSON exception in android?","Json Exception","Json Not found exception",
                "Input not found exception","None of the above", 1,QuestionAnswers.DIFFICULTY_MEDIUM,Category.ANDROID);
        addQuestion(q41);
        QuestionAnswers q42 = new QuestionAnswers("What is the HTTP response error code status in android?","status code < 100",
                "status code > 100","status >= 400","None of the above", 3,QuestionAnswers.DIFFICULTY_MEDIUM,Category.ANDROID);
        addQuestion(q42);
        QuestionAnswers q43 = new QuestionAnswers("What is the HTTP response error code status in android?","status code < 100",
                "status code > 100","status >= 400","None of the above", 3,QuestionAnswers.DIFFICULTY_MEDIUM,Category.ANDROID);
        addQuestion(q43);
        QuestionAnswers q44 = new QuestionAnswers("In which technique, we can refresh the dynamic content in android?","Ajax",
                "Android","None of the Above","All of the above", 2,QuestionAnswers.DIFFICULTY_MEDIUM,Category.ANDROID);
        addQuestion(q44);

        QuestionAnswers q45 = new QuestionAnswers("What is the library of Map View in android?"," com.map","com.goggle.gogglemaps",
                "in.maps"," com.goggle.android.maps",4,QuestionAnswers.DIFFICULTY_MEDIUM,Category.ANDROID);
        addQuestion(q45);
        QuestionAnswers q46 = new QuestionAnswers("Data can be read from local source XML in android through", "XML resource parsing",
                "XML pull parsing ","DOM parsing","None of the above", 1,QuestionAnswers.DIFFICULTY_MEDIUM,Category.ANDROID);
        addQuestion(q46);
        QuestionAnswers q47 = new QuestionAnswers(  "What does httpclient.execute() returns in android?","Http entity",
                "Http response","Http result","None of the above.", 2,QuestionAnswers.DIFFICULTY_MEDIUM,Category.ANDROID);
        addQuestion(q47);
        QuestionAnswers q48 = new QuestionAnswers("What are the functionalities of HTTP Client interface in android?","Connectionmanagement",
                "Cookies management","Authentication management","All of the above", 4,QuestionAnswers.DIFFICULTY_MEDIUM,Category.ANDROID);
        addQuestion(q48);
        QuestionAnswers q49 = new QuestionAnswers("How to fix crash using log cat in android?","Gmail","log cat contains the exception name " +
                "along with the line number","Google Search ","None of the above.", 2,QuestionAnswers.DIFFICULTY_MEDIUM,Category.ANDROID);
        addQuestion(q49);
        QuestionAnswers q50 = new QuestionAnswers("How to fix crash using log cat in android?","Gmail","log cat contains the " +
                "exception name along with the line number","Google Search ","None of the above.", 2,QuestionAnswers.DIFFICULTY_MEDIUM,Category.ANDROID);
        addQuestion(q50);
        QuestionAnswers q51 = new QuestionAnswers("What are the debugging techniques available in android?","DDMS","Breaking point",
                "Memory profiling","None of the above.", 4,QuestionAnswers.DIFFICULTY_HARD,Category.ANDROID);
        addQuestion(q51);
        QuestionAnswers q52 = new QuestionAnswers("What is fragment in android?","JSON","Peace of Activity","Layout",
                "None of the above", 2,QuestionAnswers.DIFFICULTY_HARD,Category.ANDROID);
        addQuestion(q52);
        QuestionAnswers q53 = new QuestionAnswers("Fragment in Android can be found through?","findByID", "findFragmentByID()",
                "getContext.findFragmentByID()","FragmentManager.findFragmentByID()", 4,QuestionAnswers.DIFFICULTY_HARD,Category.ANDROID);
        addQuestion(q53);
        QuestionAnswers q54 = new QuestionAnswers("What is off-line synchronization in android?","Synchronization with internet", "Background synchronization",
                "Synchronization without internet","None of the above", 3,QuestionAnswers.DIFFICULTY_HARD,Category.ANDROID);
        addQuestion(q54);
        QuestionAnswers q55 = new QuestionAnswers("What is bean class in android?","A class used to hold states and objects","A bean class can be" +
                " passed from one activity to another.","A mandatory class in android","None of the above", 2,QuestionAnswers.DIFFICULTY_HARD,Category.ANDROID);
        addQuestion(q55);
        QuestionAnswers q56 = new QuestionAnswers("What is a thread in android?","Same as services", "Background activity","Broadcast Receiver",
                "Independent dis-patchable unit is called a thread", 4,QuestionAnswers.DIFFICULTY_HARD,Category.ANDROID);
        addQuestion(q56);
        QuestionAnswers q57 = new QuestionAnswers("What is anchor view?","Same as list view","provides the information on " +
                "respective relative positions","Same as relative layout","None of the above", 2,QuestionAnswers.DIFFICULTY_HARD,Category.ANDROID);
        addQuestion(q57);
        QuestionAnswers q58 = new QuestionAnswers("What does httpclient.execute() returns in android?",
                "Http entity","Http response","Http result","None of the above.", 2,QuestionAnswers.DIFFICULTY_HARD,Category.ANDROID);
        addQuestion(q58);
        QuestionAnswers q59 = new QuestionAnswers("What are the functionalities of HTTP Client interface in android?","Connectionmanagement",
                "Cookies management","Authentication management","All of the above", 4,QuestionAnswers.DIFFICULTY_HARD,Category.ANDROID);
        addQuestion(q59);
        QuestionAnswers q60 = new QuestionAnswers("What is the package name of HTTP client in android?"," com.json"," org.apache.http.client",
                " com.android.JSON"," org.json",2,QuestionAnswers.DIFFICULTY_HARD,Category.ANDROID);
        addQuestion(q60);
        QuestionAnswers q61 = new QuestionAnswers("Which SQL statement is used to extract data from a database?", "EXTRACT", "GET",
                "OPEN", "SELECT", 4,QuestionAnswers.DIFFICULTY_EASY,Category.DATABASE);
        addQuestion(q61);
        QuestionAnswers q62 = new QuestionAnswers("Which of the following keyword can be used to return different values?", "SELECT",
                "GET", "OPEN", "DISTINCT", 4,QuestionAnswers.DIFFICULTY_EASY,Category.DATABASE);
        addQuestion(q62);
        QuestionAnswers q63 = new QuestionAnswers("……..operator is used to display a record if either the first condition or the second condition is true",
                "AND", "OR", "Both (A) & (B)", "None of the above", 2,QuestionAnswers.DIFFICULTY_EASY,Category.DATABASE);
        addQuestion(q63);
        QuestionAnswers q64 = new QuestionAnswers("Which of the following DBMS provides faster response time and better performance?", "Relational Database Management System (RDBMS)",
                "NoSQL DBMS", "In-Memory Database Management System (IMDBMS)", "None of the above", 3,QuestionAnswers.DIFFICULTY_EASY,Category.DATABASE);
        addQuestion(q64);
        QuestionAnswers q65 = new QuestionAnswers("………..is suitable for data warehouses that have a large number of similar data items", "Relational Database Management System (RDBMS)",
                "Columnar Database Management system (CDBMS)", "In-Memory Database Management System (IMDBMS)", "None of the above", 2,QuestionAnswers.DIFFICULTY_EASY,Category.DATABASE);
        addQuestion(q65);
        QuestionAnswers q66 = new QuestionAnswers("Which of the following is standard interactive and programming language for getting information from and updating a database", "SQL",
                "PHP", "ASP", "None of the above", 1,QuestionAnswers.DIFFICULTY_EASY,Category.DATABASE);
        addQuestion(q66);
        QuestionAnswers q67 = new QuestionAnswers("keyword sorts the record in ascending order by default", "ORDER BY", "SORT BY", "SORT", "None of the above", 1,QuestionAnswers.DIFFICULTY_EASY,Category.DATABASE);
        addQuestion(q67);
        QuestionAnswers q68 = new QuestionAnswers("Which of the following is an open standard Application Programming Interface (API) for accessing a database?",
                "Universal Data Access", "Open Database Connectivity", "Command Line Interface", "Open Data-Link Interface", 2,QuestionAnswers.DIFFICULTY_EASY,Category.DATABASE);
        addQuestion(q68);
        QuestionAnswers q69 = new QuestionAnswers("Which SQL statement is used to insert new data in a database?", "INSERT INTO", "ADD NEW",
                "ADD RECORD", "None of the above", 1,QuestionAnswers.DIFFICULTY_EASY,Category.DATABASE);
        addQuestion(q69);
        QuestionAnswers q70 = new QuestionAnswers("Which method of Online Analytical Processing stores data in both a relational and a multi dimensional database", "Hybrid OLAP",
                "Relational OLAP", "OLAP", "None of the above", 1,QuestionAnswers.DIFFICULTY_EASY,Category.DATABASE);
        addQuestion(q70);
        QuestionAnswers q71 = new QuestionAnswers(" A collection of conceptual tools for describing data, relationships, semantics and constraints is referred to as", " Data Model" ,
                " E-R Model", "DBMS", "All of the above", 1,QuestionAnswers.DIFFICULTY_MEDIUM,Category.DATABASE);
        addQuestion(q71);
        QuestionAnswers q72 = new QuestionAnswers("In E-R Diagram, weak entity is represented by", "Rectangle", "Square",
                "Double Rectangle", "Circle", 3,QuestionAnswers.DIFFICULTY_MEDIUM,Category.DATABASE);
        addQuestion(q72);
        QuestionAnswers q73 = new QuestionAnswers( "An entity that is related with itself is known as", "binary relationship", "recursive relationship", "ternary relationship",
                "None of the above", 2 ,QuestionAnswers.DIFFICULTY_MEDIUM,Category.DATABASE);
        addQuestion(q73);
        QuestionAnswers q74= new QuestionAnswers(" ....... is a bottom-up approach in which two lower level entities combine to form a higher level Entity", "Aggregation",
                "Specialization","Generalization","None of the above", 3,QuestionAnswers.DIFFICULTY_MEDIUM,Category.DATABASE);
        addQuestion(q74);
        QuestionAnswers q75 = new QuestionAnswers(" An entity set that does not have sufficient attributes to form a primary key, is a ......", "Primary entity set", "Weak entity set",
                "Strong entity set", "None of the above", 2,QuestionAnswers.DIFFICULTY_MEDIUM,Category.DATABASE);
        addQuestion(q75);
        QuestionAnswers q76 = new QuestionAnswers( "Which SQL command delete all the records and does not remove the structure?", " Drop",
                "Insert", "Truncate", "None of the above",  3,QuestionAnswers.DIFFICULTY_MEDIUM,Category.DATABASE);
        addQuestion(q76);
        QuestionAnswers q77 = new QuestionAnswers( "Which of the following is a Data Definition Language (DDL) command?", "Delete",
                "Insert", "Drop", "Merge", 3,QuestionAnswers.DIFFICULTY_MEDIUM,Category.DATABASE);
        addQuestion(q77);
        QuestionAnswers q78 = new QuestionAnswers("In ........ database, data is organized in the form of trees with nodes", "Hierarchical",
                "Relational", "Network", "None of the above",  1,QuestionAnswers.DIFFICULTY_MEDIUM,Category.DATABASE);
        addQuestion(q78);
        QuestionAnswers q79 = new QuestionAnswers("Which command is used to retrieve records from one or more table?", "Delete", "Insert",
                "Drop", "Select", 4,QuestionAnswers.DIFFICULTY_MEDIUM,Category.DATABASE);
        addQuestion(q79);
        QuestionAnswers q80 = new QuestionAnswers(" A collection of conceptual tools for describing data, relationships, semantics and constraints is referred to as",
                " Data Model" , " E-R Model", "DBMS", "All of the above", 1,QuestionAnswers.DIFFICULTY_MEDIUM,Category.DATABASE);
        addQuestion(q80);
        QuestionAnswers q81 = new QuestionAnswers("Rows of a relation are called…", "entity", "degree",
                "tuples", "None of the above", 3,QuestionAnswers.DIFFICULTY_HARD,Category.DATABASE);
        addQuestion(q81);
        QuestionAnswers q82 = new QuestionAnswers("The number of tuples in a relation is termed as", "cardinality", "entity",
                "column", "None of the above", 1,QuestionAnswers.DIFFICULTY_HARD,Category.DATABASE);
        addQuestion(q82);
        QuestionAnswers q83 = new QuestionAnswers("In SQL, Which of the following is a Data Manipulation Language(DML) command?", "create",
                "alter", "merge", "drop" , 3,QuestionAnswers.DIFFICULTY_HARD,Category.DATABASE);
        addQuestion(q83);
        QuestionAnswers q84 = new QuestionAnswers("..... is a top-down approach in which one higher level entity can be divided into two lower level entities",
                "Aggregation", "Specialization", "Generalization", "None of the above", 2,QuestionAnswers.DIFFICULTY_HARD,Category.DATABASE);
        addQuestion(q84);
        QuestionAnswers q85 = new QuestionAnswers("Which of the following is not a type of database?", "Hierarchical",
                "Relational", "Network", "Transition", 4,QuestionAnswers.DIFFICULTY_HARD,Category.DATABASE);
        addQuestion(q85);
        QuestionAnswers q86 = new QuestionAnswers("In a relational database, each tuple is divided into fields called…", "Relations",
                "Domains", "Queries", "None of the above", 2,QuestionAnswers.DIFFICULTY_HARD,Category.DATABASE);
        addQuestion(q86);
        QuestionAnswers q87 = new QuestionAnswers("In E-R Diagram, attribute is represented by.....", "Rectangle", "Square",
                "Double Rectangle", "eclipse", 4,QuestionAnswers.DIFFICULTY_HARD,Category.DATABASE);
        addQuestion(q87);
        QuestionAnswers q88 = new QuestionAnswers("In SQL, TCL stands for", "Transmission Control Language", "Transaction Central Language",
                "Ternary Control Language", "Transaction Control Language", 4,QuestionAnswers.DIFFICULTY_HARD,Category.DATABASE);
        addQuestion(q88);
        QuestionAnswers q89 =new QuestionAnswers("Which of the following is a part of the Oracle database system?", "Free lists",
                "Front end", "Network", "None of the above", 2,QuestionAnswers.DIFFICULTY_HARD,Category.DATABASE);
        addQuestion(q89);
        QuestionAnswers q90 = new QuestionAnswers("Which of the following is used with database?", "ATM", "Payment gateway",
                "Data Mining", "None of the above", 3,QuestionAnswers.DIFFICULTY_HARD,Category.DATABASE);
        addQuestion(q90);
    }


}
