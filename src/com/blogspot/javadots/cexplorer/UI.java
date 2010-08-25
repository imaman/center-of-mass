package com.blogspot.javadots.cexplorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class UI extends JFrame {

   private static final String CONFIG_DIR = "dir";
   private static final long serialVersionUID = -3162028643938869750L;
   private final Model model = new Model();

   public final Output output;
   private Collector collector = new Collector();
   private InputProvider provider;
   private ModelProcessor modelProcessor;
   private final Config config;

   
   private static Output defaultOutput() {
      return new Output() 
      {      
         private final JTextField textField = new JTextField();
         
         private String result;
         
         @Override
         public void setResult(String arg) {
            result = arg;
            textField.setText(result);
         }
         
         @Override
         public String getResult() {
            return result;
         }

         @Override
         public Component getWidget() {
            return textField;
         }
      };
   }
   
   public UI() {
      this(new Collector());
   }
   
   public UI(Collector c) {
      this(defaultOutput(), c);
   }
   
   public UI(Output o, Collector c) {
      config = new Config(getClass().getName() + ".props");
      collector = c;      
      setLayout(new BorderLayout());
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      output = o;
      setName("frame0");
      
      
      JLabel left = new JLabel("    ");
      left.setBorder(BorderFactory.createEmptyBorder());
      left.setOpaque(true);
      left.setBackground(Color.WHITE);      
      add(left, BorderLayout.WEST);
      
      JScrollPane sp = new JScrollPane(output.getWidget());
      sp.setBorder(BorderFactory.createEmptyBorder());
      add(sp, BorderLayout.CENTER);

      final JFileChooser fc = new JFileChooser();
      fc.setCurrentDirectory(new File(config.get(CONFIG_DIR, System.getProperty("user.dir"))).getAbsoluteFile());
      fc.setMultiSelectionEnabled(true);
      fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);      
      fc.setName("open");
      
      JMenuBar mb = new JMenuBar();
      
      JMenu m = new JMenu("File");
      m.setMnemonic('f');
      mb.add(m);
      
      JMenuItem mi = new JMenuItem("Open");
      mi.setMnemonic('o');
      m.add(mi);
      
      mi.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            int result = fc.showOpenDialog(UI.this);
            if(result == JFileChooser.APPROVE_OPTION) {
               File[] fs = fc.getSelectedFiles();
               if(fs.length <= 0)
                  return;
               
               analyze(fs);          
               config.set(CONFIG_DIR, fs[0].getAbsolutePath());
               config.store();
            }
         }
      });
      
      mi = new JMenuItem("Exit");
      mi.setMnemonic('x');
      m.add(mi);
      mi.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            System.exit(0);
         }
      });
      
      
      
      setJMenuBar(mb);
      
      pack();
      setSize(1100, 500);
      setVisible(true);
   }
   
   public Model getModel() {
      return model;
   }

   protected void analyze(final File[] files) {
      
      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      Thread th = new Thread()
      {
         @Override
         public void run() {
            collector.clear();
            model.clear();
      
            for(File f : files)
               UI.this.run(f);
            
            collector.fill(model);
            
            String base = modelProcessor.asUrlString(model);

            String avg = "?";
            
            if(!Double.isInfinite(model.getAverage()) && !Double.isNaN(model.getAverage())) {
               BigDecimal bd = new BigDecimal(model.getAverage());
               bd = bd.multiply(new BigDecimal(10));
               bd = new BigDecimal(bd.toBigInteger());
               bd = bd.divide(new BigDecimal(10));
               avg = bd.toString();
            }
            
            String ndp = "" + model.getNumDataPoints();
            if(model.getNumDataPoints() > 1000)
               ndp = Math.round(model.getNumDataPoints() / 1000) + "K";
            
            String title = "N:" + ndp + "; AVG:" + avg;
            try {
               title = "&chtt=" + URLEncoder.encode(title, "utf-8") + "&chg=25";
               System.out.println("title=" + title);
            } catch (UnsupportedEncodingException e) {
               //
            }

            final String url = base + title;
            
            SwingUtilities.invokeLater(new Runnable() {
               
               @Override
               public void run() {
                  output.setResult(url);
                  setCursor(Cursor.getDefaultCursor());
               }
            });               
         }
      };
      
      th.start();
   }

   protected void run(File selectedFile) {
      String s = selectedFile.getPath();
      provider.setRoot(s);
      for(String f : provider.paths()) 
         provider.process(f);
   }
   
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         
         @Override
         public void run() { 
            
            try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
            }
            
            UI ui = new UI(new PngOutput(), new Collector());
            ui.setProvider(new FileInputProvider(ui.collector));
            
            final int parts = 10;
            
            Charter ch = new Charter() {

               @Override
               public String asUrlString(Model model) {
                  model.normalize(parts);
                  return super.asUrlString(model);
               }               
            };
                        
            ch.setWidth(800);
            ch.setHeight(350);
            ch.setCharType("bvg");
//            ch.setCharType("p");
            ch.setShowKeys(false);
            ch.setPercentage(false);            
            ch.set("chbh", "a");
            ch.set("chxt", "x,y");
            ch.set("chds", "0,100");
            ch.set("chxr", "0,0," + (parts-1) + "|1,0,100");
            
            ui.setModelProcessor(ch);
            
         }
      });      
   }   
   
   
   public void setProvider(InputProvider p) {
      provider = p;
   }

   public void setModelProcessor(ModelProcessor mp) {
      this.modelProcessor = mp;
   }
}
