package com.hungryhackers.minesweeper;

import android.content.Context;
import android.graphics.Typeface;
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

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (this.getMine() == 1) {
            this.setTypeface(null, Typeface.BOLD_ITALIC);
        }
        super.setText(text, type);
    }

    public MyButton(Context context) {
        super(context);
    }


}
