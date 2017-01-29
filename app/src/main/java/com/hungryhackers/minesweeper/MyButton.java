package com.hungryhackers.minesweeper;

import android.content.Context;
import android.widget.TextView;

public class MyButton extends TextView{

    private int mine;
    int neighbour;
    boolean flagged;
    boolean clicked;
    int x;
    int y;

    public int getMine() {
        return mine;
    }

    public void setMine(int mine) {
        this.mine = mine;
    }

    public MyButton(Context context) {
        super(context);
    }


}
