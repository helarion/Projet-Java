package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

public abstract class FiltreFactory {
    private Color couleur;
    private WritableImage writable;
    PixelWriter writer;

    String nom;

    public FiltreFactory() {}

    public FiltreFactory(Image image)
    {
        writable = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        writer=writable.getPixelWriter();
    }

    public abstract WritableImage setFilter(int hauteur, int largeur, ImageView imageView, Image image);

    public Color getColor() {return couleur;}

    public void setColor(Color c) {couleur=c;}

    public PixelWriter getWriter() {return writer;}

    public void getWritable(WritableImage i) {writable=i;}

    public WritableImage getWritable() {return writable;}

    public void setNom(String nom) {this.nom=nom;}

    public String getNom() {return this.nom;}
}
