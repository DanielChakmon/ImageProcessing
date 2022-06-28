import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Main extends JFrame {
    private JTextField searchFiled;
    private JLabel title;
    private JLabel searchWarningsLabel;
    private JLabel noImageLabel;
    private JButton searchButton;
    private JButton grayScaleButton;
    private JButton mirrorButton;
    private JButton colorShiftRightButton;
    private JButton negativeButton;
    private JButton sepiaButton;
    private JButton lighterButton;
    private BufferedImage originalImage;
    private BufferedImage changeableImage;

    public static void main(String[] args) {
        Main myWindow = new Main();
    }

    public Main() {
        this.init();
        sendWindowData();
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\danie\\OneDrive\\Desktop\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:\\Users\\danie\\AppData\\Local\\Google\\Chrome\\User Data\\Default");
        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().window().minimize();
        title = new JLabel("Image processor by DGSTAR");
        title.setBounds(Constants.WINDOW_WIDTH / Constants.TWO - Constants.THREE * Constants.LABEL_WIDTH / Constants.FOUR, Constants.MARGIN_FROM_TOP, Constants.THREE * Constants.LABEL_WIDTH / Constants.TWO, Constants.LABEL_HEIGHT);
        this.add(title);
        searchFiled = new JTextField();
        searchFiled.setBounds(Constants.WINDOW_WIDTH / Constants.TWO - Constants.TEXT_FIELD_WIDTH / Constants.TWO - Constants.BUTTON_WIDTH / Constants.TWO, title.getY() + Constants.SPACE_BETWEEN_LINES + title.getHeight(), Constants.TEXT_FIELD_WIDTH, Constants.TEXT_FIELD_HEIGHT);
        this.add(searchFiled);
        searchButton = new JButton("Search!");
        searchButton.setBounds(searchFiled.getX() + Constants.ONE + searchFiled.getWidth(), searchFiled.getY(), Constants.BUTTON_WIDTH - Constants.ONE, Constants.BUTTON_HEIGHT);
        this.add(searchButton);
        grayScaleButton = new JButton("Grey Scale");
        grayScaleButton.setBounds(searchFiled.getX(), searchFiled.getY() + Constants.SPACE_BETWEEN_LINES + searchFiled.getHeight(), searchButton.getWidth() + searchFiled.getWidth(), Constants.BUTTON_HEIGHT);
        this.add(grayScaleButton);
        mirrorButton = new JButton("Mirror");
        mirrorButton.setBounds(grayScaleButton.getX(), grayScaleButton.getY() + Constants.SPACE_BETWEEN_LINES + grayScaleButton.getHeight(), grayScaleButton.getWidth(), grayScaleButton.getHeight());
        this.add(mirrorButton);
        colorShiftRightButton = new JButton("Color shift right");
        colorShiftRightButton.setBounds(mirrorButton.getX(), mirrorButton.getY() + Constants.SPACE_BETWEEN_LINES + mirrorButton.getHeight(), mirrorButton.getWidth(), mirrorButton.getHeight());
        this.add(colorShiftRightButton);
        negativeButton = new JButton("Negative");
        negativeButton.setBounds(colorShiftRightButton.getX(), colorShiftRightButton.getY() + Constants.SPACE_BETWEEN_LINES + colorShiftRightButton.getHeight(), colorShiftRightButton.getWidth(), colorShiftRightButton.getHeight());
        this.add(negativeButton);
        sepiaButton = new JButton("Sepia");
        sepiaButton.setBounds(negativeButton.getX(), negativeButton.getY() + Constants.SPACE_BETWEEN_LINES + negativeButton.getHeight(), negativeButton.getWidth(), negativeButton.getHeight());
        this.add(sepiaButton);
        lighterButton = new JButton("Lighter");
        lighterButton.setBounds(sepiaButton.getX(), sepiaButton.getY() + Constants.SPACE_BETWEEN_LINES + sepiaButton.getHeight(), sepiaButton.getWidth(), sepiaButton.getHeight());
        this.add(lighterButton);
        noImageLabel = new JLabel("<html>There's no image and therefore <BR>" +
                "the buttons won't work <BR>" +
                "use the search button <BR>" +
                "to find an image. </html>");
        noImageLabel.setBounds(Constants.WINDOW_WIDTH / Constants.FOUR - Constants.LABEL_WIDTH, Constants.WINDOW_HEIGHT / Constants.TWO - Constants.LABEL_HEIGHT * Constants.FOUR - Constants.SPACE_BETWEEN_LINES, Constants.TWO * Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT * Constants.FOUR + Constants.SPACE_BETWEEN_LINES);
        Font myFont = new Font("default", Font.PLAIN, Constants.BIG_TEXT_SIZE);
        noImageLabel.setFont(myFont);
        this.add(noImageLabel);
        searchWarningsLabel = new JLabel("~You must enter a search query... ");
        searchWarningsLabel.setBounds(searchButton.getX() + searchButton.getWidth() + Constants.ONE, searchButton.getY(), Constants.THREE * Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
        repaint();
        searchButton.addActionListener(e -> {
            boolean toContinue = true;
            if (searchFiled.getText().equals("") || searchFiled.getText() == null) {
                searchWarningsLabel.setText("~You must enter a search query... ");
                this.add(searchWarningsLabel);
                repaint();
                toContinue = false;
            } else {
                searchWarningsLabel.setText("");
                repaint();
            }
            if (toContinue) {
                driver.get("https://m.facebook.com/search/top/?q=" + searchFiled.getText());
                boolean loggedIn = false;
                while (true) {
                    if (!loggedIn) {
                        try {

                            if (driver.findElement(By.partialLinkText("Facebook Search")).isEnabled()) {
                                loggedIn = true;
                            } else {
                                continue;
                            }

                        } catch (NoSuchElementException exception) {
                            continue;
                        }
                    } else {
                        WebElement usersAncestor = driver.findElement(By.cssSelector("div[data-module-result-type='user']"));
                        List<WebElement> profiles = null;
                        try {
                            profiles.addAll(usersAncestor.findElements(By.xpath(".//*[@class='_k7v _2rgt _1j-f _2rgt _3zi4 _2rgt _1j-f _2rgt img']")));
                            System.out.println(profiles.isEmpty());
                        } catch (NoSuchElementException exception) {

                        } catch (NullPointerException exception2) {
                            searchWarningsLabel.setText("No results:( try something else...");
                            this.add(searchWarningsLabel);
                            repaint();
                            break;
                        }
                        if (profiles.isEmpty()){
                            searchWarningsLabel.setText("No results:( try something else...");
                            this.add(searchWarningsLabel);
                            repaint();
                            break;
                        }
                        profiles.get(Constants.ZERO).click();
                        Boolean inProfile = false;
                        WebElement profileCoverArea=driver.findElement(By.id("m-timeline-cover-section"));
                        while (true) {
                            if (!inProfile) {
                                try {

                                    if (profileCoverArea.findElement(By.xpath(".//i[@class='_img _1-yc profpic']")).isEnabled()) {
                                        inProfile = true;
                                    } else {
                                        continue;
                                    }

                                } catch (NoSuchElementException exception) {
                                    continue;
                                }

                            } else {
                                profileCoverArea.findElement(By.xpath(".//i[@class='_img _1-yc profpic']")).click();

                            }
                        }
                    }


                }
            }
        });


    }

    public BufferedImage imageScaleWithRatio(BufferedImage image) {
        double imageRatio = image.getWidth() / image.getHeight();
        double newWidth = Constants.ZERO;
        double newHeight = Constants.ZERO;
        if (image.getWidth() > image.getHeight()) {
            newWidth = Constants.IMAGE_MAX_WIDTH;
            //image ratio=new width/new height ---> new height= new width / image ratio.
            newHeight = newWidth / imageRatio;
        } else {
            newHeight = Constants.IMAGE_MAX_HEIGHT;
            //image ratio=new width/new height ---> new height * image ratio= new width.
            newWidth = newHeight * imageRatio;
        }
        BufferedImage scaledImage = new BufferedImage((int) newWidth, (int) newHeight, image.getType());
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.drawImage(image, Constants.ZERO, Constants.ZERO, (int) newWidth, (int) newHeight, null);
        graphics2D.dispose();
        return scaledImage;
    }

    private void init() {
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
    }

    public void sendWindowData() {
        Constants.getWindowData(this);
    }

}
