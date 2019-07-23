
package quizdemo;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Sreejan
 */
class noprev extends Exception
{

}
public class quizstart2 extends javax.swing.JFrame implements Runnable {

    /** Creates new form quizstart */
    public quizstart2() {
        try
        {
Class.forName("com.mysql.jdbc.Driver"); // Driver Load & Unload

con=DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz5","root","Gagan333@");
        }
        catch(Exception e)
        {
           JOptionPane.showMessageDialog(null, ""+e);
        }
        initComponents();

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int setx, sety, leftx=0, topy=0;
        setx=screenSize.width/2;
        sety=screenSize.height/2;
        leftx=setx-680/2;
        topy=sety-480/2;
        setLocation(leftx,topy-40);

        mp=new HashMap();
        jLabel5.setText("Current User: "+login.user);
        initquiz();
        Thread t=new Thread(this);
        t.start();

    }
    public void run()
    {
      for(hcount=hr; hcount>=0; hcount--)
      {
       for(mcount=min;mcount>=0;mcount--)
       {
           for(scount=sec; scount>=0;scount--)
           {
               display();
           }
        sec=59;
       }
       min=59;
        }
    }
    void display()
    {
        try
        {  
            minutes=""+mcount;
            seconds=""+scount;

            if(mcount<10)
            {
                minutes="0"+minutes;
            }
            if(scount<10)
            {
                seconds="0"+seconds;
            }
            if(hr==0)
            {
                jLabel1.setText("Time Remaining: "+minutes+":"+seconds+" min(s)");
            }
            else
            {
            hours=""+hcount;
            if(hcount<10)
            {
                hours="0"+hours;
            }
            jLabel1.setText("Time Remaining: "+hours+":"+minutes+":"+seconds+" hr(s)");

            }
            if(hcount==0 && mcount==0 && scount==0)
            {
            for(int i=0; i<nextcount; i++)
           {
               if(userans[i].equals("no")==false)
               {
                   anscount++;
                   if (ans[i].equals(userans[i]))
                    {
                        score=score+marks;
                        ccount++;
                    }
                    else
                    {
                        score=score-nmarks;
                        wcount++;
                    }
                }
            }
            if(score<0)
            {
                score=0;
            }
            uscore=score;
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
            if(showscore.equals("yes"))
            {
            new score2().setVisible(true);
            }
            else
            {
                new noscore2().setVisible(true);
             }
            }
            Thread.sleep(999);
        }
        catch(Exception e)
        {
           JOptionPane.showMessageDialog(null, ""+e);
        }
    }
    private void initquiz()
    {
       try
       {
       ps1=con.prepareStatement("select dquiz, qtotal,cmarks,wmarks,qtime,randomize,register,viewscore from settings where serial=1");
       rs1=ps1.executeQuery();
       rs1.next();
       if(rs1.getString(6).equals("yes"))
       {
           random=true;
           }
       else
       {
           random=false;
        }
       total=rs1.getInt(2);
       min=rs1.getInt(5);

       hr=min/60;
       min=min%60;

       marks=rs1.getInt(3);
       totalmarks=total*marks;
       nmarks=rs1.getInt(4);
       String testname;
       testname=rs1.getString(1);
       if(testname.equals("Test 1"))
       {
           test="test1";
       }
        else if (testname.equals("Test 2"))
       {
           
           test="test2";
       }
        else if (testname.equals("Test 3"))
       {
           test="test3";
       }
       else if(testname.equals("Test 4"))
       {
           test="test4";
       }
        else
       {
           test="test5";
       }
       showscore=rs1.getString(8);
       ps2=con.prepareStatement("select qid,question,option1,option2,option3,option4,correct from "+test+"");
       rs2=ps2.executeQuery();

       num=0;
       a=new int[total];
       b=new int[total];
       String str;
       while(rs2.next())
       {
            num++;
            str=rs2.getString(1);
            mp.put(num, ""+str);
       }
       int flag;
       if(random)
       {
       for(int i=0; i<total; i++)
       {    flag=0;
           a[i]=1+(int)(Math.random()*num-0.1);
           b[i]=a[i];
           for(int j=0; j<i; j++)
           {
               if(b[j]==a[i])
               {
               flag=1;
               break;
               }
           }
           if(flag==1)
           {
               i--;
               continue;
           }
           
       }
       }
       else
       {
          for(int i=0; i<total; i++)
          a[i]=1+i;
       }
       ans=new String[total];
       userans=new String[total];
       quiz();
       }
        catch(Exception e)
        {
           JOptionPane.showMessageDialog(this, ""+e);
        }
    }
    private void quiz()
    {
       try
       {
       jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()), "QUESTION #"+(count+1)+"", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14)));
       ps3=con.prepareStatement("select question,option1,option2,option3,option4,correct from "+test+" where qid=?");
       ps3.setString(1, ""+mp.get(a[count]));
       rs3=ps3.executeQuery();
       rs3.next();
       jLabel4.setText(rs3.getString(1));
       jRadioButton1.setText(rs3.getString(2));
       jRadioButton2.setText(rs3.getString(3));
       jRadioButton3.setText(rs3.getString(4));
       jRadioButton4.setText(rs3.getString(5));
       ans[count]=rs3.getString(6);
       }
        catch(Exception e)
        {
           JOptionPane.showMessageDialog(this, ""+e);
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jRadioButton5 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setText("jRadioButton5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Quizilla: Quiz");
        setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        setResizable(false);

        jButton1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Previous2.png"))); // NOI18N
        jButton1.setText(" Previous");
        jButton1.setIconTextGap(2);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/next2.png"))); // NOI18N
        jButton2.setText("Forward ");
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jButton2.setIconTextGap(5);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/quiz62.png"))); // NOI18N

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/change-password1.png"))); // NOI18N
        jButton3.setToolTipText("Change Password");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit1.png"))); // NOI18N
        jButton4.setToolTipText("Edit Details");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Time Remaining: t min(s)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Current User:");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "QUESTION #1", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14));
        jLabel3.setText(" ");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14));
        jLabel4.setText("What is the capital of India?");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jRadioButton1.setText("Option 1");
        jRadioButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioButton1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jRadioButton1MousePressed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jRadioButton2.setText("Option 2");
        jRadioButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jRadioButton2MousePressed(evt);
            }
        });
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jRadioButton3.setText("Option 3");
        jRadioButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jRadioButton3MousePressed(evt);
            }
        });

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jRadioButton4.setText("option 4");
        jRadioButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jRadioButton4MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton2)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton3)
                            .addComponent(jRadioButton4)))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton4)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 388, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try
        {
        if(jRadioButton1.isSelected())
        {
            userans[count]="A";
        }
        else if(jRadioButton2.isSelected())
        {
            userans[count]="B";

        }
        else if(jRadioButton3.isSelected())
        {
            userans[count]="C";

        }
        else if(jRadioButton4.isSelected())
        {
            userans[count]="D";

        }
        else
        {
            userans[count]="no";
        }
        if(count==0)
        {
            throw new noprev();
        }
        count--;
        ncount--;
        if(userans[count].equals("A"))
       {
           jRadioButton1.setSelected(true);
       }
        else if(userans[count].equals("B"))
       {
           jRadioButton2.setSelected(true);
       }
       else if(userans[count].equals("C"))
       {
           jRadioButton3.setSelected(true);
       }
       else if(userans[count].equals("D"))
       {
           jRadioButton4.setSelected(true);
       } 
        else
       {
            jRadioButton5.setSelected(true);
        }
        jButton2.setText("Forward ");
       quiz();

        }
        catch(noprev e)
        {
           JOptionPane.showMessageDialog(this, "This is the first question.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ncount++;
        if(jRadioButton1.isSelected())
        {
            userans[count]="A";
            
        }
        else if(jRadioButton2.isSelected())
        {
            userans[count]="B";
            
        }
        else if(jRadioButton3.isSelected())
        {
            userans[count]="C";
            
        }
        else if(jRadioButton4.isSelected())
        {
            userans[count]="D";
            
        }
        else
        {
            userans[count]="no";
        }
       jRadioButton5.setSelected(true);
       count++;
        if(count<total)
        {
           if(userans[count]!=null)
            {  
            if(userans[count].equals("A"))
            {
                jRadioButton1.setSelected(true);
            }
            else if(userans[count].equals("B"))
            {
                jRadioButton2.setSelected(true);
            }
            else if(userans[count].equals("C"))
            {
                jRadioButton3.setSelected(true);
            }
            else if(userans[count].equals("D"))
            {
           jRadioButton4.setSelected(true);
            }
            else
            {
                jRadioButton5.setSelected(true);
            }
            }
            else
           {
            nextcount++;
            }
            if(ncount==total-1)
            {
                jButton2.setText("Submit  ");
            }
            quiz();
        }
        else
        {
           nextcount++;
           for(int i=0; i<nextcount; i++)
           {
               if(userans[i].equals("no")==false)
               {
                   anscount++;
                   if (ans[i].equals(userans[i]))
                    {
                        score=score+marks;
                        ccount++;
                    }
                    else
                    {
                        score=score-nmarks;
                        wcount++;
                    }
                }
            }
            if(score<0)
            {
                score=0;
            }
            uscore=score;
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
            hcount=0;
            mcount=0;
            scount=0;
            if(showscore.equals("yes"))
            {
            new score2().setVisible(true);
            }
            else
            {
                new noscore2().setVisible(true);
             }
            
        
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       new usereditdetails().setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new usereditpass().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton1MouseClicked


    }//GEN-LAST:event_jRadioButton1MouseClicked

    private void jRadioButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton1MousePressed
               if(jRadioButton1.isSelected())
        {
            jRadioButton5.setSelected(true);
        }
    }//GEN-LAST:event_jRadioButton1MousePressed

    private void jRadioButton2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton2MousePressed
        if(jRadioButton2.isSelected())
        {
            jRadioButton5.setSelected(true);
        }
    }//GEN-LAST:event_jRadioButton2MousePressed

    private void jRadioButton3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton3MousePressed
         if(jRadioButton3.isSelected())
        {
            jRadioButton5.setSelected(true);
        }
    }//GEN-LAST:event_jRadioButton3MousePressed

    private void jRadioButton4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton4MousePressed
        if(jRadioButton4.isSelected())
        {
            jRadioButton5.setSelected(true);
        }
    }//GEN-LAST:event_jRadioButton4MousePressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new quizstart2().setVisible(true);

            }
        });

    }
    int hr, min, sec, hcount, mcount, scount;
    String ans[], userans[], ch[], hours, minutes, seconds, showscore;
    static int totalmarks, ccount=0,wcount=0, uscore=0,anscount=0;
    int quiztotal, count=0, score=0, nextcount=0, ncount=0;
    int a[], b[];
    String test;
    static boolean random;
    static int total, time, marks, nmarks;
    Map mp;
    int num;
    Connection con;
    PreparedStatement ps1, ps2, ps3;
    ResultSet rs1, rs2, rs3;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    // End of variables declaration//GEN-END:variables

}
