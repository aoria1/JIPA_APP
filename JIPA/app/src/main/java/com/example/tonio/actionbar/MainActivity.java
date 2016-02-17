package com.example.tonio.actionbar;

import android.content.SharedPreferences;
import android.os.Bundle;
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
    private TextView text;
    private int index = 0;
    private StringBuilder finalString;
    private static ArrayList<String> lines;
    private static final String PREFS_TAG = "IndexPlace";
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

        View frag2 = findViewById(R.id.fragment2);
        frag2.setVisibility(View.INVISIBLE);
        View frag4 = findViewById(R.id.fragment4);
        frag4.setVisibility(View.INVISIBLE);



//            View frag1 = findViewById(R.id.fragment);
//            frag1.setVisibility(View.INVISIBLE);
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

//            View frag4 = findViewById(R.id.fragment4);
//            frag4.setVisibility(View.VISIBLE);

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


            final ArrayAdapter<Object> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
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
            rand = ((int)(Math.random() * 100)) + 1;
            final ArrayAdapter<Object> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
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
            rand = ((int)(Math.random() * 2)) + 1;

            final ArrayAdapter<Object> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.card50) {

        } else if (id == R.id.card100) {

        } else if (id == R.id.card150) {

        } else if (id == R.id.card200) {

        } else if (id == R.id.links) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
