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
    private Image source;
    private ArrayList<FiltreFactory> listeFiltre;
    private ArrayList<String> listeTag;

    public ImageProjet()
    {
        source=null;
        listeFiltre=new ArrayList<FiltreFactory>();
        listeTag=new ArrayList<String>();
    }

    @XmlElement
    public void addFiltre(FiltreFactory filtre)
    {
        this.listeFiltre.add(filtre);
    }

    @XmlElement
    public void addTag(String tag)
    {
        this.listeTag.add(tag);
    }

    @XmlElement
    public void setImage(Image source)
    {
        this.source=source;
    }

    public ArrayList<String> getListeTag()
    {
        return listeTag;
    }

    public void toXml()
    {
        try {
            File file = new File("file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(FiltreNoirEtBlancFactory.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(this, file);
            jaxbMarshaller.marshal(this, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
