package me.zeroest.quartz;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import static me.zeroest.quartz.RemoteJobClassLoader.JOB_REPO;
import static org.junit.jupiter.api.Assertions.*;

class RemoteJobClassLoaderTest {

    @Test
    void loadClass() {
        System.out.println("RemoteJobClassLoader.JOB_REPO = " + JOB_REPO);

        try {
            URL url = new File(JOB_REPO).toURI().toURL();

            new URLClassLoader(
                    new URL[] {
                            new File(JOB_REPO).toURI().toURL()
                    },
                    // URLClassLoader 설정 시 parent를 webAppClassLoader로 지정해줘야
                    // org.quartz.Job 등 내부 의존 클래스 로딩 가능
                    this.getClass().getClassLoader()
            ).loadClass("me.zeroest.quartz.RemoteSimpleJob");
        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}