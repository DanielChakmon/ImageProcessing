import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainPanel extends JPanel {
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
    private JButton flipHorizontallyButton;
    private BufferedImage originalImage;
    private BufferedImage changeableImage;


    public MainPanel(int x, int y, int width, int height) {
        this.setBounds(x, y, width, height);
        this.setLayout(null);
        sendWindowData();
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\danie\\OneDrive\\Desktop\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:\\Users\\danie\\AppData\\Local\\Google\\Chrome\\User Data\\Default");
        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().window().minimize();
        driver.manage().timeouts().implicitlyWait(Constants.TWO, TimeUnit.SECONDS);
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
        flipHorizontallyButton = new JButton("Flip horizontally");
        flipHorizontallyButton.setBounds(lighterButton.getX(), lighterButton.getY() + Constants.SPACE_BETWEEN_LINES + lighterButton.getHeight(), lighterButton.getWidth(), lighterButton.getHeight());
        this.add(flipHorizontallyButton);
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
            Boolean inProfilePictureTab = false;
            boolean toContinue = true;
            if (searchFiled.getText().equals("") || searchFiled.getText() == null) {
                warningLabelChanger("~You must enter a search query... ");
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
                        inProfilePictureTab = getProfilePictureTab(driver);
                        break;
                    }
                }
                if (inProfilePictureTab) {
                    retrieveProfileImage(driver);
                }
            }
        });
        Filters filters = new Filters();
        grayScaleButton.addActionListener(e -> {
            sendImageToFilters(changeableImage, Constants.GREY_SCALE, filters);
        });
        mirrorButton.addActionListener(e -> {
            sendImageToFilters(changeableImage, Constants.MIRROR, filters);
        });
        colorShiftRightButton.addActionListener(e -> {
            sendImageToFilters(changeableImage, Constants.COLOR_SHIFT_RIGHT, filters);
        });
        negativeButton.addActionListener(e -> {
            sendImageToFilters(changeableImage, Constants.NEGATIVE, filters);
        });
        sepiaButton.addActionListener(e -> {
            sendImageToFilters(changeableImage, Constants.SEPIA, filters);
        });
        lighterButton.addActionListener(e -> {
            sendImageToFilters(changeableImage, Constants.LIGHTER, filters);
        });
        flipHorizontallyButton.addActionListener(e -> {
            sendImageToFilters(changeableImage, Constants.FLIP_HORIZONTALLY, filters);
        });
    }

    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (originalImage != null) {
            ImageIcon originalImageIcon = new ImageIcon(originalImage);
            ImageIcon changeableImageIcon = new ImageIcon(changeableImage);
            changeableImageIcon.paintIcon(this, graphics, Constants.WINDOW_WIDTH - changeableImage.getWidth() - Constants.MARGIN_SIZE_FROM_SIDE * Constants.THREE, Constants.MARGIN_FROM_TOP);
            originalImageIcon.paintIcon(this, graphics, Constants.MARGIN_SIZE_FROM_SIDE, Constants.MARGIN_FROM_TOP);
        }
    }

    public void sendImageToFilters(BufferedImage image, int filterIndex, Filters filters) {
        if (changeableImage != null) {
            if (filters.isTheSamePic(image)) {
            } else {
                filters.setImage(image);
            }
            filters.setFilterIndex(filterIndex);
        }
    }

    public void retrieveProfileImage(ChromeDriver driver) {
        try {
            driver.get(driver.findElement(By.linkText("View full size")).getAttribute("href"));
            originalImage = saveAndScale(driver);
            changeableImage = saveAndScale(driver);

            this.remove(noImageLabel);

            repaint();

        } catch (NoSuchElementException exception) {

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage saveAndScale(ChromeDriver driver) throws IOException, MalformedURLException {
        driver.manage().timeouts().implicitlyWait(Constants.TWO, TimeUnit.SECONDS);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        BufferedImage image;
        while (true) {
            if (js.executeScript("return document.readyState").toString().equals("complete")) {
                URL imageSource = new URL(driver.getCurrentUrl());
                // URL imageSource = new URL(driver.findElement(By.xpath("/html/body/img")).getAttribute("src"));
                //System.out.println(imageSource);
                image = ImageIO.read(imageSource);
                image = imageScaleWithRatio(image);
                break;
            }
        }
        return image;
    }

    public void warningLabelChanger(String text) {
        searchWarningsLabel.setText(text);
        this.add(searchWarningsLabel);
        repaint();
    }

    public boolean getProfilePictureTab(ChromeDriver driver) {
        boolean inProfilePictureTab = false;
        boolean dontEvenTry = false;
        WebElement usersAncestor = null;
        WebElement placeAncestor = null;
        WebElement pageAncestor = null;
        WebElement keyWordAncestor = null;
        try {
            usersAncestor = driver.findElement(By.cssSelector("div[data-module-result-type='user']"));
        } catch (NoSuchElementException e) {

        }
        try {
            placeAncestor = driver.findElement(By.cssSelector("div[data-module-result-type='place']"));
        } catch (NoSuchElementException e) {

        }
        try {
            pageAncestor = driver.findElement(By.cssSelector("div[data-module-result-type='page']"));
        } catch (NoSuchElementException e) {

        }
        try {
            keyWordAncestor = driver.findElement(By.cssSelector("div[data-module-result-type='keyword_entities']"));
        } catch (NoSuchElementException e) {

        }
        List<WebElement> profiles = new ArrayList<>();
        try {
            if (usersAncestor != null) {
                profiles.addAll(usersAncestor.findElements(By.xpath(".//*[@class='_9_7 _2rgt _1j-g _2rgt _86-3 _2rgt _1j-g _2rgt']")));
                profiles.addAll(usersAncestor.findElements(By.xpath(".//*[@class='_a5o _9_7 _2rgt _1j-g _2rgt _86-3 _2rgt _1j-g _2rgt']")));
            }
            if (placeAncestor != null) {
                profiles.addAll(placeAncestor.findElements(By.xpath(".//*[@class='_9_7 _2rgt _1j-g _2rgt _86-3 _2rgt _1j-g _2rgt']")));
                profiles.addAll(placeAncestor.findElements(By.xpath(".//*[@class='_a5o _9_7 _2rgt _1j-g _2rgt _86-3 _2rgt _1j-g _2rgt']")));
            }
            if (pageAncestor != null) {
                profiles.addAll(pageAncestor.findElements(By.xpath(".//*[@class='_a5o _9_7 _2rgt _1j-g _2rgt _86-3 _2rgt _1j-g _2rgt']")));
                profiles.addAll(pageAncestor.findElements(By.xpath(".//*[@class='_a5o _9_7 _2rgt _1j-g _2rgt _86-3 _2rgt _1j-g _2rgt']")));
            }
            if (keyWordAncestor != null) {
                profiles.addAll(keyWordAncestor.findElements(By.xpath(".//*[@class='_9_7 _2rgt _1j-g _2rgt _86-3 _2rgt _1j-g _2rgt _3zi4 _2rgt _1j-f _2rgt']")));
            }
            //   System.out.println(profiles.isEmpty());
        } catch (NoSuchElementException exception) {

        } catch (NullPointerException exceptionTwo) {
            warningLabelChanger("No results:( try something else...");
            dontEvenTry = true;
        }
        if (profiles.isEmpty()) {
            warningLabelChanger("No results:( try something else...");
            dontEvenTry = true;
        }
        if (!dontEvenTry) {
            Iterator<WebElement> iterator = profiles.iterator();
            iterator.next().click();
            Boolean inProfile = false;
            WebElement profilePictureAncestor = null;
            driver.manage().timeouts().implicitlyWait(Constants.TWO, TimeUnit.SECONDS);
            try {
                profilePictureAncestor = driver.findElement(By.cssSelector("div[class='_52jj _42b3']"));
            } catch (NoSuchElementException e) {

            }
            Boolean toBreak = false;
            while (true) {
                if (!inProfile) {
                    try {
                        //  if (driver.findElement(By.xpath(".//*[@class='_39pi _1mh-']")).isEnabled()) {
                        if (driver.findElement((By.id("m-timeline-cover-section"))).isEnabled()) {
                            inProfile = true;
                        } else {
                            if (driver.findElement((By.id("page"))).isEnabled()) {
                                inProfile = true;
                            } else {
                                continue;
                            }
                        }

                    } catch (NoSuchElementException exception) {
                        try {
                            if (driver.findElement((By.id("page"))).isEnabled()) {
                                inProfile = true;
                            } else {
                                continue;
                            }
                        } catch (NoSuchElementException e) {
                            continue;
                        }
                        continue;
                    }

                } else {
                    try {
                        if (!driver.findElements(By.xpath(".//*[@class='_39pi _1mh-']")).isEmpty()) {
                            if (profilePictureAncestor != null) {
                                try {
                                    profilePictureAncestor.findElement(By.xpath(".//*[@class='_39pi _1mh-']")).click();
                                } catch (NoSuchElementException exception) {
                                    driver.findElement(By.xpath(".//*[@class='_39pi _1mh-']")).click();
                                }
                            } else {
                                try {
                                    driver.findElement(By.xpath(".//*[@class='_k7v _2rgt _1j-g _2rgt _83jk img']")).click();
                                } catch (NoSuchElementException e) {
                                    driver.findElement(By.xpath(".//*[@class='_39pi _1mh-']")).click();
                                }
                            }
                        } else {
                            warningLabelChanger("Profile picture isn't clickable, try something else");
                            break;
                        }
                    } catch (NoSuchElementException exception) {
                        warningLabelChanger("Profile picture isn't clickable, try something else");
                        break;
                    } catch (NullPointerException exceptionTwo) {
                        warningLabelChanger("Profile picture isn't clickable, try something else");
                        break;
                    }
                    while (true) {
                        //  int counter = Constants.ZERO; //fix

                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        if (js.executeScript("return document.readyState").toString().equals("complete")) {
                            try {
                                if (driver.findElement(By.xpath("/html/body/div[1]/div/div[4]/div/div/div/div/div[1]/div")).isEnabled()) {
                                    inProfilePictureTab = true;
                                    toBreak = true;
                                    break;
                                }
                            } catch (NoSuchElementException exception) {
                                //    if (counter > Constants.ZERO) {
                                warningLabelChanger("Profile picture isn't clickable, try something else");
                                toBreak = true;
                                break;
//                                } else {
//                                    counter++;
//                                }

                            } catch (NullPointerException exceptionTwo) {
                                warningLabelChanger("Profile picture isn't clickable, try something else");
                                toBreak = true;
                                break;
                            }
                        }
                        try {
                            Thread.sleep(Constants.SLEEP_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (toBreak) {
                        break;
                    }
                }
            }
        }
        return inProfilePictureTab;
    }

    public BufferedImage imageScaleWithRatio(BufferedImage image) {
        double imageRatio = ((double) image.getWidth()) / ((double) image.getHeight());
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

    public void sendWindowData() {
        Constants.getWindowData(this);
    }

}

