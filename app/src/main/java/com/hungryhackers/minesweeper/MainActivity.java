package com.hungryhackers.minesweeper;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private int n = 8;
    private int level = 10;
    private MyButton buttons[][];
    private LinearLayout rows[];
    boolean gameOver;
    private LinearLayout boardLayout;
    final static public int NO_MINE = 0;
    final static public int MINE = 1;
    final static public int NO_NEIGHBOUR = 0;
//    private int firstChance = 1;
    private int score = 0;
    Random r = new Random();
    int random_x, random_y;
    ArrayList<Integer> iteration_X = new ArrayList<>(Arrays.asList(-1,-1,-1,0,1,1,1,0));
    ArrayList<Integer> iteration_Y = new ArrayList<>(Arrays.asList(-1,0,1,1,1,0,-1,-1));

    HashSet<Integer> coordinates_visited = new HashSet<>();

    ImageView reset;
    Character mine = 'Ø';
    Character flag = '¶';
    MenuItem menu_smiley_button;
    Menu menu;
    int fontSize = 45;

    TextView textViewScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewScore = (TextView) findViewById(R.id.score);

        boardLayout = (LinearLayout) findViewById(R.id.layout_board);

        reset = (ImageView) findViewById(R.id.replaySmiley);
        reset.setOnClickListener(this);

        setUpBoard();

        for (int i = 0 ; i<n ; i++) {
            for (int j = 0; j < n; j++) {
                if(buttons[i][j].getMine()==NO_MINE)
                    buttons[i][j].setText(Integer.toString(buttons[i][j].neighbour));
                else
                    buttons[i][j].setText(mine.toString());
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu,menu);
        menu_smiley_button = menu.findItem(R.id.menu_smiley_button);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        menu_smiley_button.setIcon(R.drawable.smiley);
        score = 0;

        if (id == R.id.menu_smiley_button){
            resetBoard();
        }
        if (id == R.id.easy){
            n = 8;
            level = 10;
            fontSize = 45;
            setUpBoard();
        }
        else if (id == R.id.med){
            n = 10;
            level = 20;
            fontSize = 25;
            setUpBoard();
        }
        else if (id == R.id.hard){
            n = 17;
            level = 100;
            fontSize = 18;
            setUpBoard();
        }

        return true;
    }

    public void resetBoard(){
        gameOver = false;
        for (int i = 0 ; i<n ; i++){
            for (int j = 0 ; j<n ; j++){
                buttons[i][j].setText("");
                buttons[i][j].setMine(NO_MINE);
                buttons[i][j].setTypeface(null, Typeface.NORMAL);
                buttons[i][j].neighbour = NO_NEIGHBOUR;
                buttons[i][j].flagged = false;
                buttons[i][j].clicked = false;
                buttons[i][j].setBackgroundResource(R.drawable.unclicked_tile);
            }
        }
        menu_smiley_button.setIcon(R.drawable.smiley);
        setMinesAndNeighbours();
    }

    private void setUpBoard() {
        boardLayout.removeAllViews();
        buttons = new MyButton[n][n];
        rows = new LinearLayout[n];
        gameOver = false;

        for (int i=0 ; i<n ; i++){
            rows[i] = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            rows[i].setLayoutParams(params);
            rows[i].setOrientation(LinearLayout.HORIZONTAL);
            boardLayout.addView(rows[i]);
        }
        for (int i = 0 ; i<n ; i++){
            for (int j = 0 ; j<n ; j++){
                buttons[i][j] = new MyButton(this);
                buttons[i][j].setMine(NO_MINE);
                buttons[i][j].setTypeface(null, Typeface.NORMAL);
                buttons[i][j].flagged = false;
                buttons[i][j].clicked = false;
                buttons[i][j].neighbour = NO_NEIGHBOUR;
                buttons[i][j].x = i;
                buttons[i][j].y = j;
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setOnLongClickListener(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                buttons[i][j].setLayoutParams(params);
                rows[i].addView(buttons[i][j]);
                buttons[i][j].setGravity(Gravity.CENTER);
                buttons[i][j].setTextSize(fontSize);
                buttons[i][j].setBackgroundResource(R.drawable.unclicked_tile);
            }
        }
        setMinesAndNeighbours();
    }

    private void setMinesAndNeighbours() {
        for (int i = 0 ; i<level ; i++) {
            random_x = r.nextInt(n);
            random_y = r.nextInt(n);
            buttons[random_x][random_y].setMine(MINE);
            buttons[random_x][random_y].setTypeface(null, Typeface.BOLD_ITALIC);
        }
        for (int i = 0 ; i<n ; i++) {
            for (int j = 0; j < n; j++) {
                if (buttons[i][j].getMine() == MINE){
                    for (int k = 0 ; k<iteration_X.size() ; k++){
                        int a = i+iteration_X.get(k);
                        int b = j+iteration_Y.get(k);
                        if ((a>=0&&a<n) && (b>=0&&b<n))
                            if (buttons[a][b].getMine()==NO_MINE)
                                buttons[a][b].neighbour++;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        
        if (v == reset){
            resetBoard();
            return;
        }
        
        if (gameOver)
            return;

        MyButton b = (MyButton) v;

        if (b.flagged || b.clicked)
            return;

        b.setBackgroundResource(R.drawable.clicked_button);

        if(b.getMine() == MINE) {
            b.setText(mine.toString());
            Toast.makeText(this, "You Loose", Toast.LENGTH_SHORT).show();
            gameOver = true;
//            reset.setImageDrawable(getDrawable(R.drawable.smiley_sad));
            menu_smiley_button.setIcon(R.drawable.smiley_sad);
            revealAllMines();
            b.setBackgroundColor(getResources().getColor(R.color.wrongTile));
            return;
        }else {
            if (b.neighbour == 0) {
                revealTillNoZero(b.x,b.y);
                score += coordinates_visited.size();
                textViewScore.setText("Score: " + Integer.toString(score));
                coordinates_visited.clear();
            }
            else {
                b.clicked = true;
                score ++;
                textViewScore.setText("Score: " + Integer.toString(score));
                b.setText(Integer.toString(b.neighbour));
            }
        }

//        if (score >= n*n-level){
//            int count = 0;
//            for (int i=0 ; i<n ; i++){
//                for (int j=0 ; j<n ; j++){
//                    if (buttons[i][j].clicked){
//                        count ++;
//                    }
//                }
//            }
//            if (count == n*n-level){
//                menu_smiley_button.setIcon(R.drawable.smiley_thug);
//                Toast.makeText(this, "YOU WIN !!", Toast.LENGTH_LONG);
//                gameOver = true;
//            }
//        }
    }

    private void revealAllMines() {
        for (int i = 0 ; i<n ; i++) {
            for (int j = 0; j < n; j++) {
                if(buttons[i][j].getMine()==MINE) {
                    buttons[i][j].setBackgroundResource(R.drawable.clicked_button);
                    buttons[i][j].setText(mine.toString());
                }
            }
        }
    }

    private void revealTillNoZero(int x, int y) {
        int coord = x*10+y;
        coordinates_visited.add(coord);
        for (int k = 0 ; k<iteration_X.size() ; k++){
            int a = x+iteration_X.get(k);
            int b = y+iteration_Y.get(k);
            if ((a>=0&&a<n) && (b>=0&&b<n)) {
                coord = a*10+b;
                if (!coordinates_visited.contains(coord)) {
                    buttons[a][b].setBackgroundResource(R.drawable.clicked_button);
                    buttons[a][b].clicked = true;

                    coordinates_visited.add(coord);

                    if (buttons[a][b].neighbour == 0) {
                        buttons[x][y].neighbour = -1;
                        revealTillNoZero(a, b);
                    } else if (buttons[a][b].neighbour > 0) {
                        buttons[a][b].setText(Integer.toString(buttons[a][b].neighbour));
                    }
                }


            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        MyButton b = (MyButton) v;

        if (b.clicked)
            return true;

        if (gameOver)
            return true;

        b.flagged = !b.flagged;
        if (b.flagged){
            b.setText(flag.toString());
        }
        else{
            b.setText("");
        }
        return true;
    }
}
