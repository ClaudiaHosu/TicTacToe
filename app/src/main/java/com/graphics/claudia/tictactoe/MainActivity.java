package com.graphics.claudia.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int WINNING_STREAK = 3;
    private int SQUARE_SIZE;

    private boolean shouldDropHeart = true;
    private boolean gameWon = false;
    private Boolean[][] supportingMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQUARE_SIZE = getResources().getInteger(R.integer.game_size);
        supportingMatrix = new Boolean[SQUARE_SIZE][SQUARE_SIZE];

        resetCells(null);
    }

    public void resetCells(View view) {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        supportingMatrix = new Boolean[SQUARE_SIZE][SQUARE_SIZE];
        TextView winnerTextView = (TextView) findViewById(R.id.winnerText);
        winnerTextView.setText(null);
        shouldDropHeart = true;
        gameWon = false;

        for (int counter = 0; counter < SQUARE_SIZE*SQUARE_SIZE; counter++) {

            ImageView emptyImage = new ImageView(MainActivity.this);
            emptyImage.setTag(R.id.rowIndex, counter / SQUARE_SIZE);
            emptyImage.setTag(R.id.colIndex, counter % SQUARE_SIZE);
            emptyImage.setImageResource(android.R.drawable.screen_background_light_transparent);
            emptyImage.setBackgroundColor(getResources().getColor(android.R.color.white));
            emptyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedCell(v);
                }
            });

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            Resources r = Resources.getSystem();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
            layoutParams.width = (int) px;
            layoutParams.height = (int) px;
            layoutParams.columnSpec = GridLayout.spec(counter % SQUARE_SIZE, 1,1 );
            layoutParams.rowSpec = GridLayout.spec(counter / SQUARE_SIZE,1,1 );
            emptyImage.setLayoutParams(layoutParams);

            gridLayout.addView(emptyImage);
        }
    }

    public void clickedCell(View view) {

        if (gameWon) return;

        ImageView imageView = (ImageView) view;
        Log.i("positions",imageView.getLeft()+" "+imageView.getTop());

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        ImageView dropImage = new ImageView(MainActivity.this);
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) imageView.getLayoutParams();
        dropImage.setLayoutParams(layoutParams);
        view.setOnClickListener(null);
        gridLayout.addView(dropImage);

        if (shouldDropHeart) {
            dropImage.setImageResource(R.drawable.heart);
            updateData((Integer) imageView.getTag(R.id.rowIndex), (Integer) imageView.getTag(R.id.colIndex), Boolean.TRUE);
            shouldDropHeart = false;
        } else {
            dropImage.setImageResource(R.drawable.thunder);
            updateData((Integer) imageView.getTag(R.id.rowIndex), (Integer) imageView.getTag(R.id.colIndex), Boolean.FALSE);
            shouldDropHeart = true;
        }
        dropImage.setTranslationY(-1000f);
        dropImage.bringToFront();
        dropImage.animate().translationY(0).setDuration(500);

        checkWin((Integer) imageView.getTag(R.id.rowIndex), (Integer) imageView.getTag(R.id.colIndex));
    }

    private void updateData(int rowIndex, int colIndex, Boolean value) {
        supportingMatrix[rowIndex][colIndex] = value;
    }


    public void checkWin(int currentRow, int currentCol) {

        if (isRowComplete(currentRow, currentCol) || isColumnComplete(currentRow, currentCol) || isRightDiagonalComplete(currentRow, currentCol) || isLeftDiagonalComplete(currentRow, currentCol)) {
            TextView winnerTextView = (TextView) findViewById(R.id.winnerText);
            winnerTextView.setText(supportingMatrix[currentRow][currentCol] ? "Hearts won!":"Thunder won!");
            gameWon = true;
            Log.i("winner","We have a winner!");
        }

    }

    public boolean isRowComplete(int currentRow, int currentCol) {
        int counter = 1;
        for (int i = 0; i < supportingMatrix[currentRow].length; i++) {

            if (i == currentCol) continue;

            if (supportingMatrix[currentRow][i] == null || !supportingMatrix[currentRow][i].equals(supportingMatrix[currentRow][currentCol])) {
                break;
            } else if (++counter == WINNING_STREAK){
                return true;
            }
        }
        return false;
    }

    public boolean isColumnComplete(int currentRow, int currentCol) {
        int counter = 1;
        for (int i = 0; i < supportingMatrix.length; i++) {

            if (i == currentRow) continue;

            if (supportingMatrix[i][currentCol] == null || !supportingMatrix[i][currentCol].equals(supportingMatrix[currentRow][currentCol])) {
                break;
            } else if (++counter == WINNING_STREAK){
                return true;
            }
        }
        return false;
    }

    public boolean isRightDiagonalComplete(int currentRow, int currentCol) {
        int startRow = currentRow + 1;
        int startCol = currentCol;
        int counter = 1;
        while( startRow < supportingMatrix.length) {
            startCol--;
            if (startCol >= 0) {
                if (supportingMatrix[startRow][startCol] == null || !supportingMatrix[startRow][startCol].equals(supportingMatrix[currentRow][currentCol])) {
                    break;
                } else if (++counter == WINNING_STREAK){
                    return true;
                }
            }
            startRow ++;
        }

        startRow = currentRow - 1;
        startCol = currentCol;
        while( startRow >= 0) {
            startCol++;
            if (startCol < supportingMatrix[startRow].length) {
                if (supportingMatrix[startRow][startCol] == null || !supportingMatrix[startRow][startCol].equals(supportingMatrix[currentRow][currentCol])) {
                    break;
                } else if (++counter == WINNING_STREAK){
                    return true;
                }
            }
            startRow --;
        }

        return counter == WINNING_STREAK;
    }


    public boolean isLeftDiagonalComplete(int currentRow, int currentCol) {
        int startRow = currentRow + 1;
        int startCol = currentCol;
        int counter = 1;
        while( startRow < supportingMatrix.length) {
            startCol++;
            if (startCol < supportingMatrix[startRow].length) {
                if (supportingMatrix[startRow][startCol] == null || !supportingMatrix[startRow][startCol].equals(supportingMatrix[currentRow][currentCol])) {
                    break;
                } else if (++counter == WINNING_STREAK){
                    return true;
                }
            }
            startRow ++;
        }

        startRow = currentRow - 1;
        startCol = currentCol;
        while( startRow >= 0) {
            startCol--;
            if (startCol >= 0) {
                if (supportingMatrix[startRow][startCol] == null || !supportingMatrix[startRow][startCol].equals(supportingMatrix[currentRow][currentCol])) {
                    break;
                } else if (++counter == WINNING_STREAK){
                    return true;
                }
            }
            startRow --;
        }

        return counter == WINNING_STREAK;
    }


}
