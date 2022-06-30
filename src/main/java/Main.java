import javax.swing.*;

public class Main extends JFrame {


    public static void main(String[] args) {
        Main myWindow = new Main();
    }

    public Main() {
        this.init();
        MainPanel mainPanel = new MainPanel(Constants.ZERO, Constants.ZERO, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.add(mainPanel);
    }

    private void init() {
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
    }


}
