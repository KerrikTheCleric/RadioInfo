
public class Main {

    public static void main(String[] args){
        final RadioModel model = new RadioModel();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                RadioView view = new RadioView();
                setUpProgram(view, model);
            }
        });
    }

    private static void setUpProgram(RadioView view, RadioModel model){
        new RadioController(view, model);
        view.pack();
        view.setSize(1080, 720);
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }
}
