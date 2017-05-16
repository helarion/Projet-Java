package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class FiltreFactory {
    private Color couleur;
    private WritableImage writable;
    PixelWriter writer;

    public FiltreFactory(int hauteur, int largeur)
    {
        writable = new WritableImage(largeur,hauteur);
        writer=writable.getPixelWriter();
    }

    public abstract WritableImage setFilter(int hauteur, int largeur, ImageView imageView, Image image);

    public Color getColor() {return couleur;}

    public void setColor(Color c) {couleur=c;}

    public PixelWriter getWriter() {return writer;}

    public void getWritable(WritableImage i) {writable=i;}

    public WritableImage getWritable() {return writable;}
}
