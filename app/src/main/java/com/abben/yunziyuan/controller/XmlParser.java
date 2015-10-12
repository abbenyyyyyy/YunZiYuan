package com.abben.yunziyuan.controller;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;

import com.abben.yunziyuan.model.Movie;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.ListObjectOption;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.alibaba.sdk.android.oss.util.OSSToolKit;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**进行资源文件XML的解析，读取到数组里*/
public class XmlParser {
    private static OSSService ossService;
    private static OSSBucket bucket;
    private final static String ACCESS_KEY ="0LawaTViKKt9knlw";
    private final static String SCRECT_KEY ="t2LLn0Lq9kHXAcNatjF78MxWwzEObL";
    private final static String BUCKET_NAME ="abben-version";
    private static XmlParser xmlParser = null;

    /**获取XmlParser的实例*/
    public static XmlParser getXmlparser(){
        if(xmlParser == null){
            xmlParser = new XmlParser();
        }
        return xmlParser;
    }
    /**判断资源文件XML的版本，如果有更新就下载更新*/
    public static void updataXml(Context context) {
        if (ossService == null) {
            ossService = OSSServiceProvider.getService();
            ossService.setApplicationContext(context);
            ossService.setGlobalDefaultHostId("oss-cn-shenzhen.aliyuncs.com");
            ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK); // 设置加签类型为原始AK/SK加签
            ossService.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
                @Override
                public String generateToken(String httpMethod, String md5, String type, String date,
                                            String ossHeaders, String resource) {
                    String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n" + ossHeaders
                            + resource;
                    return OSSToolKit.generateToken(ACCESS_KEY, SCRECT_KEY, content);
                }
            });
            ossService.setCustomStandardTimeWithEpochSec(System.currentTimeMillis() / 1000);

            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectTimeout(30 * 1000); // 设置全局网络连接超时时间，默认30s
            conf.setSocketTimeout(30 * 1000); // 设置全局socket超时时间，默认30s
            conf.setMaxConcurrentTaskNum(5); // 替换设置最大连接数接口，设置全局最大并发任务数，默认为6
            conf.setIsSecurityTunnelRequired(false); // 是否使用https，默认为false
            ossService.setClientConfiguration(conf);
        }

            new Thread() {
                @Override
                public void run() {
                    ListObjectOption opt = new ListObjectOption();

                    bucket = ossService.getOssBucket(BUCKET_NAME);
                    bucket.setBucketACL(AccessControlList.PUBLIC_READ);
                    try {
                        OSSData ossData = ossService.getOssData(bucket, "version.txt");
                        String version_new="";
                        String version_old="";
                        InputStream inputStream = ossData.getObjectInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                        BufferedReader reader = new BufferedReader(inputStreamReader);

                        while ((version_new=reader.readLine())!=null) {
                            String thisDir = Environment.getExternalStorageDirectory().getPath() + File.separator +
                                    "YunZiYuan" + File.separator;
                            File file = new File(thisDir);
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            String versionName = "version.txt";
                            String versionPath = thisDir + versionName;
                            String xmlPath = thisDir + "resources.xml";
                            File versionFile;
                            versionFile = new File(versionPath);
                            if (versionFile.exists()) {
                                BufferedReader old_read = new BufferedReader(new FileReader(versionPath));
                                version_old = old_read.readLine();
                                if(!version_new.equals(version_old)){
                                    FileOutputStream fos = new FileOutputStream(versionFile);
                                    byte []buff= version_new.getBytes();
                                    fos.write(buff);
                                    fos.flush();
                                    fos.close();
                                    OSSData xmlData = ossService.getOssData(bucket, "resources.xml");
                                    InputStream xmlInput = xmlData.getObjectInputStream();
                                    File xmlFile = new File(xmlPath);
                                    FileOutputStream xmlfos = new FileOutputStream(xmlFile);
                                    byte []xmlbuff = new byte [1024];
                                    int count;
                                    while ((count=xmlInput.read(xmlbuff))>0) {
                                        xmlfos.write(xmlbuff, 0, count);
                                    }
                                    xmlfos.flush();
                                    xmlInput.close();
                                    xmlfos.close();
                                }
                                old_read.close();
                            }else{
                                FileOutputStream versionOut = new FileOutputStream(versionFile);
                                byte[] versionBuff = "0.1".getBytes();
                                versionOut.write(versionBuff);
                                versionOut.flush();
                                versionOut.close();
                                OSSData xmlData = ossService.getOssData(bucket, "resources.xml");
                                InputStream xmlInput = xmlData.getObjectInputStream();
                                File xmlFile = new File(xmlPath);
                                FileOutputStream xmlFos = new FileOutputStream(xmlFile);
                                byte []xmlBuff = new byte [1024];
                                int count;
                                while ((count=xmlInput.read(xmlBuff))>0) {
                                    xmlFos.write(xmlBuff, 0, count);
                                }
                                xmlFos.flush();
                                xmlInput.close();
                                xmlFos.close();
                            }
                        }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            }.start();
    }


    /**进行资源文件XML的解析，读取到数组里*/
    public  static List<Movie> getMovies(InputStream is) throws Exception {
        List<Movie> movies = null;
        Movie movie = null;

        XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例
        parser.setInput(is, "utf-8");//设置输入流 并指明编码方式
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    movies = new ArrayList<Movie>();
                    break;
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("movie")) {
                        movie = new Movie();
                    } else if (parser.getName().equals("type")) {
                        eventType = parser.next();
                        movie.setType(parser.getText());
                    } else if (parser.getName().equals("name")) {
                        eventType = parser.next();
                        movie.setMovie_name(parser.getText());
                    } else if (parser.getName().equals("imageOfMovie")) {
                        eventType = parser.next();
                        movie.setImageUrls(parser.getText());
                    } else if (parser.getName().equals("summaryOfMovie")) {
                        eventType = parser.next();
                        movie.setSummaryOfMovie(parser.getText());
                    } else if (parser.getName().equals("printscreen"))  {
                        eventType = parser.next();
                        movie.setPrintscreen(parser.getText());
                    } else if(parser.getName().equals("baiduyun")) {
                        eventType = parser.next();
                        movie.setBaiduyun(parser.getText());
                    } else if(parser.getName().equals("yunPassword")) {
                        eventType = parser.next();
                        movie.setYunPassword(parser.getText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("movie")) {
                        movies.add(movie);
                        movie = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        is.close();
        return movies;
    }
}
