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
public class FiltreSobelFactory extends FiltreFactory {

    public FiltreSobelFactory(Image image)
    {
        super(image);
        setNom("Sobel");
    }

    public WritableImage setFilter(int hauteur, int largeur, ImageView imageView, Image image)
    {
        double x,y,g;

        for(int i=0;i<image.getWidth();i++) {
            for (int j = 0; j <image.getHeight(); j++) {
                setColor(image.getPixelReader().getColor(i,j));
                double gris=(getColor().getBlue()+getColor().getGreen()+getColor().getRed())/3;
                setColor(Color.color(gris,gris,gris));
                writer.setColor(i,j,getColor());
            }
        }

        for (int i = 1; i<image.getWidth()-2; i++)
        {
            for (int j = 1; j <image.getHeight()-2; j++)
            {
                setColor(image.getPixelReader().getColor(i,j));

                x=(image.getPixelReader().getColor(i,j+2).getGreen()+
                2*image.getPixelReader().getColor(i+1,j+2).getGreen()+
                image.getPixelReader().getColor(i+2,j+2).getGreen())-
                (image.getPixelReader().getColor(i,j).getGreen()+
                2*image.getPixelReader().getColor(i+1,j).getGreen()+
                image.getPixelReader().getColor(i+2,j).getGreen());

                //System.out.println(" x: "+x);

                y=(image.getPixelReader().getColor(i+2,j).getGreen()+
                2*image.getPixelReader().getColor(i+2,j+1).getGreen()+
                image.getPixelReader().getColor(i+2,j+2).getGreen())-
                (image.getPixelReader().getColor(i,j).getGreen()+
                2*image.getPixelReader().getColor(i,j+1).getGreen()+
                image.getPixelReader().getColor(i,j+2).getGreen());

                //System.out.println(" y: "+y);

                g=Math.abs(x)+Math.abs(y);

                //System.out.println(" g: "+g);

                if(g>1) g=1;

                setColor(Color.color(g,g,g));
                writer.setColor(i,j,getColor());

            }
        }

        imageView.setImage(getWritable());
        imageView.setFitHeight(hauteur);
        imageView.setFitWidth(largeur);
        return getWritable();

    }

}
