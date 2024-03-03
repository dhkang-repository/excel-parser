package org.example;

import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;

@Component
public class LocalFileTransfer {
    public void save(String pathString, String name, String data) throws Exception {
        Path path = Path.of(pathString, name + ".json").toAbsolutePath();
        FileChannel channel = FileChannel.open(path, Set.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));
        Charset charset = Charset.defaultCharset();
        ByteBuffer byteBuffer = charset.encode(data);
        channel.write(byteBuffer);
    }
}
