package com.example.tonio.actionbar;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NoteCardInterface.NoteCardListener,
        HomeInterface.HomeListener,StartScreen.StartListener,TipsScreen.TipsListener,LoginScreen.LogListener,
        SignUpScreen.SignUpListener,QuizScreen.QuizListener {
    private int rand;
    private int userNum;
    private String [] currentIDList = new String[20];
    private String currentID;
    private Vibrator vib;
    private int numcards;
    private TextView tipsText;
    private TextView detailsText;
    private static ArrayList<String> detailsTxtLines;
    private int index;
    private int numOfUsers;
    private StringBuilder finalStringTips;
    private StringBuilder finalStringDetails;
    private static ArrayList<String> tipsTxtLines;
    private static final String PREFS_TAG = "IndexPlace";
    private ArrayAdapter<Object> listAdapter;
    private String[]emailList = new String[20];
    private String[]passwordList = new String[20];
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://jipa-server.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

   private  final  GitHubService service = retrofit.create(GitHubService.class);



    public class likes{
        private int likes;


        public likes(int likes){
            this.likes = likes;

        }

    }
    public class dislikes{
        private int dislikes;


        public dislikes(int dislikes){
            this.dislikes = dislikes;

        }

    }
    public  class newUser{

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
        public String question;
        public String _id;
        public String answer;
        public String keywords;
        public boolean quiz;
        public Question(String question,boolean quiz,String _id,String answer,String keywords){
            this.question = question;
            this.quiz = quiz;
            this._id = _id;
            this.answer = answer;
            this.keywords = keywords;
        }
    }
    private class User {
        public String _id;
        public String email;
        public String password;
        public int points;

        public User(String _id, String email,String password, int points){
            this._id = _id;
            this.email = email;
            this.password = password;
            this.points = points;
        }
    }

    public interface GitHubService {
        @POST("/api/feedback")
        Call<newFeedback> createFeedback(@Body newFeedback feedback);

        @GET("api/user")
        Call<List<User>> getUser();

        @GET("/api/question")
        Call<List<Question>> getQuestion();


        @POST("/api/user")
       Call<newUser> createUser(@Body newUser user);

        @GET("/api/question/{likes}")
        Call<likes>addlike();

        @GET("/api/question/{disklikes}")
        Call<dislikes>adddislike();

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

        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        numcards= 100;
        rand = ((int)(Math.random() * numcards)) + 1;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
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
                    currentID = currentIDList[x];
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
             View searchbar = findViewById(R.id.action_search);
            searchbar.setVisibility(View.INVISIBLE);
        }else if(button.equals("signBtn")){
            View fragfs = findViewById(R.id.fragfscreen);
            fragfs.setVisibility(View.INVISIBLE);
            View signup = findViewById(R.id.fragsign);
            signup.setVisibility(View.VISIBLE);
            View searchbar = findViewById(R.id.action_search);
            searchbar.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void SignUpButtons(String button,String firstName,String lastName,String email,String password){
            if(button.equals("allBtn")){
                View loginScreen = findViewById(R.id.fraglogin);
                loginScreen.setVisibility(View.VISIBLE);

                View signUp = findViewById(R.id.fragsign);
                signUp.setVisibility(View.INVISIBLE);

            }else if (button.equals("creatBtn")){
                newUser user = new newUser( firstName,lastName,email,password,0);
                numOfUsers++;
                Call<newUser> call = service.createUser(user);
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

            ListView lv = (ListView) findViewById(R.id.question);
            lv.setVisibility(View.INVISIBLE);
            ListView a = (ListView) findViewById(R.id.answer);
            a.setVisibility(View.VISIBLE);
            Button ans = (Button)findViewById(R.id.answerBtn);
            ans.setVisibility(View.INVISIBLE);

            listAdapter = new ArrayAdapter<>(this, R.layout.custom_textview);
           Call<List<Question>> call = service.getQuestion();
            call.enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {

                    ListView a = (ListView) findViewById(R.id.answer);
                    a.setAdapter(listAdapter);
                    listAdapter.addAll(response.body().get(rand).answer);
                }


                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {

                }
            });
        } else if (button.equals(("backBtn"))) {
            View frag = findViewById(R.id.fragmenu);
            frag.setVisibility(View.VISIBLE);
            View frag2 = findViewById(R.id.fragnotecard);
            frag2.setVisibility(View.INVISIBLE);
        } else if (button.equals(("nextBtn"))) {
            ListView lv = (ListView) findViewById(R.id.question);
            lv.setVisibility(View.VISIBLE);
            ListView a = (ListView) findViewById(R.id.answer);
            a.setVisibility(View.INVISIBLE);
            Button ans = (Button)findViewById(R.id.answerBtn);
            ans.setVisibility(View.VISIBLE);

            rand = ((int)(Math.random() * numcards)) + 1;
            listAdapter = new ArrayAdapter<>(this, R.layout.custom_textview);
           Call<List<Question>> call = service.getQuestion();

            call.enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {

                    ListView lv = (ListView) findViewById(R.id.question);
                    lv.setAdapter(listAdapter);

                   listAdapter.addAll(response.body().get(rand).question);
                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {

                }
            });
        }else if (button.equals("like")){
            vib.vibrate(50);

            Call<likes> call = service.addlike();

            call.enqueue(new Callback<likes>() {
                @Override
                public void onResponse(Call<likes> call, Response<likes> response) {


                }

                @Override
                public void onFailure(Call<likes> call, Throwable t) {

                }
            });
        }else if (button.equals("dislike")){
            vib.vibrate(50);

            Call<dislikes> call = service.adddislike();

            call.enqueue(new Callback<dislikes>() {
                @Override
                public void onResponse(Call<dislikes> call, Response<dislikes> response) {


                }

                @Override
                public void onFailure(Call<dislikes> call, Throwable t) {

                }
            });
        }else if (button.equals("fav")){
            vib.vibrate(50);


        }

    }
    @Override
    public void QuizButtons(String button){

    }
    @Override
    public void HomeInterfaceButtons(String button) {
        if (button.equals("notecard")) {
            ListView lv = (ListView) findViewById(R.id.question);
            lv.setVisibility(View.VISIBLE);
            ListView a = (ListView) findViewById(R.id.answer);
            a.setVisibility(View.INVISIBLE);
            View frag = findViewById(R.id.fragmenu);
            frag.setVisibility(View.INVISIBLE);
            View frag2 = findViewById(R.id.fragnotecard);
            frag2.setVisibility(View.VISIBLE);
            View searchbar = findViewById(R.id.action_search);
            searchbar.setVisibility(View.VISIBLE);
            Button ans = (Button)findViewById(R.id.answerBtn);
            if (ans.getVisibility() != View.VISIBLE) {
                ans.setVisibility(View.VISIBLE);
            }

        listAdapter = new ArrayAdapter<>(this,R.layout.custom_textview);
         Call<List<Question>> call = service.getQuestion();

            call.enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {

                    ListView lv = (ListView) findViewById(R.id.question);
                    lv.setAdapter(listAdapter);
                    listAdapter.addAll(response.body().get(0));

                    Context context = getApplicationContext();
                    CharSequence text = response.body().get(rand).question;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    Context context = getApplicationContext();
                    CharSequence text = "Fail";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
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
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        View menu = findViewById(R.id.fragmenu);
        View cards = findViewById(R.id.fragnotecard);
        View tips = findViewById(R.id.fragtips);
       // TextView textl = (TextView)findViewById(R.id.tv);
        TextView texttips = (TextView)findViewById(R.id.tiptxt);

        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.blackT){

            menu.setBackgroundColor(Color.parseColor("#222930"));
            cards.setBackgroundColor(Color.parseColor("#222930"));
            tips.setBackgroundColor(Color.parseColor("#222930"));
          //  textl.setTextColor(Color.parseColor("#4eb1ba"));
            texttips.setTextColor(Color.parseColor("#4eb1ba"));
        }else if(id == R.id.blueT){
            menu.setBackgroundColor(Color.parseColor("#0066ff"));
            cards.setBackgroundColor(Color.parseColor("#0066ff"));
            tips.setBackgroundColor(Color.parseColor("#0066ff"));
//            textl.setTextColor(Color.parseColor("#ffffff"));
            texttips.setTextColor(Color.parseColor("#ffffff"));
        }else if(id == R.id.khakiT){
            menu.setBackgroundColor(Color.parseColor("#e4dbbf"));
            cards.setBackgroundColor(Color.parseColor("#e4dbbf"));
            tips.setBackgroundColor(Color.parseColor("#e4dbbf"));
           // textl.setTextColor(Color.parseColor("#dc5b21"));
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
            numcards = 199;
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
        }else if (id==R.id.nav_favorites){
           vib.vibrate(50);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void readDetails(){
        InputStream iStream = getResources().openRawResource(R.raw.details);
        BufferedReader bReader = new BufferedReader (new InputStreamReader(iStream));
        detailsText = (TextView)findViewById(R.id.textDetails);
        detailsTxtLines= new ArrayList<String>();
        finalStringDetails = new StringBuilder();
        String line = null;
        StringTokenizer st;
        String  token, delimiter = "1.2.3.4.5.6.7.8.9.10.";
        try {
            while((line = bReader.readLine()) != null) {
                st = new StringTokenizer(line,delimiter);
                while (st.hasMoreTokens())
                {
                    token = st.nextToken();

                }
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
            while((line = bReader.readLine()) != null) {
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
}
