import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_CUSTOM;

public class Filters {
    private int filterIndex;
    private BufferedImage image;


    public Filters(int filterIndex, BufferedImage image) {
        this.filterIndex = filterIndex;
        this.image = image;
        filterApplier();
    }
    public Filters(){
    }
    public boolean isTheSamePic(BufferedImage image){
        boolean ans=false;
        if (this.image!=null){
            if (this.image.equals(image)){
                ans=true;
            }
        }
        return ans;
    }
    public void setFilterIndex(int filterIndex) {
        this.filterIndex = filterIndex;
        filterApplier();
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    private void filterApplier() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++){
                switch (filterIndex){
                    case Constants.GREY_SCALE:{
                        greyScale(i,j);
                        break;
                    }
                    case Constants.MIRROR:{
                        mirror(i,j);
                        break;
                    }
                    case Constants.COLOR_SHIFT_RIGHT:{
                        colorShiftRight(i,j);
                        break;
                    }
                    case Constants.NEGATIVE:{
                        negative(i,j);
                        break;
                    }
                    case Constants.SEPIA:{
                        sepia(i,j);
                        break;
                    }
                    case Constants.LIGHTER:{
                        lighter(i,j);
                        break;
                    }
                    default:{
                        System.out.println("Invalid filter");
                        break;
                    }
                }
            }
        }
        Constants.myWindow.repaint();
    }

    private void greyScale(int x, int y) {
        Color pixelColor = new Color(image.getRGB(x, y));
        int greyScaledColorRatio = (pixelColor.getBlue() + pixelColor.getRed() + pixelColor.getGreen()) / Constants.THREE;
        Color greyScaledPixelColor = new Color(greyScaledColorRatio, greyScaledColorRatio, greyScaledColorRatio);
        image.setRGB(x, y, greyScaledPixelColor.getRGB());
    }

    private void mirror(int x, int y) {
        image.setRGB(image.getWidth() - x-Constants.ONE, y, image.getRGB(x, y));
    }

    private void colorShiftRight(int x, int y) {
        Color pixelColor = new Color(image.getRGB(x, y));
        int newRed = pixelColor.getBlue();
        int newGreen = pixelColor.getRed();
        int newBlue = pixelColor.getGreen();
        Color rightShiftedColor = new Color(newRed, newGreen, newBlue);
        image.setRGB(x, y, rightShiftedColor.getRGB());
    }

    private void negative(int x, int y) {
        Color pixelColor = new Color(image.getRGB(x, y));
        int newRed = Constants.SOLID_COLOR_MAX_RGB_INDEX - pixelColor.getRed();
        int newGreen = Constants.SOLID_COLOR_MAX_RGB_INDEX - pixelColor.getGreen();
        int newBlue = Constants.SOLID_COLOR_MAX_RGB_INDEX - pixelColor.getBlue();
        Color negativeColor = new Color(newRed, newGreen, newBlue);
        image.setRGB(x, y, negativeColor.getRGB());
    }

    private void sepia(int x, int y) {
        Color pixelColor = new Color(image.getRGB(x, y));
        int[] newRGB = new int[Constants.THREE];
        final int newRed = Constants.ZERO;
        final int newGreen = Constants.ONE;
        final int newBlue = Constants.TWO;
        newRGB[newRed] = (int) (0.393 * pixelColor.getRed() + 0.769 * pixelColor.getGreen() + 0.189 * pixelColor.getBlue());
        newRGB[newGreen] = (int) (0.349 * pixelColor.getRed() + 0.686 * pixelColor.getGreen() + 0.168 * pixelColor.getBlue());
        newRGB[newBlue] = (int) (0.272 * pixelColor.getRed() + 0.534 * pixelColor.getGreen() + 0.131 * pixelColor.getBlue());
        for (int i = 0; i < Constants.THREE; i++) {
            if (newRGB[i] > Constants.SOLID_COLOR_MAX_RGB_INDEX) {
                newRGB[i] = Constants.SOLID_COLOR_MAX_RGB_INDEX;
            }
        }
        Color sepiaColor = new Color(newRGB[newRed], newRGB[newGreen], newRGB[newBlue]);
        image.setRGB(x, y, sepiaColor.getRGB());
    }

    private void lighter(int x, int y) {
        Color pixelColor = new Color(image.getRGB(x, y));
        int[] newRGB = new int[Constants.THREE];
        final int newRed = Constants.ZERO;
        final int newGreen = Constants.ONE;
        final int newBlue = Constants.TWO;
        newRGB[newRed] = (int) (pixelColor.getRed() + ((double) (Constants.SOLID_COLOR_MAX_RGB_INDEX - pixelColor.getRed())) * Constants.TENTH);
        newRGB[newGreen] = (int) (pixelColor.getGreen() + ((double) (Constants.SOLID_COLOR_MAX_RGB_INDEX - pixelColor.getGreen())) * Constants.TENTH);
        newRGB[newBlue] = (int) (pixelColor.getBlue() + ((double) (Constants.SOLID_COLOR_MAX_RGB_INDEX - pixelColor.getBlue())) * Constants.TENTH);
        Color lighterColor = new Color(newRGB[newRed], newRGB[newGreen], newRGB[newBlue]);
        image.setRGB(x, y, lighterColor.getRGB());
    }
}
