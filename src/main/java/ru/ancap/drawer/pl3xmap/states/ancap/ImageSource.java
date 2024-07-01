package ru.ancap.drawer.pl3xmap.states.ancap;

import lombok.SneakyThrows;
import net.pl3x.map.core.image.IconImage;
import ru.ancap.framework.resource.ResourcePreparator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

public class ImageSource implements ResourcePreparator<BufferedImage> {
    
    @Override
    @SneakyThrows
    public BufferedImage prepare(InputStream inputStream, File file) {
        return ImageIO.read(inputStream);
    }
    
}