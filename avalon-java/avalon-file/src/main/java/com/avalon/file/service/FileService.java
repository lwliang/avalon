/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.file.service;

import com.avalon.file.config.FileConfig;
import com.avalon.file.util.PathUtil;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.BCryptUtil;
import com.avalon.core.util.FileUtils;
import com.avalon.core.exception.FileIOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Service
@Slf4j
public class FileService extends AbstractService {
    @Autowired
    private FileConfig fileConfig;

    @Override
    public String getServiceName() {
        return "file.upload";
    }

    protected final Field originName = Fields.createString("源文件名");//包含后缀
    protected final Field url = Fields.createString("url");
    protected final Field mime = Fields.createString("web文件类型", 100);
    protected final Field ext = Fields.createString("文件扩展名", 20);
    protected final Field filePath = Fields.createString("文件存放路径");
    protected final Field thumbPath = Fields.createString("缩略图");

    @Override
    protected Field createNameField() {
        return Fields.createString("文件名", 256);
    }


    /**
     * 保存文件
     *
     * @param fileName
     * @param mime
     * @param content
     * @return
     */
    public RecordRow saveFile(String fileName, String mime, byte[] content) throws FileIOException {
        try {

            RecordRow recordRow = new RecordRow();
            recordRow.put("originName", fileName);
            recordRow.put("mime", mime);

            String token = BCryptUtil.simpleUUID();
            String first = PathUtil.getFirst(fileConfig.getMode());
            String second = PathUtil.getSecond(fileConfig.getMode());

            String path = getContext().getBaseName() + File.separator + PathUtil.getPath(fileConfig.getMode());

            createDir(fileConfig.getDir() + path);

            String fileExt = FileUtils.getFileExt(fileName);
            recordRow.put("ext", fileExt);//唯一值
            String uuidFileName = PathUtil.getUUIDFileName(token, fileName);
            recordRow.put("name", uuidFileName);//唯一值

            String fullFileName = path + File.separator + uuidFileName;
            File file = createFile(fileConfig.getDir() + fullFileName);
            recordRow.put("filePath", fullFileName);//文件相对路径

            String url = getUrl(getContext().getBaseName(), first, second, uuidFileName);
            recordRow.put("url", url);//文件相对路径
            writeFile(file, content);
            insert(recordRow);
            return recordRow;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileIOException(e.getMessage());
        }
    }

    private String getUrl(String db, String first, String second, String uuidFileName) {
        return "/file/down/" + db + "/" + first + "/" + second + "/" + uuidFileName;
    }

    /**
     * 创建目录 如果存在则不创建
     *
     * @param path
     */
    private void createDir(String path) {

        File dir = new File(path);

        if (!dir.exists()) {//创建目录
            dir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param fullFileName
     */
    private File createFile(String fullFileName) throws IOException {

        File file = new File(fullFileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    /**
     * 写入文件
     *
     * @param file
     * @param content
     */
    private void writeFile(File file, byte[] content) throws IOException {
        FileChannel channel = new FileOutputStream(file).getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(content.length);
        byteBuffer.put(content);
        byteBuffer.flip();
        channel.write(byteBuffer);
        channel.close();
    }
}

