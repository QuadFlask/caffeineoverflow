package main.game.graphics.effects;

public class TransitEffector { // screen transition effect generator
}


/*

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.MemoryImageSource;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class trailblazer extends JFrame
    implements KeyEventDispatcher
{

    final void a(int i)
    {
        String s = Integer.toBinaryString(i);
        b.add(s.substring(s.length() - 6));
    }

    public trailblazer()
        throws Exception
    {
        a = new ArrayList();
        b = new ArrayList();
        c = new Robot();
        int i = 0;
        int j = 2;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        int ai[] = new int[4];
        int ai1[] = new int[4];
        int ai2[][] = new int[50][3];
        float f2 = 320F;
        float f3 = 0.0F;
        float f4 = 700F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        float f7 = 0.0F;
        boolean flag = true;
        Thread.currentThread().setPriority(10);
        a();
        for(i = 0; i < 50; i++)
        {
            ai2[i][0] = (int)(Math.random() * 64000D) - 32000;
            ai2[i][1] = (int)(Math.random() * 48000D) - 24000;
            ai2[i][2] = (int)(Math.random() * 200D);
        }

        enableEvents(48L);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        InputStream inputstream = getClass().getResourceAsStream("a");
        int k;
        while((k = inputstream.read()) != -1)
            switch(k & 0xc0)
            {
            default:
                break;

            case 192:
                a(k);
                // fall through

            case 128:
                a(k);
                // fall through

            case 64: // '@'
                a(k);
                break;

            case 0: // '\0'
                if(k != 0)
                {
                    int k1 = b.size() - 1;
                    for(int j2 = 0; j2 < k; j2++)
                        b.add((String)b.get(k1 - j2));

                    break;
                }
                for(int i2 = 0; i2 < 6; i2++)
                    b.add("555555");

                break;
            }
        b.set(98, "222222");
        b.set(110, "121110");
        b.set(116, "112211");
        b.set(158, "112211");
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, new int[256], 0, 16)), new Point(0, 0), ""));
        do
        {
            long l1;
label0:
            {
                l1 = System.currentTimeMillis();
                Object obj;
                while(a.size() > 0)
                    if((obj = a.remove(0)) instanceof MouseEvent)
                    {
                        MouseEvent mouseevent;
                        if((mouseevent = (MouseEvent)obj).getID() == 501)
                        {
                            if(f4 == 400F && !flag)
                                f5 = -13F;
                            if(f4 > 600F)
                            {
                                f6 = 10F;
                                f5 = 0.0F;
                                f3 = 0.0F;
                                f2 = 320F;
                                f4 = 400F;
                                i1 = 0;
                                flag = false;
                                l = 0;
                                b();
                            }
                        }
                        if(mouseevent.getID() == 503 && f4 < 600F)
                        {
                            f3 -= 320F - (float)mouseevent.getX();
                            b();
                        }
                    }
                if((i = (int)((float)i + f7)) >= 50)
                {
                    i -= 50;
                    j--;
                    l++;
                }
                f2 += f3 / 2.0F;
                f7 += (f6 - f7) / 20F;
                f4 += f5;
                f5 += 2.0F;
                if(f4 < 400F || flag)
                    break label0;
                float f8;
                if((f8 = (f2 - 74F) / 78.6F) < 0.0F || f8 > 6F)
                {
                    flag = true;
                    f6 = 0.0F;
                    break label0;
                }
                switch(((String)b.get(l + 2)).charAt((int)f8))
                {
                case 51: // '3'
                case 52: // '4'
                default:
                    break;

                case 49: // '1'
                    flag = true;
                    f6 = 0.0F;
                    break label0;

                case 50: // '2'
                    f4 = 400F;
                    f5 = -30F;
                    break label0;

                case 53: // '5'
                    if(j1 <= 0)
                    {
                        j1 = 50;
                        i1++;
                    }
                    break;
                }
                f5 = 0.0F;
                f4 = 400F;
            }
            j1--;
            BufferStrategy bufferstrategy;
            Graphics2D graphics2d;
            (graphics2d = (Graphics2D)(bufferstrategy = getBufferStrategy()).getDrawGraphics()).setColor(Color.BLACK);
            graphics2d.fillRect(0, 0, 640, 480);
            for(int i3 = 0; i3 < 50; i3++)
            {
                int k2 = 200 - (ai2[i3][2] + (l * 50 + i) / 4) % 199;
                graphics2d.setColor(new Color(200 - k2, 200 - k2, 200 - k2));
                graphics2d.drawRect(320 + ai2[i3][0] / k2, 240 + ai2[i3][1] / k2, 1, 1);
            }

            graphics2d.setColor(Color.white);
            if(flag)
                graphics2d.fillOval((int)f2, (int)f4 - 26, 20, 20);
            for(int j3 = 0; j3 < 6; j3++)
            {
                for(int k3 = 0; k3 < 16; k3++)
                {
                    float f = 7.7F / (8.5F - (float)k3 / 2.0F - (float)i / 100F);
                    float f1 = 7.7F / (9F - (float)k3 / 2.0F - (float)i / 100F);
                    ai[0] = (int)(320F + ((float)j3 - 3F) * 15F * f);
                    ai[1] = (int)(320F + ((float)j3 - 2.0F) * 15F * f);
                    ai[2] = (int)(320F + ((float)j3 - 2.0F) * 15F * f1);
                    ai[3] = (int)(320F + ((float)j3 - 3F) * 15F * f1);
                    ai1[0] = (int)(240F + 30F * f);
                    ai1[1] = (int)(240F + 30F * f);
                    ai1[2] = (int)(240F + 30F * f1);
                    ai1[3] = (int)(240F + 30F * f1);
                    String s1 = "555555";
                    if((16 - k3) + l < b.size())
                        s1 = (String)b.get((16 - k3) + l);
                    boolean flag1 = true;
                    Color color = Color.red;
                    switch(s1.charAt(j3))
                    {
                    case 48: // '0'
                        color = new Color(0, k3 * 4 + (j % 3) * 20, 0);
                        break;

                    case 49: // '1'
                        flag1 = false;
                        break;

                    case 53: // '5'
                        color = new Color(0, 0, k3 * 4 + (j % 3) * 20);
                        break;
                    }
                    graphics2d.setColor(color);
                    j++;
                    if(flag1)
                        graphics2d.fillPolygon(ai, ai1, 4);
                }

            }

            if(!flag)
            {
                graphics2d.setColor(new Color(0, 0, 0, 70));
                graphics2d.fillOval((int)f2 + ((int)f4 - 400), (10 + (int)f4) - 26, 20, 10);
                graphics2d.setColor(Color.white);
                graphics2d.fillOval((int)f2, (int)f4 - 26, 20, 20);
            }
            graphics2d.setColor(Color.white);
            graphics2d.setFont(graphics2d.getFont().deriveFont(64));
            graphics2d.drawString((new StringBuilder("Score: ")).append(l * 50 + i).toString(), 30, 40);
            String s = null;
            if(f4 > 600F)
                s = "Click to Start...Escape to Quit";
            else
            if(j1 > 0)
                s = (new StringBuilder("Level ")).append(i1).append(" Compete!....\n  Get Ready for next level!").toString();
            if(s != null)
                graphics2d.drawString(s, 240, 240);
            bufferstrategy.show();
            long l2;
            if((l2 = System.currentTimeMillis()) - l1 < 50L)
                Thread.sleep(50L - (l2 - l1));
        } while(true);
    }

    final void a()
    {
        setIgnoreRepaint(true);
        setBounds(300, 200, 640, 480);
        setVisible(true);
        createBufferStrategy(2);
    }

    public static void main(String args[])
        throws Exception
    {
        new trailblazer();
    }

    protected final void processEvent(AWTEvent awtevent)
    {
        a.add(awtevent);
    }

    public final boolean dispatchKeyEvent(KeyEvent keyevent)
    {
        if(keyevent.getKeyCode() == 27)
            System.exit(0);
        return false;
    }

    final void b()
    {
        Point point;
        SwingUtilities.convertPointToScreen(point = new Point(320, 250), this);
        c.mouseMove(point.x, point.y);
    }

    ArrayList a;
    ArrayList b;
    Robot c;
}


*/