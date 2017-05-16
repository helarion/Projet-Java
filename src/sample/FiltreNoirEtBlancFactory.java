package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FiltreNoirEtBlancFactory extends FiltreFactory {

    public FiltreNoirEtBlancFactory(int hauteur,int largeur)
    {
        super(hauteur,largeur);
    }

    public WritableImage setFilter(int hauteur, int largeur, ImageView imageView, Image image)
    {

        for(int i=0;i<hauteur;i++) {
            for (int j = 0; j <largeur; j++) {
                setColor(image.getPixelReader().getColor(i,j));
                double gris=(getColor().getBlue()+getColor().getGreen()+getColor().getRed())/3;
                setColor(Color.color(gris,gris,gris));
                writer.setColor(i,j,getColor());
            }
        }
        imageView.setImage(getWritable());
        imageView.setFitHeight(hauteur);
        imageView.setFitWidth(largeur);
        return getWritable();
    }

}
