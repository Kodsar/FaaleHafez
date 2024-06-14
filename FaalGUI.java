package com.fasterxml;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FaalGUI extends JFrame implements ActionListener {


    private static final String GET_URL = "https://faal.spclashers.workers.dev/api";
    JButton getFaal;
    JButton backButton;

    public FaalGUI() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("فال حافظ");
        this.setFont(new Font("Nazanin", Font.PLAIN, 14));
        this.setBackground(new Color(178,9,49));
        HomePage();

        this.setSize(700, 800);
        this.setVisible(true);

    }

    private static Faal getFaal() throws IOException {

        try {
            @SuppressWarnings("deprecation")
            URL url = new URL(GET_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(inline.toString());
                String poem = node.get("Poem").asText();
                poem = poem.replace("\\r\\n", "\n");
                String interpretation = node.get("Interpretation").asText();
                interpretation = interpretation.replace("\\r\\n", "\n");
                return new Faal(poem,interpretation);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Faal(null,null);
        }

    }


    public void HomePage(){

        JPanel HomePage = new JPanel();
        HomePage.setBackground(new Color(178,9,49));

        HomePage.setLayout(null);

        JLabel label2 = new JLabel("نیت کنید ...");
        label2.setFont(new Font("Nazanin", Font.BOLD, 25));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        label2.setBounds(300, 150, 200, 50);

        JLabel label3 = new JLabel("در صورت اتمام نیت خود، روی دکمه‌ی زیر کلیک کنید.");
        label3.setFont(new Font("Nazanin", Font.BOLD, 14));
        label3.setBounds(200, 350, 400, 50);

        getFaal = new JButton("دریافت فال");
        getFaal.setFont(new Font("Nazanin", Font.BOLD, 14));
        getFaal.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        getFaal.setBounds(265, 600, 200, 50);
        getFaal.addActionListener(this);

        HomePage.add(label2);
        HomePage.add(label3);
        HomePage.add(getFaal);

        this.add(HomePage);
    }


    public void FaalPage(String poem, String interpretation){
        JPanel faalPage = new JPanel();
        faalPage.setLayout(new BoxLayout(faalPage, BoxLayout.Y_AXIS));
        faalPage.setAlignmentX(Component.CENTER_ALIGNMENT);
        faalPage.setAlignmentY(Component.CENTER_ALIGNMENT);

        faalPage.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel label = new JLabel("شعر");
        label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        label.setFont(new Font("Nazanin", Font.PLAIN, 20));
        label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);



        JPanel poemPanel = new JPanel();
        poemPanel.setLayout(new GridBagLayout());


        JTextArea label2 = new JTextArea(poem);
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        label2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        label2.setFont(new Font("Nazanin", Font.PLAIN, 14));

        label2.setEditable(false);
        label2.setAlignmentY(Component.CENTER_ALIGNMENT);
        label2.setBackground(null);



        JLabel label3 = new JLabel("تفسیر");
        label3.setBackground(new Color(178,9,49));

        label3.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        label3.setAlignmentX(Component.CENTER_ALIGNMENT);
        label3.setFont(new Font("Nazanin", Font.PLAIN, 20));


        JTextArea label4 = new JTextArea(interpretation);
        label4.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        label4.setEditable(false);
        label4.setBackground(null);
        label4.setLineWrap(true);
        label4.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        label4.setFont(new Font("Nazanin", Font.PLAIN, 14));


        backButton = new JButton("بازگشت");
        backButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setFont(new Font("Nazanin", Font.PLAIN, 14));

        backButton.addActionListener(this);

        faalPage.add(label);

        poemPanel.add(label2);
        faalPage.add(poemPanel);

        faalPage.add(Box.createRigidArea(new Dimension(0, 20)));

        faalPage.add(label3);
        faalPage.add(Box.createRigidArea(new Dimension(0, 40)));

        faalPage.add(label4);
        faalPage.add(Box.createRigidArea(new Dimension(0, 20)));

        faalPage.add(backButton);
        faalPage.add(Box.createRigidArea(new Dimension(0, 20)));


        this.add(faalPage);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getFaal) {
            this.getContentPane().removeAll();

            try {
                Faal faal= getFaal();
                FaalPage(faal.getPoem(), faal.getInterpretation());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            this.revalidate();
            this.repaint();
        }

        if (e.getSource() == backButton) {
            this.getContentPane().removeAll();
            HomePage();
            this.revalidate();
            this.repaint();
        }
    }




}

class Faal{
    private String poem;
    private String interpretation;
    
    public Faal(String poem,String interpretation){
        this.poem=poem;
        this.interpretation=interpretation;
    }


    public String getPoem(){
        return poem;
    }
    public String getInterpretation(){
        return interpretation;
    }

}


