import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.event.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class GUI {
    Properties props = new Properties();
    private JTextField koondarveNr;
    private JFormattedTextField perioodiAlgus;
    private JFormattedTextField perioodiLopp;
    private JTextField seeria;
    private JTextField struk;
    private JTextField haigekassa;
    private JTextField hinnakirjaKood;
    private JFormattedTextField hulk;
    private JFormattedTextField koef;
    private JFormattedTextField hind;
    private JTextField kokku;
    private JTextField erialaKood;
    private JTextField regKood;
    private JTextField diagnoos;
    private JButton katkestaButton;
    private JButton valmisButton;
    private JPanel container;
    private JLabel label1;
    private JTextField tyyp;
    private JTextField arstiKood;
    private JTextField arveNr;
    private JLabel labelAsukohaKood;
    private JTextField asukohaKood;
    private JFrame frame;


    public GUI() {
        valmisButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                props.setProperty("eriala_kood",erialaKood.getText());
                props.setProperty("koondarve_number",koondarveNr.getText());
                props.setProperty("number",arveNr.getText());
                props.setProperty("number",arveNr.getText());
                props.setProperty("koef",koef.getText());
                props.setProperty("hind",hind.getText());
                props.setProperty("ariregistri_kood",regKood.getText());
                props.setProperty("arsti_kood", arstiKood.getText());
                props.setProperty("asukoha_kood", asukohaKood.getText());
                try {
                    props.store(new FileOutputStream("conf.properties"),"");
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                String currentPath = System.getProperty("user.dir")+"/kassa/";
                String files[] = {"ARVED.DAT", "DGN.DAT", "LEHED.DAT", "LEHED.HDR","LEHED.ITM"};
                String contents[] = {
                        //ARVED.DAT
                        "1\t1\t"+ struk.getText() +"\t"+hinnakirjaKood.getText()+"\t"+hulk.getText()+"\t1.00\t"+kokku.getText()+
                                "\t"+perioodiLopp.getText()+"\t\t"+koef.getText(),

                        //DGN.DAT
                        "1\t1\t"+diagnoos.getText()+"\t+",

                        //LEHED.DAT
                        "1\t09\t"+asukohaKood.getText()+"\tA\t"+seeria.getText()+"\t"+arveNr.getText()+"\t\t\t\t\t\t\t"+regKood.getText()+"\t"+erialaKood.getText()+
                                "\t"+arstiKood.getText()+"\tT0002\t\tE\t13\tE\t\t"+perioodiAlgus.getText()+"\t"+perioodiLopp.getText()
                                +"\t\t\t\t"+kokku.getText()+"\t\t"+struk.getText()+"\tEST\tE",

                        //LEHED.HDR
                        "RAVILEHED\t"+arveNr.getText()+"\t"+perioodiLopp.getText()+"\t"+regKood.getText()+"\t"+haigekassa.getText()+"\t\t",
                        //LEHED.ITM
                        "LEHED.DAT\tLEHED\t1\r\n" +
                                "DGN.DAT\tDIAGN\t1\r\n" +
                                "ARVED.DAT\tARVEREAD\t1"
                };
                int iter=0;
                for (String file : files) {
                    Writer writer = null;
                    try {
                        writer = new BufferedWriter(new FileWriter(currentPath+file));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        assert writer != null;
                        writer.write(contents[iter]);
                        writer.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    iter++;
                }

                frame.dispose();
            }
        });
        katkestaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();

            }
        });


        hulk.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                checkIfLegit(e, hulk);
            }
        });
        koef.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //super.keyReleased(e);
                checkIfLegit(e, koef);
            }
        });
        hind.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
              checkIfLegit(e, hind);
            }
        });
        hulk.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                 if(!hulk.getText().isEmpty() && !hind.getText().isEmpty() && !koef.getText().isEmpty()) {
            calculateSum();
        }
            }
        });

        hind.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
              if(!hulk.getText().isEmpty() && !hind.getText().isEmpty() && !koef.getText().isEmpty()) {
            calculateSum();
        }
            }
        });

        koef.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!hulk.getText().isEmpty() && !hind.getText().isEmpty() && !koef.getText().isEmpty()) {
                    calculateSum();
                }
            }
        });
        koondarveNr.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                e.consume();  // ignore event
                }
            }
        });
        arveNr.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                e.consume();  // ignore event
                }
            }
        });
    }

    private void checkIfLegit(KeyEvent e, JFormattedTextField field) {
        char c = e.getKeyChar();
        if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) && (c != '.' || field.getText().contains("."))) {
           e.consume();  // ignore event
        }else if(!hulk.getText().isEmpty() && !hind.getText().isEmpty() && !koef.getText().isEmpty()) {
            calculateSum();
        }
        else kokku.setText("-");
    }

    private void calculateSum() {
        float hulkFloat = Float.valueOf(hulk.getText());
        float koefFloat = Float.valueOf(koef.getText());
        float hindFloat = Float.valueOf(hind.getText());
        float kokkuF =  hulkFloat * koefFloat * hindFloat;
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        //DecimalFormat threeDForm = new DecimalFormat("#.###");
        kokku.setText(String.valueOf(twoDForm.format(kokkuF)));
    }

    public void initFrame() throws IOException {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setTitle(resourceBundle.getString("river.demo"));
        frame.setContentPane(container);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        initValues();

    }

    private void initValues() {

        try {
            MaskFormatter formatter = new MaskFormatter("##.##.####");
            formatter.setValidCharacters("0123456789");
            formatter.setPlaceholderCharacter('#');
            perioodiAlgus.setFormatterFactory(new DefaultFormatterFactory(formatter));
            perioodiLopp.setFormatterFactory(new DefaultFormatterFactory(formatter));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            props.load(new FileInputStream("conf.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int currentCount =  Integer.parseInt(props.getProperty("koondarve_number"));
        String newCount = String.valueOf(currentCount+1);
        koondarveNr.setText(newCount);
        seeria.setText(props.getProperty("seeria"));
        haigekassa.setText(props.getProperty("haigekassa"));

        currentCount = Integer.parseInt(props.getProperty("number"));
        newCount = String.valueOf(currentCount + 1);
        arveNr.setText(newCount);
        tyyp.setText(props.getProperty("raviteenuse_tyyp"));
        diagnoos.setText(props.getProperty("diagnoos"));
        struk.setText(props.getProperty("struk_yksus"));
        hinnakirjaKood.setText(props.getProperty("hinnakirja_kood"));
        hulk.setText(props.getProperty("hulk"));
        hulk.setName("Hulk");
        koef.setText(props.getProperty("koef"));
        koef.setName("Koefitsent");
        hind.setText(props.getProperty("hind"));
        hind.setName("Hind");
        arstiKood.setText(props.getProperty("arsti_kood"));
        erialaKood.setText(props.getProperty("eriala_kood"));
        regKood.setText(props.getProperty("ariregistri_kood"));
        String pattern = "MM.yyyy";
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String dateNow = dateFormat.format(currentDate.getTime());
        perioodiAlgus.setText("01."+dateNow);
        int maxDay = currentDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        perioodiLopp.setText(maxDay+"."+dateNow);
        asukohaKood.setText(props.getProperty("asukoha_kood"));

        calculateSum();

    }

}
