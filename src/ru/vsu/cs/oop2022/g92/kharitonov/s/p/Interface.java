package ru.vsu.cs.oop2022.g92.kharitonov.s.p;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Interface extends JFrame implements Runnable {

    private final int SIZE;
    private final Container container;
    private final GridLayout grid;
    private final JButton[][] buttons;
    private final Color noColor;
    private final InputStream realSystemIn;
    private final PipedOutputStream systemInOut;

    private boolean boardEnabled = false;

    private ImageIcon red;
    private ImageIcon redKing;
    private ImageIcon blue;
    private ImageIcon blueKing;

    public Interface(int size)
    {
        this.SIZE = size;

        grid = new GridLayout(SIZE, SIZE);
        container = getContentPane();
        container.setLayout(grid);
        buttons = new JButton[SIZE][SIZE];

        red = new ImageIcon("Images/red.png");
        redKing = new ImageIcon("Images/redKing.png");
        blue = new ImageIcon("Images/blue.png");
        blueKing = new ImageIcon("Images/blueKing.png");

        for (int row = 0; row < SIZE; ++row)
            for (int col = 0; col < SIZE; ++col)
            {
                final int actualRow = actualRow(row);
                final int finalCol = col;

                buttons[row][col] = new JButton("");
                buttons[row][col].setToolTipText(actualRow + "," + col);
                container.add(buttons[row][col]);

                buttons[row][col].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        if (boardEnabled)
                        {
                            pushBack(actualRow + "\n");
                            pushBack(finalCol + "\n");
                        }
                    }
                });
            }

        noColor = buttons[0][0].getBackground();

        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        PipedInputStream systemInIn = new PipedInputStream();
        systemInOut = new PipedOutputStream();
        try
        {
            systemInOut.connect(systemInIn);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

        realSystemIn = System.in;
        System.setIn(systemInIn);

        Thread readerWriter = new Thread(this, "ReaderWriter");
        readerWriter.setPriority(Thread.MAX_PRIORITY);
        readerWriter.start();
    }

    public void enableBoard()
    {
        boardEnabled = true;
    }

    public void disableBoard()
    {
        boardEnabled = false;
    }

    public void showBoard(int array[][])
    {
        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++)
            {
                int boardVal = array[actualRow(row)][col];

                Color color = noColor;
                ImageIcon currentImage = null;

                if ((actualRow(row) + col) % 2 == 0)
                    color = color.darker();
                else
                    color = color.brighter();

                switch (boardVal)
                {
                    case Checkers.MARK:
                        color = Color.GREEN;
                        break;

                    case Checkers.RED:
                        currentImage = red;
                        break;
                    case Checkers.BLUE:
                        currentImage = blue;
                        break;
                    case Checkers.RED * 2:
                        currentImage = redKing;
                        break;
                    case Checkers.BLUE * 2:
                        currentImage = blueKing;
                        break;
                }
                if (!buttons[row][col].getBackground().equals(color))
                {
                    buttons[row][col].setBackground(color);
                }
                buttons[row][col].setIcon(currentImage);
                buttons[row][col].repaint();

                currentImage = null;
            }
    }

    private int actualRow(int row)
    {
        return SIZE-1-row;
    }

    public void pushBack(String s)
    {
        char[] pushbackC = s.toCharArray();
        byte[] pushbackB = new byte[pushbackC.length];
        for (int i = 0; i < pushbackC.length;  ++i)
            pushbackB[i] = (byte) pushbackC[i];
        try
        {
            systemInOut.write(pushbackB);
            systemInOut.flush();
            Thread.yield();
        }
        catch (IOException e)
        {
        }
    }
    public void run()
    {
        byte[] buf = new byte[256];
        try
        {
            while (realSystemIn.read(buf) >= 0)
            {
                systemInOut.write(buf);
                systemInOut.flush();
            }
        }
        catch (IOException e) {
        }
    }
}

