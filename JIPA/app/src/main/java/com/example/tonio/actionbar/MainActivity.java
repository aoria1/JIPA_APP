package com.example.tonio.actionbar;

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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NoteCardFrag.NoteCardListener,
        MenuFrag.MenuListener,FirstScreen.StartListener,TipsFrag.TipsListener {
    int rand;
    private Vibrator vib;
    private int numcards;
    private TextView text;
    private int index;
    private StringBuilder finalString;
    private static ArrayList<String> lines;
    private static final String PREFS_TAG = "IndexPlace";
    private ArrayAdapter<Object> listAdapter;
    final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://interview-app-server.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final  GitHubService service = retrofit.create(GitHubService.class);
    private class Descript{
        public String question;


        @Override
        public String toString(){
            String output = question;

            return output;
        }

    }

    private class Answer {
        public String answer;

        @Override
        public String toString(){
            String output = answer;

            return output;
        }

    }
    public interface GitHubService {
        @GET("/api/question")
        Call<List<Descript>> getQuestion();

        @GET("/api/question")
        Call<List<Answer>> getAnswer();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        View frag2 = findViewById(R.id.fragment2);
        frag2.setVisibility(View.INVISIBLE);
        View frag4 = findViewById(R.id.fragment4);
        frag4.setVisibility(View.INVISIBLE);
        numcards= 100;
        View frag3 = findViewById(R.id.fragment3);
        frag3.setVisibility(View.INVISIBLE);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void ButtonsS(String button){
        if (button.equals("logBtn")) {
            View frag3 = findViewById(R.id.fragment);
            frag3.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void ButtonsNC(String button) {
        if (button.equals("answerBtn")) {
            ListView lv = (ListView) findViewById(R.id.question);
            lv.setVisibility(View.INVISIBLE);
            ListView a = (ListView) findViewById(R.id.answer);
            a.setVisibility(View.VISIBLE);
            Button ans = (Button)findViewById(R.id.answerBtn);
            ans.setVisibility(View.INVISIBLE);


            listAdapter = new ArrayAdapter<>(this, R.layout.custom_textview);
            Call<List<Answer>> call = service.getAnswer();
            call.enqueue(new Callback<List<Answer>>() {
                @Override
                public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {

                    ListView a= (ListView) findViewById(R.id.answer);
                    a.setAdapter(listAdapter);
                    listAdapter.addAll(response.body().get(rand));
                }


                @Override
                public void onFailure(Call<List<Answer>> call, Throwable t) {

                }


            });



        } else if (button.equals(("backBtn"))) {
            View frag = findViewById(R.id.fragment);
            frag.setVisibility(View.VISIBLE);
            View frag2 = findViewById(R.id.fragment2);
            frag2.setVisibility(View.INVISIBLE);
        }
        else if (button.equals(("nextBtn"))) {
            ListView lv = (ListView) findViewById(R.id.question);
            lv.setVisibility(View.VISIBLE);
            ListView a = (ListView) findViewById(R.id.answer);
            a.setVisibility(View.INVISIBLE);
            Button ans = (Button)findViewById(R.id.answerBtn);
            ans.setVisibility(View.VISIBLE);

            rand = ((int)(Math.random() * numcards)) + 1;
            listAdapter = new ArrayAdapter<>(this, R.layout.custom_textview);
            Call<List<Descript>> call = service.getQuestion();

            call.enqueue(new Callback<List<Descript>>() {
                @Override
                public void onResponse(Call<List<Descript>> call, Response<List<Descript>> response) {

                    ListView lv = (ListView) findViewById(R.id.question);
                    lv.setAdapter(listAdapter);

                   listAdapter.addAll(response.body().get(rand));
                }

                @Override
                public void onFailure(Call<List<Descript>> call, Throwable t) {

                }
            });
        }

    }

    @Override
    public void ButtonsM(String button) {
        if (button.equals("notecard")) {
            ListView lv = (ListView) findViewById(R.id.question);
            lv.setVisibility(View.VISIBLE);
            ListView a = (ListView) findViewById(R.id.answer);
            a.setVisibility(View.INVISIBLE);
            View frag = findViewById(R.id.fragment);
            frag.setVisibility(View.INVISIBLE);
            View frag2 = findViewById(R.id.fragment2);
            frag2.setVisibility(View.VISIBLE);
            Button ans = (Button)findViewById(R.id.answerBtn);
            if (ans.getVisibility() != View.VISIBLE) {
                ans.setVisibility(View.VISIBLE);
            }

            rand = ((int)(Math.random() * numcards)) + 1;
            final ArrayAdapter<Object> listAdapter = new ArrayAdapter<>(this,R.layout.custom_textview);
            Call<List<Descript>> call = service.getQuestion();
            call.enqueue(new Callback<List<Descript>>() {
                @Override
                public void onResponse(Call<List<Descript>> call, Response<List<Descript>> response) {

                    ListView lv = (ListView) findViewById(R.id.question);
                    lv.setAdapter(listAdapter);
                    listAdapter.addAll(response.body().get(rand));
                }

                @Override
                public void onFailure(Call<List<Descript>> call, Throwable t) {

                }
            });
        }else if (button.equals("tips")){
            View frag = findViewById(R.id.fragment);
            frag.setVisibility(View.INVISIBLE);
            View frag2 = findViewById(R.id.fragment4);
            frag2.setVisibility(View.VISIBLE);

            InputStream iStream = getResources().openRawResource(R.raw.tips);
            BufferedReader bReader = new BufferedReader (new InputStreamReader(iStream));
            text = (TextView)findViewById(R.id.tiptxt);
            lines = new ArrayList<String>();
            finalString = new StringBuilder();
            String line = null;
            try {
                while((line = bReader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            SharedPreferences settings = getSharedPreferences(PREFS_TAG, 0);
            index = settings.getInt("index",index);
            finalString.append(lines.get(index));
            text.setText(finalString);
        }
    }
    @Override
    public void ButtonsT(String button) {
        {
            if(button.equals("back"))
            {
                View frag = findViewById(R.id.fragment);
                frag.setVisibility(View.VISIBLE);
                View frag4 = findViewById(R.id.fragment4);
                frag4.setVisibility(View.INVISIBLE);
            }else if(button.equals("next")) {
                if (index == 9) {
                    index = 0;
                    index--;
                }
                index++;

                finalString.delete(0, finalString.length());
                finalString.append(lines.get(index));
                text.setText(finalString);
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
        View menu = findViewById(R.id.fragment);
        View cards = findViewById(R.id.fragment2);
        View tips = findViewById(R.id.fragment4);
        TextView textl = (TextView)findViewById(R.id.tv);
        TextView texttips = (TextView)findViewById(R.id.tiptxt);
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.greenbg){

            menu.setBackgroundColor(Color.GREEN);
            cards.setBackgroundColor(Color.GREEN);
            tips.setBackgroundColor(Color.GREEN);
        }else if(id == R.id.bluebg){
            menu.setBackgroundColor(Color.parseColor("#0066ff"));
            cards.setBackgroundColor(Color.parseColor("#0066ff"));
            tips.setBackgroundColor(Color.parseColor("#0066ff"));
        }else if(id == R.id.orbg){
            menu.setBackgroundColor(Color.parseColor("#ff751a"));
            cards.setBackgroundColor(Color.parseColor("#ff751a"));
            tips.setBackgroundColor(Color.parseColor("#ff751a"));
        }
        if(id == R.id.bluet){

            textl.setTextColor(Color.parseColor("#000066"));
            texttips.setTextColor(Color.parseColor("#000066"));
        }else if(id==R.id.redt){
            textl.setTextColor(Color.parseColor("#ff0000"));
            texttips.setTextColor(Color.parseColor("#ff0000"));
        }else if(id==R.id.greent){
            textl.setTextColor(Color.parseColor("#00cc00"));
            texttips.setTextColor(Color.parseColor("#00cc00"));
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
