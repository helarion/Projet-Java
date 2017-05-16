package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * Created by Axel on 14/05/2017.
 */
public class FiltreSepiaFactory extends FiltreFactory {

    public FiltreSepiaFactory(int hauteur,int largeur)
    {
        super(hauteur,largeur);
    }

    public WritableImage setFilter(int hauteur, int largeur, ImageView imageView, Image image)
    {
        for(int i=0;i<hauteur;i++) {
            for (int j = 0; j <largeur; j++) {
                setColor(image.getPixelReader().getColor(i,j));
                System.out.println("rouge :"+getColor().getRed()+"vert :"+getColor().getGreen()+"bleu :"+getColor().getBlue());
                double sepiaR=(getColor().getRed()*0.393) + (getColor().getGreen()*0.769) + (getColor().getBlue()*0.189);
                double sepiaG=(getColor().getRed()*0.349) + (getColor().getGreen()*0.686) + (getColor().getBlue()*0.168);
                double sepiaB=(getColor().getRed()*0.272) + (getColor().getGreen()*0.534) + (getColor().getBlue()*0.131);
                if(sepiaR>1){
                    sepiaR=1;
                }
                if(sepiaG>1){
                    sepiaG=1;
                }
                if(sepiaB>1){
                    sepiaB=1;
                }
                setColor(Color.color(sepiaR,sepiaG,sepiaB));
                writer.setColor(i,j,getColor());
            }
        }
        imageView.setImage(getWritable());
        imageView.setFitHeight(hauteur);
        imageView.setFitWidth(largeur);
        return getWritable();
    }
}
