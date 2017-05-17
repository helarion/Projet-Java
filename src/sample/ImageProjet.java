package sample;

import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ImageProjet {


    private String image;
    private int rotation;
    private String filtre;
    private ArrayList<String> listeTag;

    public ImageProjet()
    {
        image="";
        filtre="";
        listeTag=new ArrayList<String>();
    }


    public void setFiltre(String filtre)
    {
        this.filtre=filtre;
    }

    public void addTag(String tag)
    {
        this.listeTag.add(tag);
    }

    public void setImage(String image)
    {
        this.image=image;
    }

    public void setRotation(int rotation) {this.rotation=rotation;}

    @XmlElement
    public String getFiltre() { return this.filtre;}

    @XmlElement
    public int getRotation() { return this.rotation;}

    @XmlElement(name="Tag")
    public ArrayList<String> getListeTag()
    {
        return this.listeTag;
    }

    @XmlElement
    public String getImage() {return this.image;}
}
