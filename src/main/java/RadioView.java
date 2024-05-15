import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

/**
 * <h1>TestingView</h1>
 * Handles the GUI.
 * <p>
 * mainMenu - Main menu.
 * <p>
 * menu - Menu for the two menu items.
 * <p>
 * about - Item that summons the about screen.
 * <p>
 * update - Items that updates the table.
 * <p>
 * topWrapper - Wrapper panel that contains the combo box.
 * <p>
 * comboBox - Combo box for the channels
 * <p>
 * leftWrapper - Wrapper panel that contains the text area and the image.
 * <p>
 * info - Text area for program descriptions.
 * <p>
 * picLabel - Space for channel or program image.
 * <p>
 * rightWrapper - Wrapper panel that contains the table.
 * <p>
 * model - The model of the table.
 * <p>
 * table - The program table.
 * <p>
 * markedRow - The latest row whose program is expired.
 * <p>
 * defaultImage - The default image used if there aren't any other.
 *
 * @author  Victor Gustafsson, dv16vgn
 * @version 1.0
 * @since   2017-12-03
 */

public class RadioView extends JFrame {

    private JMenuBar mainMenu = new JMenuBar();
    private JMenu menu = new JMenu("Menu");
    private JMenuItem about = new JMenuItem("About");
    private JMenuItem update = new JMenuItem("Update");

    private JPanel topWrapper = new JPanel(
            new FlowLayout(0, 0, FlowLayout.LEADING));
    private JComboBox comboBox = new JComboBox();

    private JPanel leftWrapper = new JPanel();
    private JTextArea info = new JTextArea();
    private JLabel picLabel;

    private JPanel rightWrapper = new JPanel();
    private DefaultTableModel model;
    private JTable table = new JTable();

    private int markedRow;
    private ImageIcon defaultImage = new ImageIcon (
            "src/main/resources/default.png");

    RadioView(){

        /*Window setup*/

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("RadioInfo");
        this.getContentPane().setBackground(Color.white);
        setMinimumSize(new Dimension(1080, 720));

        /*Menu setup*/

        mainMenu.add(menu);
        menu.add(about);
        menu.add(update);

        /*Combo box for channels setup*/

        topWrapper.add(Box.createHorizontalStrut(480));
        comboBox.setPreferredSize(new Dimension(210, 25));
        JScrollPane comboPane = new JScrollPane(comboBox);
        topWrapper.add(comboPane);

        /*Text area for descriptions setup.*/

        leftWrapper.setLayout(new GridLayout(0, 1, 0, 10));
        leftWrapper.setPreferredSize(new Dimension(300, 620));
        info.setPreferredSize(new Dimension(300, 310));
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setFont(info.getFont().deriveFont(16f));
        leftWrapper.add(info);

        /*Image area setup.*/

        ImageIcon image = new ImageIcon("src/main/resources/default.png");
        Image image2 = image.getImage();
        Image image3 = image2.getScaledInstance(300, 310, Image.SCALE_SMOOTH);
        image = new ImageIcon(image3);
        picLabel = new JLabel(image);
        leftWrapper.add(picLabel);

        /*Program table setup.*/

        model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Program");
        model.addColumn("Start");
        model.addColumn("End");
        table.setModel(model);
        table.setFont(new Font("Serif", Font.PLAIN, 14));
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(500, 620));
        rightWrapper.setPreferredSize(new Dimension(800, 620));
        rightWrapper.add(pane);

        /*Add all the elements together*/

        this.setJMenuBar(mainMenu);
        this.add(topWrapper, BorderLayout.PAGE_START);
        this.add(leftWrapper, BorderLayout.LINE_START);
        this.add(rightWrapper, BorderLayout.LINE_END);
    }

    /**
     * Colours rows whose programs are expired.
     */

    class CustomRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent
                (JTable table, Object value, boolean isSelected,
                 boolean hasFocus, int row, int column){
            Component cellComponent = super.getTableCellRendererComponent
                    (table, value, isSelected, hasFocus, row, column);

            if(row <= markedRow){
                cellComponent.setBackground(Color.GRAY);
            } else {
                cellComponent.setBackground(Color.WHITE);
            }
            return cellComponent;
        }
    }

    /**
     * Adds MouseListener to the table.
     * @param ma The new MouseAdapter
     */

    public void addMouseListenerToTable(MouseAdapter ma){
        this.table.addMouseListener(ma);
    }

    /**
     * Adds ClickListener to MenuItem.
     * @param itemNumber The number of the item that'll get the Listener.
     * @param al The ActionListener that'll act as a ClickListener.
     */

    public void addClickListenerToMenuItem(int itemNumber, ActionListener al){
        this.menu.getItem(itemNumber).addActionListener(al);
    }

    /**
     * Adds ItemListener to the combo box.
     * @param il The ItemListener to add.
     */

    public void addComboBoxItemListener(ItemListener il){
        this.comboBox.addItemListener(il);
    }

    /**
     * Changes the text of the text area.
     * @param newText The new text.
     */

    public void changeTextAreaText(String newText){
        this.info.setText(newText);
    }

    /**
     * Changes the displayed image. Defaults to the default image if the
     * provided image is null.
     * @param newImage A new image to change too.
     */

    public void changeImage(ImageIcon newImage) {
        ImageIcon image;
        if(newImage == null){
            newImage =  defaultImage;
        }
        Image image2 = newImage.getImage();
        image2 = image2.getScaledInstance(300, 310, Image.SCALE_SMOOTH);
        image = new ImageIcon(image2);
        picLabel = new JLabel(image);
        leftWrapper.remove(1);
        leftWrapper.add(picLabel);

        this.setVisible(true);
    }

    /**
     * Updates the content of the combo box to names of the provided
     * RadioChannels.
     * @param list A list of RadioChannels whose name are to be added.
     */

    public void updateComboBoxContent(ArrayList<RadioChannel> list){
        if (comboBox.getItemCount() > 0){
            comboBox.removeAllItems();
        }

        comboBox.addItem("");
        for(RadioChannel rc: list){
            comboBox.addItem(rc.getName());
        }

        if(comboBox.getItemCount()>10){
            comboBox.setMaximumRowCount(10);
        }else {
            comboBox.setMaximumRowCount(comboBox.getItemCount());
        }
    }

    /**
     * Updates the table with the provided RadioPrograms.
     * @param list A list of RadioPrograms toadd to the table.
     */

    public void updateProgramTable(ArrayList<RadioProgram> list){
        model.setRowCount(0);
        if(list!=null){
            for (RadioProgram rp : list){
                model.addRow(new Object[]{rp.getName(),
                        rp.getFormattedStartTime(), rp.getFormattedEndTime()});
            }
        }

        table.setModel(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(0).setCellRenderer(
                new CustomRenderer());

    }

    /**
     * Setter for the marked row.
     * @param markedRow The number of the marked row.
     */

    public void setMarkedRow(int markedRow) {
        this.markedRow = markedRow;
    }

    /**
     * Getter for the table.
     * @return The table.
     */

    public JTable getTable() {
        return table;
    }

    /**
     * Getter for the Combo box.
     * @return The combo box.
     */

    public JComboBox getComboBox() {
        return comboBox;
    }
}
