package com.example.tonio.actionbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonio.actionbar.SearchInterface.SearchListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NoteCardInterface.NoteCardListener,
        HomeInterface.HomeListener,StartScreen.StartListener,TipsScreen.TipsListener,LoginScreen.LogListener,
        SignUpScreen.SignUpListener,QuizScreen.QuizListener,QuizScoreScreen.QuizScoreListener,SearchListener {

    private int userNum;
    private int quizCount =0;
    private int wrongCount = 2;
    private int score = 0;
    private String [] currentIDList = new String[20];
    private String currentID;
    private String currentEmail;
    private int userPoints;
    private Vibrator vib;
    private int numcards;
    private TextView tipsText;
    private TextView detailsText;
    private static ArrayList<String> detailsTxtLines;
    private boolean infavorites = false;
    private int index;
    private int bookmarkCount;
    int likedcount;
    int dislikescount;
    private int numOfUsers;
    private EditText inputSearch;
    private ListView noteSearchList;
    private ArrayAdapter<String> adapter;
    private StringBuilder finalStringTips;
    private StringBuilder finalStringDetails;
    private static ArrayList<String> tipsTxtLines;
    private static final String PREFS_TAG = "IndexPlace";
    private String[] noteCardAnswerList =  new String[198];
    private String[] noteCardsList = new String[198];
    private String[] noteCardsID = new String[198];
    private String[] bookmarkList = new String[198];
    private String currentQID;
    private String[] quizList = new String[10];
    private String[] quizAnswer = new String[10];
    private String[] wrongChoicesList = new String[30];
    private String[]emailList = new String[20];
    private String[]passwordList = new String[20];
    private String[] likelist = new String[198];
    private String[] dislikelist = new String[198];
    private  Random rand = new Random();
    private int place;
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://jipa-server.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

   private  final  GitHubService service = retrofit.create(GitHubService.class);




    private  class newUser{

        private String firstname;
        private String lastname;
        private String email;
        private String password;
        private Number points;


        public newUser( String firstname,String lastname,String email,String password, Number points)
        {

            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.password = password;
            this.points = points;
        }
    }
    private class newFeedback{

        public String description;
        public String  user;
        public boolean read;
        public boolean done;


        public newFeedback(String description,String user,boolean read, boolean done){
            this.description = description;
            this.user = user;
            this.read = read;
            this.done = done;
        }
    }
    private class Question{
        private String _id;
        private String question;
        private String answer;
        private boolean quiz;
        private String [] wrong_choices;
        private ArrayList<String> likes;
        private ArrayList<String> dislikes;
        private ArrayList<String> bookmark;
        private String [] keyword;
        public Question(String _id,String question,String answer,boolean quiz, String [] wrong_choices,ArrayList<String> likes,
                        ArrayList<String> dislikes, ArrayList<String> bookmark,String[]keyword){
            this._id = _id;
            this.question = question;
            this.quiz = quiz;
            this.answer = answer;
            this.keyword = keyword;
            this.wrong_choices = wrong_choices;
            this.likes = likes;
            this.dislikes = dislikes;
            this.bookmark = bookmark;

        }
    }
    private class User {
        public String _id;
        public String firstname;
        public String lastname;
        public String password;
        public String email;
        public int points;
        public ArrayList<String> likes;
        public ArrayList<String> dislikes;
        public ArrayList<String>bookmark;

        public User(String _id, String firstname, String lastname,String password,String email,int points,
                    ArrayList<String> likes, ArrayList<String> dislikes, ArrayList<String> bookmark ){
            this._id = _id;
            this.firstname = firstname;
            this.lastname = lastname;
            this.password = password;
            this.email = email;
            this.points = points;
            this.likes = likes;
            this.dislikes = dislikes;
            this.bookmark = bookmark;
        }
    }

    public interface GitHubService {
        @POST("/api/feedback")
        Call<newFeedback> createFeedback(@Body newFeedback feedback);

        @GET("api/user")
        Call<List<User>> getUser();

        @FormUrlEncoded
        @POST("api/user")
        Call<List<User>> addpoints(@Field("points")int points);

        @FormUrlEncoded
        @POST("api/question/{id}/bookmark")
        Call<Question> bookmarked(@Path("id")String quesiontID,@Field("user")String userid,@Field("bookmark")boolean bookmark);

        @FormUrlEncoded
        @POST("api/question/{id}/like")
        Call<Question> liked(@Path("id")String quesiontID,@Field("user")String userid,@Field("like")boolean likes,@Field("dislike")boolean dislikes);


        @GET("/api/question")
        Call<List<Question>> getQuestion();

        @POST("/api/user")
       Call<newUser> createUser(@Body newUser user);



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.INVISIBLE);
        setSupportActionBar(toolbar);

        View firstScreen = findViewById(R.id.fragfscreen);
        firstScreen.setVisibility(View.VISIBLE);

        hideAllScreens();
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        bookmarkCount =0;
        numcards = 198;
        place = rand.nextInt(numcards);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Call<List<Question>> call2 = service.getQuestion();
        call2.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                int numberofCards = response.body().size();
                int quizCount = 0;
                int wrongCCount = 0;
                int count = 0;
                int i =0;

                for (int x = 0; x < numberofCards; x++) {

                        if (!response.body().get(x).quiz ) {
                            noteCardsList[i] = response.body().get(x).question;
                            noteCardAnswerList[i] = response.body().get(x).answer;
                            noteCardsID[i] = response.body().get(x)._id;
                           i++;
                        } else {
                            quizList[quizCount] = response.body().get(x).question;
                            quizAnswer[quizCount] = response.body().get(x).answer;
                            while (wrongCCount <= 2) {
                                wrongChoicesList[count] = response.body().get(x).wrong_choices[wrongCCount];
                                wrongCCount++;
                                count++;
                            }
                            wrongCCount = 0;
                            quizCount++;
                        }


                    }

                }


            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {

            }
        });


        Call<List<User>> call = service.getUser();
       call.enqueue(new Callback<List<User>>() {
           @Override
           public void onResponse(Call<List<User>> call, Response<List<User>> response) {
              numOfUsers = response.body().size();
              for(int x = 0; x<numOfUsers;x++)
              {
                  emailList[x] = response.body().get(x).email;
                  passwordList[x] = response.body().get(x).password;
                  currentIDList[x] = response.body().get(x)._id;


              }


           }

           @Override
           public void onFailure(Call<List<User>> call, Throwable t) {

           }
       });

    }
    @Override
    public void SearchInterfaceButtons(String button) {
        if(button.equals("back")){
            View frag2 = findViewById(R.id.fragnotecard);
            frag2.setVisibility(View.VISIBLE);
            View search = findViewById(R.id.searchPage);
            search.setVisibility(View.INVISIBLE);
        }


    }
    @Override
    public void LoginScreenButtons(String button, String email, String password) {
        boolean test = false;
        if(button.equals("signUp")){
            View loginScreen = findViewById(R.id.fraglogin);
            loginScreen.setVisibility(View.INVISIBLE);

            View signUp = findViewById(R.id.fragsign);
            signUp.setVisibility(View.VISIBLE);

        }else if(button.equals("login")) {
            vib.vibrate(50);
            for (int x = 0; x <numOfUsers; x++) {
                if (emailList[x].equals(email) && passwordList[x].equals(password)) {
                    View loginScreen = findViewById(R.id.fraglogin);
                    loginScreen.setVisibility(View.INVISIBLE);
                    View home = findViewById(R.id.fragmenu);
                    home.setVisibility(View.VISIBLE);
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    toolbar.setVisibility(View.VISIBLE);
                    test = true;
                    userNum = x;
                    Call<List<User>> call = service.getUser();
                    call.enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                           bookmarkCount = response.body().get(userNum).bookmark.size();
                             likedcount  = response.body().get(userNum).likes.size();
                             dislikescount = response.body().get(userNum).dislikes.size();
                            for(int x =0; x<bookmarkCount;x++) {

                                    bookmarkList[x] = response.body().get(userNum).bookmark.get(x);

                            }

                            for(int x =0; x<likedcount;x++) {

                                likelist[x] = response.body().get(userNum).likes.get(x);

                            }
                            for(int x =0; x<dislikescount;x++) {

                                dislikelist[x] = response.body().get(userNum).dislikes.get(x);

                            }
                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {

                        }
                    });

                    currentID = currentIDList[x];
                    currentEmail = emailList[x];

                    break;

                }else if (!emailList[x].equals(email) && !passwordList[x].equals(password)){

                   test = false;
                }
            }

            if(!test){
                Context context = getApplicationContext();
                CharSequence text = "Try Again Wrong email/password";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }
    @Override
    public void StartScreenButtons(String button){
        if (button.equals("logBtn")) {
            View fragfs = findViewById(R.id.fragfscreen);
            fragfs.setVisibility(View.INVISIBLE);
            View loginScreen = findViewById(R.id.fraglogin);
            loginScreen.setVisibility(View.VISIBLE);

        }else if(button.equals("signBtn")){
            View fragfs = findViewById(R.id.fragfscreen);
            fragfs.setVisibility(View.INVISIBLE);
            View signup = findViewById(R.id.fragsign);
            signup.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public void SignUpButtons(String button,String firstName,String lastName,String email,String password){
            if(button.equals("allBtn")) {
                View loginScreen = findViewById(R.id.fraglogin);
                loginScreen.setVisibility(View.VISIBLE);

                View signUp = findViewById(R.id.fragsign);
                signUp.setVisibility(View.INVISIBLE);

            }else if (button.equals("creatBtn")){
                newUser user = new newUser( firstName,lastName,email,password,0);
                Call<newUser> call = service.createUser(user);
                numOfUsers = numOfUsers +1;
                emailList[numOfUsers-1] = email;
                passwordList[numOfUsers-1] = password;
                call.enqueue(new Callback<newUser>() {
                    @Override
                    public void onResponse(Call<newUser> call, Response<newUser> response) {
                        View signUp = findViewById(R.id.fragsign);
                        signUp.setVisibility(View.INVISIBLE);
                        View loginScreen = findViewById(R.id.fraglogin);
                        loginScreen.setVisibility(View.VISIBLE);

                        Context context = getApplicationContext();
                        CharSequence text = "Congratulations You are JIPA Member Login";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();


                    }

                    @Override
                    public void onFailure(Call<newUser> call, Throwable t) {

                    }
                });



            }
    }
    @Override
    public void NoteCardInterfaceButtons(String button) {

        if (button.equals("answerBtn")) {
            TextView lv = (TextView) findViewById(R.id.question);
            Button ans = (Button) findViewById(R.id.answerBtn);
            ans.setVisibility(View.INVISIBLE);
            lv.setText(noteCardAnswerList[place]);
        }else if(button.equals("search")) {
            View frag2 = findViewById(R.id.fragnotecard);
            frag2.setVisibility(View.INVISIBLE);
            View search = findViewById(R.id.searchPage);
            search.setVisibility(View.VISIBLE);
            noteSearchList = (ListView) findViewById(R.id.list_view);
            inputSearch = (EditText) findViewById(R.id.inputSearch);

            // Adding items to listview
            adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, noteCardsList);
            noteSearchList.setAdapter(adapter);


            inputSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    MainActivity.this.adapter.getFilter().filter(cs);


                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {


                }

                @Override
                public void afterTextChanged(Editable arg0) {

                }
            });
            noteSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parentView, View childView,
                                        int position, long id) {
                    TextView lv = (TextView) findViewById(R.id.question);
                    vib.vibrate(50);
                    View frag2 = findViewById(R.id.fragnotecard);
                    frag2.setVisibility(View.VISIBLE);
                    View search = findViewById(R.id.searchPage);
                    search.setVisibility(View.INVISIBLE);

                    for(int x = 0; x<198;x++){

                        if(adapter.getItem(position).equals(noteCardsList[x])){
                            place = x;
                        }

                    }


                    lv.setText(noteCardsList[place]);
                }


            });

            noteSearchList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    int position = 0;
                    for(int x = 0; x<198;x++){

                        if(adapter.getItem(pos).equals(noteCardsList[x])){
                            position = x;
                        }

                    }
                    Context context = getApplicationContext();
                    CharSequence text = "Question: " + noteCardsList[position] + "\n" + "Answer: " + noteCardAnswerList[position];
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();


                    return true;
                }
            });
        } else if (button.equals(("backBtn"))) {
            View frag = findViewById(R.id.fragmenu);
            frag.setVisibility(View.VISIBLE);
            View frag2 = findViewById(R.id.fragnotecard);
            frag2.setVisibility(View.INVISIBLE);
            infavorites = false;
        } else if (button.equals(("nextBtn"))) {
            Button ans = (Button)findViewById(R.id.answerBtn);
            ans.setVisibility(View.VISIBLE);
            place = rand.nextInt(numcards)+0;
            TextView lv = (TextView) findViewById(R.id.question);
           checkButtons();
            currentQID = noteCardsID[place];
                lv.setText(noteCardsList[place]);


        }else if (button.equals("like")){
            vib.vibrate(50);

            Call<Question> call = service.liked(currentQID,currentID,true,false);
            call.enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    Context context = getApplicationContext();
                    CharSequence text = "check "+ call.request();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                @Override
                public void onFailure(Call<Question> call, Throwable t) {

                }
            });

        }else if (button.equals("dislike")){
            vib.vibrate(50);

            Call<Question> call = service.liked(currentQID, currentID, false, true);
            call.enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    Context context = getApplicationContext();
                    CharSequence text = "check ";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                @Override
                public void onFailure(Call<Question> call, Throwable t) {

                }
            });

        }else if (button.equals("fav")){
            vib.vibrate(50);
             currentQID =  noteCardsID[0];
            bookmarkCount++;
           bookmarkList[bookmarkCount] = currentQID;

            FloatingActionButton fav = (FloatingActionButton)findViewById(R.id.fab);
            if(fav.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_favorite_border_blue_24dp).getConstantState()) {

                Call<Question> call = service.bookmarked(currentQID, currentID, true);
                call.enqueue(new Callback<Question>() {
                    @Override
                    public void onResponse(Call<Question> call, Response<Question> response) {


                    }

                    @Override
                    public void onFailure(Call<Question> call, Throwable t) {

                    }
                });
            }
            else{
                Call<Question> call = service.bookmarked(currentQID, currentID, false);
                call.enqueue(new Callback<Question>() {
                    @Override
                    public void onResponse(Call<Question> call, Response<Question> response) {


                    }

                    @Override
                    public void onFailure(Call<Question> call, Throwable t) {

                    }
                });
            }




        }

    }
    @Override
    public void QuizScoreButtons(String button) {
        if(button.equals("done")){
            userPoints = userPoints +score;
            Call<List<User>> call = service.addpoints(userPoints);
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {


                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {

                }
            });
            View qs = findViewById(R.id.quizScore);
            qs.setVisibility(View.INVISIBLE);
            View menu = findViewById(R.id.fragmenu);
            menu.setVisibility(View.VISIBLE);
            score = 0;
        }
    }
    @Override
    public void QuizButtons(String button){
        TextView quiztxt = (TextView)findViewById(R.id.quizQuestion);
        RadioButton answer = (RadioButton)findViewById(R.id.answer1);
        RadioButton wrongChoice1 = (RadioButton)findViewById(R.id.answer2);
        RadioButton wrongChoice2 = (RadioButton)findViewById(R.id.answer3);
        RadioButton wrongChoice3 = (RadioButton)findViewById(R.id.answer4);
        RadioGroup group = (RadioGroup)findViewById(R.id.radioGroup);


        if(answer.isChecked())
        {
            score++;
        }
       if(button.equals("nextQuestion")){
            group.clearCheck();
            quizCount++;
            answer.setText(quizAnswer[quizCount]);
            quiztxt.setText(quizList[quizCount]);
            wrongCount++;
            wrongChoice1.setText(wrongChoicesList[wrongCount]);
            wrongCount++;
            wrongChoice2.setText(wrongChoicesList[wrongCount]);
            wrongCount++;
            wrongChoice3.setText(wrongChoicesList[wrongCount]);

            if(quizCount==9){
                View qs = findViewById(R.id.quizScore);
                qs.setVisibility(View.VISIBLE);
                View frag4 = findViewById(R.id.quizF);
                frag4.setVisibility(View.INVISIBLE);
                TextView fScore = (TextView)findViewById(R.id.finalScore);
                if(score>0){
                    score++;
                }

                fScore.setText(score+"/10");
                quizCount =0;
                wrongCount = 2;
                Button next = (Button)findViewById(R.id.nextQuestion);
                next.setText("Next");
            }
            if(quizCount==8)
            {
                Button next = (Button)findViewById(R.id.nextQuestion);
                next.setText("Done");

            }

        }

    }
    @Override
    public void HomeInterfaceButtons(String button) {
        
        if (button.equals("notecard")) {
            TextView tv = (TextView) findViewById(R.id.question);
            tv.setVisibility(View.VISIBLE);
            View frag = findViewById(R.id.fragmenu);
            frag.setVisibility(View.INVISIBLE);
            View frag2 = findViewById(R.id.fragnotecard);
            frag2.setVisibility(View.VISIBLE);
            View is = findViewById(R.id.inputSearch);
            is.setVisibility(View.VISIBLE);
            View nv = findViewById(R.id.list_view);
            nv.setVisibility(View.VISIBLE);

            Button ans = (Button)findViewById(R.id.answerBtn);
            if (ans.getVisibility() != View.VISIBLE) {
                ans.setVisibility(View.VISIBLE);
            }

            FloatingActionButton fav = (FloatingActionButton)findViewById(R.id.fab);
            for(int x = 0; x<bookmarkCount;x++){
                if(noteCardsID[place].equals(bookmarkList[x])){
                    fav.setImageResource(R.drawable.ic_favorite_blue_24dp);
                    break;
                }
            }
            currentQID = noteCardsID[place];
            tv.setText(noteCardsList[place]);
        }else if (button.equals("tips")){
            View menu = findViewById(R.id.fragmenu);
            menu.setVisibility(View.INVISIBLE);
            View tips = findViewById(R.id.fragtips);
            tips.setVisibility(View.VISIBLE);
            View showAButton = findViewById(R.id.backToTipsButton);
            showAButton.setVisibility(View.INVISIBLE);
           detailsText = (TextView)findViewById(R.id.textDetails);
            detailsText.setVisibility(View.INVISIBLE);
            readTips();
            readDetails();
        }else if (button.equals("quiz")){
            View menu = findViewById(R.id.fragmenu);
            menu.setVisibility(View.INVISIBLE);
            View showAButton = findViewById(R.id.backToTipsButton);
            showAButton.setVisibility(View.INVISIBLE);
            detailsText = (TextView)findViewById(R.id.textDetails);
            detailsText.setVisibility(View.INVISIBLE);
            View quiz = findViewById(R.id.quizF);
            quiz.setVisibility(View.VISIBLE);
            TextView quiztxt = (TextView)findViewById(R.id.quizQuestion);
            RadioButton answer = (RadioButton)findViewById(R.id.answer1);
            RadioButton wrongChoice1 = (RadioButton)findViewById(R.id.answer2);
            RadioButton wrongChoice2 = (RadioButton)findViewById(R.id.answer3);
            RadioButton wrongChoice3 = (RadioButton)findViewById(R.id.answer4);
            answer.setText(quizAnswer[0]);
            quiztxt.setText(quizList[0]);
            wrongChoice1.setText(wrongChoicesList[0]);
            wrongChoice2.setText(wrongChoicesList[1]);
            wrongChoice3.setText(wrongChoicesList[2]);
            Call<List<User>> call = service.getUser();
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        userPoints = response.body().get(userNum).points;

                        TextView points = (TextView)findViewById(R.id.points);
                        points.setText(""+userPoints);
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {

                }
            });
        }
    }
    @Override
    public void TipsButtons(String button) {
        {
            if(button.equals("back"))
            {
                View frag = findViewById(R.id.fragmenu);
                frag.setVisibility(View.VISIBLE);
                View frag4 = findViewById(R.id.fragtips);
                frag4.setVisibility(View.INVISIBLE);
            }else if(button.equals("next")) {
                if (index == 9) {
                    index = 0;
                    index--;
                }
                index++;
                finalStringDetails.delete(0,finalStringDetails.length());
                finalStringDetails.append(detailsTxtLines.get(index));
                detailsText.setText(finalStringDetails);

                finalStringTips.delete(0, finalStringTips.length());
                finalStringTips.append(tipsTxtLines.get(index));
                tipsText.setText(finalStringTips);
            }else if (button.equals("details")){
               detailsText = (TextView)findViewById(R.id.textDetails);
                detailsText.setVisibility(View.VISIBLE);
                tipsText = (TextView)findViewById(R.id.tiptxt);
                tipsText.setVisibility(View.INVISIBLE);
                View backto = findViewById(R.id.backToTipsButton);
                backto.setVisibility(View.VISIBLE);
                View back= findViewById(R.id.bckTips);
                back.setVisibility(View.INVISIBLE);
                View next = findViewById(R.id.nxtTip);
                next.setVisibility(View.INVISIBLE);
                View details = findViewById(R.id.details);
                details.setVisibility(View.INVISIBLE);
            }else if(button.equals("backToTips")){
                detailsText = (TextView)findViewById(R.id.textDetails);
                detailsText.setVisibility(View.INVISIBLE);
                tipsText = (TextView)findViewById(R.id.tiptxt);
                tipsText.setVisibility(View.VISIBLE);
                View backto = findViewById(R.id.backToTipsButton);
                backto.setVisibility(View.INVISIBLE);
                View back= findViewById(R.id.bckTips);
                back.setVisibility(View.VISIBLE);
                View next = findViewById(R.id.nxtTip);
                next.setVisibility(View.VISIBLE);
                View details = findViewById(R.id.details);
                details.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        View menu = findViewById(R.id.fragmenu);
        View cards = findViewById(R.id.fragnotecard);
        View tips = findViewById(R.id.fragtips);
        View quiz = findViewById(R.id.quizF);
        View quizS = findViewById(R.id.quizScore);
        View searchP= findViewById(R.id.searchPage);
        TextView textl = (TextView)findViewById(R.id.question);
        TextView text2 = (TextView)findViewById(R.id.wordPoints);
        TextView text3 = (TextView)findViewById(R.id.points);
        TextView text4 = (TextView)findViewById(R.id.quizQuestion);
        TextView text5 = (TextView)findViewById(R.id.quizS);
        TextView text6 = (TextView)findViewById(R.id.finalScore);
        TextView texttips = (TextView)findViewById(R.id.tiptxt);
        ImageButton like = (ImageButton)findViewById(R.id.likebtn);
        ImageButton dislike = (ImageButton)findViewById(R.id.dislikebtn);
        ImageButton search = (ImageButton)findViewById(R.id.searchbtn);
        FloatingActionButton fav = (FloatingActionButton)findViewById(R.id.fab);
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.blackT){
            searchP.setBackgroundColor(Color.parseColor("#222930"));
            quiz.setBackgroundColor(Color.parseColor("#222930"));
            quizS.setBackgroundColor(Color.parseColor("#222930"));
            menu.setBackgroundColor(Color.parseColor("#222930"));
            cards.setBackgroundColor(Color.parseColor("#222930"));
            tips.setBackgroundColor(Color.parseColor("#222930"));
            textl.setTextColor(Color.parseColor("#4eb1ba"));
            text2.setTextColor(Color.parseColor("#4eb1ba"));
            text3.setTextColor(Color.parseColor("#4eb1ba"));
            text4.setTextColor(Color.parseColor("#4eb1ba"));
            text5.setTextColor(Color.parseColor("#4eb1ba"));
            text6.setTextColor(Color.parseColor("#4eb1ba"));
            like.setBackgroundColor(Color.parseColor("#222930"));
            dislike.setBackgroundColor(Color.parseColor("#222930"));
            fav.setBackgroundColor(Color.parseColor("#222930"));
            search.setBackgroundColor(Color.parseColor("#222930"));
            texttips.setTextColor(Color.parseColor("#4eb1ba"));
        }else if(id == R.id.blueT){
            searchP.setBackgroundColor(Color.parseColor("#0066ff"));
            quiz.setBackgroundColor(Color.parseColor("#0066ff"));
            quizS.setBackgroundColor(Color.parseColor("#0066ff"));
            menu.setBackgroundColor(Color.parseColor("#0066ff"));
            cards.setBackgroundColor(Color.parseColor("#0066ff"));
            tips.setBackgroundColor(Color.parseColor("#0066ff"));
            textl.setTextColor(Color.parseColor("#ffffff"));
            texttips.setTextColor(Color.parseColor("#ffffff"));
            text2.setTextColor(Color.parseColor("#ffffff"));
            text3.setTextColor(Color.parseColor("#ffffff"));
            text4.setTextColor(Color.parseColor("#ffffff"));
            text5.setTextColor(Color.parseColor("#ffffff"));
            text6.setTextColor(Color.parseColor("#ffffff"));
            like.setBackgroundColor(Color.parseColor("#0066ff"));
            dislike.setBackgroundColor(Color.parseColor("#0066ff"));
            fav.setBackgroundColor(Color.parseColor("#0066ff"));
            search.setBackgroundColor(Color.parseColor("#0066ff"));
            texttips.setTextColor(Color.parseColor("#ffffff"));
        }else if(id == R.id.khakiT){
            searchP.setBackgroundColor(Color.parseColor("#e4dbbf"));
            quiz.setBackgroundColor(Color.parseColor("#e4dbbf"));
            quizS.setBackgroundColor(Color.parseColor("#e4dbbf"));
            menu.setBackgroundColor(Color.parseColor("#e4dbbf"));
            cards.setBackgroundColor(Color.parseColor("#e4dbbf"));
            tips.setBackgroundColor(Color.parseColor("#e4dbbf"));
            textl.setTextColor(Color.parseColor("#dc5b21"));
            texttips.setTextColor(Color.parseColor("#dc5b21"));
            text2.setTextColor(Color.parseColor("#dc5b21"));
            text3.setTextColor(Color.parseColor("#dc5b21"));
            text4.setTextColor(Color.parseColor("#dc5b21"));
            text5.setTextColor(Color.parseColor("#dc5b21"));
            text6.setTextColor(Color.parseColor("#dc5b21"));
            like.setBackgroundColor(Color.parseColor("#e4dbbf"));
            dislike.setBackgroundColor(Color.parseColor("#e4dbbf"));
            fav.setBackgroundColor(Color.parseColor("#e4dbbf"));
            search.setBackgroundColor(Color.parseColor("#e4dbbf"));
            texttips.setTextColor(Color.parseColor("#dc5b21"));
        }else if (id == R.id.logout) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setVisibility(View.INVISIBLE);

            View firstScreen = findViewById(R.id.fragfscreen);
            firstScreen.setVisibility(View.VISIBLE);

            View noteCard = findViewById(R.id.fragnotecard);
            noteCard.setVisibility(View.INVISIBLE);


            menu.setVisibility(View.INVISIBLE);


            tips.setVisibility(View.INVISIBLE);

            View loginScreen = findViewById(R.id.fraglogin);
            loginScreen.setVisibility(View.INVISIBLE);

            View signUp = findViewById(R.id.fragsign);
            signUp.setVisibility(View.INVISIBLE);

        }else if (id == R.id.feedback){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Get the layout inflater
            LayoutInflater inflater = this.getLayoutInflater();
           // newFeedback feedback = newFeedback
            final EditText input = new EditText(MainActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setTitle("FeedBack")
                    .setView(input)
                    // Add action buttons
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            String inputfb = input.getText().toString();
                            newFeedback feedback = new newFeedback(inputfb,currentID,false,false);
                            Call<newFeedback> call = service.createFeedback(feedback);
                            call.enqueue(new Callback<newFeedback>() {
                                @Override
                                public void onResponse(Call<newFeedback> call, Response<newFeedback> response) {

                                    Context context = getApplicationContext();
                                    CharSequence text = "FeedBack Sent";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();


                                }

                                @Override
                                public void onFailure(Call<newFeedback> call, Throwable t) {
                                    Context context = getApplicationContext();
                                    CharSequence text = "FeedBack Not Sent";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                            });

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.card50) {
            vib.vibrate(50);
            numcards = 50;
        } else if (id == R.id.card100) {
            vib.vibrate(50);
            numcards = 100;
        } else if (id == R.id.card150) {
            vib.vibrate(50);
            numcards = 150;
        } else if (id == R.id.card200) {
            vib.vibrate(50);
            numcards = 198;
        } else if (id == R.id.tutorialP) {
            vib.vibrate(50);
            Uri uri = Uri.parse("http://www.tutorialspoint.com/java/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }else if (id == R.id.bucky) {
            vib.vibrate(50);
            Uri uri = Uri.parse("https://thenewboston.com/videos.php?cat=31");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkButtons(){
        FloatingActionButton fav = (FloatingActionButton)findViewById(R.id.fab);
        for(int x = 0; x<bookmarkCount;x++){
            if(noteCardsID[place].equals(bookmarkList[x])){
                fav.setImageResource(R.drawable.ic_favorite_blue_24dp);
                break;
            }else{
                fav.setImageResource(R.drawable.ic_favorite_border_blue_24dp);
            }
        }
        ImageButton like = (ImageButton)findViewById(R.id.likebtn);
        for(int x = 0; x<likedcount;x++){
            if(noteCardsID[place].equals(likelist[x])){
                like.setImageResource(R.drawable.thumbsupgreen);
                break;
            }else{
                like.setImageResource(R.drawable.thumbsup);
            }
        }

        ImageButton dislike = (ImageButton)findViewById(R.id.dislikebtn);
        for(int x = 0; x<dislikescount;x++){
            if(noteCardsID[place].equals(dislikelist[x])){
                dislike.setImageResource(R.drawable.thumbsdownred);
                break;
            }else{
                dislike.setImageResource(R.drawable.thumbsdown);
            }
        }
    }
    public void readDetails(){
        InputStream iStream = getResources().openRawResource(R.raw.details);
        BufferedReader bReader = new BufferedReader (new InputStreamReader(iStream));
        detailsText = (TextView)findViewById(R.id.textDetails);
        detailsTxtLines= new ArrayList<String>();
        finalStringDetails = new StringBuilder();
        String line = null;


        try {
            while((line = bReader.readLine()) != null) {
                detailsTxtLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences settings = getSharedPreferences(PREFS_TAG, 0);
        index = settings.getInt("index",index);
        finalStringDetails.append(detailsTxtLines.get(index));
        detailsText.setText(finalStringDetails);
    }

    public void readTips(){
        InputStream iStream = getResources().openRawResource(R.raw.tips);
        BufferedReader bReader = new BufferedReader (new InputStreamReader(iStream));
        tipsText = (TextView)findViewById(R.id.tiptxt);
        tipsTxtLines = new ArrayList<String>();
        finalStringTips = new StringBuilder();
        String line = null;
        try {
            while ((line = bReader.readLine()) != null) {
                tipsTxtLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences settings = getSharedPreferences(PREFS_TAG, 0);
        index = settings.getInt("index",index);
        finalStringTips.append(tipsTxtLines.get(index));
        tipsText.setText(finalStringTips);
    }

    public void hideAllScreens(){
        View qs = findViewById(R.id.quizScore);
        qs.setVisibility(View.INVISIBLE);

        View noteCard = findViewById(R.id.fragnotecard);
        noteCard.setVisibility(View.INVISIBLE);

        View menu = findViewById(R.id.fragmenu);
       menu.setVisibility(View.INVISIBLE);

        View tips = findViewById(R.id.fragtips);
        tips.setVisibility(View.INVISIBLE);

        View loginScreen = findViewById(R.id.fraglogin);
        loginScreen.setVisibility(View.INVISIBLE);

        View signUp = findViewById(R.id.fragsign);
        signUp.setVisibility(View.INVISIBLE);

        View quiz = findViewById(R.id.quizF);
        quiz.setVisibility(View.INVISIBLE);

        View search = findViewById(R.id.searchPage);
        search.setVisibility(View.INVISIBLE);

    }


}
