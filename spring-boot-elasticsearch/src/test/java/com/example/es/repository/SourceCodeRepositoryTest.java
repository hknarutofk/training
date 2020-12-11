package com.example.es.repository;

import com.example.es.entity.SourceCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author yeqiang
 * @since 12/11/20 11:35 AM
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class SourceCodeRepositoryTest {
    @Autowired
    SourceCodeRepository sourceCodeRepository;
    ExecutorService executorService = Executors.newFixedThreadPool(100);


    @Test
    public void testImportCode() throws InterruptedException {
        importDirData(new File("/home/yeqiang/Downloads/src/linux-5.9.12"));
        executorService.awaitTermination(1, TimeUnit.DAYS);
    }

    void importDirData(File fd) {
        if (!fd.exists()) {
            return;
        }
        for (File f : fd.listFiles()) {
            if (f.isFile()) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        importFileData(f);
                    }
                });
            } else if (f.isDirectory()) {
                importDirData(f);
            }
        }

    }

    private void importFileData(File f) {
        log.info(f.getAbsolutePath());
        SourceCode code = new SourceCode();
        code.setPath(f.getAbsolutePath());

        try {
            FileInputStream in = new FileInputStream(f);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            String content = new String(buffer, StandardCharsets.UTF_8);
            code.setContent(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sourceCodeRepository.save(code);
    }


}